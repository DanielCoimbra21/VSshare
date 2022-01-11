package Server;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Connection {

    private InetAddress ia = null;
    private String ipConnection = null;

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
                    //System.out.println("interface name: " + nix.getName());

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getIpConnection() {
        return ipConnection;
    }

    public InetAddress getIa() {
        return ia;
    }
}
