package Server;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Connection {
    private InetAddress ia = null;
    private String ipConnection = null;


    /**
     * Constructeur de la classe Connection qui sert à initier la Network Interface utilisée par le serveur
     */

    public Connection()
    {
        try {
            //list of all interfaces
            Enumeration<NetworkInterface> allni;
            //get all the interfaces of your machine
            allni = NetworkInterface.getNetworkInterfaces();
            while(allni.hasMoreElements()) {
                NetworkInterface nix = allni.nextElement();
                //get the interfaces names if connected
                if (nix.isUp()){
                    //get the addresses of each interface
                    Enumeration<InetAddress> LocalAddress =  nix.getInetAddresses();
                    while(LocalAddress.hasMoreElements()) {
                        InetAddress ia = LocalAddress.nextElement();
                        if(!ia.isLinkLocalAddress()) {
                            if(!ia.isLoopbackAddress()) {
                                this.ia = ia;
                                ipConnection = ia.getHostAddress();
                                System.out.println(ipConnection);
                            }
                        }
                    }
                }
                if(ia!=null && ipConnection!= null) break;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode de la classe Connection qui sert à get l'adresse ip du serveur
     */

    public InetAddress getIa() {
        return ia;
    }
}
