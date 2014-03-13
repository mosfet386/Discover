import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class ChatServer extends UnicastRemoteObject implements ChatServerInt {

	private static final long serialVersionUID = 1L;
	private ArrayList<ChatClientInt> chatClients;
	
	protected ChatServer() throws RemoteException {
		super();
		chatClients=new ArrayList<ChatClientInt>();
	}
	@Override
	public synchronized void registerChatClient(ChatClientInt chatClient) throws RemoteException {
		this.chatClients.add(chatClient);	
	}
	@Override
	public synchronized void broadcastMessage(String message) throws RemoteException {
		for(int i=0; i<chatClients.size(); i++) {
			chatClients.get(i).retrieveMessage(message);}
	}
}
