package Client;

import java.io.*;

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

    public void transferFile(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }


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


