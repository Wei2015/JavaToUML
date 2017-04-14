import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by weiyao on 4/13/17.
 */
public class SeqReader {

    private ArrayList<File> javaFiles;
    //private ArrayList<String> classList;
    //private ArrayList<String> interfaceList;
   // private Set<RelationWrapper> relationShips;


    public SeqReader() {
        javaFiles = new ArrayList<>();
        //classList = new ArrayList<>();
        //interfaceList = new ArrayList<>();
        //relationShips = new HashSet<>();
    }

    /*
     * This method will parse sequence diagram information from java files to a string. In progress 03/30/17
    */
    public String parseSequenceDiagram(String path) {


        //read files from given path
        readDirectory(path);


        //check if there is Main.java file in the folder.
        boolean hasMain = false;
        File main = null;
        for (File file : javaFiles) {
            if(file.getName().equals("Main.java")) {
                hasMain = true;
                main = file;
            }
        }

        //if there is no Main.java in this folder, return empty string.
        if (!hasMain) {
            return "";
        }
        //if there is a Main.java, parse this file
        StringBuilder result = new StringBuilder();
        parseMain(result, main);

        System.out.println("new methods:");

        listMethodCalls(new File(path));




        //convert StringBuilder to String for return
        result.insert(0, "@startuml\nautonumber\n");
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

    private void parseMain(StringBuilder result, File main) {

        CompilationUnit cu = new CompilationUnit();

        //parse file
        try {
            cu = JavaParser.parse(main);
        } catch (FileNotFoundException e) {
            System.out.print(e);
        }

        TypeDeclaration type = cu.getType(0);

        //iterating members
        NodeList<BodyDeclaration> members = type.getMembers();

        BlockStmt statement = null;

        for (BodyDeclaration m : members) {
            if (m instanceof MethodDeclaration && ((MethodDeclaration) m).getNameAsString().equals("main")) {
                Optional<BlockStmt> bodyStmt = ((MethodDeclaration) m).getBody();
                try {
                    statement = bodyStmt.get();
                } catch (NoSuchElementException e) {
                    System.out.println(e);
                }
            }
        }




        NodeList<Statement> contents = statement.getStatements();
        for (Statement s : contents) {
            String[] cc = s.toString().split(" ");
            System.out.println(cc[0]);
        }

    }

    private void listMethodCalls(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {

            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(MethodCallExpr n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(n);
                    }
                }.visit(JavaParser.parse(file), null);
                System.out.println(); // empty line
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }
}

/*
    Optional<BlockStmt> bodyStmt = ((MethodDeclaration) m).getBody();
                try {
                        statement = bodyStmt.get();
                        } catch (NoSuchElementException e) {
                        System.out.println(e);
                        }
*/