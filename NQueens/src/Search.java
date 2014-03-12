//import java.awt.Cursor;
//import java.awt.Toolkit;
//import java.util.HashMap;
//
//public class Search implements Runnable {
//	
//	static int n=4; //board size nxn
//	static int progress;
//	static int solutions; //number of legal positions found
//	static int totalNodes; //number of positions that have been examined
//	static long startTime, endTime; //search time
//	static boolean doneSearch; //thearded search can terminate
//	static HashMap<Integer, int[][]> solutionsHMap=new HashMap<Integer, int[][]>();
//	
//	public static void guess(int row, int col, int[][] queenBoard) {
//		while(col<n)
//		{
//			queenBoard[row][col]=1; //try to set queen
//			totalNodes++;
//			if(Legal.possibleQ(row*n+col, queenBoard)) {
//				if(row+1==n) {
//					//final solution found
//					int queenBoard2[][]=new int[n][n];
//					for(int i=0; i<n; i++){System.arraycopy(queenBoard,0,queenBoard2,0,n);}
//					solutionsHMap.put(progress,queenBoard2);
//					progress++;
//					return;
//				}
//				//recusively solve next row
//				guess(row+1,0,queenBoard);
//			}
//			queenBoard[row][col]=0; //clear queen if invalid
//			col++; //try placing queen on next column
//		}
//	}
//	public static void masterController() {
//		doneSearch=false;
//		totalNodes=0;
//		solutionsHMap.clear();
//		UserInterface.currentHKey=-1;
//		solutions=0;
//		progress=0;
//		guess(0,0,new int[n][n]); //start the search
//		doneSearch=true; //search complete
//	}
//	@Override
//	public void run() {
//		UserInterface.gameFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
//		startTime=System.currentTimeMillis();
//		masterController();
//		endTime=System.currentTimeMillis();
//		Toolkit.getDefaultToolkit().beep();
//		UserInterface.gameFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//		UserInterface.gameFrame.repaint();
//	}
//}
import java.awt.*;
import java.util.*;
public class Search implements Runnable{
    static int n=4;//the size of the board
    static long startTime, endTime;//used to determin the length of a search function
    static HashMap<Integer, int[][]> solutionsHMap=new HashMap<Integer, int[][]>();
    static int progress;
    static int solutions;//the number of legal positions found
    static boolean doneSearch;//indicates if the thread should terminate or not
    static int totalNodes;//the number of positions analyzed
    public static void masterController() {
        doneSearch=false;
        totalNodes=0;
        solutionsHMap.clear();
        UserInterface.currentHKey=-1;
        solutions=0;
        progress=0;
        guess(0,0,new int[n][n]);
        //unique();
        doneSearch=true;
    }
    public static void guess(int x, int y, int[][]queenBoard) {
        while(y<n)
        {
            queenBoard[x][y]=1;
            totalNodes++;
            if (Legal.possibleQ(x*n+y, queenBoard)) {
                if (x+1==n) {
                    //a solution has been found
                    int queenBoard2[][]=new int[n][n];
                    for (int i=0;i<n;i++) {
                        System.arraycopy(queenBoard[i], 0, queenBoard2[i], 0, n);
                    }
                    solutionsHMap.put(progress,queenBoard2);
                    progress++;
                    if (UserInterface.currentHKey==-1) {
                        UserInterface.currentHKey=0;
                        UserInterface.getSolution();
                    }
                    return;
                }
                guess(x+1, 0, queenBoard);
            }
            queenBoard[x][y]=0;
            y++;
        }
    }
    @Override
    public void run() {
        UserInterface.gameFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        startTime=System.currentTimeMillis();
        masterController();
        endTime=System.currentTimeMillis();
        Toolkit.getDefaultToolkit().beep();
        UserInterface.gameFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        UserInterface.gameFrame.repaint();
    }
}