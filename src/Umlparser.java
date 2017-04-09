

public class Umlparser {

    public static void main(String[] args) {



        Reader dir= new Reader();

        String result = dir.parseClassDiagram(args[0]);
        System.out.println(result);

        Writer.draw(result, args[1]);


    }
}
