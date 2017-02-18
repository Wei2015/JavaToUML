

public class Umlparser {

    public static void main(String[] args) {


        //Read files from the args[0] into string

        Reader newDir = new Reader(args[0]);
        String result = newDir.readDirectory();





    }
}
