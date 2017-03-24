/**
 * One object of this class contains relation ship information and override the equals() and hashCode() methods to remove
 * duplicated relationships.
 */
public class RelationWrapper {

    private String leftClass;

    private String rightClass;

    private String relation;



    public RelationWrapper (String leftClass, String rightClass, String relation) {
        this.leftClass = leftClass;
        this.rightClass = rightClass;
        this.relation = relation;

    }


    public String toString() {
        String result = leftClass;
        switch (relation) {
            case "implement":
                result += " <|.. ";
                break;
            case "extend":
                result += " <|-- ";
                break;
            case "dependency":
                result += " <.. ";
                break;
            case "association":
                result += " -- ";
                break;
            case "ASSOCIATION":
                result += " \"*\"-- ";
        }
        result += rightClass + "\n";
        return result;
    }
@Override
    public boolean equals(Object newWrapper) {

        try {
            RelationWrapper newOne = (RelationWrapper)newWrapper;
            String newThisRelation = this.relation.toLowerCase();
            String newThatRelation = newOne.relation.toLowerCase();

            boolean result = this.leftClass.equals(newOne.leftClass) && this.rightClass.equals(newOne.rightClass) &&
                                newThisRelation.equals(newThatRelation)
                        || this.leftClass.equals(newOne.rightClass) && this.rightClass.equals(newOne.leftClass) &&
                            newThisRelation.equals(newThatRelation);


            return result;

        } catch (ClassCastException e) {
            System.out.println(e.toString());
            return false;
        }
    }
@Override
    public int hashCode () {

        String newRelation = this.relation.toLowerCase();
        return this.leftClass.hashCode() + this.rightClass.hashCode() + newRelation.hashCode();

}
}
