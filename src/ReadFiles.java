import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import sun.tools.java.ClassDeclaration;


public class ReadFiles {

    private String path;

    private HashSet<String> classSet;

    public ReadFiles(String path) {

        this.path = path;
        classSet = new HashSet<>();
    }

    /*This method will print all files' name from a given path
    * */
    public String open(){

        File directory = new File(path);

        StringBuilder result = new StringBuilder();

        File[] fList = directory.listFiles();

        //Collect all file names into a set, they are either class name or interface names.
        for (File file : fList){
            if (file.isFile()){
                //add class name into a set
                classSet.add(file.getName());
            }
        }
        //For each java file, perform parsing.
        for (File file :fList) {
            if (file.isFile()) {
                parseOneFile(file, result);
            }
        }

        return result.toString();

    }
    //one java file is one class
    private void parseOneFile(File file, StringBuilder content) {
        CompilationUnit cu = new CompilationUnit();
        //parse file
        try {
           cu = JavaParser.parse(file);
        } catch (FileNotFoundException e) {
            System.out.print(e);
        }

        //get class name
        TypeDeclaration type = cu.getType(0);

        String className = type.getName().getIdentifier();

        System.out.println(className);

        NodeList<BodyDeclaration> members = type.getMembers();
        for (BodyDeclaration m: members) {
            if (m instanceof MethodDeclaration && ((MethodDeclaration) m).isPublic()) {
                String methodName = ((MethodDeclaration) m).getName().toString();

                String returnType = ((MethodDeclaration) m).getType().toString();

                ((MethodDeclaration) m).getTypeParameters();
            }

        }






    }


}












