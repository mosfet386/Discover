import java.util.Arrays;


public class Moves {
	
	//Special Layouts
    static long FILE_A=72340172838076673L;
    static long FILE_H=-9187201950435737472L;
    static long FILE_AB=217020518514230019L;
    static long FILE_GH=-4557430888798830400L;
    static long RANK_1=-72057594037927936L;
    static long RANK_4=1095216660480L;
    static long RANK_5=4278190080L;
    static long RANK_8=255L;
    static long CENTRE=103481868288L;
    static long EXTENDED_CENTRE=66229406269440L;
    static long KING_SIDE=-1085102592571150096L;
    static long QUEEN_SIDE=1085102592571150095L;
    static long KING_SPAN=460039L;
    static long KNIGHT_SPAN=43234889994L;
    static long NOT_MY_PIECES;
    static long MY_PIECES;
    static long OCCUPIED;
    static long EMPTY;
    static long CASTLE_ROOKS[]={63,56,7,0};
    static long RankMasks8[] = { //from rank1 to rank8
        			0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 
        			0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L};
    static long FileMasks8[] = { //from fileA to FileH
        			0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
        			0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L};
    static long DiagonalMasks8[] = { //from top left to bottom right
		0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
		0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
		0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L};
    static long AntiDiagonalMasks8[] = { //from top right to bottom left
		0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
		0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
		0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L};
    
