package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NewClientConnection{

    public static void main(String[] args) {

        Socket clientSocket;
        InetAddress serverAddress;
        String serverName = "127.0.0.1";
        PrintWriter pout ;


        Scanner sc ;

        try {
            serverAddress = InetAddress.getByName(serverName);
            System.out.println("Get the address of the server : "+ serverAddress);

            //Ask the server to create a new socket
            clientSocket = new Socket(serverAddress,12468);

            // écoute le serveur
            BufferedReader buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
            String message_disatnt = buffin.readLine() ;
            System.out.println(message_disatnt);

            //open the output data stream to write on the client
            pout = new PrintWriter(clientSocket.getOutputStream());
            //wait for an input from the client
            sc = new Scanner(System.in);
            String message = sc.nextLine();
            //write the message on the output stream
            pout.println(message);
            pout.flush();


            // écoute le serveur back
            buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
            message_disatnt = buffin.readLine() ;
            System.out.println(message_disatnt);


            // répond au serveur
            pout = new PrintWriter(clientSocket.getOutputStream(), true) ;
            Scanner console = new Scanner(System.in) ;
            String messageToSend = console.nextLine() ;
            pout.println(messageToSend);


            // écoute le fichier du serveur
            buffin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
            StringBuffer sb = new StringBuffer();
            String line;
            while((line = buffin.readLine()) != null)
            {
                // ajoute la ligne au buffer
                sb.append(line);
                sb.append("\n");
            }
            System.out.println("Contenu du fichier: ");
            System.out.println(sb.toString());



            // meurt
            System.out.println("now dying");
            clientSocket.close();

        }catch (UnknownHostException e) {

            e.printStackTrace();

        }catch (IOException e) {

            e.printStackTrace();
        }
    }

    public int openPorts() throws IOException {
        int openport = 1 ;
        for (int i = 44980 ; i < 45010 ; i++){
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost",i),1000);
            openport = i ;
            System.out.println("port " + i + "is open");
        }
        return openport ;
    }
}
