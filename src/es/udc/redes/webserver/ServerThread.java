package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.nio.Buffer;


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
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            String request = input.readLine();
            String[] requestParam = request.split("");
            String path = requestParam[1];
            File file = new File(path);
            if(!file.exists()){
                output.write("HTTP 404");
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line=br.readLine())!=null)
                output.write(line);
            br.close();
            fr.close();
            output.close();
          // Uncomment next catch clause after implementing the logic

         } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close the client socket
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
