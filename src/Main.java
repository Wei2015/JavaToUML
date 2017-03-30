/**
 * This is the main class for sequence diagram parser
 */
public class Main {

    public static void main(String[] args) {


        //Read files from the args[0] into string

        Reader dir = new Reader();
        String result = dir.parseSequenceDiagram(args[0]);
        System.out.println(result);

        Writer.draw(result, args[1]);


    }
}
