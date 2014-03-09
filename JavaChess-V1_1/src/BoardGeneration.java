import java.util.Arrays;

public class BoardGeneration {

	public static void initiateStandardChess() {
		//64bit long
		long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,   
				BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L;
		String chessBoard[][] = {
			{"r","n","b","k","q","b","n","r"},
			{"p","p","p","p","p","p","p","p"},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{"P","P","P","P","P","P","P","P"},
			{"R","N","B","K","Q","B","N","R"}
		};
		arrayToBitBoards(chessBoard,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
	}
	public static void initiateChess960() {
		
	}
	public static void arrayToBitBoards(String[][] chessBoard, long WP, long WN, long WB, 
			long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
		String Binary;
		for(int i=0; i<64; i++) {
			Binary= "00000000" +
					"00000000" +
					"00000000" +
					"00000000" +
					"00000000" +
					"00000000" +
					"00000000" +
					"00000000";
			Binary = Binary.substring(i+1)+"1"+Binary.substring(0,1);
			switch(chessBoard[i/8][i%8]) {
				case "P": WP+=convertStringToBitBoard(Binary);
					break;
				case "N": WN+=convertStringToBitBoard(Binary);
					break;
				case "B": WB+=convertStringToBitBoard(Binary);
					break;
				case "R": WR+=convertStringToBitBoard(Binary);
					break;
				case "Q": WQ+=convertStringToBitBoard(Binary);
					break;
				case "K": WK+=convertStringToBitBoard(Binary);
					break;
				case "p": BP+=convertStringToBitBoard(Binary);
					break;
				case "n": BN+=convertStringToBitBoard(Binary);
					break;
				case "b": BB+=convertStringToBitBoard(Binary);
					break;
				case "r": BR+=convertStringToBitBoard(Binary);
					break;
				case "q": BQ+=convertStringToBitBoard(Binary);
					break;
				case "k": BK+=convertStringToBitBoard(Binary);
					break;
			}
		}
		drawArray(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
	}
	public static long convertStringToBitBoard(String Binary) {
		if(Binary.charAt(0)=='0') { //not going to be a negative number
			return Long.parseLong(Binary,2);
		} else {
			return Long.parseLong("1"+Binary.substring(2),2)*2;
		}
	}
	public static void drawArray(long WP, long WN, long WB, 
			long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
		String chessBoard[][] = new String[8][8];
		for(int i=0; i<64; i++) {
			chessBoard[i/8][i%8] = " ";
		}
		for(int i=0; i<64; i++) {
			if(((WP>>i)&1)==1) {chessBoard[i/8][i%8]="P";}
			if(((WN>>i)&1)==1) {chessBoard[i/8][i%8]="N";}
			if(((WB>>i)&1)==1) {chessBoard[i/8][i%8]="B";}
			if(((WR>>i)&1)==1) {chessBoard[i/8][i%8]="R";}
			if(((WQ>>i)&1)==1) {chessBoard[i/8][i%8]="Q";}
			if(((WK>>i)&1)==1) {chessBoard[i/8][i%8]="K";}
			if(((BP>>i)&1)==1) {chessBoard[i/8][i%8]="p";}
			if(((BN>>i)&1)==1) {chessBoard[i/8][i%8]="n";}
			if(((BB>>i)&1)==1) {chessBoard[i/8][i%8]="b";}
			if(((BR>>i)&1)==1) {chessBoard[i/8][i%8]="r";}
			if(((BQ>>i)&1)==1) {chessBoard[i/8][i%8]="q";}
			if(((BK>>i)&1)==1) {chessBoard[i/8][i%8]="k";}
		}
		for(int i=0; i<8; i++) {
			System.out.println(Arrays.toString(chessBoard[i]));
		}
	}

}
