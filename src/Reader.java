/**
* The Reader class extract java file contents to a string in plantUML format.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import com.github.javaparser.*;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;


public class Reader {

    private ArrayList<File> javaFiles;
    private ArrayList<String> classList;
    private ArrayList<String> interfaceList;
    private Set<RelationWrapper> relationShips;


    public Reader() {
        javaFiles = new ArrayList<>();
        classList = new ArrayList<>();
        interfaceList = new ArrayList<>();
        relationShips = new HashSet<>();

    }

    /*
     *This method will parse class diagram information from java files to a string.
     */
    public String parseClassDiagram(String path) {
        //read files from given path
        readDirectory(path);

        //Obtain two lists of class names and interface names.
        ArrayList<TypeDeclaration> types = new ArrayList<>();
        for (File file : javaFiles) {
            getClassType(file, types);
        }

        StringBuilder result = new StringBuilder();
        //iterating each file to extract necessary information, store in result
        for (TypeDeclaration type : types) {
            parseType(type, result);
        }

        for (RelationWrapper s : relationShips) {
            result.append(s.toString());
        }

        //convert StringBuilder to String for return
        result.insert(0, "@startuml\n");
        result.append("@enduml");
        return result.toString();

    }

    /*
     * This method will take a string as path and read all java files.
     */
    private void readDirectory(String path) {
        File directory = new File(path);
        File[] fList = directory.listFiles();


        //Collect all java files,ignore non java files.
        for (File file : fList) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }
    }


    /*
    * Obtain two lists of class names and interface names.
     */
    private void getClassType(File file, ArrayList<TypeDeclaration> types) {
        CompilationUnit cu = new CompilationUnit();

        //parse file
        try {
            cu = JavaParser.parse(file);
        } catch (FileNotFoundException e) {
            System.out.print(e);
        }

        TypeDeclaration type = cu.getType(0);
        types.add(type);

        //check if the class is interface or concrete class and store its name
        if (type instanceof ClassOrInterfaceDeclaration) {
            if (((ClassOrInterfaceDeclaration) type).isInterface()) {
                interfaceList.add(type.getNameAsString());
            } else {
                classList.add(type.getNameAsString());
            }
        }
    }

    
    /*
     * Extract one file content to StringBuilder
     */
    private void parseType(TypeDeclaration type, StringBuilder result) {

        String className = type.getNameAsString();

        ArrayList<AttributeWrapper> fields = new ArrayList<>();
        ArrayList<MethodWrapper> methods = new ArrayList<>();
        ArrayList<ConstructorWrapper> constructors = new ArrayList<>();

        //add implement or extend to relationships
        addImplementOrExtend(type, className, relationShips);

        //iterating members
        NodeList<BodyDeclaration> members = type.getMembers();

        for (BodyDeclaration m : members) {

            //add method information into methods List
            if (m instanceof MethodDeclaration && ((MethodDeclaration) m).isPublic()) {
                retrieveMethodInfo((MethodDeclaration) m, className, methods, relationShips);

                //add attribute information into fields List
            } else if (m instanceof FieldDeclaration) {
                retrieveAttributeInfo((FieldDeclaration) m, className, fields, relationShips);

                //add constructor information to constructor List
            } else if (m instanceof ConstructorDeclaration && ((ConstructorDeclaration) m).isPublic()) {
                retrieveConstructorInfo((ConstructorDeclaration) m, className, constructors, relationShips);
            }
        }

        result.append(extractInfo(className,constructors,fields,methods));

    }



    private void addImplementOrExtend(TypeDeclaration type, String className, Set<RelationWrapper> relationShips) {
        if (classList.contains(className)) {
            //add inheritance relationship
            List<ClassOrInterfaceType> extendedTypes = ((ClassOrInterfaceDeclaration) type).getExtendedTypes();

            for (ClassOrInterfaceType t : extendedTypes) {
                String extendedClass = t.getName().getIdentifier();
                if (classList.contains(extendedClass)) {
                    relationShips.add(new RelationWrapper(extendedClass, className, "extend"));
                }
            }
            //add implement interface relationship
            List<ClassOrInterfaceType> implementedTypes = ((ClassOrInterfaceDeclaration) type).getImplementedTypes();

            for (ClassOrInterfaceType i : implementedTypes) {
                String implementInterface = i.getName().getIdentifier();
                if (interfaceList.contains(implementInterface)) {
                    relationShips.add(new RelationWrapper(implementInterface, className, "implement"));
                }
            }
        }
    }


    private void retrieveMethodInfo(MethodDeclaration m, String className, ArrayList<MethodWrapper> methods,
                                    Set<RelationWrapper> relationShips) {
        String methodName = m.getNameAsString();

        String returnType = m.getType().toString();

        ArrayList<String> parameters = new ArrayList<>();

        for (Parameter p : m.getParameters()) {
            String parameterType = p.getType().toString();

            if (interfaceList.contains(parameterType) && !interfaceList.contains(className)) {
                relationShips.add(new RelationWrapper(parameterType, className, "dependency"));
            }

            String parameter = p.getNameAsString() + ":" + parameterType;
            parameters.add(parameter);
        }

        NodeList<Statement> contents = new NodeList<>();
        Optional<BlockStmt> bodyStmt = m.getBody();
        if (bodyStmt.isPresent()) {

            try {
                contents = m.getBody().get().getStatements();
            } catch (NoSuchElementException e) {
                System.out.println(e);
            }
            for (Statement s : contents) {
                String[] statement = s.toString().split(" ");
                if (interfaceList.contains(statement[0]) && !interfaceList.contains(className)) {
                    relationShips.add(new RelationWrapper(statement[0], className, "dependency"));
                }
            }
        }

        methods.add(new MethodWrapper(methodName, returnType, parameters));
    }


    private void retrieveAttributeInfo(FieldDeclaration m, String className, ArrayList<AttributeWrapper> fields,
                                       Set<RelationWrapper> relationShips) {
        boolean isPublic, isPrivate;
        if (m.isPublic()) {
            isPublic = true;
            isPrivate = false;
        } else if (m.isPrivate()) {
            isPublic = false;
            isPrivate = true;
        } else {
            isPublic = false;
            isPrivate = false;
        }

        List<VariableDeclarator> variables = m.getVariables();

        for (VariableDeclarator v : variables) {
            String fieldType = v.getType().toString(); //variable type
            String varParameterType = ""; //variable parameter type

            if (fieldType.contains("<") && fieldType.contains(">")) {
                varParameterType = fieldType.substring(fieldType.indexOf("<") + 1, fieldType.indexOf(">"));
            }
            if (classList.contains(fieldType)||interfaceList.contains(fieldType)) {
                if (v.getType().getArrayLevel() > 0) {
                    relationShips.add(new RelationWrapper(fieldType, className, "ASSOCIATION"));
                } else {
                    relationShips.add(new RelationWrapper(fieldType, className, "association"));
                }
            } else if (classList.contains(varParameterType) || interfaceList.contains(varParameterType)) {
                relationShips.add(new RelationWrapper(varParameterType, className, "ASSOCIATION"));
            } else {
                String fieldName = v.getNameAsString(); //variable name
                if (isPublic || isPrivate)
                    fields.add(new AttributeWrapper(isPublic, fieldName, fieldType));
            }
        }
    }


    private void retrieveConstructorInfo(ConstructorDeclaration m, String className, ArrayList<ConstructorWrapper> constructors,
                                        Set<RelationWrapper> relationShips) {
        String constructorName =  m.getNameAsString();
        ArrayList<String> parameters = new ArrayList<>();

        for (Parameter p : m.getParameters()) {
            String parameterType = p.getType().toString();
            if (interfaceList.contains(parameterType)) {
                relationShips.add(new RelationWrapper(parameterType, className, "dependency"));
            }
            String parameter = p.getNameAsString() + ":" + parameterType;
            parameters.add(parameter);
        }
        constructors.add(new ConstructorWrapper(constructorName, parameters));
    }


    private StringBuilder extractInfo(String className, ArrayList<ConstructorWrapper> constructors,
                                      ArrayList<AttributeWrapper> fields, ArrayList<MethodWrapper> methods) {
        //format class information into result
        StringBuilder result = new StringBuilder();


        //display interface or class
        if (interfaceList.contains(className)) {
            result.append("interface ").append(className).append("{\n");
        } else if (classList.contains(className)) {
            result.append("class ").append(className).append("{\n");
        }

        //extract constructors info
        for (ConstructorWrapper c : constructors)
            result.append(c.toString());

        //check if there are getters and setters for private attributes
        for (AttributeWrapper a : fields) {
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

        return result;
    }




}












