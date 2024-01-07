import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
public class BoardNodeList <T> extends List
{
	int[] Board;
	int[] Goal;
	int boardSize; 
	int size; 
	int nodeSum; 
	int lengthOfPath;
	int mode;
	static List <int[]> closedList = new List <int[]>();
	List <int[]> SolutionStack = new List<int[]>();
	
	public BoardNodeList()
	{
		super();
	}
	
    public BoardNodeList(int[] start)
    {
        super();
        Board = start;
    }
    
    public BoardNodeList(int[] start, int[] goal, int s, int m)
    {
        super();
        Board = start;
        Goal = goal;
        size = s;
        mode = m;
    }
    
    public void setSize(int s)
    {
        size = s;
    }
    
    public void setInitialBoard(int[] start)
    {
    	Board = start;
    } 
    
    public void showClosedList()
    {
    	for(int i = 0; i <= closedList.size();i++)
    	{
            int[] tempEl = closedList.getData(i);
            printBoard(tempEl);
    	}
    }
    
    public void showSolutionSteps(BoardNode node)
    {
    	BoardNode tempNode = node;
    	int[] tempArray;
    	int moveCnt = 0;
    	boolean done = false;
    	while(!done)
    	{
    		SolutionStack.push(Arrays.copyOf(tempNode.getBoardState(), tempNode.getBoardState().length));
    		tempNode = tempNode.getParent(); 
    		if(tempNode == null)
    		{ done = true; }
    	}
    	while(SolutionStack.size() > 0)
    	{
    		tempArray = SolutionStack.top().getData();
    		System.out.println("Move " + moveCnt);
    		printBoard(tempArray);
    		SolutionStack.pop();
    		moveCnt++;
    	}
    }
    
    public static boolean duplicateMove(int[] current)
    {
    	int[] currentMove = Arrays.copyOf(current,current.length);
    	for(int i = 0; i <= closedList.size();i++)
    	{
    		int[] previousMove = closedList.getData(i);
    		if(Arrays.equals(currentMove, previousMove))
    		{ return true; }
    	}
    	return false;
    }
    
    public int findPriorityIndex()
    {
    	BoardNode temp = (BoardNode)getData(0);
    	int tempF = temp.getF();
    	int bestIdx = 0;
    	for (int i = 0; i < this.size(); i++)
    	{
    		temp = (BoardNode)getData(i);
    		if(temp.getF() < tempF)
    		{
    			tempF = temp.getF();
    			bestIdx = i;
    		}
    	}
    	return bestIdx;
    }
    
    public void queueStartingBoard()
    {
    	BoardNode start = new BoardNode(Board,size);
    	start.setG(0);
		if(mode == 1)
		{ start.setH(manhattanDistance(Arrays.copyOf(Board, Board.length),Goal)); }
		else if(mode == 2)
		{ start.setH(tilesOutOfPlace(Arrays.copyOf(Board, Board.length),Goal)); }
		else 
		{ start.setH(0); }
		start.setF();
    	enqueue(start);
    }
    
    public boolean queueAndDequeueAStar() 
    {
        if(head == null)
        {
            System.out.println("No more boards to print.");
            return true;
        }
        else
        {
        	//PRIORITY QUEUEING
        	int priorityIndex = findPriorityIndex();
        	swapToHead(priorityIndex);
            BoardNode tempEl = (BoardNode)getData(0);
            if (tempEl != null)
            {
            	if(Arrays.equals(Arrays.copyOf(tempEl.getBoardState(),tempEl.getBoardState().length), Goal))
            	{
            		System.out.println("********************SOLUTION FOUND********************");
            		tempEl.print();
            		System.out.println("\nSolution Sequence:");
            		showSolutionSteps(tempEl);
            	    System.out.println("End of Solution.");
            		System.out.println("G cost: " + tempEl.getG());
            		System.out.println("Number of nodes expanded: " + nodeSum);
            		return true;
            	}
            	closedList.enqueue(tempEl.getBoardState());  
            	setChildren(tempEl,3);
            	pop();
            	queueChildren(tempEl);
            }
        }
        return false;
    }
    
