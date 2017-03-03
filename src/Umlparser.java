

public class Umlparser {

    public static void main(String[] args) {


        //Read files from the args[0] into string

        Reader newDir = new Reader();
        String result = newDir.readDirectory(args[0]);
        System.out.println(result);

        Writer.draw(result, args[1]);


    }
}
