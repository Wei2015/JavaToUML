public class Umlparser {

    public static void main(String[] args) {

        ReadFiles newDir = new ReadFiles(args[0]);

        System.out.print(newDir.open());

    }
}
