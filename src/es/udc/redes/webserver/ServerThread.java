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
            DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            Date date = new Date();
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            String msg = input.readLine();
            String[] parts = msg.split(" ");
            StringBuilder file = new StringBuilder(parts[1]);
            if (file.charAt(0) == '/')
                file.insert(0, '.');
            System.out.println(file);
            FileInputStream input2 = new FileInputStream(file.toString());
            System.out.println(input2.getChannel().size());
            int c;
            OutputStream clientOutput = socket.getOutputStream();
            clientOutput.write(("HTTP/1.0 200 OK\r\n").getBytes());
            clientOutput.write(("Date: " + dateFormat.format(date) + "\r\n").getBytes());
            clientOutput.write(("Server: WebServer_695\r\n").getBytes());
            clientOutput.write(("Content-Length: "+input2.getChannel().size()+"\r\n").getBytes());
            //clientOutput.write(("Content-Type: "));
            //splitear stringbuilder por el punto y dsps hacer if con la extension if parts [1] == gif tal ...
            clientOutput.write(("\r\n").getBytes());
            while ((c = input2.read()) != -1)
                clientOutput.write(c);
            clientOutput.flush();
            clientOutput.close();
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
}



