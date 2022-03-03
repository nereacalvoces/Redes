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
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            String msg = input.readLine();
            System.out.println(msg);
            while (true) {
                //Socket client = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                /*StringBuilder requestBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (!line.isBlank()) {
                    requestBuilder.append(line).append("\r\n");
                    line = input.readLine();
                }*/
                String[] parts = msg.split(" ");
                StringBuilder stringBuilder = new StringBuilder(parts[1]);
                System.out.println(stringBuilder);
                if (stringBuilder.charAt(0) == '/')
                    stringBuilder.insert(0, '.');
                FileInputStream input2 = new FileInputStream(stringBuilder.toString());
                int c;
                OutputStream clientOutput = socket.getOutputStream();
                clientOutput.write(("HTTP/1.0 200 OK\r\n").getBytes());
                DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
                Date date = new Date();
                clientOutput.write(("Date: " + dateFormat.format(date) + "\r\n").getBytes());
                clientOutput.write(("Server: WebServer_695\r\n").getBytes());
                //clientOutput.write(("Content-Length: "+input.length+"\r\n").getBytes());
                clientOutput.write(("\r\n").getBytes());

                while ((c = input2.read()) != -1)
                    clientOutput.write(c);
                clientOutput.flush();
                input.close();
                clientOutput.close();
                socket.close();
            }
        }
         catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



