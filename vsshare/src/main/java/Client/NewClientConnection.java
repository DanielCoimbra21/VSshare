package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NewClientConnection {
    public static void main(String[] args) {

        InetAddress serverAddress;
        String serverName = "127.0.0.1";
        String ipAddresse;
        String password;
        PrintWriter pout ;

        Scanner sc;

        try {
            serverAddress = InetAddress.getByName(serverName);
            System.out.println("Get the address of the server : "+ serverAddress);

            Scanner scanner = new Scanner(System.in);

            Socket mySocket = new Socket(serverAddress,45007);

            BufferedReader buffin = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            PrintWriter Pout = new PrintWriter(mySocket.getOutputStream());

            boolean stop = false;

            while (!stop) {
                //read received messgae
                String message = buffin.readLine();
                System.out.println("received message : " + message);

                if (message.equals("quit"))
                {
                    stop = true;
                    break;
                }

                //wait for an input from the console
                sc = new Scanner(System.in);
                System.out.println("Your message :");
                String messageToSend = sc.nextLine();

                //write the message on the output stream
                if (messageToSend.equals("quit"))
                {
                    stop = true;
                    Pout.println(messageToSend);
                    Pout.flush();
                    break;
                }

                Pout.println(messageToSend);
                Pout.flush();

            }

            /*Client c = new Client(serverName, password);
            Thread client = new Thread();*/

            System.out.println("We got the connexion to  "+ serverAddress);

            //wait a bit before exit
            Thread.sleep(30000);

            System.out.println("\nTerminate client program...");
            mySocket.close();


        }catch (UnknownHostException e) {

            e.printStackTrace();
        }catch (IOException e) {
            System.out.println("server connection error, dying.....");
        }catch(NullPointerException e){
            System.out.println("Connection interrupted with the server");
        }
        catch (InterruptedException e) {
            System.out.println("interrupted exception");
        }
    }

}