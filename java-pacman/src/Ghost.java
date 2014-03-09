import java.awt.Color;


public class Ghost {
	
	float x, y;
	int dir;
	Color color;
	
	public Ghost(int x, int y, Color c)
	{
		this.x=x;
		this.y=y;
		color=c;
	}
	
	public void chooseDir(PacModel world) 
	{
		float dx=world.pacx-x;
		float dy=world.pacy-y;
		
		if(Math.abs(dx)>Math.abs(dy))
		{
			if(dx<0)
				dir=PacModel.LEFT;
			else
				dir=PacModel.RIGHT;
		} 
		else 
		{
			if(dy<0)
				dir=PacModel.UP;
			else
				dir=PacModel.DOWN;
		}
	}

}
