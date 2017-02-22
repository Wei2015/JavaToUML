/**
 * Created by weiyao on 2/22/17.
 */
public class AttributeWrapper {

    private boolean isPublic;
    private String attributeName;
    private String attributeType;

    public AttributeWrapper(boolean isPublic, String name, String type) {
        this.isPublic = isPublic;
        attributeName = name;
        attributeType = type;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setPublic() {
        isPublic = true;
    }

    public String toString() {
        String result = "";
        if (isPublic) {
            result += "+";
        } else {
            result +="-";
        }
        result += attributeName + ":" + attributeType + "\n";

        return result;
    }



}
