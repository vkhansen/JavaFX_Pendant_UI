package sample;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import static java.lang.Double.parseDouble;

public class UDPServer extends Thread {
    private DatagramSocket udpSocket;
    private int port;
    private InetAddress initAddr;
    ObservablePosition position;


    public UDPServer(ObservablePosition myPosition){
        //Local instance of observable position passed through the constructor
        position = myPosition;
        }

        //Thread to run the UDP server
        @Override
        public void run() {

        //Try catch block to handle exceptions related to UDP
        try{
            //Default listening port and localhost interface
            this.port = 5000;
            initAddr = InetAddress.getByName("127.0.0.1");

            //Creates UDP socket listener
            this.udpSocket = new DatagramSocket(this.port, initAddr);

            //Debugging output
            //System.out.println(this.udpSocket.getLocalAddress().toString());
            //System.out.println(this.udpSocket.getLocalPort());
            System.out.println("-- Running Server at " + initAddr + "--");


            String msg; // holds each packets received data

            //loop to receive packets
            while (true) {
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                // blocks until a packet is received
                udpSocket.receive(packet);
                msg = new String(packet.getData()).trim();

                //Cuts the array based on character #, limits length to 15
                String[] ss = Arrays.copyOf(msg.split("#"),15);

                //Checks if the array of strings is the correct length
                //Debugging messages
                //System.out.println(Arrays.toString(ss));
                //System.out.println(ss.length);
                //Parse string array remove empty and null values

                ss = Arrays.stream(ss)
                        .filter(s -> (s != null && s.length() > 0 && isNumeric(s)))
                        .toArray(String[]::new);

                //System.out.println(isNumeric(ss[0]));
                //Debugging messages
                //System.out.println(Arrays.toString(ss));
                //System.out.println(ss.length);


                if (ss.length == 15){
                    //Prepares new array to store the Double values
                    double[] Robot_State = new double[15];

                    //Parse the strings into double and put them in the other array
                    for (int i = 0; i < ss.length; i++) {
                            Robot_State[i] = parseDouble(ss[i]);
                    }

                    position.setPosition(Robot_State);
                    //passes array of variables to the
                    //control.setAxis(Arrays.toString(output));
                    //




                } else {
                    System.out.println("Array was not parsed correctly, should be numeric only with length 15");
                    System.out.println("Debug(message contents):" + msg);
                    //Debug
                    //Thread t = Thread.currentThread();
                    //System.out.println("UDP Server Thread:" + t.currentThread().getName());
                }


            }

                } catch (Exception ex) {
                    //Handles exceptions within the thread
                    Thread t = Thread.currentThread();
                    t.getUncaughtExceptionHandler().uncaughtException(t, ex);
                    }
                }

                public static boolean isNumeric(String s) {
                    return s.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
                }
        }
