/**
 * One object of this class wrap method information inside.
 */
public class MethodWrapper {

    private String methodName;
    private String returnType;
    private String[] parameters;

    public MethodWrapper() {
        methodName = "";
        returnType = "";
        parameters = null;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}

