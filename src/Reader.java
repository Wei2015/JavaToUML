import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.Attributes;

import com.github.javaparser.*;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;


public class Reader {

    private String path;
    private ArrayList<String> classList;
    private StringBuilder result;


    public Reader(String path) {

        this.path = path;
        classList = new ArrayList<>();
        result = new StringBuilder();

    }

    /*
     *This method will print all files' name from a given path
     */
    public String readDirectory() {

        File directory = new File(path);
        File[] fList = directory.listFiles();
        ArrayList<File> javaFiles = new ArrayList<>();

        //Collect all file names into a set, they are either class or interface.
        for (File file : fList) {
            String fileName = file.getName();
            if (file.isFile() && fileName.endsWith(".java")) {
                int index = file.getName().indexOf(".java");
                String name =file.getName().substring(0,index);
                classList.add(name);
                javaFiles.add(file);
            }
        }

        //For each java file, perform parsing.
        for (File file : javaFiles) {
            parseFile(file);
        }

        //convert StringBuilder to String for return
        return getResult();

    }

    /*
     * Extract file content to StringBuilder
     */
    private void parseFile(File file) {
        CompilationUnit cu = new CompilationUnit();
        //parse file
        try {
            cu = JavaParser.parse(file);
        } catch (FileNotFoundException e) {
            System.out.print(e);
        }

        TypeDeclaration type = cu.getType(0);

        //check if the class is interface or concrete class and store its name
        boolean isInterface = false;
        String className = "";
        ArrayList<String> relationShips = new ArrayList<>();

        if (type instanceof ClassOrInterfaceDeclaration) {
            if (((ClassOrInterfaceDeclaration) type).isInterface()) {
                isInterface = true;
            }
            className = type.getNameAsString();

            //add implement or extend to relationships

            if (!isInterface) {
               //add inheritance relationship
                List<ClassOrInterfaceType> extendedTypes = ((ClassOrInterfaceDeclaration) type).getExtendedTypes();

                for (ClassOrInterfaceType t : extendedTypes) {
                    String extendedClass = t.getName().getIdentifier();
                    if (classList.contains(extendedClass)) {
                        relationShips.add(extendedClass + "<|--" + className + "\n");
                    }
                }
                //add implement interface relationship
                List<ClassOrInterfaceType> implementedTypes = ((ClassOrInterfaceDeclaration) type).getImplementedTypes();

                for (ClassOrInterfaceType i : implementedTypes) {
                    String implementInterface = i.getName().getIdentifier();
                    if (classList.contains(implementInterface)) {
                        relationShips.add(implementInterface + "<|.." + className + "\n");
                    }
                }
            }


            //get methods, attributes and constructors information.
            ArrayList<AttributeWrapper> fields = new ArrayList<>();
            ArrayList<MethodWrapper> methods = new ArrayList<>();
            ArrayList<ConstructorWrapper> constructors = new ArrayList<>();

            //iterating members
            NodeList<BodyDeclaration> members = type.getMembers();

            for (BodyDeclaration m : members) {

                //add method information into methods List
                if (m instanceof MethodDeclaration && ((MethodDeclaration) m).isPublic()) {

                    String methodName = ((MethodDeclaration) m).getNameAsString();

                    String returnType = ((MethodDeclaration) m).getType().toString();

                    ArrayList<String> parameters = new ArrayList<>();

                    for (Parameter p: ((MethodDeclaration) m).getParameters() ) {
                        String parameterType = p.getType().toString();

                        if (!isInterface && classList.contains(parameterType)) {
                            relationShips.add(parameterType + " <.. " + className + "\n");
                        }

                        String parameter = p.getNameAsString() + ":" + parameterType;
                        parameters.add(parameter);
                    }

                    methods.add(new MethodWrapper(methodName, returnType, parameters));

                    //add attribute information to fields List
                } else if (m instanceof FieldDeclaration){
                    boolean isPublic;
                    if (((FieldDeclaration) m).isPublic()) {
                        isPublic = true;
                    } else if (((FieldDeclaration) m).isPrivate()) {
                        isPublic = false;
                    } else {
                        continue;
                    }

                    List<VariableDeclarator> variables = ((FieldDeclaration)m).getVariables();

                    for (VariableDeclarator v : variables) {
                        String fieldType = v.getType().toString(); //variable type
                        String varParameterType = ""; //variable parameter type
                        if (fieldType.contains("<") && fieldType.contains(">")) {
                            varParameterType = fieldType.substring(fieldType.indexOf("<")+1, fieldType.indexOf(">"));
                        }
                        if (classList.contains(fieldType)) {
                            relationShips.add(fieldType + " -- " + className + "\n");
                        } else if (classList.contains(varParameterType)) {
                            relationShips.add(varParameterType + " -- " + className + "\n");
                        } else {
                            String fieldName = v.getNameAsString();
                            fields.add(new AttributeWrapper(isPublic, fieldName, fieldType));
                        }
                    }

                } else if (m instanceof ConstructorDeclaration && ((ConstructorDeclaration) m).isPublic()){
                    String constructorName = ((ConstructorDeclaration) m).getNameAsString();

                    ArrayList<String> parameters = new ArrayList<>();

                    for (Parameter p: ((ConstructorDeclaration) m).getParameters() ) {
                        String parameterType = p.getType().toString();
                        String parameter = p.getNameAsString() + ":" + parameterType;
                        parameters.add(parameter);
                    }
                    constructors.add(new ConstructorWrapper(constructorName, parameters));

                }

            }
            //format class information into result
            for (String s : relationShips) {
                result.append(s);
            }

            //display interface or class
            if (isInterface) {
                result.append("interface ").append(className).append("{\n");
            } else {
                result.append("class ").append(className).append("{\n");
            }

            //extract constructors info
            for (ConstructorWrapper c : constructors)
                result.append(c.toString());

            //check if there are getters and setters for private attributes
            for(AttributeWrapper a : fields) {
                String getter = "get" + a.getAttributeName();
                String setter = "set" + a.getAttributeName();

                ArrayList<String> methodNames = new ArrayList<>();
                for (MethodWrapper m : methods) {
                    methodNames.add(m.getMethodName().toLowerCase());
                }

                if (methodNames.contains(getter) && methodNames.contains(setter)) {
                   a.setPublic();
                    for (int j = 0; j < methods.size(); j++) {
                        if (methods.get(j).getMethodName().toLowerCase().equals(getter))
                            methods.remove(j);

                        if (methods.get(j).getMethodName().toLowerCase().equals(setter))
                            methods.remove(j);

                    }
                }
            }

            //extract attributes info
            for (AttributeWrapper a : fields)
                result.append(a.toString());

            //extract methods info
            for (MethodWrapper m : methods)
                result.append(m.toString());

            result.append("}\n");


        }

    }


    /*
     * format result to plantUML readable string
     */
    private String getResult() {
        result.insert(0, "@startuml\n");
        result.append("@enduml");
        return result.toString();
    }


}












