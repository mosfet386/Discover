import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;


public class Board extends JPanel implements GameProperties {
	
	Image boardImage;
	Timer timer;
	String message="Game Over!";
	Ball ball;
	Paddle paddle;
	Brick bricks[];
	boolean ballInGame=true;
	int timerId;
	
	public Board() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		bricks=new Brick[30];
		setDoubleBuffered(true); //draw in hidden buffer before the screen JPanel vs Canvas
		timer=new Timer();
		//schedule for 10ms (100Hz) after a dalay of 1s
		timer.scheduleAtFixedRate(new ScheduleTask(), 1000, 5);
	}
	//Manage Keyboard Input from the board class
	//dispatch key commands to the paddle class
	private class TAdapter extends KeyAdapter {
		public void keyReleased(KeyEvent e) {paddle.keyReleased(e);}
		public void keyPressed(KeyEvent e) {paddle.keyPressed(e);}
	}
	//Tasks scheduled to repeat periodically
	private class ScheduleTask extends TimerTask {
		public void run() {
				ball.move();
				paddle.move();
				checkCollision();
				repaint();
		}
	}
	//Use Rectangle class within sprites to detect collisions
	public void checkCollision() {
		//ball rectangle leaves frame
		if(ball.getRect().getMaxY()>GameProperties.BOTTOM) {stopGame();}
		for(int i=0, j=0; i<30; i++) {
			if(bricks[i].isDestoyed()) {j++;}
			if(j==30) {message="Success!"; stopGame();}
		}
		//ball rectangle intersects paddle
		if(ball.getRect().intersects(paddle.getRect())) {
			int paddleLPos=(int)paddle.getRect().getMinX();
			int ballLPos=(int)ball.getRect().getMinX();
			int first=paddleLPos+8;
			int second=paddleLPos+16;
			int third=paddleLPos+24;
			int fourth=paddleLPos+32;
			//ball travels diagonally up and to the left
			if(ballLPos<first) {ball.setXDir(-1);ball.setYDir(-1);}
			//ball reverses y direction and to the left
			if(ballLPos>=first&&ballLPos<second) {ball.setXDir(-1);ball.setYDir(-1*ball.getYDir());}
			//ball travels straight up
			if(ballLPos>=second&&ballLPos<third) {ball.setXDir(0);ball.setYDir(-1);}
			//ball reverse y direction and to the right
			if(ballLPos>=third&&ballLPos<fourth) {ball.setXDir(1);ball.setYDir(-1*ball.getYDir());}
			//ball travels diagonally up and to the right
			if(ballLPos>fourth) {ball.setXDir(1);ball.setYDir(-1);}
		}
		//ball paddle intersects brick
		for(int i=0; i<30; i++) {  //for each brick
			if(ball.getRect().intersects(bricks[i].getRect())) { //if intersected by ball
				int ballLeft=(int)ball.getRect().getMinX();
				int ballHeight=(int)ball.getRect().getHeight();
				int ballWidth=(int)ball.getRect().getWidth();
				int ballTop=(int)ball.getRect().getHeight();
				Point pointRight=new Point(ballLeft+ballWidth+1, ballTop);
				Point pointLeft=new Point(ballLeft-1, ballTop);
				Point pointTop=new Point(ballLeft, ballTop-1);
				Point pointBottom=new Point(ballLeft, ballTop+ballHeight+1);
				if(!bricks[i].isDestoyed()) { //change direction and remove brick
					if(bricks[i].getRect().contains(pointRight)) {ball.setXDir(-1);}
					else if(bricks[i].getRect().contains(pointLeft)) {ball.setXDir(1);}
					if(bricks[i].getRect().contains(pointTop)) {ball.setYDir(1);}
					else if(bricks[i].getRect().contains(pointBottom)) {ball.setYDir(-1);}
					bricks[i].setDestroyed(true);
				}
			}
		}
	}
	public void stopGame() {
		ballInGame=false;
		timer.cancel();
	}
	public void addNotify() {
		super.addNotify();
		gameInit();
	}
	public void gameInit() {
		ball=new Ball();
		paddle=new Paddle();
		int k=0;
		for(int i=0; i<5; i++) { //replace by modulus
			for(int j=0; j<6; j++) {
				bricks[k]=new Brick(j*40+30,i*10+50);
				k++;
			}
		}
	}
	public void paint(Graphics g) {
		super.paint(g);
		setBackground(new Color(200, 100, 0));
		if(ballInGame) {
			g.drawImage(ball.getImage(), ball.getX(), ball.getY(),
						ball.getWidth(), ball.getHeight(), this);
			g.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
						paddle.getWidth(), paddle.getHeight(), this);
			for(int i=0; i<30; i++) {
				if(!bricks[i].isDestoyed())
					g.drawImage(bricks[i].getImage(), bricks[i].getX(), bricks[i].getY(),
								bricks[i].getWidth(), bricks[i].getHeight(),this);
			}
		} else {
			Font font=new Font("Verdana",Font.BOLD,18);
			FontMetrics metr=this.getFontMetrics(font);
			g.setColor(Color.BLACK);
			g.setFont(font);
			g.drawString(message, (GameProperties.WIDTH-metr.stringWidth(message))/2,
							GameProperties.WIDTH/2);
		}
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
}
