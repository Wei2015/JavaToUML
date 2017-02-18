import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;


public class Reader {

    private String path;

    private HashSet<String> classSet;

    private StringBuilder result;


    public Reader(String path) {

        this.path = path;
        classSet = new HashSet<>();
        result = new StringBuilder();
    }

    /*
     *This method will print all files' name from a given path
     */
    public String readDirectory() {

        File directory = new File(path);


        File[] fList = directory.listFiles();

        //Collect all file names into a set, they are either class name or interface names.
        for (File file : fList) {
            if (file.isFile()) {
                //add class name into a set
                classSet.add(file.getName());
            }
        }

        //For each java file, perform parsing.
        for (File file : fList) {
            if (file.isFile()) {

                parseFile(file);
            }
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

        //add Class name to result
        TypeDeclaration type = cu.getType(0);
        String className = type.getName().getIdentifier();



        //add Attributes, distinguish composite class and other variable

        NodeList<BodyDeclaration> members = type.getMembers();
        for (BodyDeclaration m : members) {
            if (m instanceof MethodDeclaration && ((MethodDeclaration) m).isPublic()) {
                String methodName = ((MethodDeclaration) m).getName().toString();

                String returnType = ((MethodDeclaration) m).getType().toString();
                if (returnType != "void") {
                    System.out.println(methodName + "; " + returnType);
                }

          

                for (Parameter p: ((MethodDeclaration) m).getParameters() ){
                    String parameter = p.getName().getIdentifier();
                    System.out.println(parameter);
                };
            }
        }


        //add Methods info


    }


    /*
     * format result to plantUML readable string
     */
    private String getResult() {


        return result.toString();
    }
}












