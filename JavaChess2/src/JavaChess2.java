/***************************************************
Chess Programming: http://chessprogramming.wikispaces.com/
		 Chess960: http://en.wikipedia.org/wiki/Chess960_starting_position
		BitBoards: http://www.onjava.com/pub/a/onjava/2005/02/02/bitsets.html?page=2
    Opening Moves: http://www.eudesign.com/chessops
       More Ideas: http://www.gamedev.net
    Chess Engines: http://mediocrechess.sourceforge.net/guides/movegeneration.html
 Stockfish Engine: http://stockfishchess.org
****************************************************/
	
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JavaChess2 extends JPanel {
	
	private static final long serialVersionUID = 1L;
	static int humanIsWhite=1;
	static int rating=0;
	static int border=10;//Spacing around the frame
	static double squareSize=64; //Board square size
	static JFrame gameFrame = new JFrame("JavaChess2");
	static JavaChess2 gameUI = new JavaChess2(); //Static allows repaint calls outside of JavaChess2 Class
	static boolean CWK=true,CWQ=true,CBK=true,CBQ=true,WhiteToMove=true;;//true=castle is possible
    static boolean UniCWK=true,UniCWQ=true,UniCBK=true,UniCBQ=true;//true=castle is possible
    static long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,   
				BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L,
				EP=0L;
	static long UniWP=0L,UniWN=0L,UniWB=0L,UniWR=0L,UniWQ=0L,UniWK=0L,   
			UniBP=0L,UniBN=0L,UniBB=0L,UniBR=0L,UniBQ=0L,UniBK=0L,
			UniEP=0L;
	
	public static void main(String[] args) {
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //enable window close button
		gameFrame.add(gameUI); //attach JavaChess2's panels to frame
		gameFrame.setSize(757,870);
		gameFrame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-gameFrame.getWidth())/2,
				(Toolkit.getDefaultToolkit().getScreenSize().height-gameFrame.getHeight())/2);
		gameFrame.setVisible(true);
		newGame();
        gameFrame.repaint();
//		try {
//			testframework.test();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public static void newGame() {
		
		//BoardCreation.initiateChess960();
		BoardCreation.initiateStandardChess();
        CWK=true; CWQ=true; CBK=true; CBQ=true;
        EP=0;
        BoardCreation.drawArray(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
        PerformanceTest.perft(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove,0);
        System.out.print(PerformanceTest.perftMoveCounter);
		//Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
		
	}
	@Override
	public void paintComponent(Graphics gameGraphics) {
		super.paintComponent(gameGraphics);
		this.setBackground(new Color(200,100,0));
		this.addComponentListener(new ComponentAdapter() { //create anonymous class
			@Override
			public void componentResized(ComponentEvent e) {
				squareSize=(double)(Math.min(getHeight(), getWidth()-200-border)-2*border)/8;
			}
		});
		drawBorders(gameGraphics);
		drawBoard(gameGraphics);
		drawPieces(gameGraphics);
	}
	public void drawBoard(Graphics boardGraphics) {
		int wSquareX=0, wSquareY=0, bSquareX=0, bSquareY=0;
		for(int i=0; i<64; i+=2) { //draw board
			wSquareX=(int)((i%8+(i/8)%2)*squareSize);
			wSquareY=(int)((i/8)*squareSize);
			bSquareX=(int)(((i+1)%8-((i+1)/8)%2)*squareSize);
			bSquareY=(int)(((i+1)/8)*squareSize);
			boardGraphics.setColor(new Color(255,200,100));
			boardGraphics.fillRect(wSquareX+border, wSquareY+border,
										(int)squareSize, (int)squareSize);
			boardGraphics.setColor(new Color(150,50,30));
			boardGraphics.fillRect(bSquareX+border, bSquareY+border,
										(int)squareSize, (int)squareSize);
		}
	}
	public void drawPieces(Graphics pieceGraphics) {
		Image chessPieceImage;
		chessPieceImage = new ImageIcon(System.getProperty("user.dir")+"/Images/ChessPieces.png").getImage();
        for (int i=0;i<64;i++) {
            int imageX=-1,imageY=-1;
            if (((WP>>i)&1)==1) {imageX=5; imageY=1-humanIsWhite;}
            else if (((BP>>i)&1)==1) {imageX=5; imageY=humanIsWhite;}
            else if (((WB>>i)&1)==1) {imageX=3;imageY=1-humanIsWhite;}
            else if (((BB>>i)&1)==1) {imageX=3;imageY=humanIsWhite;}
            else if (((WN>>i)&1)==1) {imageX=4;imageY=1-humanIsWhite;}
            else if (((BN>>i)&1)==1) {imageX=4;imageY=humanIsWhite;}
            else if (((WQ>>i)&1)==1) {imageX=1;imageY=1-humanIsWhite;}
            else if (((BQ>>i)&1)==1) {imageX=1;imageY=humanIsWhite;}
            else if (((WR>>i)&1)==1) {imageX=2;imageY=1-humanIsWhite;}
            else if (((BR>>i)&1)==1) {imageX=2;imageY=humanIsWhite;}
            else if (((WK>>i)&1)==1) {imageX=0;imageY=1-humanIsWhite;}
            else if (((BK>>i)&1)==1) {imageX=0;imageY=humanIsWhite;}
            if (imageX!=-1 && imageY!=-1) {
            	int destX1=(int)((i%8)*squareSize)+border;
            	int destY1=(int)((i/8)*squareSize)+border;
            	int destX2=(int)((i%8+1)*squareSize)+border;
            	int destY2=(int)((i/8+1)*squareSize)+border;
            	int sourceX1=imageX*64;
            	int sourceY1=imageY*64;
            	int sourceX2=(imageX+1)*64;
            	int sourceY2=(imageY+1)*64;
            	pieceGraphics.drawImage(chessPieceImage, 
            			destX1, destY1, destX2, destY2,
            			sourceX1, sourceY1, sourceX2, sourceY2, this);
            }
        }
	}
    public void drawBorders(Graphics g) {
        g.setColor(new Color(100, 0, 0));
        g.fill3DRect(0, border, border, (int)(8*squareSize), true);
        g.fill3DRect((int)(8*squareSize)+border, border, border, (int)(8*squareSize), true);
        g.fill3DRect(border, 0, (int)(8*squareSize), border, true);
        g.fill3DRect(border, (int)(8*squareSize)+border, (int)(8*squareSize), border, true);
        
        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, border, border, true);
        g.fill3DRect((int)(8*squareSize)+border, 0, border, border, true);
        g.fill3DRect(0, (int)(8*squareSize)+border, border, border, true);
        g.fill3DRect((int)(8*squareSize)+border, (int)(8*squareSize)+border, border, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, 0, border, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, (int)(8*squareSize)+border, border, border, true);
        
        g.setColor(new Color(0,100,0));
        g.fill3DRect((int)(8*squareSize)+2*border, 0, 200, border, true);
        g.fill3DRect((int)(8*squareSize)+2*border+200, border, border, (int)(8*squareSize), true);
        g.fill3DRect((int)(8*squareSize)+2*border, (int)(8*squareSize)+border, 200, border, true);
    }
}
