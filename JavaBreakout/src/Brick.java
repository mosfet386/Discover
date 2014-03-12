import javax.swing.ImageIcon;


/**
 * Brick Class:
 *	Defines the aspects of a Brick Sprite
 */
public class Brick extends Sprite {
	
	protected String brickImagePath=System.getProperty("user.dir")+"/images/brick.png";
	boolean destroyed;
	
	public Brick(int x, int y) {
		this.x=x;
		this.y=y;
//		ImageIcon brickImage =
//				new ImageIcon(this.getClass().getResource(brickImagePath));
		ImageIcon brickImage = new ImageIcon(System.getProperty("user.dir")+"/images/brick.png");
		image=brickImage.getImage();
		width=image.getWidth(null);
		height=image.getHeight(null);
		destroyed=false;		
	}
	public boolean isDestoyed() {return destroyed;}
	public void setDestroyed(boolean destroyed) {this.destroyed=destroyed;}
}
