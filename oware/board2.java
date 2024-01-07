
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class board 
{
	//TODO:
	//GetWhoMovesFirst 
	//GetHumanMove
	//GenerateComputerMove
	//SowStones 
	//CaptureStones
	//CheckBoard 
	//GenerateSuccessors
	//DoMiniMax
	//DoAlphaBetaPruning 

	public board()
	{
		boardstate = new int[]{3,3,3,3,3,3,0,3,3,3,3,3,3,0};
	}
	
	int[] getboard()
	{
		return boardstate;
	}
	
	void printboard(int[] board)
	{
		int i = 0;
		while(i < 6) //print north side (in reverse)
		{
			System.out.print(board[12-i]);
			i++;
		}
		
		System.out.println();
		System.out.print(board[13]); //north goal
		System.out.print("    ");
		System.out.print(board[6]); //south goal
		System.out.println();
		
		i = 0;
		while(i < 6) //print south side
		{
			System.out.print(board[i]);
			i++;
		}
		System.out.println();
		System.out.println();
	}
	
	boolean victory()
	{
		int i = 0;
		while(i < 6) //check for victory
		{
			southsidesum = southsidesum + boardstate[i];
			northsidesum = northsidesum + boardstate[i+7];
			i++;
		}
		
		if(southsidesum == 0 || northsidesum == 0)
		{
			int totalSouthSum = southsidesum + boardstate[6];
			int totalNorthSum = northsidesum + boardstate[13];
			if(totalSouthSum > totalNorthSum)
			{
				System.out.println("South victory!");
				System.out.println("South total: " + totalSouthSum + "\nNorth total: " + totalNorthSum);
				return true;
			}
			else if (totalSouthSum < totalNorthSum)
			{
				System.out.println("North victory!");
				System.out.println("North total: " + totalNorthSum + "\nSouth total: " + totalSouthSum );
				return true;
			}
			else
			{
				System.out.println("Tie!");
				return true;
			}
		}

			return false;
		
	}
	
	boolean illegalMove(int p, boolean SMove, int[] board)
	{
		int[] newBoard = Arrays.copyOf(board, board.length);
		if (p == 6 || p == 13)
		{
			System.out.println("Cannot sow goal pits");
			return true;
		}
		else if (p < 0 || p > 13)
		{
			System.out.println("Pit out of bounds");
			return true;
		}
		else if (newBoard[p] == 0)
		{
			System.out.println("Cannot sow empty pits");
			return true;
		}
		else if (SMove == true && p > 6)
		{
			System.out.println("Cannot sow from North's pits");
			return true;
		}
		else if (SMove == false && p < 7)
		{
			System.out.println("Cannot sow from South's pits");
			return true;
		}
		return false;
	}
	
	void checkForCaptures(int pit, int seeds, int numberOfSows)
	{
		int lastpit = (pit+numberOfSows) % 14;
		if (boardstate[lastpit] == 1) //check for captures
		{
			if (lastpit < 6 && southturn)
			{
				seeds = boardstate[lastpit+2*(6-lastpit)];
				if(seeds != 0)
				{
					boardstate[lastpit+2*(6-lastpit)] = 0;
					boardstate[6] = boardstate[6] + seeds;
					System.out.println("South captured!");
				}
			}
			else if (lastpit > 6 && !southturn)
			{
				seeds = boardstate[lastpit+2*(6-lastpit)];
				if(seeds != 0)
				{
					boardstate[lastpit+2*(6-lastpit)] = 0;
					boardstate[13] = boardstate[13] + seeds;
					System.out.println("North captured!");
				}
			}
		}
	}
	
	List<int[]> generateMoves(int[] currBoard)
	{
		List<int[]> newMoves = new ArrayList<int[]>();
		if(southturn)	//generate south's moves
		{
			for(int i = 0; i < 6; i++)	//for all of south's pits
			{
				if(!illegalMove(i, southturn,currBoard))	//if it is a valid move, add it to the list
				{
					newMoves.add(computerSow(i, currBoard));
				}
			}
		}
		else if (!southturn)	//generate north's moves 
		{
			for(int i = 7; i < 13; i++)	//for all of north's pits 
			{
				if(!illegalMove(i,southturn, currBoard))	
				{
					newMoves.add(computerSow(i, currBoard));
				}
			}
		}
		return newMoves;
	}
	
	void sow(int pit)
	{	
		if (!illegalMove(pit,southturn,boardstate))
		{
			System.out.println("Sowing pit " + pit);	
			
			int seeds = boardstate[pit];
			boardstate[pit] = 0;
			
			int i = 0;
			
			while( i < seeds) //sow around the board
			{
				boardstate[(pit+i+1) % 14]++;
				i++;
			}
			
			checkForCaptures(pit, seeds, i);
			printboard(boardstate);			
		}
	}
	
	int[] computerSow(int pit, int[] board) 	//generate a new move and its resulting board 
	{
		int[] newBoard = Arrays.copyOf(board, board.length);
		//System.out.println("Sowing pit " + pit);	
			
		int seeds = newBoard[pit];
		newBoard[pit] = 0;
			
		int i = 0;
		while( i < seeds) //sow around the board
		{
			newBoard[(pit+i+1) % 14]++;
			i++;
		}		
		return newBoard; 
	}
	
	int takeUserInput()
	{
		int i = 0;
		do 	//take in user input 
		{
			System.out.print(northPrompt);
			i = input.nextInt();
		}while(illegalMove(i,southturn,boardstate));
		return i;
	}
	
	void play()
	{
			
		do
		{
			southturn = false;
			int i = 0;
			System.out.println("North's possible moves:");	//show all of north's possible moves
			List<int[]> northMoves = generateMoves(boardstate);
			for(int[] move : northMoves)
			{
				System.out.println("Move " + i);
				printboard(move);
				i++;
			}
			
			int pit = takeUserInput();	//ask user to make a move
			sow(pit);
			
			//boardstate = northMoves.get(0);
			System.out.println("\n\nNEW BOARDSTATE");	//set the new move
			printboard(boardstate);
			System.out.println("\n\n");
			
			southturn = true;
			int j = 0;
			System.out.println("South's possible moves:");	//show all of south's possible moves
			List<int[]> southMoves = generateMoves(boardstate);
			for(int[] move : southMoves)
			{
				System.out.println("Move " + j);
				printboard(move);
			}
			
		}while(victory());
		System.out.println("\nGame Over...");
		input.close();
	}
	
	String northPrompt = "North player, enter move: ";
	String southPrompt = "South player, enter move: ";
	boolean southturn; //determine whose turn it is
	int southsidesum = 0;
	int northsidesum = 0;
	int[] boardstate;
	Scanner input = new Scanner(System.in);
	
	
	public static void main(String[] args)
	{	
		board owari = new board(); //initialize board
		System.out.println("Initial board:");
		owari.printboard(owari.boardstate);
		owari.play();
	}
}






