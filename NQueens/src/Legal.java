
public class Legal {
	
	public static boolean possibleQ(int i, int[][] queenBoard) {
		int row=i/Search.n, col=i%Search.n;
		int temp=1;
		try //Search for a queen, diagonal to selected square
		{
			while(0==queenBoard[row-temp][col-temp]) {temp++;}
			if(1==queenBoard[row-temp][col-temp]) {return false;}
		}
		catch(Exception e){}
		temp=1;
		try //Search for a queen above selected square
		{
			while(0==queenBoard[row-temp][col]) {temp++;}
			if(1==queenBoard[row-temp][col]) {return false;}
		}
		catch(Exception e){}
		temp=1;
		try //Search for a queen, antidiagonal to selected square
		{
			while(0==queenBoard[row-temp][col+temp]) {temp++;}
			if(1==queenBoard[row-temp][col+temp]) {return false;}
		}
		catch(Exception e){}
		//No need to check Horizonal, always inserting on a new row

		return true;
	}
}
