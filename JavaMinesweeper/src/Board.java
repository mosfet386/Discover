import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Board extends JPanel {

	/* Board CONSTANTS */
	private final int NUM_IMAGES=13;
	private final int CELL_SIZE=15;
	
	private final int COVER_FOR_CELL=10;
	private final int MARK_FOR_CELL=10;
	private final int EMPTY_CELL=0;
	private final int MINE_CELL=9;
	
	private final int COVERED_MINE_CELL=MINE_CELL+COVER_FOR_CELL;
	private final int MARKED_MINE_CELL=COVERED_MINE_CELL+MARK_FOR_CELL;
	
	private final int DRAW_MINE=9;
	private final int DRAW_COVER=10;
	private final int DRAW_MARK=11;
	private final int DRAW_WRONG_MARK=12;
	
	private final int N_MINES=40;
	private final int N_ROWS=16;
	private final int N_COLS=16;
	
	/* Board Variables */
	private int[] field;
	private boolean inGame;
	private int mines_left;
	private Image[] img;
	
	int fileX1,fileY1;
	int fileX2,fileY2;
	int frameX1,frameY1;
	int frameX2,frameY2;
	
	private Image img2;
	
	private int all_cells;
	private JLabel statusbar;
	
	public Board(JLabel statusbar) {
		this.statusbar=statusbar;
		img=new Image[NUM_IMAGES];
		
		
		img2=(new ImageIcon(System.getProperty("user.dir")+
				"/images/minesweeper_tiles.png")).getImage();
		

		fileX1=0;
		fileY1=0;
		fileX2=fileX1+128;
		fileY2=fileY1+128;
		frameX1=0;
		frameX2=0;
		frameX2=frameX1+15;
		frameY2=frameY1+15;
		
		
		
		for(int i=0;i<NUM_IMAGES;i++){img[i]=(new ImageIcon(i+".jpg")).getImage();}
		setDoubleBuffered(true);
		addMouseListener(new MinesAdapter());
		newGame();
	}
	private void newGame() {
		int current_col;
		Random random=new Random();
		int i=0, position=0, cell=0;
		inGame=true;
		mines_left=N_MINES;
		all_cells=N_ROWS*N_COLS;
		field=new int[all_cells];
		//cover all the cells
		for(i=0;i<all_cells;i++){field[i]=COVER_FOR_CELL;}
		i=0;
		//place mines at random location
		while(i<N_MINES) {
			position=(int)(all_cells*random.nextDouble());
			if((position<all_cells)&&(field[position]!=COVERED_MINE_CELL)) {
				current_col=position%N_COLS;
				field[position]=COVERED_MINE_CELL;
				i++;
				//Locate field cells to the left of each random mine
				if(current_col>0) {
					cell=position-1-N_COLS;
					if(cell>=0)
						if(field[cell]!=COVERED_MINE_CELL)
							field[cell]+=1;
					cell=position-1;
					if(cell>=0)
						if(field[cell]!=COVERED_MINE_CELL)
							field[cell]+=1;
					cell=position+N_COLS-1;
					if(cell<all_cells)
						if(field[cell]!=COVERED_MINE_CELL)
							field[cell]+=1;
				}
				//Locate field cells above & below each random mine
				cell=position-N_COLS;
				if(cell>=0)
					if(field[cell]!=COVERED_MINE_CELL)
						field[cell]+=1;
				cell=position+N_COLS;
				if(cell<all_cells)
					if(field[cell]!=COVERED_MINE_CELL)
						field[cell]+=1;
				//Locate field cells to the right of each random mine
				if(current_col<(N_COLS-1)) {
					cell=position-N_COLS+1;
					if(cell>=0)
						if(field[cell]!=COVERED_MINE_CELL)
							field[cell]+=1;
					cell=position+N_COLS+1;
					if(cell<all_cells)
						if(field[cell]!=COVERED_MINE_CELL)
							field[cell]+=1;
					cell=position+1;
					if(cell<all_cells)
						if(field[cell]!=COVERED_MINE_CELL)
							field[cell]+=1;
				}
			}
		}
	}
	//recursively find empty cells
	public void find_empty_cells(int j) {
		int current_col=j%N_COLS;
		int cell;
		//Check left column cells
		if(current_col>0){
			cell=j-N_COLS-1;
			if(cell>=0)
				if(field[cell]>MINE_CELL){
					field[cell]-=COVER_FOR_CELL;
					if(field[cell]==EMPTY_CELL)
						find_empty_cells(cell);
				}
			cell=j-1;
			if(cell>=0)
				if(field[cell]>MINE_CELL){
					field[cell]-=COVER_FOR_CELL;
					if(field[cell]==EMPTY_CELL)
						find_empty_cells(cell);
				}
			cell=j+N_COLS-1;
			if(cell<all_cells)
				if(field[cell]>MINE_CELL){
					field[cell]-=COVER_FOR_CELL;
					if(field[cell]==EMPTY_CELL)
						find_empty_cells(cell);
				}
			
		}
		//Check upper and lower cells
		cell=j-N_COLS;
		if(cell>=0)
			if(field[cell]>MINE_CELL){
				field[cell]-=COVER_FOR_CELL;
				if(field[cell]==EMPTY_CELL)
					find_empty_cells(cell);
			}
		cell=j+N_COLS;
		if(cell<all_cells)
			if(field[cell]>MINE_CELL){
				field[cell]-=COVER_FOR_CELL;
				if(field[cell]==EMPTY_CELL)
					find_empty_cells(cell);
			}
		//Check right column cells
		if(current_col<(N_COLS-1)){
			cell=j-N_COLS+1;
			if(cell>=0)
				if(field[cell]>MINE_CELL){
					field[cell]-=COVER_FOR_CELL;
					if(field[cell]==EMPTY_CELL)
						find_empty_cells(cell);
				}
			cell=j+N_COLS+1;
			if(cell<all_cells)
				if(field[cell]>MINE_CELL){
					field[cell]-=COVER_FOR_CELL;
					if(field[cell]==EMPTY_CELL)
						find_empty_cells(cell);
				}
			cell=j+1;
			if(cell<all_cells)
				if(field[cell]>MINE_CELL){
					field[cell]-=COVER_FOR_CELL;
					if(field[cell]==EMPTY_CELL)
						find_empty_cells(cell);
				}
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		int cell=0;
		int uncover=0;
		for(int i=0;i<N_ROWS;i++){
			for(int j=0;j<N_COLS;j++){
				cell=field[(i*N_COLS)+j];
				if(inGame&&cell==MINE_CELL){inGame=false;}
				if(!inGame){
					if(cell==COVERED_MINE_CELL){cell=DRAW_MINE;}
					else if (cell==MARKED_MINE_CELL){cell=DRAW_MARK;}
					else if (cell>COVERED_MINE_CELL){cell=DRAW_WRONG_MARK;}
					else if (cell>MINE_CELL){cell=DRAW_COVER;}
				} else {
					if(cell>COVERED_MINE_CELL){cell=DRAW_MARK;}
					else if(cell>MINE_CELL){cell=DRAW_COVER;uncover++;}
				}
				g.drawImage(img2,fileX1,fileY1,fileX2,fileY2,frameX1,frameY1,frameX2,frameY2,this);
				//g.drawImage(img[cell],(j*CELL_SIZE),(i*CELL_SIZE),this);
			}
		}
		if(uncover==0&&inGame){inGame=false;statusbar.setText("Success!");}
		else if(!inGame){statusbar.setText("Game Lost");}
	}
	//MouseAdapter vs MouseListener, only need to override required methods
	class MinesAdapter extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e){
			int x=e.getX();
			int y=e.getY();
			int cCol=x/CELL_SIZE;
			int cRow=y/CELL_SIZE;
			boolean rep=false;
			if(!inGame){newGame();repaint();}
			if((x<N_COLS*CELL_SIZE)&&(y<N_ROWS*CELL_SIZE)){
				if(e.getButton()==MouseEvent.BUTTON3){
					if(field[(cRow*N_COLS)+cCol]>MINE_CELL){
						rep=true;
						if(field[(cRow*N_COLS)+cCol]<=COVERED_MINE_CELL){
							if(mines_left>0){
								field[(cRow*N_COLS)+cCol]+=MARK_FOR_CELL;
								mines_left--;
								statusbar.setText(Integer.toString(mines_left));
							}else{
								field[(cRow*N_COLS)+cCol]-=MARK_FOR_CELL;
								mines_left++;
								statusbar.setText(Integer.toString(mines_left));
							}
						}
					}else{
						if(field[(cRow*N_COLS)+cCol]>COVERED_MINE_CELL){return;}
						if((field[(cRow*N_COLS)+cCol]>MINE_CELL)&&
								(field[(cRow*N_COLS)+cCol]<MARKED_MINE_CELL)){
							field[(cRow*N_COLS)+cCol]-=COVER_FOR_CELL;
							rep=true;
							if(field[(cRow*N_COLS)+cCol]==MINE_CELL){inGame=false;}
							if(field[(cRow*N_COLS)+cCol]==EMPTY_CELL)
								find_empty_cells((cRow*N_COLS)+cCol);	
						}
					}
					if(rep){repaint();}
				}
			}
		}
	}
}
