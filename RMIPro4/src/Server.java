import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	public final int PORT=4444;
	public static void main(String[] args) throws IOException {
		new Server().runServer();
		//runServer();
	}
	public void runServer() throws IOException{
		ServerSocket serverSocket=new ServerSocket(PORT);
		System.out.println("Server up & ready for connections...");
		while(true){
			Socket clientSocket=serverSocket.accept(); //waits on client call then loops
			new ServerThread(clientSocket).start();
		}
	}
}
