import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JavaTetris extends JFrame {

	JLabel statusbar;
		
		public JavaTetris() {
			statusbar=new JLabel(" 0");
			add(statusbar, BorderLayout.SOUTH);
			Board board= new Board(this);
			add(board);
			board.start();
			
			setSize(200,400);
			setTitle("JavaTetris");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		public JLabel getStatusBar() {
			return statusbar;
		}
		public static void main(String[] args) {
			JavaTetris newGame=new JavaTetris();
			newGame.setLocationRelativeTo(null);
			newGame.setVisible(true);
		}
}
