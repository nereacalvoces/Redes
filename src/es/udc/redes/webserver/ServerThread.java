package es.udc.redes.webserver;

import jdk.swing.interop.SwingInterOpUtils;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        this.socket = s;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = reader.readLine();
            if (msg!=null) {
                String[] parts = msg.split(" ");
                File archivo = new File("p1-files"+parts[1]);
                File archivoError = new File("p1-files/error400.html");
                File archivoNotFound = new File("p1-files/error404.html");
                try {
                FileInputStream input = new FileInputStream(archivo.toString());
                    if ((parts[0].equals("GET")) && archivo.exists())
                        processRequest(reader, input, archivo, true);
                    else if ((parts[0].equals("HEAD")) && archivo.exists())
                        processRequest(reader, input, archivo, false);
                    else if ((!parts[0].equals("GET")) && (!parts[0].equals("HEAD")))
                        processNotValidRequests(archivoError, true,true);
                }
                catch (FileNotFoundException e) {
                    processNotValidRequests(archivoNotFound,false, !parts[0].equals("HEAD"));
                }
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
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",new Locale("eng","UK"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    public String getDateModified(File file){
        Date fechaModi = new Date(file.lastModified());
        DateFormat formato = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",new Locale("eng","UK"));
        return formato.format(fechaModi);
    }
    //public void dateToSecs(String )
    public void processRequest(BufferedReader reader,FileInputStream input, File file,boolean writer) throws IOException, ParseException {
        OutputStream clientOutput = socket.getOutputStream();
        boolean isModifiedSince = true;Date modifiedSince;
        String lineaPeticion = reader.readLine();
        while(!lineaPeticion.equals("")) {
            String[] parts = lineaPeticion.split(": ");
            if (parts[0].equals("If-Modified-Since")){
                DateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",new Locale("eng","UK"));
                Date fechaUltimaMod = new Date(file.lastModified());
                modifiedSince = sdf.parse(parts[1]);
                if (!fechaUltimaMod.before(modifiedSince)) {
                     isModifiedSince = false;
                     break;
                 }
            }
            lineaPeticion = reader.readLine();
        }
        if (!isModifiedSince)
            clientOutput.write(("HTTP/1.0 304 Not Modified\r\n").getBytes());
        else
            clientOutput.write(("HTTP/1.0 200 OK\r\n").getBytes());
        setValues(clientOutput,file);
        if (writer && isModifiedSince) {
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
    public void processNotValidRequests(File file,boolean found,boolean writer) throws IOException{
        FileInputStream input = new FileInputStream(file.toString());
        OutputStream clientOutput = socket.getOutputStream();
        if(found)
            clientOutput.write(("HTTP/1.0 400 Bad Request\r\n").getBytes());
        else
            clientOutput.write(("HTTP/1.0 404 Not Found\r\n").getBytes());
        setValues(clientOutput,file);
        if (writer) {
            int c;
            while ((c = input.read()) != -1)
                clientOutput.write(c);
            clientOutput.flush();
            clientOutput.close();
        }
    }
    public void setValues(OutputStream clientOutput, File file) throws IOException {
        clientOutput.write(("Date: " + getDate()+ "\r\n").getBytes());
        clientOutput.write(("Server: WebServer_695\r\n").getBytes());
        clientOutput.write(("Content-Length: "+file.length()+"\r\n").getBytes());
        selectContentType(file,clientOutput);
        clientOutput.write(("Last-Modified:"+(getDateModified(file))+"\r\n").getBytes());
        clientOutput.write(("\r\n").getBytes());
    }
}


