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
            if (msg!=null) {
                String[] parts = msg.split(" ");
                File archivo = new File("p1-files"+parts[1]);
                File archivoError = new File("p1-files/error400.html");
                File archivoNotFound = new File("p1-files/error404.html");
                FileInputStream input2 = new FileInputStream(archivo.toString());
                if ((parts[0].equals("GET")) && archivo.exists())
                    processRequest(input2, archivo, true);
                else if ((parts[0].equals("HEAD")) && archivo.exists())
                    processRequest(input2, archivo, false);
                else if ((!parts[0].equals("GET")) && (!parts[0].equals("HEAD")))
                    processBadRequest(archivoError);
                else if (!archivo.exists())
                    processNotFound(archivoNotFound);
            }
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

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public void processRequest(FileInputStream input, File file,boolean writer) throws IOException {
        OutputStream clientOutput = socket.getOutputStream();
        clientOutput.write(("HTTP/1.0 200 OK\r\n").getBytes());
        clientOutput.write(("Date: " + getDate()+ "\r\n").getBytes());
        clientOutput.write(("Server: WebServer_695\r\n").getBytes());
        clientOutput.write(("Content-Length: "+file.length()+"\r\n").getBytes());
        selectContentType(file,clientOutput);
        clientOutput.write(("\r\n").getBytes());
        if (writer) {
            int c;
            while ((c = input.read()) != -1)
                clientOutput.write(c);
        }
            clientOutput.flush();
            clientOutput.close();
    }
    public void selectContentType(File file,OutputStream output) throws IOException {
        String[] particion = file.toString().split("\\.");
        switch (particion[1]) {
            case "html" -> output.write(("Content-Type: text/html\r\n").getBytes());
            case "txt" -> output.write(("Content-Type: text/plain\r\n").getBytes());
            case "gif" -> output.write(("Content-Type: image/gif\r\n").getBytes());
            case "png" -> output.write(("Content-Type: image/png\r\n").getBytes());
            default -> output.write(("Content-Type: application/octet-stream\r\n").getBytes());
        }
    }
    public void processBadRequest(File file) throws IOException {
        FileInputStream input = new FileInputStream(file.toString());
        OutputStream clientOutput = socket.getOutputStream();
        clientOutput.write(("HTTP/1.0 400 Bad Request\r\n").getBytes());
        clientOutput.write(("Date: " + getDate()+ "\r\n").getBytes());
        clientOutput.write(("Content-Length: "+input.getChannel().size()+"\r\n").getBytes());
        clientOutput.write(("Content-Type: text/html\r\n").getBytes());
        clientOutput.write(("\r\n").getBytes());
        int c;
        while ((c = input.read()) != -1)
            clientOutput.write(c);
        clientOutput.flush();
        clientOutput.close();
    }
    public void processNotFound(File file) throws IOException {
        FileInputStream input = new FileInputStream(file.toString());
        OutputStream clientOutput = socket.getOutputStream();
        clientOutput.write(("HTTP/1.0 404 Not Found\r\n").getBytes());
        clientOutput.write(("Date: " + getDate()+ "\r\n").getBytes());
        clientOutput.write(("Content-Length: "+input.getChannel().size()+"\r\n").getBytes());
        clientOutput.write(("Content-Type: text/html\r\n").getBytes());
        clientOutput.write(("\r\n").getBytes());
        int c;
        while ((c = input.read()) != -1)
            clientOutput.write(c);
        clientOutput.flush();
        clientOutput.close();
    }
}



