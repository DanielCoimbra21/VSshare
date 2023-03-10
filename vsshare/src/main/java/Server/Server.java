package Server;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Server implements Runnable {

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
    Log log;

    /**
     * Constructeur de la classe Server qui sert à gérer la communication entre le client et le serveur
     * @param clientSocketOnServer Socket disponible pour le serveur
     * @param clientNo Numéro du client souhaitant se connecter au serveur
     * @param log logbook créé dans l'initialisation
     */

    public Server(Socket clientSocketOnServer, int clientNo, Log log)
    {
        this.clientSocketOnServer = clientSocketOnServer;
        this.clientNumber = clientNo;
        this.log=log;
    }

    /**
     * Thread de la classe Server qui sert à gérer la communication entre le serveur et plusieurs clients en même temps
     */

    public void run() {
        try {
            // parameter part
            createShareDirectory(shareDirectory);
            System.out.println("Client Nr "+clientNumber+ " is connected");
            System.out.println("Socket is available for connection"+ clientSocketOnServer);
            pout = new PrintWriter(clientSocketOnServer.getOutputStream(), true);

            // read the file
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

            // Si l'addresse IP n'existe pas, crée un compte. Sinon se connecte au compte
            String ipAddr = clientSocketOnServer.getInetAddress().toString() ;
            if(!items.contains(ipAddr)){
                // Demander le nom de compte au client
                String premierContact = "Bonjour, veuillez insérer votre Username" ;
                pout.println(premierContact);
                pout.flush();

                // récupère le nom de compte du client
                String username = getInput();

                // Crée un directory avec le username
                createShareDirectory(new File("C:\\ShareFiles\\" + username));

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
                System.out.println("Ip address found in <TheList.txt>.");
                // On va donc envoyer un message au client
                do {
                    // demande et récupère le username du client
                    String deuxiemeContact = "Username :";
                    pout.println(deuxiemeContact);
                    pout.flush();
                    String message_distant_Username = getInput();

                    // demande et récupère le password
                    String troixiemeContact = "Password :";
                    pout.println(troixiemeContact);
                    pout.flush();
                    String message_distant = getInput();

                    // va chercher le code correspondant à l'adresse ip
                    for(int i = 0 ; i < mdp.length ; i++){
                        if(mdp[i].equals(message_distant_Username)){
                            // username correct
                            if(mdp[i+1].equals(message_distant)){
                                // mot de passe correct
                                idIPAddr = i-1 ;
                                idUsername = i;
                                idPassword = i+1;
                                codeClient = true ;
                                break;
                            }
                            else{
                                // mot de passe incorrect
                                String incorrectPassword = "Wrong username or password.";
                                pout.println(incorrectPassword);
                                pout.flush();
                            }
                        }
                    }
                    // Username incorrect
                    String incorrectPassword = "Wrong username or password.";
                    pout.println(incorrectPassword);
                    pout.flush();
                }while (codeClient==false);
            }
            // connection réussie, écrit un log dans le serveur.
            String firstLogInfoMessageConnection = "A new Client is connected with the address : " + mdp[idIPAddr] + " and the username : " + mdp[idUsername];
            log.info(firstLogInfoMessageConnection);

            // envoie le message de bienvenue
            pout.println(help());
            pout.flush();

            //call switch with the client choices
            switchMethod();

            // ferme le serveur
            //clientSocketOnServer.close();
            Thread.sleep(3000);
            System.out.println("end of connection to the client " + clientNumber);
        } catch (IOException e) {
            log.severe("Exception thrown "+e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            log.severe("Connection interrupted "+e);
            e.printStackTrace();
        }
    }

    /**
     * Méthode de la classe Server qui sert à gérer les demandes du client
     */

    private void switchMethod() {
        try {
            do {
                pout.println("\nContinue choices : ");
                pout.flush();
                String choice = getInput();

                switch (choice) {
                    // 1. Send a list of all the files
                    case "1":
                        pout.println("The files available on the server are : ");
                        pout.flush();
                        listFiles(shareDirectory, 2);
                        break;

                    //2. delete file wanted
                    case "2":
                        // 1. demande le username
                        pout.println("Enter an Username :");
                        pout.flush();
                        String usernameToDel = getInput();

                        // 2. liste tous les fichiers liés a cet username
                        pout.println("Here is a list for the selected username : ");
                        pout.flush();
                        listFilesForUsername(shareDirectory, usernameToDel);

                        // 3. Récupérer le nom du fichié devant être deleted
                        pout.println("Please enter the name of the file you want to delete.");
                        pout.flush();
                        String nameOfWantedDeletedFile = getInput();

                        // 4. Test le password du username précédemment rentré
                        boolean pwd = false ;
                        do {
                            pout.println("Password : ");
                            pout.flush();
                            String passwordFile = getInput();
                            for(int i = 0 ; i < mdp.length ; i++){
                                if(mdp[i].equals(usernameToDel)){
                                    if(mdp[i+1].equals(passwordFile)){
                                        // mot de passe correct
                                        idIPAddr = i-1 ;
                                        idUsername = i;
                                        idPassword = i+1;
                                        pwd = true ;
                                        break;
                                    }
                                    else {
                                        // mot de passe incorrect
                                        String incorrectPassword = "Wrong password.";
                                        pout.println(incorrectPassword);
                                        pout.flush();
                                    }
                                }
                            }
                        }while (pwd==false);
                        String fileToDel = usernameToDel+"\\"+nameOfWantedDeletedFile ;
                        deleteFile(shareDirectory, fileToDel);
                        break;

                    //3. download a file
                    case "3":
                        pout.println("which file do you want to download");
                        pout.flush();
                        String fileToSend = getInput();
                        pout.println("File "+ fileToSend + " was sent");
                        pout.flush();

                        log.info("Client has asked to download a file");

                        File file = new File(fileToSend);
                        transferFile(file);
                        break;

                    //4. upload
                    case "4":
                        pout.println("which file do you want to upload");
                        pout.flush();

                        log.info("Client has asked to upload");

                        uploadFile();
                        break;

                    //5. help
                    case "5":
                        // envoie le message de bienvenue
                        pout.println(help());
                        pout.flush();
                        break;

                    //6. quit
                    case "quit":
                        quitting = true;
                        log.info("Client has disconnected");
                        break;
                }
            }while (quitting == false);
        }catch (IOException e) {
            log.severe("Exception thrown "+e);
            e.printStackTrace();
        }
    }

    /**
     * Méthode de la classe Server qui sert à récupérer l'input du client
     */

    public String getInput() throws IOException {
        pout = new PrintWriter(clientSocketOnServer.getOutputStream());
        String initiate = "input";
        pout.println(initiate);
        pout.flush();
        buffin = new BufferedReader(new InputStreamReader(clientSocketOnServer.getInputStream())) ;
        String message_distant = buffin.readLine() ;
        return message_distant;
    }

    /**
     * Méthode de la classe Server qui sert à afficher le message d'aide
     * Déclencheur : input "5"
     */

    public String help() throws IOException {
        // va lire le fichier d'aide stocké dans le fichier Help.txt
        Scanner s = new Scanner(new File("vsshare/src/main/java/Server/Help.txt"));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNext()){
            list.add(s.next());
        }
        s.close();

        // mise en forme du fichier
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

    /**
     * Méthode de la classe Server qui sert à gérer le download d'un fichier du serveur au client
     * @param dest destination du fichier stocké sur le serveur
     * Déclencheur : input "3"
     */

    public void transferFile(File dest) throws IOException
    {
        //Ask the server to create a new socket
        String filename = dest.getName();
        String pathname = shareDirectory+"\\"+dest.getPath();
        File myFile = new File(pathname);
        long myFileSize = Files.size(Paths.get(pathname));

        PrintWriter Pout2 = new PrintWriter(clientSocketOnServer.getOutputStream(), true);
        Pout2.println(myFileSize);
        Pout2.println(filename);

        byte[] mybytearray = new byte[(int)myFileSize];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
        bis.read(mybytearray, 0, mybytearray.length);
        OutputStream os = clientSocketOnServer.getOutputStream();
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
    }

    /**
     * Méthode de la classe Server qui sert à supprimer un fichier stocké sur le serveur
     * @param file chemin du fichier à supprimer
     * @param fileToDel nom du fichier à supprimer
     * Déclencheur : input "2"
     */

    public void deleteFile(File file, String fileToDel) {
        File fileDel = new File(file.getAbsolutePath()+'\\'+fileToDel);
        try {
            if (fileDel.delete()) {
                System.out.println("This file was deleted");
            }
        }catch(Exception e) {
            System.out.println("Failed to delete the file");
            log.warning("failed to delete a file");
            e.printStackTrace();
        }
    }

    /**
     * Méthode de la classe Server qui sert à lister tous les fichiers liés à un username
     * @param f liste des fichiers
     * @param username username concerné
     * Déclencheur : Le switch du mode "Delete"
     */

    public void listFilesForUsername(File f, String username){
        File[] files = f.listFiles();

        for (File file : files) {
            if(file.getName().equals(username)){
                File[] childs = file.listFiles();
                for (File child : childs){
                    String fileName = child.getName() + "\n";
                    pout.println(fileName);
                    pout.flush();
                }
            }
        }
    }

    /**
     * Méthode de la classe Server qui sert à afficher tous les fichiers du dossier sharefiles ainsi que sa structure
     * @param nbDir définit la profindeur de la recherche, récursive afin d'aller lister les fichiers des sous dossiers.
     * @param f liste des fichiers du dossier ShareFiles
     * Déclencheur : input "1"
     */

    public void listFiles(File f, int nbDir) {
        File[] files = f.listFiles();

        // For each pathname in the pathnames array
        for (File file : files) {
            String retourLigne = "\n";
            pout.print(retourLigne);
            pout.flush();

            //tabulation
            for (int i = 0; i < nbDir-2; i++) {
                String fileName = "\t" ;
                pout.print(fileName);
                pout.flush();
            }

            //if it's a file it shows the file
            if(file.isFile()) {
                String fileName = file.getName() ;
                pout.println(fileName);
                pout.flush();
            }
            //if its directory, it shows the directory name, and then recursive with the next level
            else if(file.isDirectory()) {
                String dirName = "["+file.getName()+"]";
                pout.print(dirName);
                pout.flush();

                listFiles( file ,nbDir+1);
            }
        }
    }

    /**
     * Méthode de la classe Server permettant de créer un fichier "shareFiles" lors de la première connection du client
     * @param f liste des fichiers du dossier ShareFiles
     * Déclencheur : login
     */

    public void createShareDirectory(File f)
    {
        if(!f.exists()) {
            f.mkdirs();
            System.out.println("Directory Created");
            log.info("Directory was created");
        }
        else {
            System.out.println("Directory exists already");
        }
    }

    /**
     * Méthode de la classe Server permettant à l'utilisateur de stocker des fichiers depuis son pc jusqu'au serveur
     * Déclencheur : input "5"
     */

    public void uploadFile() {
        try {
            BufferedReader Buffin = new BufferedReader(new InputStreamReader(clientSocketOnServer.getInputStream()));

            // Va chercher les informations sur le fichier
            String size = Buffin.readLine();
            System.out.println(size);
            String filePath = Buffin.readLine();
            System.out.println(filePath);

            // Crée les différentes variables selon les informations du file
            File file = new File(filePath);
            int totalsize = Integer.parseInt(size);
            String filename = file.getName();
            byte[] mybytearray = new byte[totalsize];

            // prépare la transaction et l'écriture des fichiers
            InputStream is = new BufferedInputStream(clientSocketOnServer.getInputStream());
            FileOutputStream fos = new FileOutputStream(shareDirectory+"\\"+mdp[idUsername]+"\\"+filename);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int byteReadTot = 0;

            // va read le fichier et le réécrit dans les dossiers du serveur
            while(byteReadTot<totalsize) {
                int byteRead = is.read(mybytearray, 0, mybytearray.length);
                byteReadTot += byteRead;
                System.out.println("Byte read : " + byteReadTot);
                bos.write(mybytearray, 0, byteRead);
            }
            bos.close();
        }catch (IOException e) {
            log.severe("error while uploading file "+e);
            e.printStackTrace();
        }
    }
}
