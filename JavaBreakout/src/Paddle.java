import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 * Paddle Class:
 *	Defines the characteristics of a paddle sprite
 */
public class Paddle extends Sprite implements GameProperties {
	
	protected String paddleImagePath=System.getProperty("user.dir")+"/images/brick.png";
	int dx; //paddle x speed
	
	public Paddle() {
//		ImageIcon paddleImage =
//				new ImageIcon(this.getClass().getResource(paddleImagePath));
		ImageIcon paddleImage = new ImageIcon(System.getProperty("user.dir")+"/images/paddle.png");
		image=paddleImage.getImage();
		width=image.getHeight(null);
		height=image.getHeight(null);
		resetState();
	}
	public void resetState() {x=200;y=360;} //reset the paddle to the bottom right
	public void move() {
		x+=dx;
		//maintain paddle within the frame's horizontal bounds
		if(x<=2) {x=2;}
		if(x>=GameProperties.PADDLE_RIGHT) {x=GameProperties.PADDLE_RIGHT;}
	}
	public void keyPressed(KeyEvent e) {
		int key=e.getKeyCode();
		if(key==KeyEvent.VK_LEFT) {dx=-2;}
		if(key==KeyEvent.VK_RIGHT) {dx=2;}
	}
	public void keyReleased(KeyEvent e) {
		int key=e.getKeyCode();
		if(key==KeyEvent.VK_LEFT || key==KeyEvent.VK_RIGHT) {dx=0;}
	}

}