    public boolean queueAndDequeueBFS() 
    {
        if(head == null)
        {
            System.out.println("No more boards to print.");
            return true;
        }
        else
        {
            BoardNode tempEl = (BoardNode)getData(0);
            if (tempEl != null)
            {
            	if(Arrays.equals(Arrays.copyOf(tempEl.getBoardState(),tempEl.getBoardState().length), Goal))
            	{
            		System.out.println("********************SOLUTION FOUND********************");
            		tempEl.print();
            		System.out.println("\nSolution Sequence:");
            		showSolutionSteps(tempEl);
            	    System.out.println("End of Solution.");
            		System.out.println("G cost: " + tempEl.getG());
            		System.out.println("Number of nodes expanded: " + nodeSum);
            		return true;
            	}
            	//tempEl.print();
            	closedList.enqueue(tempEl.getBoardState());  
            	setChildren(tempEl,3);
            	pop();
            	queueChildren(tempEl);
            }
        }
        return false;
    }
    
    public boolean queueAndDequeueGreedy() 
    {
        if(head == null)
        {
            System.out.println("No more boards to print.");
            return true;
        }
        else
        {
            BoardNode tempEl = (BoardNode)getData(0);
            if (tempEl != null)
            {
            	if(Arrays.equals(Arrays.copyOf(tempEl.getBoardState(),tempEl.getBoardState().length), Goal))
            	{
            		System.out.println("********************SOLUTION FOUND********************");
            		tempEl.print();
            		System.out.println("\nSolution Sequence:");
            		showSolutionSteps(tempEl);
            	    System.out.println("End of Solution.");
            		System.out.println("G cost: " + tempEl.getG());
            		System.out.println("Number of nodes expanded: " + nodeSum);
            		return true;
            	}
            	tempEl.print();
            	closedList.enqueue(tempEl.getBoardState());  
            	setChildrenGreedy(tempEl,3);
            	
            	pop();
            	queueChildrenGreedy(tempEl);
            	//sort
            }
        }
        return false;
    }
    
    public void queueChildren(BoardNode node)
    {
    	if(node.getUp() != null) 
    	{
    		enqueue(node.getUp());
    		nodeSum++;
    	}
    	if(node.getDown() != null) 
    	{
    		enqueue(node.getDown());
    		nodeSum++;
    	}
    	if(node.getLeft() != null) 
    	{
    		enqueue(node.getLeft());
    		nodeSum++;
    	}
    	if(node.getRight() != null) 
    	{
    		enqueue(node.getRight());
    		nodeSum++;
    	}
    }
    
    public void queueChildrenGreedy(BoardNode node)
    {
    	int[] priority = new int[4];
    	if(node.getUp() != null && (!duplicateMove(node.getUp().getBoardState()))) 
    	{
    		priority[0] = node.getUp().getH(); 
    		System.out.println("H1: " + node.getUp().getH());
    	}
    	else 
    	{ priority[0] = -1; }
    	
    	if(node.getDown() != null && (!duplicateMove(node.getDown().getBoardState()))) 
    	{
    		priority[1] = node.getDown().getH();
    		System.out.println("H2: " + node.getDown().getH());
    	}
    	else 
    	{ priority[1] = -1; }
    	
    	if(node.getLeft() != null && (!duplicateMove(node.getLeft().getBoardState()))) 
    	{
    		priority[2] = node.getLeft().getH();
    		System.out.println("H3: " + node.getLeft().getH());
    	}
    	else 
    	{ priority[2] = -1; }
    	
    	if(node.getRight() != null && (!duplicateMove(node.getRight().getBoardState()))) 
    	{
    		priority[3] = node.getRight().getH();
    		System.out.println("H4: " + node.getRight().getH());
    	}
    	else 
    	{ priority[3] = -1; }
    	
    	int minIdx = 0;
    	int val = 1000;
    	double randomizer;
    	for(int i = 0; i < 4; i++)
    	{
    		System.out.println("H value of Child: " + priority[i]);
    		
    		if(priority[i] < val && priority[i] != -1)
    		{ 
    			minIdx = i;
    			val = priority[i];
    		}
    		else if(priority[i] == val)
    		{
    			randomizer = Math.random();
    			
    			if(randomizer < .5)
    			{
    				minIdx = i;
    			}
    		}
    	}
    	
    	System.out.println("Child " + minIdx + " H " + priority[minIdx]);
    	if(minIdx == 0) 
    	{
    		enqueue(node.getUp());
    		nodeSum++;
    	}
    	if(minIdx == 1) 
    	{
    		enqueue(node.getDown());
    		nodeSum++;
    	}
    	if(minIdx == 2) 
    	{
    		enqueue(node.getLeft());
    		nodeSum++;
    	}
    	if(minIdx == 3) 
    	{
    		enqueue(node.getRight());
    		nodeSum++;
    	}
    }
    
