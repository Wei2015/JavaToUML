
import java.util.HashSet;
/**
 * This is test class for Relation class functionality. 
 */
public class TestRelation {

    public static void main (String[] args) {
        HashSet<RelationWrapper> testSet = new HashSet<>();

        RelationWrapper r1 = new RelationWrapper("A", "B", "association");

        RelationWrapper r2 = new RelationWrapper("B", "A", "ASSOCIATION");

        testSet.add(r1);
        testSet.add(r2);

        System.out.println("set contains:");
        for (RelationWrapper r: testSet)
            System.out.println("set:" + r.toString());
    }
}
