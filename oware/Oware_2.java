
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Oware

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

	public Oware()
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
		System.out.print("  ");
		while(i < 6) //print north side (in reverse)
		{
			System.out.print(board[12-i] + " ");
			i++;
		}
		
		System.out.println();
		System.out.print(board[13]); //north goal
		System.out.print("            ");
		System.out.print(board[6]); //south goal
		System.out.println();
		
		i = 0;
		System.out.print("  ");
		while(i < 6) //print south side
		{
			System.out.print(board[i] + " ");
			i++;
		}
		System.out.println();
		System.out.println();
	}
	
	boolean victory()
	{
		int i = 0;
	    southsidesum = 0;
	    northsidesum = 0;
	    
		while(i < 6) //check for victory
		{
			southsidesum = southsidesum + boardstate[i];
			northsidesum = northsidesum + boardstate[i+7];
			i++;
		}
		int totalSouthSum = southsidesum + boardstate[6];
		int totalNorthSum = northsidesum + boardstate[13];
		if(southsidesum == 0 || northsidesum == 0)
		{
			
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
		System.out.println("South total: " + totalSouthSum + "\nNorth total: " + totalNorthSum);
		return false;
		
	}
	
	boolean illegalMove(int p, boolean SMove, int[] board)
	{
		int[] newBoard = Arrays.copyOf(board, board.length);
		if (p == 6 || p == 13)
		{
			return true;
		}
		else if (p < 0 || p > 13)
		{		
			return true;
		}
		else if (newBoard[p] == 0)
		{	
			return true;
		}
		else if (SMove == true && p > 6)
		{	
			return true;
		}
		else if (SMove == false && p < 7)
		{	
			return true;
		}
		return false;
	}

	//System.out.println("Cannot sow goal pits");
	//System.out.println("Pit out of bounds");
	//System.out.println("Cannot sow empty pits");
	//System.out.println("Cannot sow from North's pits");
	//System.out.println("Cannot sow from South's pits");
	
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
					southCaptures++;
				}
			}
			else if (lastpit > 6 && !southturn && lastpit < 13)
			{
				seeds = boardstate[lastpit+2*(6-lastpit)];
				if(seeds != 0)
				{
					boardstate[lastpit+2*(6-lastpit)] = 0;
					boardstate[13] = boardstate[13] + seeds;
					System.out.println("North captured!");
					northCaptures++;
				}
			}
		}
	}
	
	int[] computerCheckForCaptures(boolean south,int pit, int seeds, int numberOfSows, int[] board)
	{
		int lastpit = (pit+numberOfSows) % 14;
		if (board[lastpit] == 1) //check for captures
		{
			if (lastpit < 6 && south)
			{
				seeds = board[lastpit+2*(6-lastpit)];
				if(seeds != 0)
				{
					board[lastpit+2*(6-lastpit)] = 0;
					board[6] = board[6] + seeds;
					//System.out.println("GENERATED South captured!");
				}
			}
			else if (lastpit > 6 && !south && lastpit < 13)
			{
				seeds = board[lastpit+2*(6-lastpit)];
				if(seeds != 0)
				{
					board[lastpit+2*(6-lastpit)] = 0;
					board[13] = board[13] + seeds;
					//System.out.println("GENERATED North captured!");
				}
			}
		}
		return board;
	}
	
	List<int[]> generateMoves(int[] currBoard,boolean southturn)
	{
		List<int[]> newMoves = new ArrayList<int[]>();
		int[] invalid = {-1};
		if(southturn)	//generate south's moves
		{
			for(int i = 0; i < 6; i++)	//for all of south's pits
			{
				if(!illegalMove(i, southturn,currBoard))	//if it is a valid move, add it to the list
				{
					newMoves.add(computerSow(i, currBoard,southturn));
				}
				else
				{
					newMoves.add(invalid);
				}
			}
		}
		else if (!southturn)	//generate north's moves 
		{
			for(int i = 7; i < 13; i++)	//for all of north's pits 
			{
				if(!illegalMove(i,southturn, currBoard))	
				{
					newMoves.add(computerSow(i, currBoard,southturn));
				}
				else
				{
					newMoves.add(invalid);
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
			int j = 0;
			boolean done = false;
			while(!done) //sow around the board
			{
				if(southturn && ((pit+i+1) % 14) != 13)	//skip North's goal pit 
				{
					if(((pit+i+1) % 14) != 13)
						boardstate[(pit+i+1) % 14]++;
					j++;
					if(j > (seeds - 1))
						done = true;
				}
				else if(!southturn && ((pit+i+1) % 14) != 6) //skip South's goal pit 
				{
					boardstate[(pit+i+1) % 14]++;	
					j++;
					if(j > (seeds - 1))
						done = true;
				}
				i++;
			}				
			checkForCaptures(pit, seeds, i);		
		}
	}
	
	int[] computerSow(int pit, int[] board, boolean south) 	//generate a new move and its resulting board 
	{
		int[] newBoard = Arrays.copyOf(board, board.length);
			
		int seeds = newBoard[pit];
		newBoard[pit] = 0;
			
		int i = 0;
		int j = 0;
		boolean done = false;
		while(!done) //sow around the board
		{
			if(south && ((pit+i+1) % 14) != 13)	//skip North's goal pit 
			{
				if(((pit+i+1) % 14) != 13)
					newBoard[(pit+i+1) % 14]++;
				j++;
				if(j > (seeds - 1))
					done = true;
			}
			else if(!south && ((pit+i+1) % 14) != 6) //skip South's goal pit 
			{
				newBoard[(pit+i+1) % 14]++;	
				j++;
				if(j > (seeds - 1))
					done = true;
			}
			i++;
		}		
		newBoard = computerCheckForCaptures(south,pit, seeds, i, newBoard);
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
    int EVALUATE_TEST(int[] board, boolean south)
	{
		if(south)
			return evaluateBoard2(board, south);
		else
			return evaluateBoard1(board, south);
		
	}
	
	int evaluateBoard0(int[] board, boolean south)
	{
		if(south)
			return (board[6] - board[13]);
		else 
			return (board[13] - board[6]);
	}
	
	int evaluateBoard1(int[] board, boolean south)
	{
		int i = 0;
	    int southSum = 0;
	    int northSum = 0;
		while(i < 6) //sum both sides 
		{
			southSum = southSum + board[i];
			northSum = northSum + board[i+7];
			i++;
		}
		if(south)
		{
			return (10 * southSum + 100 * board[6]) - (10 * northSum + 100 * board[13]);
		}
		else
		{
			return (10 * northSum + 100 * board[13]) - (10 * southSum + 100 * board[6]);
		}
	}
	
	int evaluateBoard2(int[] board, boolean south)
	{
		int i = 0;
		int fitness = 0; 
		//int pit1 = 0;
		//int pit2 = 0;
		//int pit3 = 0;
	    int southSum = 0;
	    int northSum = 0;
		while(i < 6) //sum both sides 
		{
			southSum = southSum + board[i];
			northSum = northSum + board[i+7];
			i++;
		}
		if(south)
		{
			fitness = ((10 * southSum + 100 * board[6]) - (10 * northSum + 100 * board[13])
					   + 10 * board[3] + 10 * board[4] + 10 * board[5]);
			
		}
		else
		{
			fitness = ((10 * northSum + 100 * board[13]) - (10 * southSum + 100 * board[6])
					   + 10 * board[10] + 10 * board[11] + 10 * board[12]);
		}	
		return fitness;
	}
	
	int[] minimax(int depth, boolean south, int[] board)
	{
		int i = 0;
		int bestVal = 0;
		int currVal = 0; 
		int pit = -1;
		List<int[]> newMoves = generateMoves(board,south);
		if(depth == 0)
		{
			leaves++;
			bestVal = evaluateBoard0(board,south);
		}
		else 
		{
			if(south)	//SOUTH'S TURN 
			{
				bestVal = Integer.MIN_VALUE;
				pit = -1;
				i = 0;
				for(int[] move : newMoves)
				{
					if(move != null)
					{
						currVal = minimax(depth - 1,!south,move)[0];
						if(currVal > bestVal)
						{
							bestVal = currVal;
							pit = i;
						}	
					}
					i++;
				}
			}
			else		//NORTH'S TURN 
			{
				bestVal = Integer.MAX_VALUE;
				pit = -1;
				i = 0;
				for(int[] move : newMoves)
				{
					if(move != null)
					{
						currVal = minimax(depth - 1,south,move)[0];
						if(currVal < bestVal)
						{
							bestVal = currVal;
							pit = i + 7;
						}
					}
					i++;
				}
			}
		}
		return new int[] {bestVal, pit};
	}
	
	int[] minimaxAlphaBetaPruining(int depth, boolean south, int[] board, int alpha, int beta)
	{	
		int i = 0;
		int currVal = 0; 
		int pit = -1;
		List<int[]> newMoves = generateMoves(board,south);
		boolean allInvalid = true;
		for(int j = 0; j < 6; j++)
		{
			if(newMoves.get(j)[0] != -1)
			{
				allInvalid = false; 
				break; 
			}
		}
		if(depth == 0 || allInvalid)
		{
			//leaves++;
			currVal = evaluateBoard1(board,south);//EVALUATE_TEST(board,south);
			return new int[] {currVal, pit};
		}
		else 
		{
			if(south)	//SOUTH'S TURN 
			{
				currVal = Integer.MIN_VALUE;
				pit = -1;
				i = 0;
				for(int[] move : newMoves)
				{
					
					if(move[0] != -1)
					{
						currVal =  minimaxAlphaBetaPruining(depth - 1,!south,move,alpha, beta)[0];
						if(currVal > alpha)
						{
							alpha = currVal;
							pit = i;
						}	
						if(beta <= alpha) break;
					}
					i++;
				}
				return new int[] {alpha, pit};
			}
			else		//NORTH'S TURN 
			{
				currVal = Integer.MAX_VALUE;
				pit = -1;
				i = 0;
				for(int[] move : newMoves)
				{
					if(move[0] != -1)
					{
						currVal =  minimaxAlphaBetaPruining(depth - 1,!south,move, alpha, beta)[0];
						if(currVal < beta)
						{
							beta = currVal;
							pit = i + 7;
						}
						if(beta <= alpha) break;
					}
					i++;
				}
				return new int[] {beta, pit};
			}
		}	
	}
	
	void play()
	{	
		do
		{
			southturn = false;
			System.out.println("\nNORTH TURN");
			//int pitMove1 = takeUserInput();	//ask user to make a move
			int[] eval1 = minimaxAlphaBetaPruining(14,southturn,Arrays.copyOf(boardstate, boardstate.length),Integer.MIN_VALUE,Integer.MAX_VALUE);
			int pitMove1 = eval1[1];
			sow(pitMove1);

			
			printboard(boardstate);
			
			
			southturn = true;
			System.out.println("\nSOUTH TURN");	//Program makes a move 
			int[] eval2 = minimaxAlphaBetaPruining(14,southturn,Arrays.copyOf(boardstate, boardstate.length),Integer.MIN_VALUE,Integer.MAX_VALUE);
			int pitMove2 = eval2[1];
			sow(pitMove2);
			
			
			printboard(boardstate);
				
		}while(!victory());
		System.out.println("South Captures: " + southCaptures);
		System.out.println("North Captures: " + northCaptures);	
		System.out.println("\nGame Over...");
		System.out.println("\nFinal board:");	//set the new move
		printboard(boardstate);
		input.close();
		

	}
	
	String northPrompt = "North player, enter move: ";
	String southPrompt = "South player, enter move: ";
	boolean southturn; //determine whose turn it is
	int southsidesum = 0;
	int northsidesum = 0;
	int southCaptures = 0;
	int northCaptures = 0;
	int leaves = 0; 
	int[] boardstate;
	Scanner input = new Scanner(System.in);
	
	
	public static void main(String[] args)
	{	
		Oware oware = new Oware(); //initialize board
		System.out.println("Initial board:");
		oware.printboard(oware.boardstate);
		oware.play();
	}
}

//System.out.println("\n");
//System.out.println("\nNEW BOARDSTATE");	//show the new move
//System.out.println("\nNEW BOARDSTATE");	//show the new move
//System.out.println("\n");
//int pit = takeUserInput();	//ask user to make a move
//System.out.println("Leaves evaluated: " + leaves);
//System.out.println("SOUTH'S best move: " + pitMove2);
