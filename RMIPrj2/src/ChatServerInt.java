import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ChatServerInt extends Remote {
	void registerChatClient(ChatClientInt chatClient) throws RemoteException;
	void broadcastMessage(String message) throws RemoteException;
}