/*
  southturn = false; //north turn
//check for captures
int lastpit = (pit+i) % 14;
if (boardstate[lastpit] == 1)
{
	if (lastpit < 6 && southturn)
	{
		seeds = boardstate[lastpit+2*(6-lastpit)];
		if(seeds != 0)
		{
			boardstate[lastpit+2*(6-lastpit)] = 0;
			boardstate[6] = boardstate[6] + seeds;
			System.out.println("South captured!");
		}
	}
	else if (lastpit > 6 && !southturn)
	{
		seeds = boardstate[lastpit+2*(6-lastpit)];
		if(seeds != 0)
		{
			boardstate[lastpit+2*(6-lastpit)] = 0;
			boardstate[13] = boardstate[13] + seeds;
			System.out.println("North captured!");
		}
	}
}

//check for victory
i = 0;

while(i < 6) 
{
	southsidesum = southsidesum + boardstate[i];
	northsidesum = northsidesum + boardstate[i+7];
	i++;
}

if(southsidesum == 0 || northsidesum == 0)
	if(southsidesum + boardstate[6] > northsidesum + boardstate[13])
		System.out.println("South victory!");
	else if (southsidesum + boardstate[6] < northsidesum + boardstate[13])
		System.out.println("North victory!");
	else
		System.out.println("Tie!");
*/	
/*
do 
{
	System.out.print(southPrompt);
	pit = input.nextInt();
}while(illegalMove(pit,southturn,boardstate));
sow(pit);
*/
/*
owari.sow(4,true); //south turn
owari.sow(12,false); //north turn
owari.sow(1,true); //south turn
owari.sow(9,false); //north turn
owari.sow(10,true); //south turn - illegal 
*/