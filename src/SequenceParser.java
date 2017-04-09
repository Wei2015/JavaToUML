/**
 * This is for sequence diagram parser
 */
public class SequenceParser {

    public static void main(String[] args) {


        //Read files from the args[0] into string

        Reader dir = new Reader();
        String result = dir.parseSequenceDiagram(args[0]);

        //String result = "@startuml\n"
          //          + "Alice -> Bob: getName\n"
          //      +"Bob --> Alice: return Name\n"
          //      +"@enduml\n";
        System.out.println(result);

        Writer.draw(result, args[1]);


    }
}
