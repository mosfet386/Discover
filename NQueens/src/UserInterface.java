import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class UserInterface extends JPanel {
	
	static JFrame gameFrame=new JFrame("N Queens Solver");
	static UserInterface gameUI=new UserInterface();
	static int counter;
	static int currentHKey=-1;
	static double squareSize;
	static Thread SearchThread;
	static int activeQueenBoard[][]=new int[Search.n][Search.n];
	
	public static void start() {
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.add(gameUI);
		gameFrame.setSize(750,512);
		gameFrame.setLocation(
				(Toolkit.getDefaultToolkit().getScreenSize().width-gameFrame.getWidth())/2,
				(Toolkit.getDefaultToolkit().getScreenSize().height-gameFrame.getHeight())/2
				);
		gameFrame.setVisible(true);
		squareSize=(double)(Math.min(gameUI.getHeight(), gameUI.getWidth()-240)-20)/Search.n;
		Thread TimerThread=new Thread(new timerDraw());
		TimerThread.start();
		//timerDraw.timerCounter();
		computerThink();
	}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(200, 100, 0));
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID()==KeyEvent.KEY_PRESSED) {
                    if (!Search.solutionsHMap.isEmpty()) {
                        if (e.getKeyCode()==KeyEvent.VK_DOWN) {
                            int length=Search.solutionsHMap.size();
                            if (length-1==currentHKey) {
                                currentHKey=0;
                                getSolution();
                            } else {
                                currentHKey++;
                                getSolution();
                            }
                        }
                        if (e.getKeyCode()==KeyEvent.VK_UP) {
                            int length=Search.solutionsHMap.size();
                            if (currentHKey==0) {
                                currentHKey=length-1;
                                getSolution();
                            } else {
                                currentHKey--;
                                getSolution();
                            }
                        }
                    }
                    if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
                        Search.n++;
                        newDimension();
                    }
                    if (e.getKeyCode()==KeyEvent.VK_LEFT) {
                        if (Search.n>4) {
                            Search.n--;
                            newDimension();
                        }
                    }
                }
                return true;
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                squareSize=(double)(Math.min(getHeight(), getWidth()-240)-20)/Search.n;
            }
        });
        if (Search.n%2==0) {
            for (int i=0;i<Search.n*Search.n;i+=2) {//draw chess board
                g.setColor(new Color(255, 200, 100));
                g.fillRect((int)((i%Search.n+(i/Search.n)%2)*squareSize)+10, (int)((i/Search.n)*squareSize)+10, (int)squareSize, (int)squareSize);
                g.setColor(new Color(150, 50, 30));
                g.fillRect((int)(((i+1)%Search.n-((i+1)/Search.n)%2)*squareSize)+10, (int)(((i+1)/Search.n)*squareSize)+10, (int)squareSize, (int)squareSize);
            }
        } else {
            for (int i=0;i<Search.n*Search.n-1;i+=2) {//draw chess board
                g.setColor(new Color(255, 200, 100));
                g.fillRect((int)((i%Search.n)*squareSize)+10, (int)((i/Search.n)*squareSize)+10, (int)squareSize, (int)squareSize);
                g.setColor(new Color(150, 50, 30));
                g.fillRect((int)(((i+1)%Search.n)*squareSize)+10, (int)(((i+1)/Search.n)*squareSize)+10, (int)squareSize, (int)squareSize);
            }
            int i=Search.n*Search.n-1;
            g.setColor(new Color(255, 200, 100));
            g.fillRect((int)((i%Search.n)*squareSize)+10, (int)((i/Search.n)*squareSize)+10, (int)squareSize, (int)squareSize);
        }
        g.setColor(new Color(100, 0, 0));
        g.fill3DRect(0, 10, 10, (int)(Search.n*squareSize), true);
        g.fill3DRect((int)(Search.n*squareSize)+10, 10, 10, (int)(Search.n*squareSize), true);
        g.fill3DRect(10, 0, (int)(Search.n*squareSize), 10, true);
        g.fill3DRect(10, (int)(Search.n*squareSize)+10, (int)(Search.n*squareSize), 10, true);
        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, 10, 10, true);
        g.fill3DRect((int)(Search.n*squareSize)+10, 0, 10, 10, true);
        g.fill3DRect(0, (int)(Search.n*squareSize)+10, 10, 10, true);
        g.fill3DRect((int)(Search.n*squareSize)+10, (int)(Search.n*squareSize)+10, 10, 10, true);
        
        Image chessPieceImage;
        chessPieceImage=new ImageIcon(System.getProperty("user.dir")+"/Images/ChessPieces.png").getImage();
        for (int i=0;i<Search.n*Search.n;i++) {
            int j=-1,k=-1;
            if (activeQueenBoard[i/Search.n][i%Search.n]==1) {j=1;k=0;}//k=1 is the other colored queen
            if (j!=-1 && k!=-1) {
                g.drawImage(chessPieceImage, (int)((i%Search.n)*squareSize)+10, (int)((i/Search.n)*squareSize)+10, (int)((i%Search.n+1)*squareSize)+10, (int)((i/Search.n+1)*squareSize)+10, j*64, k*64, (j+1)*64, (k+1)*64, this);
            }
        }
        g.setColor(Color.BLACK);
        Font fontDepth=new Font("Serif", Font.PLAIN, 20);
        g.setFont(fontDepth);
        int x=(int)(Search.n*squareSize)+20+10;
        int y=20;
        String intType;
        switch (currentHKey+1) {//in order of likelyhood of that piece being selected
            case 1: intType="st";
                break;
            case 2: intType="nd";
                break;
            case 3: intType="rd";
                break;
            default: intType="th";
                break;
        }
        g.drawString("STATS",x+60,y);
        g.drawString("1) Grid size: "+Search.n+"x"+Search.n,x,y+2*g.getFont().getSize());
        if (Search.doneSearch) {
            g.drawString("2) There are "+Search.progress+" solutions.",x,y+3*g.getFont().getSize());
            g.drawString("3) "+Search.totalNodes+" positions were",x,y+4*g.getFont().getSize());
            g.drawString("considered.",x,y+5*g.getFont().getSize());
            g.drawString("4) That took "+((Search.endTime-Search.startTime)/1000)+" seconds.",x,y+6*g.getFont().getSize());
            //g.drawString("6) There are "+Search.HMapUnique.size()+" unique solutions.",x,y+17*g.getFont().getSize());
        } else {
            g.drawString("2) Currently, "+Search.progress+" solutions",x,y+3*g.getFont().getSize());
            g.drawString("have been found.",x,y+4*g.getFont().getSize());
            g.drawString("3) "+Search.totalNodes+" positions have",x,y+5*g.getFont().getSize());
            g.drawString("been considered.",x,y+6*g.getFont().getSize());
            g.drawString("4) "+((System.currentTimeMillis()-Search.startTime)/1000)+" seconds",x,y+7*g.getFont().getSize());
            g.drawString("have elapsed so far.",x,y+8*g.getFont().getSize());
        }
        g.drawString("5) You are currently looking at",x,y+10*g.getFont().getSize());
        g.drawString("the "+(currentHKey+1)+intType+" solution.",x,y+11*g.getFont().getSize());
        
        g.setColor(Color.WHITE);
        g.drawString("UP/DOWN arrow keys",x,y+13*g.getFont().getSize());
        g.drawString("navigate through solutions",x,y+14*g.getFont().getSize());
        g.drawString("RIGHT/LEFT arrow keys",x,y+15*g.getFont().getSize());
        g.drawString("adjust grid size",x,y+16*g.getFont().getSize());
    }	
	
	public static void computerThink() {
		activeQueenBoard=new int[Search.n][Search.n];
		SearchThread=new Thread(new Search());
		SearchThread.start();
		gameFrame.repaint();
	}
	public static void newDimension() {
		computerThink();
		squareSize=(double)(Math.min(gameUI.getHeight(), gameUI.getWidth()-240)-20)/Search.n;
	}
	public static void getSolution() {
		activeQueenBoard=Search.solutionsHMap.get(currentHKey);
		gameFrame.repaint();
	}

}
