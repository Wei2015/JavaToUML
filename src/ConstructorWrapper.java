/**
 * Created by weiyao on 2/23/17.
 */
import java.util.ArrayList;
public class ConstructorWrapper {
    private String constructorName;


    private ArrayList<String> parameters;

    public ConstructorWrapper(String name, ArrayList<String> parameters) {
        constructorName = name;
        this.parameters = parameters;
    }


    public String toString() {
        String result = "+" + constructorName + "(";
        for (String s : parameters) {
            result += s;
        }

        result += ")\n";
        return result;
    }
}