    public void setChildren(BoardNode node,int boardSize)
    {
    	//Duplicate board
    	int[] boardTemp = Arrays.copyOf(node.getBoardState(),node.getBoardState().length); 
    	int[] boardTemp2 = Arrays.copyOf(node.getBoardState(),node.getBoardState().length);
    	int[] boardTemp3 = Arrays.copyOf(node.getBoardState(),node.getBoardState().length);
    	int[] boardTemp4 = Arrays.copyOf(node.getBoardState(),node.getBoardState().length);

    	//Find moves
    	boardTemp = findUp(boardTemp);
    	boardTemp2 = findDown(boardTemp2);
    	boardTemp3 = findLeft(boardTemp3);
    	boardTemp4 = findRight(boardTemp4);

    	//Check duplicates
		if((boardTemp != null) && (duplicateMove(Arrays.copyOf(boardTemp, boardTemp.length))))
		{ boardTemp = null; }
		if((boardTemp2 != null) && (duplicateMove(Arrays.copyOf(boardTemp2, boardTemp2.length))))
		{ boardTemp2 = null; }
		if((boardTemp3 != null) && (duplicateMove(Arrays.copyOf(boardTemp3, boardTemp3.length))))
		{ boardTemp3 = null; }
		if((boardTemp4 != null) && (duplicateMove(Arrays.copyOf(boardTemp4, boardTemp4.length))))
		{ boardTemp4 = null; }
		
		//UP Child
    	if (boardTemp != null )
    	{
    		BoardNode upNode = new BoardNode(boardTemp, 3,node);
    		//Set G
    		int G = node.getG() + 1;
    		node.setUp(upNode);
    		node.getUp().setG(G);
    		//Set H
    		if(mode == 1)
    		{ node.getUp().setH(manhattanDistance(Arrays.copyOf(boardTemp, boardTemp.length),Goal)); }
    		else if(mode == 2)
    		{ node.getUp().setH(tilesOutOfPlace(Arrays.copyOf(boardTemp, boardTemp.length),Goal)); }
    		else
    		{ node.getUp().setH(0); }
    		//Set F
    		node.getUp().setF();
    	}
    	else 
    	{node.setUp(null);}
    	
    	//DOWN Child 
    	if(boardTemp2 !=null)
    	{
    		BoardNode downNode = new BoardNode(boardTemp2, 3, node);
    		//Set G
    		int G = node.getG() + 1;
    		node.setDown(downNode);
    		node.getDown().setG(G);
    		//Set H
    		if(mode == 1)
    		{ node.getDown().setH(manhattanDistance(Arrays.copyOf(boardTemp2, boardTemp2.length),Goal)); }
    		else if(mode == 2)
    		{ node.getDown().setH(tilesOutOfPlace(Arrays.copyOf(boardTemp2, boardTemp2.length),Goal)); }
    		else
    		{ node.getDown().setH(0); } 
    		//Set F
    		node.getDown().setF();
    	}
    	else 
    	{ node.setDown(null); }
    	
    	//LEFT Child
    	if(boardTemp3 !=null)
    	{
    		BoardNode leftNode = new BoardNode(boardTemp3, 3, node);
    		//Set G
    		int G = node.getG() + 1;
    		node.setLeft(leftNode);
    		node.getLeft().setG(G);
    		//Set H 
    		if(mode == 1)
    		{ node.getLeft().setH(manhattanDistance(Arrays.copyOf(boardTemp3, boardTemp3.length),Goal)); }
    		else if(mode == 2)
    		{ node.getLeft().setH(tilesOutOfPlace(Arrays.copyOf(boardTemp3, boardTemp3.length),Goal)); }
    		else
    		{ node.getLeft().setH(0); }		
    		//Set F
    		node.getLeft().setF();
    	}
    	else 
    	{ node.setLeft(null); }
    	
    	//RIGHT Child
    	if(boardTemp4 !=null )
    	{
    		BoardNode rightNode = new BoardNode(boardTemp4, 3, node);
    		//Set G
    		int G = node.getG() + 1;
    		node.setRight(rightNode);
    		node.getRight().setG(G);
    		//Set H
    		if(mode == 1)
    		{ node.getRight().setH(manhattanDistance(Arrays.copyOf(boardTemp4, boardTemp4.length),Goal)); }
    		else if(mode == 2)
    		{ node.getRight().setH(tilesOutOfPlace(Arrays.copyOf(boardTemp4, boardTemp4.length),Goal)); }
    		else
    		{ node.getRight().setH(0); }
    		//Set F
    		node.getRight().setF();
    	}
    	else 
    	{ node.setRight(null); }
    }

