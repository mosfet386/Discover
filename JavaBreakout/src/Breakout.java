import java.awt.Color;

import javax.swing.JFrame;


public class Breakout extends JFrame {

	public static void main(String[] args) {
		Breakout newGame = new Breakout();
		newGame.game();
	}
	public void game() {
		add(new Board());
		setTitle("JavaBreakout");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(GameProperties.WIDTH,GameProperties.HEIGHT);
		setLocationRelativeTo(null); //place window at center
		//setIgnoreRepaint(true); //improves response, ignore OS source repaint commands
		setResizable(false);
		setVisible(true);
	}

}
