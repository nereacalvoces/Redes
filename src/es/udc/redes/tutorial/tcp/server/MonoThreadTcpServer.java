package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) throws IOException {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket socketServidor = null;
        try {
            // Create a server socket
             socketServidor = new ServerSocket(Integer.parseInt(argv[0]));
            // Set a timeout of 300 secs
            socketServidor.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                Socket socket = socketServidor.accept();
                // Set the input channel
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Set the output channel
                PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
                // Receive the client message
                String msg = input.readLine();
                output.println(msg);
                System.out.println("SERVER: Received " + (msg) +
                                    " from " + socket.getInetAddress().toString() +
                                    ":" + socket.getPort());
                // Send response to the client
                output.println(msg);
                System.out.println("SERVER: Sending " + (msg) +
                                    " to " + socket.getInetAddress().toString() +
                                    ":" + socket.getPort());
                // Close the streams
                input.close();
                output.close();
                socket.close();
            }
        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
          System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	        //Close the socket
            socketServidor.close();
        }
    }
}
