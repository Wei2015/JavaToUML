/**
 * This class wrap all information about constructor of a java file.
 */
import java.util.ArrayList;

class ConstructorWrapper {
    private String constructorName;


    private ArrayList<String> parameters;

    ConstructorWrapper(String name, ArrayList<String> parameters) {
        constructorName = name;
        this.parameters = parameters;
    }


    @Override
    public String toString() {
        String result = "+" + constructorName + "(";
        for (String s : parameters) {
            result += s;
        }

        result += ")\n";
        return result;
    }
}
