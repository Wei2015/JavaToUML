
/*
* This is the entry program for parse java files to class diagram.
 */
public class Umlparser {

    public static void main(String[] args) {


        Reader dir = new Reader();

        String result = dir.parseClassDiagram(args[0]);

        Writer.draw(result, args[1]);


    }
}
