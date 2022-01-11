package Server;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            pout = new PrintWriter(clientSocketOnServer.getOutputStream(), true);

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
                System.out.println("Ip address not found in <TheList.txt>.");
                // On va donc envoyer un message au client pour lui demander son nom de compte
                String premierContact = "Bonjour, veuillez insérer votre Username" ;
                pout.println(premierContact);
                pout.flush();

                // récupère le nom de compte du client
                String username = getInput();

                //create dir with ipAddre
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
                    String deuxiemeContact = "Password :";
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
                                String incorrectPassword = "Wrong password.";
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

            //call switch with the choices
            switchMethod();

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

    private void switchMethod() {

        try
        {
            // écoute le client afin de savoir l'action à faire
            do {
                pout.println("\nContinue choices : ");
                pout.flush();

                String choice = getInput();

                switch (choice)
                {
                    // 1. Send a list of all the files
                    case "1":
                        pout.println("The files available on the server are : ");
                        pout.flush();

                        listFiles(shareDirectory, 2);
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
                        pout.println("which file do you want to download");
                        pout.flush();

                        String fileToSend = getInput();

                        pout.println("File "+ fileToSend + " was sent");
                        pout.flush();

                        File file = new File(fileToSend);

                        transferFile(file);

                        break;

                    //4. upload
                    case "4":
                        pout.println("which file do you want to upload");
                        pout.flush();

                        uploadFile();


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
        }catch (IOException e)
        {
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

    public void transferFile(File dest) throws IOException
    {
        //Ask the server to create a new socket
        String filename = dest.getName();
        String pathname = shareDirectory+"\\"+dest.getName();
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
        /*clientSocketOnServer.close();
        System.out.println("end of connection to the client " + clientNumber);*/
    }

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

    public void listFiles(File f, int nbDir)
    {

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
            if(file.isFile())
            {
                String fileName = file.getName() ;
                pout.println(fileName);
                pout.flush();
            }
            //if its directory, it shows the directory name, and then recursive with the next level
            else if(file.isDirectory())
            {
                String dirName = "["+file.getName()+"]";
                pout.print(dirName);
                pout.flush();

                listFiles( file ,nbDir+1);
            }

            // Print the names of files and directories
            /*String fileName = file.getName();
            pout.println(fileName);
            pout.flush();*/
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

    public void uploadFile()
    {
        try {
            BufferedReader Buffin = new BufferedReader(new InputStreamReader(clientSocketOnServer.getInputStream()));

            //Ask the server to create a new socket



            String size = Buffin.readLine();
            System.out.println(size);

            String filePath = Buffin.readLine();
            System.out.println(filePath);
            //System.out.println(Buffin.readLine());

            File file = new File(filePath);

            int totalsize = Integer.parseInt(size);

            String filename = file.getName();
            byte[] mybytearray = new byte[totalsize];

            InputStream is = new BufferedInputStream(clientSocketOnServer.getInputStream());

            System.out.println("You received a file in :");

            FileOutputStream fos = new FileOutputStream(shareDirectory+"\\"+mdp[idUsername]+"\\"+filename);
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
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }




}
