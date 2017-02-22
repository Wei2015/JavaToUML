/**
 * Created by weiyao on 2/21/17.
 */
import java.util.ArrayList;
public class MethodWrapper {

    private String methodName;

    private String returnType;

    private ArrayList<String> parameters;

    public MethodWrapper(String name, String type, ArrayList<String> parameters) {
        methodName = name;
        returnType = type;
        this.parameters = parameters;
    }

    public String toString() {
        String result = "+" + methodName + "(";
        for (String s : parameters) {
            result += s;
        }

        result += "): " + returnType +"\n";
        return result;
    }
}
