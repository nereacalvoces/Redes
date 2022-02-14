package es.udc.redes.tutorial.tcp.server;
import java.net.*;
import java.io.*;
import java.util.Scanner;

/** Thread that processes an echo server connection. */

public class ServerThread extends Thread {

  private Socket socket;

  public ServerThread(Socket s) {
    // Store the socket s
    this.socket = s;
  }

  public void run() {
    try {
      // Set the input channel
      BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      // Set the output channel
      PrintWriter output = new PrintWriter(socket.getOutputStream());
      // Receive the message from the client
      Scanner sc = new Scanner(System.in);
      String line = null;
      while(!"exit".equalsIgnoreCase(line)) {
        line = sc.nextLine();
        output.println(line);
        output.flush();
        // Sent the echo message to the client
        System.out.println("SERVER: Sending " + line +
                " to " + socket.getInetAddress().toString() +
                ":" + socket.getPort());
      }
      // Close the streams
        input.close();
        output.close();
        sc.close();
    // Uncomment next catch clause after implementing the logic
     } catch (SocketTimeoutException e) {
     System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
    finally {
	// Close the socket
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
