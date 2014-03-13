import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ChatClientInt extends Remote {
	void retrieveMessage(String message) throws RemoteException;
}
