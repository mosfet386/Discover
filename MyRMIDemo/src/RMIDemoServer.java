import java.net.InetAddress;
import java.rmi.Naming;


public class RMIDemoServer {

	public static void main(String[] args) throws Exception {
		RMIDemoImp rmiDemoImp=new RMIDemoImp();
		Naming.rebind("RMIDemo", rmiDemoImp);
		System.out.println("RMIDemo object bound to the name 'RMIDemo' and is ready for use...");
	}

}
