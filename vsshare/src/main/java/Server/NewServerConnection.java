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


            ServerSocket mySkServer ;
            Socket srvSocket = null ;
            InetAddress localAddress=null;
            String interfaceName = "lo"; //args[0];
            PrintWriter pout ;
            String demandeNbLignes ;
            String linkURL ;
            String result ;
            BufferedReader buffin ;

            try {
                NetworkInterface ni = NetworkInterface.getByName(interfaceName);
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
                String port = "12468" ; //args[1] ;
                int myPort = Integer.parseInt(port) ;
                mySkServer = new ServerSocket(myPort,5,localAddress);

                System.out.println("Default Timeout :" + mySkServer.getSoTimeout());
                System.out.println("Used IpAddress :" + mySkServer.getInetAddress());
                System.out.println("Listening to Port :" + mySkServer.getLocalPort());

                mySkServer.setSoTimeout(180000);//set 30 sec timout

                //Listen to a client connection wait until a client connects
                System.out.println("Waiting for a client connection:");
                srvSocket = mySkServer.accept();
                System.out.println("A client is connected");

                // demander l'URL au client
                //open the output data stream to write on the server
                pout = new PrintWriter(srvSocket.getOutputStream());
                //wait for an input from the client
                linkURL = "Passe moi le lien du fichier que tu veux lire :" ;
                //write the message on the output stream
                pout.println(linkURL);
                pout.flush();

                // récupère l'URL du client
                buffin = new BufferedReader(new InputStreamReader(srvSocket.getInputStream())) ;
                String message_distant = buffin.readLine() ;
                System.out.println(message_distant);




                // va te demander le nombre de lignes que tu veux
                //open the output data stream to write on the server
                pout = new PrintWriter(srvSocket.getOutputStream());
                //wait for an input from the client
                demandeNbLignes = "tu veux regarder combien de lignes ? :" ;
                //write the message on the output stream
                pout.println(demandeNbLignes);
                pout.flush();
                // récupère le nb de lignes du client
                buffin = new BufferedReader(new InputStreamReader(srvSocket.getInputStream())) ;
                String nblignes = buffin.readLine() ;
                System.out.println(nblignes);




                // va rechercher le fichier en local et lis le nb de lignes
                // changer le nb de lignes en int
                int lignes = Integer.parseInt(nblignes) ;
                // Le fichier d'entrée
                File file = new File(message_distant);
                // Créer l'objet File Reader
                FileReader fr = new FileReader(file);
                // Créer l'objet BufferedReader
                buffin = new BufferedReader(fr);
                StringBuffer sb = new StringBuffer();
                String line;
                //while ((line = br.readLine()) != null) {
                for(int i = 0 ; i < lignes ; i++) {
                    // ajoute la ligne au buffer
                    line = buffin.readLine();
                    sb.append(line);
                    sb.append("\n");
                }
                fr.close();
                System.out.println("Contenu du fichier: ");
                System.out.println(sb.toString());




                // envoies le résultat au client
                //open the output data stream to write on the server
                pout = new PrintWriter(srvSocket.getOutputStream());
                //wait for an input from the client
                result = sb.toString() ;
                //write the message on the output stream
                pout.println(result);
                pout.flush();


                //then die
                System.out.println("now dying");
                mySkServer.close();
                srvSocket.close();

            }catch (SocketException e) {
                //System.out.println("Connection Timed out");
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

