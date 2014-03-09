
public class JavaSudoku {
	
	//[row][column]
	static int userGrid[][] = new int[][] {
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,2}};
	static int grid[][] = new int[9][9]; //experimental grid
	//Static String pgrid[][] = new String[9][9]; //possibilities grid
	
	public static void main(String[] args){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				grid[i][j]=userGrid[i][j];
			}
		}
		print(grid);
		double timestart=System.currentTimeMillis();
		print(loop(0, 0, grid));
		double timeend=System.currentTimeMillis();
		System.out.println("\nSolution Time: " + (timeend-timestart) + "ms");
	}
	public static int[][] loop2(int y, int x, int[][] grid){
		//while not solved
		while(!validity(8,8,grid) || grid[8][8]==0)
		{
			//If user defined value
			if(userGrid[y][x]!=0){
				//proceed to validate next square
				int yy, xx;
				if(x==8){yy=y+1; xx=0;} else{yy=y; xx=x+1;}
				loop(yy, xx, grid);
			} else {
				//Don't increment beyond 9
				if(grid[y][x]<9)
				{
					//try adding 1 to grid value
					grid[y][x]++;
					if(validity( y, x, grid))
					{
						//if adding 1 is valid proceed to validate next square
						int yy, xx;
						if(x==8){yy=y+1; xx=0;} else{yy=y; xx=x+1;}
						loop(yy, xx, grid);
					}
				} else {grid[y][x]=0; break;}
			}
		}
		return grid;
	}
	public static int[][] loop(int y, int x, int[][] grid){
		//while not solved
		while(!validity(8,8,grid) || grid[8][8]==0)
		{
			//Don't increment beyond 9
			if(grid[y][x]<9)
			{
				//If not user defined value
				if(userGrid[y][x]==0){
					//try adding 1 to grid value
					grid[y][x]++;
				}
				if(validity( y, x, grid))
				{
					//if adding 1 is valid proceed to validate next square
					int yy, xx;
					if(x==8){yy=y+1; xx=0;} else{yy=y; xx=x+1;}
					loop(yy, xx, grid);
				}
			} else {grid[y][x]=0; break;}
		}
		return grid;
	}
	public static boolean validity(int x, int y, int[][] grid){
		String temp = "";
		for(int i=0; i<9; i++){
			temp+=Integer.toString(grid[i][y]); //row
			temp+=Integer.toString(grid[x][i]); //column
			temp+=Integer.toString(grid[(x/3)*3+i/3][(y/3)*3+i%3]); //square
		}
		int count=0, idx=0;
		while((idx=temp.indexOf(Integer.toString(grid[x][y]), idx))!=-1){
			idx++; count++;
		}
		return count==3;
	}
	public static boolean validity2(int x, int y, int[][] grid){
		String temp = "";
		for(int i=0; i<9; i++){
			temp+=Integer.toString(grid[i][y]); //row
			temp+=Integer.toString(grid[x][i]); //column
			temp+=Integer.toString(grid[(x/3)*3+i/3][(y/3)*3+i%3]); //square
		}
		return temp.length()-temp.replace(Integer.toString(grid[x][y]), "").length()==3;
	}
	public static void print(int[][] grid){
		System.out.println();
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				System.out.print(grid[i][j]);
			}
			System.out.println();
		}
	}

}
