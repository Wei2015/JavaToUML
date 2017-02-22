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


public class Reader {

    private String path;

    private ArrayList<String> classList;

    private StringBuilder result;


    private final String NEWLINE = "\n";
    private final String LCURL = "{";


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
                System.out.println("className: " +name);
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
            /*
            if (((ClassOrInterfaceDeclaration)type).getExtendedTypes(0) != null) {
                String extendClass = ((ClassOrInterfaceDeclaration)type).getExtendedTypes(0).getNameAsString();
                if (classSet.contains(extendClass)) {
                    relationShips.add(extendClass + "<|--" +className + "\n");
                }
            } else if (((ClassOrInterfaceDeclaration)type).getImplementedTypes(0) != null) {
                String implementInterface = ((ClassOrInterfaceDeclaration)type).getImplementedTypes(0).getNameAsString();
                if (classSet.contains((implementInterface))) {
                    relationShips.add(implementInterface + "<|.." + implementInterface +"\n");
                }
            }

*/

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<MethodWrapper> methods = new ArrayList<>();
            //
            NodeList<BodyDeclaration> members = type.getMembers();

            for (BodyDeclaration m : members) {
                //add method information into methods List
                if (m instanceof MethodDeclaration && ((MethodDeclaration) m).isPublic()) {
                    String methodName = ((MethodDeclaration) m).getNameAsString();

                    String returnType = ((MethodDeclaration) m).getType().toString();

                    ArrayList<String> parameters = new ArrayList<>();

                    for (Parameter p: ((MethodDeclaration) m).getParameters() ){
                        String parameterType = p.getType().toString();

                        if (classList.contains(parameterType)) {
                            relationShips.add(parameterType + " <.. " + className + "\n");
                            System.out.println("parameterType: " + parameterType);
                        } else {
                            String parameter = p.getNameAsString() + ":" + parameterType;
                            parameters.add(parameter);
                            methods.add(new MethodWrapper(methodName, returnType, parameters));
                        }
                    }

                    //add attribute information to fields List
                } else if (m instanceof FieldDeclaration){
                    String newField ="";
                    if (((FieldDeclaration) m).isPublic()) {
                        newField +="+";
                    } else if (((FieldDeclaration) m).isPrivate()) {
                        newField +="-";
                    } else {
                        continue;
                    }
                    VariableDeclarator field = ((FieldDeclaration)m).getVariable(0);

                    String fieldType = field.getType().toString();
                    if (classList.contains(fieldType)) {
                        relationShips.add(fieldType + " -- " + className + "\n");
                    }
                    String fieldName = field.getNameAsString();
                    newField += fieldName + ":" +fieldType + "\n";
                    fields.add(newField);
                }

            }
            //format class information into result
            for (String s : relationShips) {
                result.append(s);
            }
            if (isInterface) {
                result.append("interface " + className +"{\n");
            } else {
                result.append("class " + className +"{\n");
            }

            for (String s : fields) {
                result.append(s);
            }

            for (MethodWrapper m : methods) {
                result.append(m.toString());
            }

            result.append("}\n");


        }

    }


    /*
     * format result to plantUML readable string
     */
    private String getResult() {


        return result.toString();
    }


}












