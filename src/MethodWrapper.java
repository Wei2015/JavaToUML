/**
 * One object of this class wrap the information of a method.
 */
import java.util.ArrayList;
class MethodWrapper {

    private String methodName;

    private String returnType;

    private ArrayList<String> parameters;

    MethodWrapper(String name, String type, ArrayList<String> parameters) {
        methodName = name;
        returnType = type;
        this.parameters = parameters;
    }

    String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        String result = "+" + methodName + "(";
        for (String s : parameters) {
            result += s;
        }

        result += "):" + returnType +"\n";
        return result;
    }
}
