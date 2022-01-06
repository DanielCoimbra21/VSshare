package Server;
import java.io.*;
import java.net.*;
import java.util.*;

public class AcceptClient implements Runnable {

    private Socket clientSocketOnServer;
    private int clientNumber;
    private BufferedReader buffin ;
    private PrintWriter pout ;
    private String delimiter = "--";
    private int cmpt = 0;
    private boolean codeClient = false ;
    private String[] mdp ;
    private int idIPAddr;
    private int idPassword;
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
            Scanner s = new Scanner(new File("vsshare/src/main/java/RegisteredClients/TheList.txt"));
            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNext()){
                list.add(s.next());

            }
            s.close();

            
            // va stocker toutes les addresses ip en position 0 et tous les mot de passes en position 1 d'un tableau de string
            List<String> items = null;
            mdp = new String[list.size()*2];
            for (String str : list){
                items = Arrays.asList(str.split("\\s*:\\s*"));
                System.out.println(items.toString());
                mdp[cmpt] = items.get(0);
                cmpt++;
                mdp[cmpt] = items.get(1);
                cmpt++;
            }

            // va regarder si l'adresse ip existe déjà
            String ipAddr = clientSocketOnServer.getInetAddress().toString() ;
            if(!items.contains(ipAddr)){
                System.out.println("la liste n'a pas trouvé l'adresse ip");
                // On va donc envoyer un message au client
                String premierContact = "Oy tu as l'air nouveau par ici, je vais te créer un compte, quel mot de passe veux-tu ?" ;
                pout.println(premierContact);
                pout.flush();
                // récupère le code du client
                String message_distant = getInput();
                // crée une arraylist avec le nouveau client
                String stockage = ipAddr + ":" + message_distant;
                // ajoute cette arraylist au fichier
                list.add(stockage);
                FileWriter writer = new FileWriter("vsshare/src/main/java/RegisteredClients/TheList.txt");
                for(String str: list) {
                    writer.write(str + System.lineSeparator());
                }
                writer.close();
            }
            else {
                System.out.println("adresse ip trouvée dans le fichier");
                // On va donc envoyer un message au client
                do {
                    String deuxiemeContact = "REBONJOUR C EST QUOI TON CODE STP STP STP ?????";
                    pout.println(deuxiemeContact);
                    pout.flush();
                    // récupère le code du client
                    String message_distant = getInput();
                    // va chercher le code correspondant à l'adresse ip
                    for(int i = 0 ; i < mdp.length ; i++){
                        if(mdp[i].equals(ipAddr)){
                            if(mdp[i+1].equals(message_distant)){
                                // mot de passe correct
                                idIPAddr = i ;
                                idPassword = i+1;
                                codeClient = true ;
                                break;
                            }
                            else{
                                // mot de passe incorrect
                                String incorrectPassword = "TON MDP EST FAUX, T ES MAUVAIS JACK";
                                pout.println(incorrectPassword);
                                pout.flush();
                            }
                        }
                    }
                }while (codeClient==false);
            }

            // envoie le message de bienvenue
            pout.println(help());
            pout.flush();

            // écoute le client afin de savoir l'action à faire
            String choice = getInput();

            // 1. Send a list of all the files
            if(choice.equals("1")){

            }

            // ferme le serveur
            clientSocketOnServer.close();
            Thread.sleep(3000);
            System.out.println("end of connection to the client " + clientNumber);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getInput() throws IOException {
        pout = new PrintWriter(clientSocketOnServer.getOutputStream());
        String initiate = "input";
        pout.println(initiate);
        pout.flush();

        buffin = new BufferedReader(new InputStreamReader(clientSocketOnServer.getInputStream())) ;
        String message_distant = buffin.readLine() ;

        return message_distant;
    }

    public String help() throws IOException {
        // va lire le fichier
        Scanner s = new Scanner(new File("vsshare/src/main/java/Server/Help.txt"));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNext()){
            list.add(s.next());
        }
        s.close();

        // va envoyer le message de bienvenue au client
        String affiche="";
        for(String str : list){
            if(str.equals("uwu")){
                affiche += "\n" ;
            }
            else {
                affiche += str + " " ;
            }
        }

        return affiche;
    }


}
