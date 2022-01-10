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
    private int idUsername;
    private boolean quitting = false;


    File shareDirectory = new File("C:\\ShareFiles");

    //Constructor
    public AcceptClient (Socket clientSocketOnServer, int clientNo)
    {
        this.clientSocketOnServer = clientSocketOnServer;
        this.clientNumber = clientNo;

    }



    //overwrite the thread run()
    public void run() {
        try {

            //
            createShareDirectory(shareDirectory);


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
            mdp = new String[list.size()*3];
            for (String str : list){
                items = Arrays.asList(str.split("\\s*:\\s*"));
                System.out.println(items.toString());
                mdp[cmpt] = items.get(0);
                cmpt++;
                mdp[cmpt] = items.get(1);
                cmpt++;
                mdp[cmpt] = items.get(2);
                cmpt++;
            }

            System.out.println(items.toString());

            // va regarder si l'adresse ip existe déjà
            String ipAddr = clientSocketOnServer.getInetAddress().toString() ;
            if(!items.contains(ipAddr)){
                System.out.println("la liste n'a pas trouvé l'adresse ip");
                // On va donc envoyer un message au client pour lui demander son nom de compte
                String premierContact = "Bonjour, veuillez insérer votre Username" ;
                pout.println(premierContact);
                pout.flush();
                // récupère le nom de compte du client
                String username = getInput();
                // demande le mot de passe du client
                String demandeMdp = "Mot de passe" ;
                pout.println(demandeMdp);
                pout.flush();
                // récupère le code du client
                String password = getInput();
                // crée une arraylist avec le nouveau client
                String stockage = ipAddr + ":" + username + ":" + password;
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
                            if(mdp[i+2].equals(message_distant)){
                                // mot de passe correct
                                idIPAddr = i ;
                                idUsername = i+1;
                                idPassword = i+2;
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



            do {

                // envoie le message de bienvenue
                pout.println("\nContinue choices : ");
                pout.flush();


                // écoute le client afin de savoir l'action à faire
                String choice = getInput();

                switch (choice)
                {
                    // 1. Send a list of all the files
                    case "1":
                        pout.println("The files available on the server are : ");
                        pout.flush();
                        listFiles(shareDirectory);
                        break;

                    //2. delete file wanted
                    case "2":
                        pout.println("which file do you want to delete");
                        pout.flush();
                        String fileToDel = getInput();
                        pout.println("what is the password ?");
                        pout.flush();
                        //if (test password)
                        String passwordFile = getInput();
                        deleteFile(shareDirectory, fileToDel);
                        break;

                    //3. add a file
                    case "3":

                        pout.println("which file do you want to upload");
                        pout.flush();
                        String sourceFile = getInput();
                        File fileToSend = new File(sourceFile);
                        //transferFile(fileToSend ,shareDirectory);
                        break;

                    //4. oui
                    case "4":
                        break;

                    //5. help
                    case "5":
                        // envoie le message de bienvenue
                        pout.println(help());
                        pout.flush();
                        break;

                    //3. oui
                    case "quit":
                        quitting = true;
                        break;

                }
            }while (quitting == false);



            // ferme le serveur
            //clientSocketOnServer.close();
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

 /*   public void transferFile(File source, File dest) throws IOException {
        BufferedReader Buffin = new BufferedReader(new InputStreamReader(clientSocketOnServer.getInputStream()));
        PrintWriter Pout = new PrintWriter(clientSocketOnServer.getOutputStream(), true);


        int totalsize = Integer.parseInt(Buffin.readLine());
        String filename = Buffin.readLine();
        byte[] mybytearray = new byte[totalsize];

        InputStream is = new BufferedInputStream(clientSocketOnServer.getInputStream());


        System.out.println("You received a file in :");

        FileOutputStream fos = new FileOutputStream("c://received//"+filename);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int byteReadTot = 0;
        while(byteReadTot<totalsize)
        {
            int byteRead = is.read(mybytearray, 0, mybytearray.length);
            byteReadTot += byteRead;
            System.out.println("Byte read : " + byteReadTot);
            bos.write(mybytearray, 0, byteRead);

        }

        bos.close();


    }catch(UnknownHostException e) {

        e.printStackTrace();

    }catch (IOException e) {

        e.printStackTrace();
    }*/



    public void deleteFile(File file, String fileToDel)
    {
        File fileDel = new File(file.getAbsolutePath()+'\\'+fileToDel);
        try {
            if (fileDel.delete())
            {
                System.out.println("This file was deleted");
            }
        }catch(Exception e)
        {
            System.out.println("Failed to delete the file");
            e.printStackTrace();
        }
    }

    public void listFiles(File f)
    {

        File[] files = f.listFiles();

        // For each pathname in the pathnames array
        for (File file : files) {
            // Print the names of files and directories
            String fileName = file.getName();
            pout.println(fileName);
            pout.flush();
        }
    }

    public void createShareDirectory(File f)
    {
        if(!f.exists())
        {
            f.mkdirs();
            System.out.println("Directory Created");
        }
        else
        {
            System.out.println("Directory exists already");
        }
    }



}
