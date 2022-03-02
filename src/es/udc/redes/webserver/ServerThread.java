package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            String msg = input.readLine();
            output.println(msg);
            System.out.println("SERVER: Received " +(msg)+
                    " from " + socket.getInetAddress().toString() +
                    ":" + socket.getPort());
            output.println(msg);
            input.close();
            output.close();
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            serverSocket.setSoTimeout(3000000);
            while(true) {
                Socket client = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                StringBuilder requestBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (!line.isBlank()) {
                    requestBuilder.append(line).append("\r\n");
                    line = bufferedReader.readLine();
                }
                System.out.println(requestBuilder);
                String[] parts = requestBuilder.toString().split(" ");
                StringBuilder stringBuilder = new StringBuilder(parts[1]);
                if (stringBuilder.charAt(0) == '/')
                    stringBuilder.insert(0,'.');
                FileInputStream input = new FileInputStream(stringBuilder.toString());
                int c;
                OutputStream clientOutput = client.getOutputStream();
                clientOutput.write(("HTTP/1.0 200 OK\r\n").getBytes());
                DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
                Date date = new Date();
                clientOutput.write(("Date: "+dateFormat.format(date)+"\r\n").getBytes());
                clientOutput.write(("Server: WebServer_695\r\n").getBytes());
                clientOutput.write(("Content-Length: "+input.length+"\r\n").getBytes());
                clientOutput.write(("\r\n").getBytes());

                while ((c = input.read()) != -1)
                    clientOutput.write(c);
                clientOutput.flush();input.close();clientOutput.close();client.close();
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
