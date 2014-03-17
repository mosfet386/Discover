import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class Client {
	public static void main(String[] args) {
		if(args.length==1){
			try{
				//Create a new socket connection
				Socket socket=new Socket("localhost",4444);
				//Send a message to the server via the socket
				PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);
				printWriter.println(args[0]);
				//display the server's response
				BufferedReader bufferedReader =
						new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println("The following reply was recieved from Server: "
									+bufferedReader.readLine());
			}catch(Exception e){
				System.out.println("exception in listenSocket "+e);
				System.exit(1);
			}
		}else{System.err.println("Usage: Client <server> <name>");}
	}
}
