package es.udc.redes.tutorial.copy;
import java.io.*;

public class Copy {
    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream("udc.gif");
            out = new FileOutputStream("gifVacio.gif");
            int c;

            while((c = in.read()) != -1)
                    out.write(c);
        } finally {
            if (in != null)
                    in.close();
            if (out != null)
                    out.close();
        }
    }
}
