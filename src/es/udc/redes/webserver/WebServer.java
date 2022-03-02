package es.udc.redes.webserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

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
            System.out.println("Server started, listening for messages.");
            while(true) {
                Socket client = serverSocket.accept();
                InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder requestBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (!line.isBlank()) {
                    requestBuilder.append(line).append("\r\n");
                    line = bufferedReader.readLine();
                }
                System.out.println("--REQUEST--");
                System.out.println(requestBuilder);
                String[] parts = requestBuilder.toString().split(" ");
                StringBuilder stringBuilder = new StringBuilder(parts[1]);
                if(stringBuilder.charAt(0) == '/') {
                    stringBuilder.deleteCharAt(0);
                }
                FileInputStream input = new FileInputStream(stringBuilder.toString());
                int c;
                    OutputStream clientOutput = client.getOutputStream();
                    clientOutput.write(("HTTP/1.1 200 OK \r\n").getBytes());
                    clientOutput.write(("\r\n").getBytes());
                while ((c = input.read()) != -1)
                    clientOutput.write(c);
                clientOutput.flush();
                client.close();
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
