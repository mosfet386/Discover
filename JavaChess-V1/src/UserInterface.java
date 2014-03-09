import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener{
	
	static int mouseX, mouseY, newMouseX, newMouseY;
	
	static int squareSize = 70;
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.yellow);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		//Draw Checkered Board
		for(int i=0; i<64; i+=2) {
			g.setColor(new Color(255,200,100));
			g.fillRect((i%8+(i/8)%2)*squareSize, (i/8)*squareSize, squareSize, squareSize);
			g.setColor(new Color(150,50,30));
			g.fillRect(((i+1)%8-((i+1)/8)%2)*squareSize, ((i+1)/8)*squareSize, squareSize, squareSize);
		}
		//Draw Chess Pieces
		Image chessPiecesImage;
		chessPiecesImage = new ImageIcon("ChessPieces.png").getImage();
		for(int i=0; i<64; i++) {
			int j=-1, k=-1;
			switch(JavaChess.chessBoard[i/8][i%8]){
				case "P": j=5; k=0;
					break;
				case "p": j=5; k=1;
					break;
				case "R": j=2; k=0;
					break;
				case "r": j=2; k=1;
					break;
				case "K": j=4; k=0;
					break;
				case "k": j=4; k=1;
					break;
				case "B": j=3; k=0;
					break;
				case "b": j=3; k=1;
					break;
				case "Q": j=1; k=0;
					break;
				case "q": j=1; k=1;
					break;
				case "A": j=0; k=0;
					break;
				case "a": j=0; k=1;
					break;
			}
			if(j!=-1 && k!=-1) {
				g.drawImage(chessPiecesImage, (i%8)*squareSize, (i/8)*squareSize, (i%8+1)*squareSize, (i/8+1)*squareSize, j*64, k*64, (j+1)*64, (k+1)*64, this);
			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent e){

	}
	@Override
	public void mousePressed(MouseEvent e){
		if(e.getX()<8*squareSize && e.getY()<8*squareSize) {
			//inside board
			mouseX=e.getX();
			mouseY=e.getY();
			repaint();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e){
		if(e.getX()<8*squareSize && e.getY()<8*squareSize) {
			//inside board
			newMouseX=e.getX();
			newMouseY=e.getY();
			if(e.getButton()==MouseEvent.BUTTON1) {
				String dragMove;
				if(newMouseY/squareSize==0 && mouseY/squareSize==1 && "P".equals(JavaChess.chessBoard[mouseY/squareSize][mouseX/squareSize])) {
					//pawn promotion
					dragMove = ""+mouseX/squareSize+newMouseX/squareSize+JavaChess.chessBoard[newMouseY/squareSize][newMouseX/squareSize]+"QP";
				} else {
					//normal move
					dragMove = ""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+JavaChess.chessBoard[newMouseY/squareSize][newMouseX/squareSize];
				}
				String userPossibilities = JavaChess.possibleMoves();
				if(userPossibilities.replaceAll(dragMove, "").length()<userPossibilities.length()) {
					//if valid move
					JavaChess.makeMove(dragMove);
					JavaChess.flipBoard();
					JavaChess.makeMove(JavaChess.alphaBeta(JavaChess.globaldepth, Integer.MAX_VALUE, Integer.MIN_VALUE, "", 0));
					JavaChess.flipBoard();
					repaint();
				}
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e){

	}
	@Override
	public void mouseDragged(MouseEvent e){

	}
	@Override
	public void mouseEntered(MouseEvent e){

	}
	@Override
	public void mouseExited(MouseEvent e){}
}
