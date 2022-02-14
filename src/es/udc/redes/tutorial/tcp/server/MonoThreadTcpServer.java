package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        try {
            // Create a server socket
            ServerSocket socket = new ServerSocket(Integer.parseInt(argv[0]));
            // Set a timeout of 300 secs
            socket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                Socket socket1 = socket.accept();
                // Set the input channel
                InputStream input = socket1.getInputStream();
                // Set the output channel
                OutputStream output = socket1.getOutputStream();
                // Receive the client message
                
                // Send response to the client

                // Close the streams
            }
        // Uncomment next catch clause after implementing the logic            
        //} catch (SocketTimeoutException e) {
        //    System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	        //Close the socket
        }
    }
}
