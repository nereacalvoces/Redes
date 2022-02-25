package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class WebServer {
    
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
            System.exit(-1);
        }
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            serverSocket.setSoTimeout(3000000);
            while(true){
                Socket socket = serverSocket.accept();
                ServerThread server = new ServerThread(socket);
                server.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }
}
