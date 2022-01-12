package Server;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

public class NewServerConnection {

    /*********************************************************************************************
     *                     Main class du serveur de notre application VSShare
     *
     * Pour pouvoir lancer l'application, il est nécessaire de créer un variable d'environnement
     * nommée "HOME" pointant sur le \target\classes\ du projet
     *
     * Lien vers le GitLab : https://gitlab.com/DanielCoimbra21/vsshare
     * ******************************************************************************************/

        public static void main(String[] args) {

            InetAddress localAddress = null;
            ServerSocket mySkServer;
            Connection connection = new Connection();
            Log log = new Log();
            int ClientNo = 1;

            try {
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

                //When the server receive a new connection, starts a new Thread
                while(true)
                {
                    Socket clientSocket = mySkServer.accept();
                    System.out.println("connection request received");
                    Thread t = new Thread(new Server(clientSocket,ClientNo, log));
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