    public void setChildrenGreedy(BoardNode node,int boardSize)
    {
    	//Duplicate board
    	int[] boardTemp = Arrays.copyOf(node.getBoardState(),node.getBoardState().length); 
    	int[] boardTemp2 = Arrays.copyOf(node.getBoardState(),node.getBoardState().length);
    	int[] boardTemp3 = Arrays.copyOf(node.getBoardState(),node.getBoardState().length);
    	int[] boardTemp4 = Arrays.copyOf(node.getBoardState(),node.getBoardState().length);

    	//Find moves
    	boardTemp = findUp(boardTemp);
    	boardTemp2 = findDown(boardTemp2);
    	boardTemp3 = findLeft(boardTemp3);
    	boardTemp4 = findRight(boardTemp4);

    	//Check duplicates
		if((boardTemp != null) && (duplicateMove(Arrays.copyOf(boardTemp, boardTemp.length))))
		{ boardTemp = null; }
		if((boardTemp2 != null) && (duplicateMove(Arrays.copyOf(boardTemp2, boardTemp2.length))))
		{ boardTemp2 = null; }
		if((boardTemp3 != null) && (duplicateMove(Arrays.copyOf(boardTemp3, boardTemp3.length))))
		{ boardTemp3 = null; }
		if((boardTemp4 != null) && (duplicateMove(Arrays.copyOf(boardTemp4, boardTemp4.length))))
		{ boardTemp4 = null; }
		
		//UP Child
    	if (boardTemp != null )
    	{
    		BoardNode upNode = new BoardNode(boardTemp, 3,node);
    		//Set G
    		int G = node.getG() + 1;
    		node.setUp(upNode);
    		node.getUp().setG(G);
    		//Set H
    		if(mode == 1)
    		{ node.getUp().setH(manhattanDistance(Arrays.copyOf(boardTemp, boardTemp.length),Goal)); }
    		else if(mode == 2)
    		{ node.getUp().setH(tilesOutOfPlace(Arrays.copyOf(boardTemp, boardTemp.length),Goal)); }
    		else
    		{ node.getUp().setH(0); }
    		//Set F
    		node.getUp().setF();
    		setChildren(node.getUp(), 3);
    		
    		if(node.getUp().getUp() == null && node.getUp().getDown() == null &&
    				node.getUp().getLeft() == null && node.getUp().getRight() == null)
    		{
    			node.setUp(null);
    		}
    	}
    	else 
    	{node.setUp(null);}
    	
    	//DOWN Child 
    	if(boardTemp2 !=null)
    	{
    		BoardNode downNode = new BoardNode(boardTemp2, 3, node);
    		//Set G
    		int G = node.getG() + 1;
    		node.setDown(downNode);
    		node.getDown().setG(G);
    		//Set H
    		if(mode == 1)
    		{ node.getDown().setH(manhattanDistance(Arrays.copyOf(boardTemp2, boardTemp2.length),Goal)); }
    		else if(mode == 2)
    		{ node.getDown().setH(tilesOutOfPlace(Arrays.copyOf(boardTemp2, boardTemp2.length),Goal)); }
    		else
    		{ node.getDown().setH(0); } 
    		//Set F
    		node.getDown().setF();
    		
    		setChildren(node.getDown(), 3);
    		
    		if(node.getDown().getUp() == null && node.getDown().getDown() == null &&
    				node.getDown().getLeft() == null && node.getDown().getRight() == null)
    		{
    			node.setDown(null);
    		}
    	}
    	else 
    	{ node.setDown(null); }
    	
    	//LEFT Child
    	if(boardTemp3 !=null)
    	{
    		BoardNode leftNode = new BoardNode(boardTemp3, 3, node);
    		//Set G
    		int G = node.getG() + 1;
    		node.setLeft(leftNode);
    		node.getLeft().setG(G);
    		//Set H 
    		if(mode == 1)
    		{ node.getLeft().setH(manhattanDistance(Arrays.copyOf(boardTemp3, boardTemp3.length),Goal)); }
    		else if(mode == 2)
    		{ node.getLeft().setH(tilesOutOfPlace(Arrays.copyOf(boardTemp3, boardTemp3.length),Goal)); }
    		else
    		{ node.getLeft().setH(0); }		
    		//Set F
    		node.getLeft().setF();
    		
    		setChildren(node.getLeft(), 3);
    		if(node.getLeft().getUp() == null && node.getLeft().getDown() == null &&
    				node.getLeft().getLeft() == null && node.getLeft().getRight() == null)
    		{
    			node.setLeft(null);
    		}
    	}
    	else 
    	{ node.setLeft(null); }
    	
    	//RIGHT Child
    	if(boardTemp4 !=null )
    	{
    		BoardNode rightNode = new BoardNode(boardTemp4, 3, node);
    		//Set G
    		int G = node.getG() + 1;
    		node.setRight(rightNode);
    		node.getRight().setG(G);
    		//Set H
    		if(mode == 1)
    		{ node.getRight().setH(manhattanDistance(Arrays.copyOf(boardTemp4, boardTemp4.length),Goal)); }
    		else if(mode == 2)
    		{ node.getRight().setH(tilesOutOfPlace(Arrays.copyOf(boardTemp4, boardTemp4.length),Goal)); }
    		else
    		{ node.getRight().setH(0); }
    		//Set F
    		node.getRight().setF();
    		
    		setChildren(node.getRight(), 3);
    		if(node.getRight().getUp() == null && node.getRight().getDown() == null &&
    				node.getRight().getLeft() == null && node.getRight().getRight() == null)
    		{
    			node.setRight(null);
    		}
    	}
    	else 
    	{ node.setRight(null); }
    }
    public static int getParity(int[] board)
    {
    	int parity = 0;
    	for(int i = 0; i < board.length; i++)
    	{
    		for(int j = i + 1; j < board.length; j++)
    		{
    			//find greater-than's
    			if((board[i] < board[j]) && (board[i] != 0))
    			{ parity++; }
    		}
    	}
    	return parity;
    }
    
