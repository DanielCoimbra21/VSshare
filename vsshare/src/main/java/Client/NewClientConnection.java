package Client;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class NewClientConnection {
    public static void main(String[] args) {

        InetAddress serverAddress;
        String serverName = "127.0.0.1";
        String messageToSend="";
        Socket mySocket;

        Scanner sc;

        try {
            serverAddress = InetAddress.getByName(serverName);
            System.out.println("Get the address of the server : "+ serverAddress);

            Scanner scanner = new Scanner(System.in);

            mySocket = new Socket(serverAddress,45007);
            Gui gui = new Gui();
            gui.setVisible(true);
            gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            BufferedReader buffin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            PrintWriter Pout = new PrintWriter(mySocket.getOutputStream());

            boolean stop = false;

            while (!stop) {
                //read received messgae

                String message = buffin.readLine();

                if (message.equals("quit"))
                {
                    stop = true;
                    break;
                }

                if(message.equals("input")){
                    //wait for an input from the console
                    sc = new Scanner(System.in);

                    messageToSend = sc.nextLine();
                    Pout.println(messageToSend);
                    Pout.flush();

                }

                /*if (messageToSend.equals("3"))
                {
                    File file = new File("C:\\toSend\\send.txt");
                    transferFile(file);
                }*/



                //write the message on the output stream
                if (messageToSend.equals("quit"))
                {
                    stop = true;
                    Pout.println(messageToSend);
                    Pout.flush();
                    //break;
                    System.out.println("\nTerminate client program...");
                    mySocket.close();
                }

                System.out.println(message);


            }

            /*Client c = new Client(serverName, password);
            Thread client = new Thread();*/

            System.out.println("We got the connexion to  "+ serverAddress);

            //wait a bit before exit
            //Thread.sleep(30000);

            System.out.println("\nTerminate client program...");
            mySocket.close();


        }catch (UnknownHostException e) {

            e.printStackTrace();
        }catch (IOException e) {
            System.out.println("server connection error, dying.....");
        }catch(NullPointerException e){
            System.out.println("Connection interrupted with the server");
        }
    }

    /*private static void transferFile(File file) {

        String filename = "file.pdf";
        String pathname = file.getName();
        File myFile = new File(pathname);
        long myFileSize = Files.size(Paths.get(pathname));

        PrintWriter Pout2 = new PrintWriter(.getOutputStream(), true);
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


}