    public static long HVmoves(int slide) {
    	//Horizontal Moves -> (a-2s)`XOR(a'-2s')'
    	//Vertical Moves   -> (a&m-2slide)`XOR((a&m)`-2slide`)`
    	long binaryS = 1L<<slide;
    	long possibleHmoves = 
    			(OCCUPIED-2*binaryS)^
    			Long.reverse(Long.reverse(OCCUPIED)-2*Long.reverse(binaryS));
    	long possibleVmoves = 
    			((OCCUPIED&FileMasks8[slide%8])-(2*binaryS))^
    			Long.reverse(Long.reverse(OCCUPIED&FileMasks8[slide%8])-(2*Long.reverse(binaryS)));
    	//validate HVmoves
    	//drawBitboard(possibleHmoves&RankMasks8[slide/8]|possibleVmoves&FileMasks8[slide%8]);
    	return possibleHmoves&RankMasks8[slide/8]|possibleVmoves&FileMasks8[slide%8];
    }
    public static long Dmoves(int slide)
    {
        long binaryS = 1L<<slide;
        long possibleD = 
        		((OCCUPIED&DiagonalMasks8[(slide/8)+(slide%8)])-(2*binaryS))^
        		Long.reverse(Long.reverse(OCCUPIED&DiagonalMasks8[(slide/8)+(slide%8)])-(2*Long.reverse(binaryS)));
        long possibleAntiD = 
        		((OCCUPIED&AntiDiagonalMasks8[(slide/8)+7-(slide%8)])-(2*binaryS))^
        		Long.reverse(Long.reverse(OCCUPIED&AntiDiagonalMasks8[(slide/8)+7-(slide%8)])-(2*Long.reverse(binaryS)));
        //Validate Dmoves
        //drawBitboard((possibleD&DiagonalMasks8[(slide/8)+(slide%8)])|
        //		(possibleAntiD&AntiDiagonalMasks8[(slide/8)+ 7-(slide%8)]));
        return (possibleD&DiagonalMasks8[(slide/8)+(slide%8)])|
        		(possibleAntiD&AntiDiagonalMasks8[(slide/8)+ 7-(slide%8)]);
    }
    public static String possibleMovesW(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,
    		long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,
    		boolean CBQ) {
        NOT_MY_PIECES=~(WP|WN|WB|WR|WQ|WK|BK);//added BK to avoid illegal capture
        MY_PIECES=WP|WN|WB|WR|WQ;//omitted WK to avoid illegal capture
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        EMPTY=~OCCUPIED;
        //possibleWN(OCCUPIED,WN); //possibleWN validation
        //HVmoves(36); //HVmove validation
        //DMoves(36); //HVmove validation, place piece at row3 col4
        //timeExperiment(WP); //algorithm benchmarking
        String list = possibleWP(WP,BP,EP)+
		                possibleN(OCCUPIED,WN)+
		                possibleB(OCCUPIED,WB)+
		                possibleR(OCCUPIED,WR)+
		                possibleQ(OCCUPIED,WQ)+
		                possibleK(OCCUPIED,WK);
        //unsafeForBlack(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK); //verify unsafeForBlack bitmat
        //String a = possibleMovesB(history,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK); //verifying
        return list;
    }
    public static String possibleMovesB(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,
    		long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,
    		boolean CBQ) {
        NOT_MY_PIECES=~(BP|BN|BB|BR|BQ|BK|WK);//added WK to avoid illegal capture
        MY_PIECES=BP|BN|BB|BR|BQ;//omitted BK to avoid illegal capture
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        EMPTY=~OCCUPIED;
        //possibleWN(OCCUPIED,WN); //possibleWN validation
        //HVmoves(36); //HVmove validation
        //DMoves(36); //HVmove validation, place piece at row3 col4
        //timeExperiment(WP); //algorithm benchmarking
        String list = possibleWP(WP,BP,EP)+
		                possibleN(OCCUPIED,BN)+
		                possibleB(OCCUPIED,BB)+
		                possibleR(OCCUPIED,BR)+
		                possibleQ(OCCUPIED,BQ)+
		                possibleK(OCCUPIED,BK);
        //String a = possibleMovesW(history,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK); //verifying
        return list;
    }
    public static String possibleB(long OCCUPIED,long b)
    {
        String list="";
        long i=b&~(b-1); //bitmask of first bishop position
        long possibility;
        while(i!=0)
        {
            int iLocation=Long.numberOfTrailingZeros(i); //current bishop position
            possibility=Dmoves(iLocation)&NOT_MY_PIECES;
            long j=possibility&~(possibility-1); //bitmask of first possible bishop move
            while (j!=0)
            {
                int index=Long.numberOfTrailingZeros(j); //current move index
                //from,to y1x1y2x2
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j; //remove current move from possible moves bitmap
                j=possibility&~(possibility-1); //next move bitmap
            }
            b&=~i; //remove current bishop from bishop bitmap
            i=b&~(b-1); //next bishop bitmap
        }
        return list;
    }
    public static String possibleR(long OCCUPIED,long r)
    {
        String list="";
        long i=r&~(r-1); //bitmask of first rook position
        long possibility;
        while(i!=0)
        {
            int iLocation=Long.numberOfTrailingZeros(i); //current rook position
            possibility=HVmoves(iLocation)&NOT_MY_PIECES;
            long j=possibility&~(possibility-1); //bitmask of first possible rook move
            while (j!=0)
            {
                int index=Long.numberOfTrailingZeros(j); //current move index
                //from,to y1x1y2x2
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j; //remove current move from possible moves bitmap
                j=possibility&~(possibility-1); //next move bitmap
            }
            r&=~i; //remove current rook from rook bitmap
            i=r&~(r-1); //next rook bitmap
        }
        return list;
    }
    public static String possibleQ(long OCCUPIED,long q)
    {
        String list="";
        long i=q&~(q-1); //bitmask of first queen position
        long possibility;
        while(i!=0)
        {
            int iLocation=Long.numberOfTrailingZeros(i); //current queen position
            possibility=(HVmoves(iLocation)|Dmoves(iLocation))&NOT_MY_PIECES;
            long j=possibility&~(possibility-1); //bitmask of first possible queen move
            while (j!=0)
            {
                int index=Long.numberOfTrailingZeros(j); //current move index
                //from,to y1x1y2x2
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j; //remove current move from possible moves bitmap
                j=possibility&~(possibility-1); //next move bitmap
            }
            q&=~i; //remove current queen from queen bitmap
            i=q&~(q-1); //next queen bitmap
        }
        return list;
    }
    public static String possibleN(long OCCUPIED,long n)
    {
        String list="";
        long i=n&~(n-1); //bitmap of first white knight
        long possibility;
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            if (iLocation>18)
            {
                possibility=KNIGHT_SPAN<<(iLocation-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-iLocation);
            }
            if (iLocation%8<4)
            {
                possibility &=~FILE_GH&NOT_MY_PIECES;
            }
            else {
                possibility &=~FILE_AB&NOT_MY_PIECES;
            }
            //drawBitboard(possibility); //verify possibleWN
            long j=possibility&~(possibility-1);
            while (j != 0)
            {
                int index=Long.numberOfTrailingZeros(j);
                list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            n&=~i; //remove current white night from bitmap
            i=n&~(n-1); //get next white night bitmap
        }
        return list;
    }
    public static String possibleK(long OCCUPIED,long k)
    {
        String list="";
        long possibility;
        int iLocation=Long.numberOfTrailingZeros(k);
        if (iLocation>9)
        {
            possibility=KING_SPAN<<(iLocation-9);
        }
        else {
            possibility=KING_SPAN>>(9-iLocation);
        }
        if (iLocation%8<4)
        {
            possibility &=~FILE_GH&NOT_MY_PIECES;
        }
        else {
            possibility &=~FILE_AB&NOT_MY_PIECES;
        }
        long j=possibility&~(possibility-1);
        while (j != 0)
        {
            int index=Long.numberOfTrailingZeros(j);
            list+=""+(iLocation/8)+(iLocation%8)+(index/8)+(index%8);
            possibility&=~j;
            j=possibility&~(possibility-1);
        }
        return list;
    }
    public static String possibleWP(long WP,long BP, long EP) {
        String list="";
        //x1,y1,x2,y2
        long PAWN_MOVES=(WP>>7)&MY_PIECES&~RANK_8&~FILE_A;//capture right
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8-1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>9)&MY_PIECES&~RANK_8&~FILE_H;//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8+1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>8)&EMPTY&~RANK_8;//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>16)&EMPTY&(EMPTY>>8)&RANK_4;//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+2)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Promotion Type,"P"
        PAWN_MOVES=(WP>>7)&MY_PIECES&RANK_8&~FILE_A;//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"QP"+(index%8-1)+(index%8)+"RP"+(index%8-1)+(index%8)+"BP"+(index%8-1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>9)&MY_PIECES&RANK_8&~FILE_H;//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"QP"+(index%8+1)+(index%8)+"RP"+(index%8+1)+(index%8)+"BP"+(index%8+1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>8)&EMPTY&RANK_8;//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"QP"+(index%8)+(index%8)+"RP"+(index%8)+(index%8)+"BP"+(index%8)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Space,"E
        //en passant right
        possibility = (WP << 1)&BP&RANK_5&~FILE_A&EP;//shows piece to remove, not the destination
        if (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+" E";
        }
        //en passant left
        possibility = (WP >> 1)&BP&RANK_5&~FILE_H&EP;//shows piece to remove, not the destination
        if (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+" E";
        }
       return list;
    }
    public static String possibleBP(long BP,long WP, long EP) {
        String list="";
        //x1,y1,x2,y2
        long PAWN_MOVES=(BP<<7)&MY_PIECES&~RANK_1&~FILE_H;//capture right
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8+1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<9)&MY_PIECES&~RANK_1&~FILE_A;//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8-1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<8)&EMPTY&~RANK_1;//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<16)&EMPTY&(EMPTY<<8)&RANK_5;//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-2)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Promotion Type,"P"
        PAWN_MOVES=(BP<<7)&MY_PIECES&RANK_1&~FILE_H;//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"qP"+(index%8+1)+(index%8)+"rP"+(index%8+1)+(index%8)+"bP"+(index%8+1)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<9)&MY_PIECES&RANK_1&~FILE_A;//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"qP"+(index%8-1)+(index%8)+"rP"+(index%8-1)+(index%8)+"bP"+(index%8-1)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(BP<<8)&EMPTY&RANK_1;//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"qP"+(index%8)+(index%8)+"rP"+(index%8)+(index%8)+"bP"+(index%8)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,"bE"
        //en passant right
        possibility = (BP >> 1)&WP&RANK_4&~FILE_H&EP;//shows piece to remove, not the destination
        if (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"bE";
        }
        //en passant left
        possibility = (BP << 1)&WP&RANK_4&~FILE_A&EP;//shows piece to remove, not the destination
        if (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"bE";
        }
       return list;
    }
    public static String possibleWC(long WR,boolean CWK,boolean CWQ) {
    	//Weighter king nor rook have moved
    	String list="";
    	if(CWK&&(((1L<<CASTLE_ROOKS[0])&WR)!=0)) {list+="7476";}
    	if(CWQ&&(((1L<<CASTLE_ROOKS[1])&WR)!=0)) {list+="7472";}
    	return list;
    }
    public static String possibleBC(long BR,boolean CBK,boolean CBQ) {
    	//Neither king nor rook have moved
    	String list="";
    	if(CBK&&(((1L<<CASTLE_ROOKS[3])&BR)!=0)) {list+="0406";}
    	if(CBQ&&(((1L<<CASTLE_ROOKS[4])&BR)!=0)) {list+="0402";}
    	return list;
    }
    public static long unsafeForWhite(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        long unsafe;
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        //pawn
        unsafe=((BP<<7)&~FILE_H);//pawn capture right
        unsafe|=((BP<<9)&~FILE_A);//pawn capture left
        long possibility;
        //knight
        long i=BN&~(BN-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            if (iLocation>18)
            {
                possibility=KNIGHT_SPAN<<(iLocation-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-iLocation);
            }
            if (iLocation%8<4)
            {
                possibility &=~FILE_GH;
            }
            else {
                possibility &=~FILE_AB;
            }
            unsafe |= possibility;
            BN&=~i;
            i=BN&~(BN-1);
        }
        //bishop/queen
        long QB=BQ|BB;
        i=QB&~(QB-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=Dmoves(iLocation);
            unsafe |= possibility;
            QB&=~i;
            i=QB&~(QB-1);
        }
        //rook/queen
        long QR=BQ|BR;
        i=QR&~(QR-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=HVmoves(iLocation);
            unsafe |= possibility;
            QR&=~i;
            i=QR&~(QR-1);
        }
        //king
        int iLocation=Long.numberOfTrailingZeros(BK);
        if (iLocation>9)
        {
            possibility=KING_SPAN<<(iLocation-9);
        }
        else {
            possibility=KING_SPAN>>(9-iLocation);
        }
        if (iLocation%8<4)
        {
            possibility &=~FILE_GH;
        }
        else {
            possibility &=~FILE_AB;
        }
        unsafe |= possibility;
        return unsafe;
    }
    public static long unsafeForBlack(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK)
    {
        long unsafe;
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;
        //pawn
        unsafe=((WP>>>7)&~FILE_A);//pawn capture right
        unsafe|=((WP>>>9)&~FILE_H);//pawn capture left
        long possibility;
        //knight
        long i=WN&~(WN-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            if (iLocation>18)
            {
                possibility=KNIGHT_SPAN<<(iLocation-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-iLocation);
            }
            if (iLocation%8<4)
            {
                possibility &=~FILE_GH;
            }
            else {
                possibility &=~FILE_AB;
            }
            unsafe |= possibility;
            WN&=~i;
            i=WN&~(WN-1);
        }
        //bishop/queen
        long QB=WQ|WB;
        i=QB&~(QB-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=Dmoves(iLocation);
            unsafe |= possibility;
            QB&=~i;
            i=QB&~(QB-1);
        }
        //rook/queen
        long QR=WQ|WR;
        i=QR&~(QR-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=HVmoves(iLocation);
            unsafe |= possibility;
            QR&=~i;
            i=QR&~(QR-1);
        }
        //king
        int iLocation=Long.numberOfTrailingZeros(WK);
        if (iLocation>9)
        {
            possibility=KING_SPAN<<(iLocation-9);
        }
        else {
            possibility=KING_SPAN>>(9-iLocation);
        }
        if (iLocation%8<4)
        {
            possibility &=~FILE_GH;
        }
        else {
            possibility &=~FILE_AB;
        }
        unsafe |= possibility;
//        System.out.println();
//        drawBitboard(unsafe);
        return unsafe;
    }
    public static void makeMoveWrong(String move,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ) {
        //can not opperate on a single board since moves are not backwards compatable
        EP=0;
        if (Character.isDigit(move.charAt(3))) {
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            int end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
            if ((WK&(1L<<start))!=0) {//white castle move
                WK^=(1L<<start);
                WK^=(1L<<end);
                if (end>start) {//kingside
                    WR^=(1L<<63);
                    WR^=(1L<<61);
                    CWK=false;
                } else {//queenside
                    WR^=(1L<<56);
                    WR^=(1L<<59);
                    CWQ=false;
                }
            } else if ((BK&(1L<<start))!=0) {//black castle move
                WK^=(1L<<start);
                WK^=(1L<<end);
                if (end>start) {//kingside
                    WR^=(1L<<7);
                    WR^=(1L<<5);
                    CBK=false;
                } else {//queenside
                    WR^=1L;
                    WR^=(1L<<3);
                    CBQ=false;
                }
            } else {//'regular' move
                //clear destination:
                WP&=~(1L<<end);
                WN&=~(1L<<end);
                WB&=~(1L<<end);
                WR&=~(1L<<end);
                WQ&=~(1L<<end);
                WK&=~(1L<<end);
                //move piece:
                if ((WP&(1L<<start))!=0)
                {
                    WP^=(1L<<start);
                    WP^=(1L<<end);
                    if ((end-start)==16) {//pawn double push
                        EP=FileMasks8['0'-move.charAt(1)];
                    }
                }
                else if ((BP&(1L<<start))!=0)
                {
                    BP^=(1L<<start);
                    BP^=(1L<<end);
                    if ((start-end)==16) {//pawn double push
                        EP=FileMasks8['0'-move.charAt(1)];
                    }
                }
                else if ((WN&(1L<<start))!=0)
                {
                    WN^=(1L<<start);
                    WN^=(1L<<end);
                }
                else if ((BN&(1L<<start))!=0)
                {
                    BN^=(1L<<start);
                    BN^=(1L<<end);
                }
                else if ((WB&(1L<<start))!=0)
                {
                    WB^=(1L<<start);
                    WB^=(1L<<end);
                }
                else if ((BB&(1L<<start))!=0)
                {
                    BB^=(1L<<start);
                    BB^=(1L<<end);
                }
                else if ((WR&(1L<<start))!=0)
                {
                    WR^=(1L<<start);
                    WR^=(1L<<end);
                }
                else if ((BR&(1L<<start))!=0)
                {
                    BR^=(1L<<start);
                    BR^=(1L<<end);
                }
                else if ((WQ&(1L<<start))!=0)
                {
                    WQ^=(1L<<start);
                    WQ^=(1L<<end);
                }
                else if ((BQ&(1L<<start))!=0)
                {
                    BQ^=(1L<<start);
                    BQ^=(1L<<end);
                }
                else if ((WK&(1L<<start))!=0)
                {
                    WK^=(1L<<start);
                    WK^=(1L<<end);
                    CWK=false;
                    CWQ=false;
                }
                else if ((BK&(1L<<start))!=0)
                {
                    BK^=(1L<<start);
                    BK^=(1L<<end);
                    CBK=false;
                    CBQ=false;
                }
                else
                {
                    System.out.print("error: can not move empty piece");
                }
            }
        } else if (move.charAt(3)=='P') {//pawn promotion
            //y1,y2,Promotion Type,"P"
            if (Character.isUpperCase(move.charAt(2)))//white piece promotion
            {
                WP^=(RankMasks8[6]&FileMasks8[move.charAt(0)-'0']);
                switch (move.charAt(2)) {
                case 'N': WN^=(RankMasks8[7]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'B': WB^=(RankMasks8[7]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'R': WR^=(RankMasks8[7]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'Q': WQ^=(RankMasks8[7]&FileMasks8[move.charAt(1)-'0']);
                    break;
                }
            } else {//black piece promotion
                BP^=(RankMasks8[1]&FileMasks8[move.charAt(0)-'0']);
                switch (move.charAt(2)) {
                case 'n': BN^=(RankMasks8[0]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'b': BB^=(RankMasks8[0]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'r': BR^=(RankMasks8[0]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'q': BQ^=(RankMasks8[0]&FileMasks8[move.charAt(1)-'0']);
                    break;
                }
            }
        } else if (move.charAt(3)=='E') {//en passant move
            if (move.charAt(2)=='w') {//white move
                //y1,y2,"BE"
                WP^=(RankMasks8[4]&FileMasks8['0'-move.charAt(0)]);//remove white pawn
                WP^=(RankMasks8[5]&FileMasks8['0'-move.charAt(1)]);//add white pawn
                BP^=(RankMasks8[4]&FileMasks8['0'-move.charAt(1)]);//remove black pawn)
            } else {//black move
                BP^=(RankMasks8[3]&FileMasks8['0'-move.charAt(0)]);//remove black pawn
                BP^=(RankMasks8[2]&FileMasks8['0'-move.charAt(1)]);//add black pawn
                WP^=(RankMasks8[3]&FileMasks8['0'-move.charAt(1)]);//remove white pawn)
            }
        } else {
            System.out.print("error: not a valid move type");
        }
    }
    /*
    long WPt=Moves.makeMove(WP, moves.substring(i,i+4), 'P'), WNt=Moves.makeMove(WN, moves.substring(i,i+4), 'N'),
    WBt=Moves.makeMove(WB, moves.substring(i,i+4), 'B'), WRt=Moves.makeMove(WR, moves.substring(i,i+4), 'R'),
    WQt=Moves.makeMove(WQ, moves.substring(i,i+4), 'Q'), WKt=Moves.makeMove(WK, moves.substring(i,i+4), 'K'),
    BPt=Moves.makeMove(BP, moves.substring(i,i+4), 'p'), BNt=Moves.makeMove(BN, moves.substring(i,i+4), 'n'),
    BBt=Moves.makeMove(BB, moves.substring(i,i+4), 'b'), BRt=Moves.makeMove(BR, moves.substring(i,i+4), 'r'),
    BQt=Moves.makeMove(BQ, moves.substring(i,i+4), 'q'), BKt=Moves.makeMove(BK, moves.substring(i,i+4), 'k'),
    EPt=Moves.makeMoveEP(moves.substring(i,i+4));
    */
    public static long makeMove(long board, String move, char type) {
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            int end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
            if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);} else {board&=~(1L<<end);}
        } else if (move.charAt(3)=='P') {//pawn promotion
            int start, end;
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[6]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[7]);
            } else {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[1]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[0]);
            }
            if (type==move.charAt(2)) {board&=~(1L<<start); board|=(1L<<end);} else {board&=~(1L<<end);}
        } else if (move.charAt(3)=='E') {//en passant
            int start, end;
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[4]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[5]);
                board&=~(1L<<(FileMasks8[move.charAt(1)-'0']&RankMasks8[4]));
            } else {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[3]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[2]);
                board&=~(1L<<(FileMasks8[move.charAt(1)-'0']&RankMasks8[3]));
            }
            if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);}
        } else {
            System.out.print("ERROR: Invalid move type");
        }
        return board;
    }
    public static long makeMoveEP(long board,String move) {
        if (Character.isDigit(move.charAt(3))) {
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            if ((Math.abs(move.charAt(0)-move.charAt(2))==2)&&(((board>>>start)&1)==1)) {//pawn double push
                return FileMasks8[move.charAt(1)-'0'];
            }
        }
        return 0;
    }
    public static void timeExperiment(long WP) {
        int loopLength=1000;
        long startTime=System.currentTimeMillis();
        tEMethodA(loopLength, WP);
        long endTime=System.currentTimeMillis();
        System.out.println("That took "+(endTime-startTime)+" milliseconds for the first method");
        startTime=System.currentTimeMillis();
        tEMethodB(loopLength, WP);
        endTime=System.currentTimeMillis();
        System.out.println("That took "+(endTime-startTime)+" milliseconds for the second method");
    }
    public static void tEMethodA(int loopLength, long WP) {
        for (int loop=0;loop<loopLength;loop++)
        {
            long PAWN_MOVES=(WP>>7)&MY_PIECES&~RANK_8&~FILE_A;//capture right
            String list="";
            for (int i=0;i<64;i++) {
                if (((PAWN_MOVES>>i)&1)==1) {
                    list+=""+(i/8+1)+(i%8-1)+(i/8)+(i%8);
                }
            }
        }
    }
    public static void drawBitboard(long bitBoard) {
        String chessBoard[][] = new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]="";
        }
        for (int i=0;i<64;i++) {
            if (((bitBoard>>>i)&1)==1) {chessBoard[i/8][i%8]="P";}
            if ("".equals(chessBoard[i/8][i%8])) {chessBoard[i/8][i%8]=" ";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
    public static void tEMethodB(int loopLength, long WP) {
        for (int loop=0;loop<loopLength;loop++)
        {
            long PAWN_MOVES=(WP>>7)&MY_PIECES&~RANK_8&~FILE_A;//capture right
            String list="";
            long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
            while (possibility != 0)
            {
                int index=Long.numberOfTrailingZeros(possibility);
                list+=""+(index/8+1)+(index%8-1)+(index/8)+(index%8);
                PAWN_MOVES&=~(possibility);
                possibility=PAWN_MOVES&~(PAWN_MOVES-1);
            }
        }
    }
}
