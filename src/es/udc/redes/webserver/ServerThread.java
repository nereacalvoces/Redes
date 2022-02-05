package es.udc.redes.webserver;

import java.net.*;
import java.io.*;


public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        try {
          // This code processes HTTP requests and generates 
          // HTTP responses
          // Uncomment next catch clause after implementing the logic
          //
        // } catch (SocketTimeoutException e) {
        //    System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close the client socket
        }
    }
}
