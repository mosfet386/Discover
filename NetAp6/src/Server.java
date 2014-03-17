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
		while(true){
			Socket aClientSocket = serverSocket.accept();
			System.out.println("Sever connected to client");
			new ServerThread(aClientSocket).start();
		}
	}
}
