import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class PacView extends Canvas{
	
	private static final long serialVersionUID=1L;
	PacModel world;
	final int T=40;
	
	public PacView(PacModel world) 
	{
		this.world=world;
		setSize(world.getWidth()*T,world.getHeight()*T);
	}
	
	public void update(Graphics g)
	{
		Image im = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		paint(im.getGraphics()); 
		g.drawImage(im, 0, 0, null);
	}
	
	public void paint(Graphics g)
	{
		//DRAW WORLD BOARD
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		
		//DRAW WORLD OBJECTS
		for(int x=0; x<world.getWidth(); x++)
		{
			for(int y=0;y<world.getHeight();y++)
			{
				char c=world.getCell(x,y);
				switch(c)
				{
					case '.': 
						if((world.time/8)%4!=0)
							g.drawOval(x*T+T/3, y*T+T/3, T/3, T/3); 
						break;
					case '#': g.fillRect(x*T, y*T, T, T); break;
					case 'o': g.drawOval(x*T+1, y*T+1, T-2, T-2); break;
					case ' ': break;
					default: System.err.println("What is a '" + c + "'");
				}
			}
		}
		
		//Draw PacMan
		g.setColor(Color.YELLOW);
		g.fillArc((int)(world.pacx*T),(int)(world.pacy*T),T,T,6*world.pacmouth,360-2*6*world.pacmouth);
		g.setColor(Color.BLACK);
		g.drawArc((int)(world.pacx*T),(int)(world.pacy*T),T,T,6*world.pacmouth,360-2*6*world.pacmouth);
		g.fillOval((int)(world.pacx*T)+T/2,(int)(world.pacy*T)+T/4,T/10,T/10);
		
		//DRAW GHOSTS
		for(Ghost ghost : world.ghosts)
		{
			g.setColor(ghost.color);
			g.fillOval((int)(ghost.x*T),(int)(ghost.y*T),T,T);
			g.fillRect((int)(ghost.x*T),(int)(ghost.y*T)+T/2,T,T/2);
			g.setColor(Color.WHITE);
			g.fillOval((int)(ghost.x*T)+T/3-T/10,(int)(ghost.y*T)+T/2-T/10,T/5,T/5);
			g.fillOval((int)(ghost.x*T)+2*(T/3)-T/10,(int)(ghost.y*T)+T/2-T/10,T/5,T/5);
		}
		
	}
}
