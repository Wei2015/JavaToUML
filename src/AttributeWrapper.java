/**
 * One object of this class wrap information of all attributes from a class.
 */
public class AttributeWrapper {

    private boolean isPublic;

    private String attributeName;

    private String attributeType;

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
