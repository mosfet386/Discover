import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class PacListener implements KeyListener {

	PacModel world;
	
	public PacListener(PacModel world) {
		this.world = world;
	}

	@Override
	public void keyPressed(KeyEvent ke) 
	{
		int code=ke.getKeyCode();
		if(code==KeyEvent.VK_LEFT) {
			world.nextpacdir=PacModel.LEFT;
		} else if(code==KeyEvent.VK_RIGHT) {
			world.nextpacdir=PacModel.RIGHT;
		} else if(code==KeyEvent.VK_UP) {
			world.nextpacdir=PacModel.UP;
		} else if(code==KeyEvent.VK_DOWN) {
			world.nextpacdir=PacModel.DOWN;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}
