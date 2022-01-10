package Client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client {

    private String ipAddresse;
    private String password;

    public Client(String ipAddresse, String password)
    {
        this.ipAddresse = ipAddresse;
        this.password = password;
    }

    public void run()
    {

    }

/*    public void transferFile(File source, File dest) throws IOException {
        String filename = "file.pdf";
        String pathname = "C://toSend//"+filename;
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
        clientSocketOnServer.close();
        System.out.println("end of connection to the client " + clientNumber);
    }*/


    public void deleteFile(File file)
    {
        try {
        if (file.delete())
        {
            System.out.println("This file was deleted");
        }
        }catch(Exception e)
        {
            System.out.println("Failed to delete the file");
            e.printStackTrace();
        }
    }

    public void listFiles(File f, String path)
    {
        String[] pathnames;

        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        f = new File(path);

        // Populates the array with names of files and directories
        pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            System.out.println(pathname);
        }
    }
}


