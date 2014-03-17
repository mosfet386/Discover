import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ServerThread extends Thread {
	
	Socket aClientSocket=null;
	ServerThread(Socket aClientSocket){
		this.aClientSocket=aClientSocket;
	}
	public void run() {
		try{
			ObjectInputStream serverInputObject = new ObjectInputStream(aClientSocket.getInputStream());
			System.out.println("Input stream created...");
			ObjectOutputStream serverOutputObject = new ObjectOutputStream(aClientSocket.getOutputStream());
			System.out.println("Output stream created...");
			
			Message serverRecievedObject = (Message) serverInputObject.readObject();
			doSomething(serverRecievedObject);
			serverOutputObject.writeObject(serverRecievedObject);
		} catch (IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	private void doSomething(Message message) {
		message.setResult(message.getX().intValue()*message.getY().intValue());
	}
}
 