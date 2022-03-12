package es.udc.redes.webserver;
import java.io.IOException;
import java.net.*;

/** Multithread Web server. */

public class WebServer {

    public static void main(String[] argv) throws IOException {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }
        ServerSocket socketServidor = null;
        try {
            socketServidor = new ServerSocket(Integer.parseInt(argv[0]));
            socketServidor.setSoTimeout(300000);
            while (true) {
                Socket socket = socketServidor.accept();
                ServerThread servidor = new ServerThread(socket);
                servidor.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            socketServidor.close();
        }
    }
}
