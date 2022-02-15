package es.udc.redes.tutorial.copy;
import java.io.*;

public class Copy {
    public static void main(String[] args) throws IOException {
        FileInputStream input = null;
        FileOutputStream output = null;

        try {
            input = new FileInputStream(args[0]);
            output = new FileOutputStream(args[1]);
            int c;

            while((c = input.read()) != -1)
                    output.write(c);
        } finally {
            if (input != null)
                    input.close();
            if (output != null)
                    output.close();
        }
    }
}
