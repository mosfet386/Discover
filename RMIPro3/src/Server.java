import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	public static void main(String[] args) {
		try{
			//create a server socket, use it to attache client sockets
			ServerSocket serverSocket=new ServerSocket(4444);
			Socket socket=serverSocket.accept();
			//Read & Print out the client's message
			BufferedReader bufferedReader =
					new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message=bufferedReader.readLine();
			System.out.println("The following message was recieved from Client: "+message);
			//Echo back to the client
			PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);
			printWriter.println("Server echoing back the following message '"
								+message+"' from Client");
		}catch(IOException e){
			System.out.println("exception "+e);
			System.exit(-1);
		}
	}
}
