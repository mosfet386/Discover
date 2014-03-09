import javax.swing.JFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		JFrame app = new JFrame("Pac Man");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		Frame app = new Frame("Pac Man");
//		app.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent we){System.exit(0);}});
		
		PacModel world = new PacModel();
		PacView screen = new PacView(world);
		
		app.add(screen);
		app.pack();
		app.setVisible(true);

		screen.addKeyListener(new PacListener(world));
		
		while(!world.gameOver())
		{
			world.tick();
			screen.repaint();
			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
 
	}

}
