import java.awt.Canvas;
import javax.swing.JFrame;


public class Gen3D extends Canvas implements Runnable {

	public static final int WIDTH = 800, HEIGHT = 600;
	public static final String TITLE = "3DGen";
	
	private Thread thread;
	private boolean running = false;
	
	public static void main(String[] args) {
		Gen3D game = new Gen3D();
		JFrame gameFrame = new JFrame();
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.add(game);
		gameFrame.pack();
		gameFrame.setTitle(TITLE);
		gameFrame.setSize(WIDTH,HEIGHT);
		gameFrame.setResizable(false);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);
		game.start();
		
		System.out.println("Running...");
	}
	public void start(){
		if(running)
			return;
		running=true;
		thread=new Thread(this);
		thread.start();
	}
	private void stop() {
		if(!running)
			return;
		running=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	@Override
	public void run() {
		
	}

}
