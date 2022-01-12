package Server;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Scanner;

public class NewServerConnection {

        /**
         * @param args
         */
        public static void main(String[] args) {

            Socket srvSocket = null ;
            InetAddress localAddress = null;
            ServerSocket mySkServer;
            Connection connection = new Connection();
            //String interfaceName = connection.getIa();
            Log log = new Log();

            int ClientNo = 1;

            try {
                //NetworkInterface ni = NetworkInterface.getByName(interfaceName);
                NetworkInterface ni = NetworkInterface.getByInetAddress(connection.getIa());
                Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
                while(inetAddresses.hasMoreElements()) {
                    InetAddress ia = inetAddresses.nextElement();

                    if(!ia.isLinkLocalAddress()) {
                        if(!ia.isLoopbackAddress()) {
                            System.out.println(ni.getName() + "->IP: " + ia.getHostAddress());
                            localAddress = ia;
                        }
                    }
                }

                //Warning : the backlog value (2nd parameter is handled by the implementation
                mySkServer = new ServerSocket(45007,10,localAddress);
                System.out.println("Default Timeout :" + mySkServer.getSoTimeout());
                System.out.println("Used IpAddress :" + mySkServer.getInetAddress());
                System.out.println("Listening to Port :" + mySkServer.getLocalPort());

                log.info("Server is online");

                //wait for a client connection
                while(true)
                {
                    Socket clientSocket = mySkServer.accept();
                    System.out.println("connection request received");
                    Thread t = new Thread(new AcceptClient(clientSocket,ClientNo, log));
                    ClientNo++;
                    //starting the thread
                    t.start();
                }

            } catch (IOException e) {

                log.severe("Exception " +e);
                e.printStackTrace();
            }
        }
}

