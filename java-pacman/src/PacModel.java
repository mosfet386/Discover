import java.awt.Color;
import java.util.ArrayList;

public class PacModel {
	private String startboard =
			"####################\n"
		  + "#o.....#...#...#..o#\n"
		  + "#.####.#.#...#...#.#\n"
		  + "#......#...#...#...#\n"
		  + "#..##......#.......#\n"
		  + "#..................#\n"
		  + "#......##.###......#\n"
		  + "#......#....#......#\n"
		  + "#......######......#\n"
		  + "#..................#\n"
		  + "#..................#\n"
		  + "#..................#\n"
		  + "#..................#\n"
		  + "#..................#\n"
		  + "#..................#\n"
		  + "#..................#\n"
		  + "#..................#\n"
		  + "#o................o#\n"
		  + "####################";
	
	private ArrayList<ArrayList<Character>> board = new ArrayList<ArrayList<Character>>();
	public int time=0;
	public float pacx=5, pacy=6;
	public final static int STILL=0, UP=1, RIGHT=2, DOWN=3, LEFT=4;
	public int pacdir=RIGHT;
	public int nextpacdir=pacdir;
	public int pacmouth=10;
	public boolean closing=true;
	public ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
	
	public PacModel()
	{
		for(String row : startboard.split("\n"))
		{
			ArrayList<Character> r = new ArrayList<Character>();
			for(int i=0; i<row.length(); i++)
				r.add(row.charAt(i));
			board.add(r);
		}	
		
		ghosts.add(new Ghost(3,10,Color.PINK));
		ghosts.add(new Ghost(16,16,Color.CYAN));
	}
	
	public int getHeight() 
	{
		return board.size();
	}

	public int getWidth() 
	{
		return board.get(0).size();
	}

	public char getCell(int x, int y) 
	{
		return board.get(y).get(x);
	}
	
	public void setCell(int x, int y, char c)
	{
		board.get(y).set(x,  c);
	}
	
	public void tick()
	{
		time += 1;
		
		if(pacdir==RIGHT && nextpacdir==LEFT)
			pacdir=LEFT;
		else if(pacdir==LEFT && nextpacdir==RIGHT)
			pacdir=RIGHT;
		else if(pacdir==UP && nextpacdir==DOWN)
			pacdir=DOWN;
		else if(pacdir==DOWN && nextpacdir==UP)
			pacdir=UP;
		else if(Math.abs(pacx-(int)pacx)<0.1 && Math.abs(pacy-(int)pacy)<0.1)
			pacdir=nextpacdir;
		
		if(pacmouth==0)
			closing=false;
		else if(pacmouth==10)
			closing=true;
		
		if(closing)
			pacmouth--;
		else
			pacmouth++;
		
		int x=Math.round(pacx);
		int y=Math.round(pacy);
		
		if(getCell(x,y)=='.')
			setCell(x,y,' ');
		
		if((pacdir==RIGHT && getCell(x+1,y)=='#' && pacx>=x)
			|| (pacdir==LEFT && getCell(x-1,y)=='#' && pacx<=x)
			|| (pacdir==UP && getCell(x,y-1)=='#' && pacy<=y)
			|| (pacdir==DOWN && getCell(x,y+1)=='#' && pacy>=y)){
			pacdir=STILL;
			pacx=x;
			pacy=y;
		}
		
		//Move PacMan
		if(pacdir==RIGHT)
			pacx+=0.1;
		else if(pacdir==LEFT)
			pacx-=0.1;
		else if(pacdir==UP)
			pacy-=0.1;
		else if(pacdir==DOWN)
			pacy+=0.1;
		
		//Move Ghosts
		for(Ghost g :ghosts)
		{
			if(Math.abs(g.x-(int)g.x)<0.1 && Math.abs(g.y-(int)g.y)<0.1)
			{
				g.chooseDir(this);
			}
			
			int a=Math.round(g.x);
			int b=Math.round(g.y);

			if((g.dir==RIGHT && getCell(a+1,b)=='#' && g.x>=a)
				|| (g.dir==LEFT && getCell(a-1,b)=='#' && g.x<=a)
				|| (g.dir==UP && getCell(a,b-1)=='#' && g.y<=b)
				|| (g.dir==DOWN && getCell(a,b+1)=='#' && g.y>=b)){
				g.dir=STILL;
				g.x=a;
				g.y=b;
			}
			
			if(g.dir==LEFT)
				g.x-=0.08;
			else if(g.dir==RIGHT)
				g.x+=0.08;
			else if(g.dir==UP)
				g.y-=0.08;
			else if(g.dir==DOWN)
				g.y+=0.08;
			
		}
	}

	public boolean gameOver() 
	{
		return false;
	}
}
