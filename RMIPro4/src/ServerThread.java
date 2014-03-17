import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerThread extends Thread {
	Socket socket;
	public ServerThread(Socket clientSocket) {
		socket=clientSocket;
	}
	public void run(){
		try {
			String message=null;
			PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("user '"+bufferedReader.readLine()+"' is now connectedto the server...");
			while(null != (message=bufferedReader.readLine())){
				System.out.println("Incoming client message: "+message);
				printWriter.println("Server echoing Client message==>"+message);
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
