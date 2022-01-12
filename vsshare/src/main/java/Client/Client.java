package Client;

import Client.View.FrameView;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

    InetAddress serverAddress;
    String serverName ;
    String messageToSend = "";
    Socket mySocket;
    PrintWriter Pout;

    Scanner sc = new Scanner(System.in);;

    public Client() {
        try {
            // initialise la vue
            FrameView frameView = new FrameView(null, null);
            frameView.setVisible(true);


            System.out.println("Enter server IP please");

            //enter server ip addresse
            messageToSend = sc.nextLine();
            serverName = messageToSend;
            serverAddress = InetAddress.getByName(serverName);
            System.out.println("Get the address of the server : " + serverAddress);
            mySocket = new Socket(serverAddress, 45007);



            BufferedReader buffin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            Pout = new PrintWriter(mySocket.getOutputStream(), true);

            boolean stop = false;

            while (!stop) {
                //read received messgae

                String message = buffin.readLine();
                if (!message.equals("input"))
                {
                    System.out.println(message);
                }

                if (message.equals("input")) {
                    //wait for an input from the console
                    //sc = new Scanner(System.in);
                    messageToSend = sc.nextLine();
                    Pout.println(messageToSend);
                    Pout.flush();
                }

                //write the message on the output stream
                if (messageToSend.equals("quit")) {
                    stop = true;
                    Pout.println(messageToSend);
                    Pout.flush();
                    //break;
                    System.out.println("\nTerminate client program...");
                    mySocket.close();
                }

                //if client chooses to download it laucnhes the transfer file methode
                if (messageToSend.equals("3"))
                {
                    transferFile();
                }

                //if client choses to upload file
                if (messageToSend.equals("4"))
                {
                    System.out.println("which file do you want to upload");
                    messageToSend = sc.nextLine();
                    uploadFile(messageToSend);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("server connection error, dying.....");
        } catch (NullPointerException e) {
            System.out.println("Connection interrupted with the server");
        }
    }

    private void transferFile() throws IOException {

        BufferedReader Buffin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
       // Pout = new PrintWriter(mySocket.getOutputStream(), true);

        //Ask the server to create a new socket


        System.out.println(Buffin.readLine());
        System.out.println(Buffin.readLine());

        sc = new Scanner(System.in);

        messageToSend = sc.nextLine();
        Pout.println(messageToSend);
        Pout.flush();

        System.out.println(Buffin.readLine());

        int totalsize = Integer.parseInt(Buffin.readLine());

        String filename = Buffin.readLine();
        byte[] mybytearray = new byte[totalsize];

        InputStream is = new BufferedInputStream(mySocket.getInputStream());

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

    }

    private void uploadFile(String filepath) throws IOException {
        BufferedReader Buffin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        String message = Buffin.readLine();
        System.out.println(message);

        //Ask the server to create a new socket
        String filename = filepath;
        String pathname = filename;
        File myFile = new File(pathname);
        long myFileSize = Files.size(Paths.get(pathname));

        PrintWriter Pout2 = new PrintWriter(mySocket.getOutputStream(), true);
        Pout2.println(myFileSize);
        Pout2.println(filename);

        byte[] mybytearray = new byte[(int)myFileSize];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
        bis.read(mybytearray, 0, mybytearray.length);
        OutputStream os = mySocket.getOutputStream();
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
    }
}