    public static boolean checkParity(int[] start, int[] goal)
    {
    	int startParity = getParity(start);
    	int goalParity = getParity(goal);
    	boolean evenStartPar = false;
    	boolean evenGoalPar = false;
    	if(startParity % 2 == 0) { evenStartPar = true; }
    	if(goalParity % 2 == 0) { evenGoalPar = true; }
    	if(evenStartPar == evenGoalPar) 
    	{  
    		System.out.println("Game can be played. Matching Parities.");
    		return true;
    	}
    	else 
    	{
    		System.out.println("Error. Game cannot be played. Unmatching parities.");
    		return false;
    	}
    }
    public static int[] findUp(int[] board)
    {
    	int blankIdx = findBlank(board);
    	int[] upMove = Arrays.copyOf(board, board.length);
    	if(blankIdx == 0 || blankIdx == 1 ||blankIdx == 2)
    	{
    		//impossible move 
    		return upMove = null;
    	}
    	//find open move
    	int upVal = upMove[blankIdx - 3];
    	upMove[blankIdx] = upVal;
    	upMove[blankIdx - 3] = 0;
    	return upMove;   	
    }
    
    public static int[] findDown(int[] board)
    {
    	int blankIdx = findBlank(board);
    	int[] downMove = Arrays.copyOf(board, board.length);
    	if(blankIdx == 6 || blankIdx == 7 ||blankIdx == 8)
    	{
    		//impossible move, return null
    		return downMove = null;
    	}
        //find open move
    	int downVal = downMove[blankIdx + 3];
    	downMove[blankIdx] = downVal;
    	downMove[blankIdx + 3] = 0;
    	return downMove;  	
    }
    
    public static int[] findLeft(int[] board)
    {
    	int blankIdx = findBlank(board);
    	int[] leftMove = Arrays.copyOf(board, board.length);
    	if(blankIdx == 0 || blankIdx == 3 ||blankIdx == 6)
    	{
    		//impossible move
    		return leftMove = null;
    	}
    	//find open move
    	int leftVal = leftMove[blankIdx - 1];
    	leftMove[blankIdx] = leftVal;
    	leftMove[blankIdx - 1] = 0;
    	return leftMove;     	   	
    }
    
    public static int[] findRight(int[] board)
    {
    	int blankIdx = findBlank(board);
    	int[] rightMove = Arrays.copyOf(board, board.length);
    	if(blankIdx == 2 || blankIdx == 5 ||blankIdx == 8)
    	{
    		//impossible move
    		return rightMove = null;
    	}
        //find open move
       	int rightVal = rightMove[blankIdx + 1];
       	rightMove[blankIdx] = rightVal;
       	rightMove[blankIdx + 1] = 0;
       	return rightMove; 	
    }

