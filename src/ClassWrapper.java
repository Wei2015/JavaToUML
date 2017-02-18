
/**
 * One object of this class wrap all information about a single class
 */

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.ArrayList;

public class ClassWrapper {

    private String className;

    private ArrayList<String> methods;

    private ArrayList<String> attributes;


    private String attributeName;

    private String attributeType;

    private boolean attributeIsPublic;

    private String methodName;

    private String returnType;

    private String[] parameters;



}
