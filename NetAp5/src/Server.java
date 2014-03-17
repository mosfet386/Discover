import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	public static final int PORT=4445;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		//to do make static
		new Server().runServer();
	}
	public void runServer() throws IOException, ClassNotFoundException {
		
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server up and accepting clients...");
		Socket aClientSocket = serverSocket.accept();
		System.out.println("Sever connected to client");
		
		ObjectInputStream serverInputObject = new ObjectInputStream(aClientSocket.getInputStream());
		System.out.println("Input stream created...");
		ObjectOutputStream serverOutputObject = new ObjectOutputStream(aClientSocket.getOutputStream());
		System.out.println("Output stream created...");
		
		Message serverRecievedObject = (Message) serverInputObject.readObject();
		doSomething(serverRecievedObject);
		serverOutputObject.writeObject(serverRecievedObject);
		aClientSocket.close();
		serverSocket.close();
	}
	private void doSomething(Message message) {
		message.setResult(message.getX().intValue()*message.getY().intValue());
	}
}
