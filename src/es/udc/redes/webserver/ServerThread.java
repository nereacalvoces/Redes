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
            String msg = input.readLine();
            String[] parts = msg.split(" ");
            StringBuilder file = new StringBuilder(parts[1]);
            if (file.charAt(0) == '/')
                file.insert(0, '.');
            System.out.println(file);
            FileInputStream input2 = new FileInputStream(file.toString());
            System.out.println(input2.getChannel().size());
            if (parts[0].equals("GET"))
                processGet(input2,file);
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
    public void processGet(FileInputStream input, StringBuilder file) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        Date date = new Date();
        OutputStream clientOutput = socket.getOutputStream();
        clientOutput.write(("HTTP/1.0 200 OK\r\n").getBytes());
        clientOutput.write(("Date: " + dateFormat.format(date) + "\r\n").getBytes());
        clientOutput.write(("Server: WebServer_695\r\n").getBytes());
        clientOutput.write(("Content-Length: "+input.getChannel().size()+"\r\n").getBytes());
        selectContentType(file,clientOutput);
        clientOutput.write(("\r\n").getBytes());
        int c;
        while ((c = input.read()) != -1)
            clientOutput.write(c);
        clientOutput.flush();
        clientOutput.close();
    }

    public void selectContentType(StringBuilder file,OutputStream output) throws IOException {
        String[] particion = file.toString().split("\\.");
        System.out.println(particion[2]);
        switch (particion[2]) {
            case "html" -> output.write(("Content-Type: text/html\r\n").getBytes());
            case "txt" -> output.write(("Content-Type: text/plain\r\n").getBytes());
            case "gif" -> output.write(("Content-Type: image/gif\r\n").getBytes());
            case "png" -> output.write(("Content-Type: image/png\r\n").getBytes());
            default -> output.write(("Content-Type: application/octet-stream\r\n").getBytes());
        }
    }
}



