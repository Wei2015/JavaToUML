/**
 * This class is taking string input in PlantUML format and write into a svg file in the given file name.
 */

import net.sourceforge.plantuml.*;
import java.io.*;
public class Writer {

    public static void draw(String source, String fileName) {

        source = source.replaceAll ("^[ |\t]*\n$", "").trim();


        SourceStringReader reader = new SourceStringReader(source);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileFormatOption f_option = new FileFormatOption(FileFormat.SVG);
        String svg="";

        try {
            reader.generateImage(os,f_option);
            svg = new String(os.toByteArray());
        } catch (IOException e) {
            System.out.println("could not generate the image");
        }

        try {
            File file = new File(fileName);
            file.createNewFile();
            FileWriter stream = new FileWriter(file);
            BufferedWriter wr = new BufferedWriter(stream);
            wr.write(svg);
            wr.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            os.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}
