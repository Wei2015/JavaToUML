/**
 * One object of this class contains relation ship information and override the equals() and hashCode() methods to remove
 * duplicated relationships.
 */
public class RelationWrapper {

    private String leftClass;

    private String rightClass;

    private String relation;

    public RelationWrapper (String left, String right, String relation) {
        leftClass = left;
        rightClass = right;
        this.relation = relation;
    }


    public String toString() {
        String result = leftClass;
        switch (relation) {
            case "implement":
                result += "<|..";
                break;
            case "extend":
                result += "<|--";
                break;
            case "dependency":
                result += "<..";
                break;
            case "association":
                result += "--";
                break;
        }
        result += rightClass + "\n";
        return result;
    }
@Override
    public boolean equals(Object newWrapper) {

        try {
            RelationWrapper newOne = (RelationWrapper)newWrapper;
            return (this.leftClass.equals(newOne.leftClass)
                && this.rightClass.equals(newOne.rightClass)
                && this.relation.equals(newOne.relation));
        } catch (ClassCastException e) {
            System.out.println(e.toString());
            return false;
        }
    }
@Override
    public int hashCode () {
        return this.leftClass.hashCode() + this.rightClass.hashCode()
                + this.relation.hashCode();

}
}
