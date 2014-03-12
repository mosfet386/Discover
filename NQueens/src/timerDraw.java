
public class timerDraw implements Runnable {

	@Override //Second Thread
	public void run() {
		//Continuously repaint gameFrame each second
		while(true)
		{
			UserInterface.gameFrame.repaint();
			try {Thread.sleep(1000);} 
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	//Part of main thread
	public static void timerCounter() {
		while(true){UserInterface.counter++;}
	}

}
