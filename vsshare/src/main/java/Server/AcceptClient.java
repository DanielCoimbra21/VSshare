package Server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AcceptClient implements Runnable {

    private Socket clientSocketOnServer;
    private int clientNumber;

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
