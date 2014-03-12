import javax.swing.ImageIcon;


/**
 * Ball Class:
 *	Defines the characteristics of a ball sprite
 */
public class Ball extends Sprite implements GameProperties {

	private int dx,dy;
	protected String ballImagePath=System.getProperty("user.dir")+"/images/ball.png";
	
	public Ball() {
		//initialize ball direction to upper right
		dx=1; 
		dy=-1;
//		ImageIcon ballImage =
//				new ImageIcon(this.getClass().getResource(ballImagePath));
		
		ImageIcon ballImage = new ImageIcon(System.getProperty("user.dir")+"/images/ball.png");
		image=ballImage.getImage();
		width=ballImage.getIconWidth();
		height=ballImage.getIconHeight();
		resetState();
	}
	public void resetState() {x=230;y=355;}
	public void setXDir(int x) {dx=x;}
	public void setYDir(int y) {dy=y;}
	public int getYDir() {return dy;}
	public void move() {
		x+=dx;
		y+=dy;
		if(x==0) {setXDir(1);}  //bounce off fame's left side
		if(x==BALL_RIGHT) {setXDir(-1);} //bounce back from frame's right side
		if(y==0) {setYDir(1);} //bounce off frame's top side
	}
	
}
