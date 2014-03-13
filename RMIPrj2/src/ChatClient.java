import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ChatClient extends UnicastRemoteObject implements ChatClientInt, Runnable {

	private static final long serialVersionUID = 1L;
	private ChatServerInt chatServer;
	private String name=null;
	
	protected ChatClient(String name, ChatServerInt chatServer) throws RemoteException {
		super();
		this.name=name;
		this.chatServer=chatServer;
		chatServer.registerChatClient(this);
	}
	@Override
	public void retrieveMessage(String message) throws RemoteException {
		System.out.println("message: "+message);		
	}
	@Override
	public void run() {
		Scanner scanner=new Scanner(System.in);
		String message;
		while(true) {
			message=scanner.nextLine();
			try {chatServer.broadcastMessage(name+" : "+message);}
			catch (RemoteException e) {e.printStackTrace();}
		}
	}
	

}
