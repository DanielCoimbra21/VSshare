package Server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class AcceptClient implements Runnable {

    private Socket clientSocketOnServer;
    private int clientNumber;
    private BufferedReader buffin ;
    private PrintWriter pout ;

    //Constructor
    public AcceptClient (Socket clientSocketOnServer, int clientNo)
    {
        this.clientSocketOnServer = clientSocketOnServer;
        this.clientNumber = clientNo;

    }

    //overwrite the thread run()
    public void run() {
        try {
            System.out.println("Client Nr "+clientNumber+ " is connected");
            System.out.println("Socket is available for connection"+ clientSocketOnServer);
            pout = new PrintWriter(clientSocketOnServer.getOutputStream());

            // va lire le fichier
            Scanner s = new Scanner(new File("vsshare/src/main/java/RegisteredClients/TheList"));
            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNext()){
                list.add(s.next());
            }
            s.close();

            // va regarder si l'adresse ip existe déjà
            String ipAddr = clientSocketOnServer.getInetAddress().toString() ;
            if(!list.contains(ipAddr)){
                System.out.println("la liste n'a pas trouvé l'adresse ip");
                // On va donc envoyer un message au client
                String premierContact = "Oy tu as l'air nouveau par ici, je vais te créer un compte, quel mot de passe veux-tu ?" ;
                pout.println(premierContact);
                pout.flush();
                // récupère le code du client
                buffin = new BufferedReader(new InputStreamReader(clientSocketOnServer.getInputStream())) ;
                String message_distant = buffin.readLine() ;
                System.out.println(message_distant);

            }
            else {
                System.out.println("adresse ip trouvée dans le fichier");
                // On va donc envoyer un message au client
                String deuxiemeContact = "REBONJOUR C EST QUOI TON CODE STP STP STP ?????" ;
                pout.println(deuxiemeContact);
                pout.flush();
                // récupère le code du client
                buffin = new BufferedReader(new InputStreamReader(clientSocketOnServer.getInputStream())) ;
                String message_distant = buffin.readLine() ;
                System.out.println(message_distant);
            }


            clientSocketOnServer.close();
            Thread.sleep(3000);
            System.out.println("end of connection to the client " + clientNumber);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
