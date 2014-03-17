import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class JavaMinesweeper extends JFrame implements Runnable {
	
	private final int FRAME_WIDTH=250;
	private final int FRAME_HEIGHT=290;
	private final JLabel statusbar;
	
	public JavaMinesweeper() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("JavaMinesweeper");
		statusbar=new JLabel("");
		add(statusbar,BorderLayout.SOUTH);
		add(new Board(statusbar));
		setResizable(false);
	}
	public static void main(String[] args) {
		Thread gameThread=new Thread(new JavaMinesweeper());
		gameThread.start();
	}

	public void run() {
		JFrame gameFrame=new JavaMinesweeper();
		gameFrame.setVisible(true);
	}

}
