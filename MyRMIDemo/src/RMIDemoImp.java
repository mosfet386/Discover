import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RMIDemoImp extends UnicastRemoteObject implements RMIDemo  {

	private static final long serialVersionUID=1L;
	protected RMIDemoImp() throws RemoteException {super();}
	public String doCommunicate(String name) throws RemoteException {
		return "\nServer says: Hi " + name + "\n";
	}
}
