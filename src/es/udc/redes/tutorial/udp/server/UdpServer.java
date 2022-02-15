package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket datagrama =null;
        try {
            // Create a server socket
            datagrama = new DatagramSocket(Integer.parseInt(argv[0]));
            // Set maximum timeout to 300 secs
            datagrama.setSoTimeout(300000);
            while (true) {
                // Prepare datagram for reception
                byte[] buffer = new byte[256];
                DatagramPacket diagramRecibido = new DatagramPacket(buffer,buffer.length);
                // Receive the message
                datagrama.receive(diagramRecibido);
                System.out.println("SERVER: Received "
                        + new String (diagramRecibido.getData(),0,diagramRecibido.getLength())
                        + " from " + diagramRecibido.getAddress().toString() + ":"
                        + diagramRecibido.getPort());
                // Prepare datagram to send response
                InetAddress direccion = diagramRecibido.getAddress();
                int puerto = diagramRecibido.getPort();
                int length = diagramRecibido.getLength();
                diagramRecibido = new DatagramPacket(buffer,length,direccion,puerto);
                // Send response
                datagrama.send(diagramRecibido);
                System.out.println("SERVER: Sending "
                        + new String (diagramRecibido.getData(),0,diagramRecibido.getLength())
                        + " to " + diagramRecibido.getAddress().toString() + ":"
                        + diagramRecibido.getPort());
            }
        // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // Close the socket
            datagrama.close();
        }
    }
}
