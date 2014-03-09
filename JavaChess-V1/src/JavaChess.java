import javax.swing.*;

import java.util.*;

public class JavaChess {
	
	static String chessBoard[][] = {
		{"r","k","b","a","q","b","k","r"},
		{"p","p","p","p","p","p","p","p"},
		{" "," "," "," "," "," "," "," "},
		{" "," "," "," "," "," "," "," "},
		{" "," "," "," "," "," "," "," "},
		{" "," "," "," "," "," "," "," "},
		{"P","P","P","P","P","P","P","P"},
		{"R","K","B","A","Q","B","K","R"}
	};
	static int kingPositionC, kingPositionL;
	static int humanAsWhite=1; //1=human as white, 0=human as black
	static int globaldepth=4;
	
	public static void main(String[] args) {
		while(!"A".equals(chessBoard[kingPositionC/8][kingPositionC%8])) {kingPositionC++;}
		while(!"a".equals(chessBoard[kingPositionL/8][kingPositionL%8])) {kingPositionL++;}
		
		JFrame f = new JFrame("Chess!");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserInterface ui = new UserInterface();
		f.add(ui);
		f.setSize(560,590);
		f.setVisible(true);
		System.out.println("possible moves: " + possibleMoves());
		
		Object[] option = {"Computer","Human"};
		humanAsWhite = JOptionPane.showOptionDialog(null, "Who should play as white?", 
				"ABC Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
					null, option, option[1]);
		if(humanAsWhite==0) {
			long startTime=System.currentTimeMillis();
			makeMove(alphaBeta(globaldepth, Integer.MAX_VALUE, Integer.MIN_VALUE, "", 0));
			long endTime=System.currentTimeMillis();
			System.out.println("That took "+(endTime-startTime)+" milliseconds");
			flipBoard();
			f.repaint();
		}
		
		makeMove("7657 ");
		undoMove("7657 ");
		for(int i=0; i<8; i++) {
			System.out.println(Arrays.toString(chessBoard[i]));
		}
		
//		for(int i=0; i<=10; i++) {
//			for(int j=0; j<8; j++) {
//				System.out.println(Arrays.toString(chessBoard[j]));
//			}
//			System.out.println();
//			makeMove(alphaBeta(globaldepth, Integer.MAX_VALUE, Integer.MIN_VALUE, "", 0));
//			flipBoard();
//			makeMove(alphaBeta(globaldepth, Integer.MAX_VALUE, Integer.MIN_VALUE, "", 0));
//			flipBoard();
//		}
	}
	public static String possibleMoves() {
		String list = "";
		for(int i=0; i<64; i++){
			switch(chessBoard[i/8][i%8]){
				case "P": list+=possibleP(i);
					break;
				case "R": list+=possibleR(i);
					break;
				case "K": list+=possibleK(i);
					break;
				case "B": list+=possibleB(i);
					break;
				case "Q": list+=possibleQ(i);
					break;
				case "A": list+=possibleA(i);
					break;
				
			}
		}
		return list;//x1,y1,x2,y2,captured piece
	}
	public static String alphaBeta(int depth, int beta, int alpha, String move,int player) {
		//two most important goals (speed/effectiveness)
		//good sorting/searching of moves && reliable ratings
		String list=possibleMoves();
		if(depth==0 || list.length()==0){return move+Rating.rating(list.length(), depth)*(player*2-1);}
		list=sortMoves(list);
		player=1-player;//either 1 or 0
		for(int i=0; i<list.length(); i+=5) {
			makeMove(list.substring(i,i+5));
			flipBoard();
			String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
			int value=Integer.valueOf(returnString.substring(5));
			flipBoard();
			undoMove(list.substring(i,i+5));
			if(player==0) { //set beta if player move, set beta if enemy move
				if(value<=beta){beta=value; if(depth==globaldepth){move=returnString.substring(0,5);}}
			} else {
				if(value>alpha){alpha=value; if(depth==globaldepth){move=returnString.substring(0,5);}}
			}
			if(alpha>=beta) {
				if(player==0){return move+beta;} else {return move+alpha;}
			}
		}
		if(player==0){return move+beta;} else {return move+alpha;}
		//return in the form of 1234###########...
	}
	public static void flipBoard() {
		//rotate board and exchange pieces
		String temp;
		for(int i=0; i<32; i++) {
			int r=i/8, c=i%8;
			if(Character.isUpperCase(chessBoard[r][c].charAt(0))) {
				temp=chessBoard[r][c].toLowerCase();
			} else {
				temp=chessBoard[r][c].toUpperCase();
			}
			if(Character.isUpperCase(chessBoard[7-r][7-c].charAt(0))) {
				chessBoard[r][c]=chessBoard[7-r][7-c].toLowerCase();
			} else {
				chessBoard[r][c]=chessBoard[7-r][7-c].toUpperCase();
			}
			chessBoard[7-r][7-c]=temp;
		}
		int kingtemp=kingPositionC;
		kingPositionC=63-kingPositionL;
		kingPositionL=63-kingtemp;
	}
	public static void makeMove(String move) {
		if(move.charAt(4)!='P') {
			//move format y1,x1,y2,x2,captured piece
			chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
			chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
			if("A".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
				kingPositionC=8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
			}
		} else { //pawn promotion
			//move format column1,column2,captured-piece,new-piece,P
			chessBoard[1][Character.getNumericValue(move.charAt(0))]=" ";
			chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
		}
	}
	public static void undoMove(String move) {
		if(move.charAt(4)!='P') {
			//move format x1,y1,x2,y2,captured piece
			chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
			chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
			if("A".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
				kingPositionC=8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
			}
		} else { //pawn promotion
			//move format column1,column2,captured-piece,new-piece,P
			chessBoard[1][Character.getNumericValue(move.charAt(0))]="P";
			chessBoard[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
		}
	}
	public static String possibleP(int i) {
		String list = "", oldPiece;
		int r=i/8, c=i%8;
		for(int j=-1; j<=1; j+=2) {
			try { //Pawn capture
				if(Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i>=16) {
					oldPiece=chessBoard[r-1][c+j];
					chessBoard[r][c]=" ";
					chessBoard[r-1][c+j]="P";
					if(kingSafe()) {
						list=list+r+c+(r-1)+(c+j)+oldPiece;
					}
					chessBoard[r][c]="P";
					chessBoard[r-1][c+j]=oldPiece;
				}
			} catch(Exception e) {}
			try { //Pawn promotion && capture
				if(Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i<16) {
					String[] temp={"Q","R","B","K"};
					for(int k=0; k<4; k++) {
						oldPiece=chessBoard[r-1][c+j];
						chessBoard[r][c]=" ";
						chessBoard[r-1][c+j]=temp[k];
						if(kingSafe()) {
							//columb1,columb2,captured-piece,new-piece,P
							list=list+c+(c+j)+oldPiece+temp[k]+"P";
						}
						chessBoard[r][c]="P";
						chessBoard[r-1][c+j]=oldPiece;
					}
				}
			} catch(Exception e) {}
		}
		try { //Pawn move one up
			if(" ".equals(chessBoard[r-1][c]) && i>=16) {
				oldPiece=chessBoard[r-1][c];
				chessBoard[r][c]=" ";
				chessBoard[r-1][c]="P";
				if(kingSafe()) {
					list=list+r+c+(r-1)+c+oldPiece;
				}
				chessBoard[r][c]="P";
				chessBoard[r-1][c]=oldPiece;
			}
		} catch(Exception e) {}
		try { //Pawn move one up && promotion
			if(" ".equals(chessBoard[r-1][c]) && i<16) {
				String[] temp={"Q","R","B","K"};
				for(int k=0; k<4; k++) {
					oldPiece=chessBoard[r-1][c];
					chessBoard[r][c]=" ";
					chessBoard[r-1][c]=temp[k];
					if(kingSafe()) {
						//columb1,columb2,captured-piece,new-piece,P
						list=list+c+c+oldPiece+temp[k]+"P";
					}
					chessBoard[r][c]="P";
					chessBoard[r-1][c]=oldPiece;
				}
			}
		} catch(Exception e) {}
		try { //Pawn move two up
			if(" ".equals(chessBoard[r-1][c]) && " ".equals(chessBoard[r-2][c]) && i>=48) {
				oldPiece=chessBoard[r-2][c];
				chessBoard[r][c]=" ";
				chessBoard[r-2][c]="P";
				if(kingSafe()) {
					list=list+r+c+(r-2)+c+oldPiece;
				}
				chessBoard[r][c]="P";
				chessBoard[r-2][c]=oldPiece;
			}
		} catch(Exception e) {}
			
		return list;
	}
	public static String possibleR(int i) {
		String list = "", oldPiece;
		int r=i/8, c=i%8;
		int temp=1; //for spaces outside piece parameter
		for(int j=-1; j<=1; j+=2) {
			try { //Move Horizontal
				while(" ".equals(chessBoard[r][c+temp*j]))
				{
					oldPiece=chessBoard[r][c+temp*j];
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="R";
					if(kingSafe()) {
						list=list+r+c+r+(c+temp*j)+oldPiece;
					}
					chessBoard[r][c]="R";
					chessBoard[r][c+temp*j]=oldPiece;
					temp++;
				}
				if(Character.isLowerCase(chessBoard[r][c+temp*j].charAt(0))) {
					oldPiece=chessBoard[r][c+temp*j];
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="R";
					if(kingSafe()) {
						list=list+r+c+r+(c+temp*j)+oldPiece;
					}
					chessBoard[r][c]="R";
					chessBoard[r][c+temp*j]=oldPiece;
				}
			} catch(Exception e) {}
			temp=1;
			try { //Move Vertical
				while(" ".equals(chessBoard[r+temp*j][c]))
				{
					oldPiece=chessBoard[r+temp*j][c];
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="R";
					if(kingSafe()) {
						list=list+r+c+(r+temp*j)+c+oldPiece;
					}
					chessBoard[r][c]="R";
					chessBoard[r+temp*j][c]=oldPiece;
					temp++;
				}
				if(Character.isLowerCase(chessBoard[r+temp*j][c].charAt(0))) {
					oldPiece=chessBoard[r+temp*j][c];
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="R";
					if(kingSafe()) {
						list=list+r+c+(r+temp*j)+c+oldPiece;
					}
					chessBoard[r][c]="R";
					chessBoard[r+temp*j][c]=oldPiece;
				}
			} catch(Exception e) {}
			temp=1;
		}
		return list;
	}
	public static String possibleK(int i) {
		String list = "", oldPiece;
		int r=i/8, c=i%8;
		int temp = 1;
		for(int j=-1; j<=1; j+=2) {
			for(int k=-1; k<=1; k+=2) {
				try {
					if(Character.isLowerCase(chessBoard[r+j][c+k*2].charAt(0)) || " ".equals(chessBoard[r+j][c+k*2])) {
						oldPiece=chessBoard[r+j][c+k*2];
						chessBoard[r][c]=" ";
						if(kingSafe()) {
							list=list+r+c+(r+j)+(c+k*2)+oldPiece;
						}
						chessBoard[r][c]="K";
						chessBoard[r+j][c+k*2]=oldPiece;
					}
				} catch(Exception e) {}
				try {
					if(Character.isLowerCase(chessBoard[r+j*2][c+k].charAt(0)) || " ".equals(chessBoard[r+j*2][c+k])) {
						oldPiece=chessBoard[r+j*2][c+k];
						chessBoard[r][c]=" ";
						if(kingSafe()) {
							list=list+r+c+(r+j*2)+(c+k)+oldPiece;
						}
						chessBoard[r][c]="K";
						chessBoard[r+j*2][c+k]=oldPiece;
					}
				} catch(Exception e) {}
			}
		}
		return list;
	}
	public static String possibleB(int i) {
		String list = "", oldPiece;
		int r=i/8, c=i%8;
		int temp = 1;
		for(int j=-1; j<=1; j+=2) {
			for(int k=-1; k<=1; k+=2) {
				try {
					while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
					{
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="B";
						if(kingSafe()) {
							list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
						}
						chessBoard[r][c]="B";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
						temp++;
					}
					if(Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="B";
						if(kingSafe()) {
							list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
						}
						chessBoard[r][c]="B";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
					}
				} catch(Exception e) {}
				temp=1;
			}
		}
		return list;
	}
	public static String possibleQ(int i) {
		String list = "", oldPiece;
		int r=i/8, c=i%8;
		int temp = 1;
		for(int j=-1; j<=1; j++) {
			for(int k=-1; k<=1; k++) {
				if(j!=0 || k!=0)
				{
					try {
						while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
						{
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="Q";
							if(kingSafe()) {
								list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
							}
							chessBoard[r][c]="Q";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
							temp++;
						}
						if(Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="Q";
							if(kingSafe()) {
								list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
							}
							chessBoard[r][c]="Q";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
						}
					} catch(Exception e) {}
					temp=1;
				}
			}
		}
		return list;
	}
	public static String possibleA(int i) {
		String list = "", oldPiece;
		int r=i/8, c=i%8;
		for(int j=0; j<9; j++) {
			if(j!=4) {
				try { //try, catch if out of board bounds!
					if(Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) {
						oldPiece = chessBoard[r-1+j/3][c-1+j%3];
						chessBoard[r][c] = " ";
						chessBoard[r-1+j/3][c-1+j%3] = "A";
						int kingTemp = kingPositionC;
						kingPositionC = (i-9)+(j/3)*8+(j%3);
						if(kingSafe()) {
							list = list+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;
						}
						chessBoard[r][c] = "A";
						chessBoard[r-1+j/3][c-1+j%3] = oldPiece;
						kingPositionC = kingTemp;
					}
				} catch(Exception e) {}
			}
		}
		//need to add casting later
		return list;
	}
	public static String sortMoves(String list) {
		int[] score = new int[list.length()/5];
		for(int i=0; i<list.length(); i+=5) {
			makeMove(list.substring(i, i+5));
			score[i/5]=-Rating.rating(-1,0);
			undoMove(list.substring(i, i+5));
		}
		String newListA="", newListB="";
		for(int i=0; i<Math.min(6,list.length()/5); i++) {
			int max=-1000000, maxLocation=0;
			for(int j=0; j<list.length()/5; j++) {
				if(score[j]>max){max=score[j]; maxLocation=j;}
			}
			score[maxLocation]=-1000000;
			newListA+=list.substring(maxLocation*5,maxLocation*5+5);
			newListB=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");
		}
		return newListA+newListB;
	}
	public static boolean kingSafe() {
		//Danger bishop or queen
		int temp=1;
		for(int i=-1; i<=1; i+=2) {
			for(int j=-1; j<=1; j+=2) {
				try {
					//loop check until in danger
					while(" ".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j])) {temp++;}
					if("b".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j])
						|| "q".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8+temp*j])) 
					{
						//king in diagonal danger, first since more likely
						return false;
					}
				} catch(Exception e) {}
				temp=1;
			}
		}
		//Danger rook or queen
		for(int i=-1; i<=1; i+=2) {
			try {
				//loop check until in danger
				while(" ".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i])) {temp++;}
				if("r".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i])
					|| "q".equals(chessBoard[kingPositionC/8][kingPositionC%8+temp*i])) 
				{
					//king in horizontal danger, more likely
					return false;
				}
			} catch(Exception e) {}
			temp=1;
			try {
				//loop check until in danger
				while(" ".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8])) {temp++;}
				if("r".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8])
					|| "q".equals(chessBoard[kingPositionC/8+temp*i][kingPositionC%8])) 
				{
					//king in vertical danger, more likely
					return false;
				}
			} catch(Exception e) {}
			temp=1;
		}
		//Danger knight
		for(int i=-1; i<=1; i+=2) {
			for(int j=-1; j<=1; j+=2) {
				try {
					//for rows +-1 and columns +-2
					if("k".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j*2]))
					{
						//king in knight danger
						return false;
					}
				} catch(Exception e) {}
				try {
					//for rows +-2 and columns +-1
					if("k".equals(chessBoard[kingPositionC/8+i*2][kingPositionC%8+j]))
					{
						//king in knight danger
						return false;
					}
				} catch(Exception e) {}
			}
		}
		//Danger pawn
		if(kingPositionC>=16) {//only in danger for positions 16 or greater
			try {
				//upper left
				if("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8-1]))
				{
					//king in pawn danger
					return false;
				}
			} catch(Exception e) {}
			try {
				//upper right
				if("p".equals(chessBoard[kingPositionC/8-1][kingPositionC%8+1]))
				{
					//king in pawn danger
					return false;
				}
			} catch(Exception e) {}
			//king block, can't move within 1 space of other king
			for(int i=-1; i<=1; i++) {
				for(int j=-1; j<=1; j++) {
					if(i!=0 || j!=0) {
						try {
							//for all 8 edges
							if("a".equals(chessBoard[kingPositionC/8+i][kingPositionC%8+j]))
							{
								//king block, least likely
								return false;
							}
						} catch(Exception e) {}
					}
				}
			}
		}
		return true;
	}


} //End of Class
