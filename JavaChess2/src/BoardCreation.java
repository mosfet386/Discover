import java.util.Arrays;


public class BoardCreation {

	public static void initiateStandardChess() {
		//64bit long
		long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,   
				BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L;
		String chessBoard[][] = {
			{"r","n","b","q","k","b","n","r"},
			{"p","p","p","p","p","p","p","p"},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{"P","P","P","P","P","P","P","P"},
			{"R","N","B","Q","K","B","N","R"}};
		arrayToBitBoards(chessBoard,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
	}
	public static void initiateChess960() {
		//64bit long
		long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,   
				BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L;
		//Place Pawns
		String chessBoard[][] = {
			{" "," "," "," "," "," "," "," "},
			{"p","p","p","p","p","p","p","p"},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{" "," "," "," "," "," "," "," "},
			{"P","P","P","P","P","P","P","P"},
			{" "," "," "," "," "," "," "," "}
		};
		//Place Bishops
		int random1 = (int)(Math.random()*8); //take floor
		chessBoard[0][random1]="b";
		chessBoard[7][random1]="B";
		int random2 = (int)(Math.random()*8);
		while(random2%2==random1%2) { //Create random even & odd pair
			random2 = (int)(Math.random()*8);
		}
		chessBoard[0][random2]="b";
		chessBoard[7][random2]="B";
		//Place Queens
		int random3 = (int)(Math.random()*8);
		while(random3==random1 || random3==random2) {
			random3 = (int)(Math.random()*8);
		}
		chessBoard[0][random3]="q";
		chessBoard[7][random3]="Q";
		//Place Knights
		int random4a = (int)(Math.random()*5);
		int counter=0, loop=0;
		while(counter-1<random4a) {
			if(" ".equals(chessBoard[0][loop])) {counter++;}
			loop++;
		}
		chessBoard[0][loop-1]="n";
		chessBoard[7][loop-1]="N";
		int random4b = (int)(Math.random()*4);
		counter=loop=0;
		while(counter-1<random4b) {
			if(" ".equals(chessBoard[0][loop])) {counter++;}
			loop++;
		}
		chessBoard[0][loop-1]="n";
		chessBoard[7][loop-1]="N";
		//Place Rooks and Kings
		//Rook, King, Rook order asserts castling rules
		//Kings must be surrounded by rooks
		counter=0;
		while(!" ".equals(chessBoard[0][counter])) {
			counter++;
		}
		chessBoard[0][counter]="r";
		chessBoard[7][counter]="R";
		while(!" ".equals(chessBoard[0][counter])) {
			counter++;
		}
		chessBoard[0][counter]="k";
		chessBoard[7][counter]="K";
		while(!" ".equals(chessBoard[0][counter])) {
			counter++;
		}
		chessBoard[0][counter]="r";
		chessBoard[7][counter]="R";
		arrayToBitBoards(chessBoard,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
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
			Binary = Binary.substring(i+1)+"1"+Binary.substring(0,i);
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
        JavaChess2.WP=WP; JavaChess2.WN=WN; JavaChess2.WB=WB;
        JavaChess2.WR=WR; JavaChess2.WQ=WQ; JavaChess2.WK=WK;
        JavaChess2.BP=BP; JavaChess2.BN=BN; JavaChess2.BB=BB;
        JavaChess2.BR=BR; JavaChess2.BQ=BQ; JavaChess2.BK=BK;
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