    public static int tilesOutOfPlace(int[] board, int[] goalBoard)
    {
    	int numTiles = 0;
    	for(int i = 0; i < board.length; i++)
    	{
    		if((board[i] != goalBoard[i]) && (board[i] != 0))
    		{ numTiles++; }
    	}
    	return numTiles;
    }
    
    public static int manhattanDistance(int[] currBoard, int[] goalBoard)
    {
    	int rowSize = (int) Math.sqrt(currBoard.length);
    	int manhatDist = 0; 
    	for(int i = 0; i < currBoard.length; i++)
    	{
    		for(int j = 0; j < currBoard.length; j++)
    		{
    			if (currBoard[i] == goalBoard[j])
    			{	//determine how much each tile is out of place by their row and column position
    				manhatDist += (Math.abs(i / rowSize - j / rowSize) + Math.abs(i % rowSize - j % rowSize));
    			}
    		}
    	}
    	return manhatDist;
    }
    
    public static int findBlank(int[] board)
    {
    	int idx = 0;
    	for(int i = 0; i < board.length; i++)
    	{
    		if(board[i] == 0)
    		{ idx = i; }
    	}
    	return idx;
    }
    
    public static void printBoard(int[] board)
    {
    	for(int i = 0; i < 3; i++)
    	{
    		System.out.println(board[3*i] + " " + board[3*i+1] + " " + board[3*i+2]);
    	}
    	System.out.println();
    }
    
    public void findSolutionBFS()
    {
    	while(!queueAndDequeueBFS());
    }
    
    public void findSolutionGreedy()
    {
    	while(!queueAndDequeueGreedy());	
    }
    
    public void findSolutionAStar()
    {
    	while(!queueAndDequeueAStar());
    }
    public static void main(String[] args)
	{
   	 Scanner reader = new Scanner(System.in);
   	 
   	 System.out.println("Welcome to the 8-puzzle solver!  Please choose a starting board state.");
   	 System.out.println("Example syntax: 1 2 3 4 5 6 7 8 0 (enter)");
   	 System.out.println("First three #'s are the 1st row, next three are the 2nd row, etc.");
   	 
   	 int[] board = new int[9];
   	 int[] goal =  {1,2,3,8,0,4,7,6,5}; //fixed goal state
   	 
   	 for (int i = 0; i < 9; i++)
   	 {
   		 int n = reader.nextInt();
   		 if (0 <= n && n <= 9 )
   		 {
   			 board[i] = n;
   		 }
   		 else
   		 {
   			 System.out.println("Error: Please enter integers 1-9");
   			 System.exit(0);
   		 }
   	 }

   	 System.out.println("Starting board:");
   	 printBoard(board);
   	 System.out.println("Goal board:");
   	 printBoard(goal);
   	 
   	 if(checkParity(board,goal))
   	 {   		 
   		 System.out.println("");
   		 System.out.println("Please choose an algorithm (enter 1-4):");
   		 System.out.println("1 - Breadth First Search");
   		 System.out.println("2 - Greedy Best First w/ Manhattan Distance Heuristic");
   		 System.out.println("3 - A* w/ Manhattan Distance Heuristic");
   		 System.out.println("4 - A* w/ Misplaced Tiles Heuristic");
   		 
   		 int a = reader.nextInt();
   		 int mode = 1;
   		 if (a == 4)
   		 {
   			 mode = 2;
   		 }
   		 
   		 BoardNodeList <BoardNode> boardQueue = new BoardNodeList <BoardNode>(board,goal,3,mode);
   		 boardQueue.queueStartingBoard();
   		 
   		 final long startTime = System.currentTimeMillis();
   		 
   		 System.out.println("Searching...");
   		 if(a == 1)
   		 {
   			 boardQueue.findSolutionBFS();
   		 }
   		 else if(a == 2)
   		 {
   			 boardQueue.findSolutionGreedy();
   		 }
   		 else if(a == 3)
   		 {
   			 boardQueue.findSolutionAStar();
   		 }
   		 else if(a == 4)
   		 {
   			 boardQueue.findSolutionAStar();
   		 }
   		 else
   		 {
   			 System.out.println("Error: Please enter an integer 1-4");
   			 System.exit(0);
   		 }
   		 
   		 final long endTime = System.currentTimeMillis();
   		 System.out.println("Total time: " + (endTime - startTime) + " milliseconds");
   	 }
}
}
