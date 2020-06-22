package sample;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {
    private DatagramSocket udpSocket;
    private InetAddress serverAddress;
    private InetAddress localAddress;
    private int targetPort;
    private int sourcePort;

    public UDPClient(String sourceAddr, String destinationAddr, int sport, int tport)  {
        try {
            //Attempt to set local variables, exception's will be thrown if the IP's are not formatted correctly
            this.serverAddress = InetAddress.getByName(destinationAddr);
            this.localAddress = InetAddress.getByName(sourceAddr);
            this.sourcePort = sport;
            this.targetPort = tport;
        } catch (Exception ex) {
            //Exception handling code
            System.out.println(ex.toString());
        }
    }

    public void UDPSend (String Message)  {
        try {
            //Creates the SENDING udp socket using source port and source IP
            udpSocket = new DatagramSocket( this.sourcePort,this.localAddress);
        } catch (Exception ex) {
            //Exception handling code
            //For example if socket is already bound
            System.out.println(ex.toString());
        }

        try {
            byte[] b = Message.getBytes(StandardCharsets.UTF_8);
            //Creates a packet with the specified ports, ip and message contents
            DatagramPacket p = new DatagramPacket(b, b.length, serverAddress, targetPort);
            this.udpSocket.send(p); // Sends the packet
            //Debug messages
            //System.out.println("DEBUG:" + Message + "Sent to:" + serverAddress.toString() + ":" + targetPort + "From Local Address:" + udpSocket.getLocalSocketAddress().toString());
        } catch (Exception ex) {
            //Exception handling code
            System.out.println(ex.toString());
        }

        //Closes the UDP socket
        try {
            udpSocket.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        //Clears the memory by running the garbage collector
        System.gc();
    }
}