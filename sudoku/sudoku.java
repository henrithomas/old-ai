
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class sudoku 
{
	static int[][] Board = new int[9][9];
	static int[][] AllDiffs = new int[27][9];
	static int[][] Domains = new int[81][9];
	static int[] NakedPair_A = new int[2];
	static int[] NakedPair_B = new int[2];
	static int[] NakedTriple_A = new int[3];
	static int[] NakedTriple_B = new int[3];
	static int[] NakedTriple_C = new int[3];
	static boolean SINGLES = false;
	static boolean DOUBLES = false;
	static boolean TRIPLES = false;
	static void printBoard()
	{
		System.out.println("Board:");
		for(int i =0; i < 9; i++)
		{
				for(int j = 0; j < 9; j++)
				{
					if(j == 3 || j == 6)
						System.out.print("| " + Board[i][j]+" ");
					else
						System.out.print(Board[i][j]+" ");
				}
				System.out.println();
				if(i == 2 || i == 5)
				{
					for(int k = 0; k < 11; k++)
					{
						System.out.print("- ");
					}
					System.out.println();
				}
		}
	}
	
	static void printAllDiffs()
	{
		System.out.println("\nAllDiffs: \nROWS");
		for(int i =0; i < 27; i++)
		{
			if(i == 9)
				System.out.println("COLUMNS");
			if(i==18)
				System.out.println("BOXES");
			for(int j = 0; j < 9; j++)
			{
				System.out.print(AllDiffs[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	static void printDomains()
	{
		int l = -1, m = 0;
		System.out.println("\nDomains:");
		for(int i =0; i < 81; i++)
		{
			if(i%9 == 0)
				l++;
			if(m == 9)
				m = 0;
			System.out.print(i+": Entry("+l+","+m+") ");
			for(int j = 0; j < 9; j++)
				System.out.print(Domains[i][j]+" ");
			System.out.println();
			m++;
		}
	}
	
	static void setDomains(int row, int col, int el)
	{
		for(int k = 0; k < 9; k++) //zero out domain
		{
			if(el != (k+1))
				Domains[9 * row + col][k] = 0;
		} 
	}
	
	static void setConstraints(int row, int col, int el)
	{
		//ROWS
		AllDiffs[row][col] = el; 
		//COLUMNS
		AllDiffs[col + 9][row] = el;
		//BOXES
		if(row >= 0 && row <= 2)
		{
			if(col >= 0 && col <= 2)
			{
				//box 0
				AllDiffs[18][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 3 && col <= 5)
			{
				//box 1
				AllDiffs[19][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 6 && col <= 8)
			{
				//box 2
				AllDiffs[20][(3 * (row % 3) + (col % 3))] = el;
			}
		}
		if(row >= 3 && row <= 5)
		{
			if(col >= 0 && col <= 2)
			{
				//box 3
				AllDiffs[21][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 3 && col <= 5)
			{
				//box 4
				AllDiffs[22][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 6 && col <= 8)
			{
				//box 5
				AllDiffs[23][(3 * (row % 3) + (col % 3))] = el;
			}
		}
		if(row >= 6 && row <= 8)
		{
			if(col >= 0 && col <= 2)
			{
				//box 6
				AllDiffs[24][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 3 && col <= 5)
			{
				//box 7
				AllDiffs[25][(3 * (row % 3) + (col % 3))] = el;
			}
			if(col >= 6 && col <= 8)
			{
				//box 8
				AllDiffs[26][(3 * (row % 3) + (col % 3))] = el;
			}
		}
	}
	
	static void updateDomains(int r, int c, int val)
	{	
		//ROWS
		for(int i=0; i< 9;i++)
		{
			if(9 * r + i != 9 * r + c)
			{
				Domains[9 * r + i][val - 1] = 0;
			}
		}
		//COLUMNS
		for(int i=0; i< 9;i++)
		{
			if(9 * i + c != 9 * r + c)
			{
				Domains[9 * i + c][val - 1] = 0;
			}
		}
		//BOXES
		if(r >= 0 && r <= 2)
		{
			if(c >= 0 && c <= 2)
			{
				//box 0
				for(int i=0;i<=2;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 3 && c <= 5)
			{
				//box 1
				for(int i=0;i<=2;i++)
				{
					for(int j=3;j<=5;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 6 && c <= 8)
			{
				//box 2
				for(int i=0;i<=2;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
		}
		if(r >= 3 && r <= 5)
		{
			if(c >= 0 && c <= 2)
			{
				//box 3
				for(int i=3;i<=5;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 3 && c <= 5)
			{
				//box 4
				for(int i=3;i<=5;i++)
				{
					for(int j=3;j<=5;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 6 && c <= 8)
			{
				//box 5
				for(int i=3;i<=5;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
		}
		if(r >= 6 && r <= 8)
		{
			if(c >= 0 && c <= 2)
			{
				//box 6
				for(int i=6;i<=8;i++)
				{
					for(int j=0;j<=2;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 3 && c <= 5)
			{
				//box 7
				for(int i=6;i<=8;i++)
				{
					for(int j=3;j<=5;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
			if(c >= 6 && c <= 8)
			{
				//box 8
				for(int i=6;i<=8;i++)
				{
					for(int j=6;j<=8;j++)
					{
						if(9 * i + j != 9 * r + c)
						{
							Domains[9 * i + j][val - 1] = 0;
						}
					}
				}
			}
		}
	}
	
	
	static void findAndEnterSingleDomain(int row, int col)
	{
		int val = 0,count = 0;
		for(int j = 0;j<9;j++)
		{
			if(Domains[9 * row + col][j] > 0)
			{
				count++;
				val = Domains[9 * row + col][j];
			}
		}	
		if(count == 1 && Board[row][col] == 0 && val != 0)
		{
			System.out.println("New entry - ("+row+","+col+"): " + val);
			Board[row][col] = val;
		}
	}
	
	static void makeSingleEntries()
	{
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				findAndEnterSingleDomain(i,j);
			}
		}
	}

	static boolean singlesExist()	//Loop through all boxes, see if you find one with a  
	{								//new single domain to enter
		int count = 0;
		boolean isSingle = false;
		singleCheck:
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				count = 0;
				for(int k = 0;k<9;k++)
				{
					if(Domains[9 * i + j][k] > 0)
					{
						count++;
					}
				}
				if(count == 1 && Board[i][j] == 0)	//a new, guaranteed entry to make
				{
					isSingle = true;
					break singleCheck;
				}
			}
		}
		if(isSingle)
		{
			SINGLES = true;	//update the global too, will be used for the doubles search
			return true;
		}
		else
		{
			SINGLES = false;
			return false;
		}
	}
	
	static boolean doublesExist()
	{
		int count = 0;
		boolean isDouble = false;
		doublesCheck:
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				count = 0;
				for(int k = 0;k<9;k++)
				{
					if(Domains[9 * i + j][k] > 0)
					{
						count++;
					}
				}
				if(count == 2 && Board[i][j] == 0)
				{
					
					isDouble = true;
					break doublesCheck;
				}
			}
		}
		if(isDouble && !SINGLES) //if we have a domain of 2 and no singles
		{
			DOUBLES = true;
			return true;
		}
		else
		{
			DOUBLES = false;
			return false;
		}
	}
	
	static void nakedDoublesRows(int r, int c)
	{
		int val1 = 0, val2 = 0, count = 0, idx1 = 0, idx2 = 0, compCount = 0;
		boolean isPair = false; 
		int[] compare = Domains[9 * r + c];
		idx1 = 9 * r + c;
		for(int i = 0;i<9;i++) //check where we are at for a domain of only 2
		{
			if(compare[i] > 0)
			{
				compCount++;
				if(val1 == 0)
					val1 = compare[i];
				else if (val2 == 0)
					val2 = compare[i];
			}
			if(compCount > 2)	//forget it.
				break;
		}
		if(compCount == 2)
		{
			rows:
			for(int i = (9 * r + c); i<(9 * r + 9)-(9 * r + c);i++) //begin only at the coordinates passed in,
			{														//no need to restart in the entire row
				count = 0;
				for(int j = 0; j< 9; j++)
				{
					if(Domains[9 * r + i][j] >0)
						count++;
					if(count > 2)
						break;
				}
				if(count == 2 && Domains[9 * r + i].equals(compare))
				{
					isPair = true;
					idx2 = i;
					break rows;
				}
			}
			if(isPair)
			{
				//eliminate val1 and val2 from other boxes, restart from beginning of row, skip the pair
				for(int i = 0; i< 9;i++)
				{
					if(i != idx1 || i != idx2)
					{
						for(int j = 0; j<9;j++)
						{
							if(Domains[9 * r + i][j] == val1 || Domains[9 * r + i][j] == val2)
								Domains[9 * r + i][j] = 0;
						}
					}
				}
			}
		}
	}
	
	static void runSingles()
	{
		while(singlesExist())
		{
			makeSingleEntries();	//make any single entries, update
			for(int i=0;i < 9;i++)
			{
				for(int j=0;j < 9;j++)
				{
					if(Board[i][j] > 0)
						updateDomains(i,j,Board[i][j]);
				}
			}	
		}
	}
	
	//runDoubles()
	
	static boolean isWin()
	{
		int sum = 0;
		
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				sum += Board[i][j];
			}
			
			if(sum < 45)
				{return false;}
			sum = 0;
		}
		System.out.println("Congrats Broseph, you win this round of SuBroKu!");
		return true;
	}
	
	public static void main(String[] args)
	{
		int el = 0;
		Scanner inFile = null;
		String inFileName = "/Users/henrithomas/Documents/workspace/AI - Sudoku/src/board.txt";
		try
		{
			inFile = new Scanner(new File(inFileName));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File "+inFileName+" not found!");
		}
		
		//initializes all row/col/boxes domains to 1,2,3,4,5,6,7,8,9 (all possible values)
	    for(int i =0; i < 81;i++)
	    {
	    	for(int j = 0; j < 9;j++)
	    	{
	    		Domains[i][j] = j + 1;
	    	}
	    }
	    
	    //initializes board from input text file
		for(int row = 0; row<9; row++)
		{
			for(int col = 0; col<9; col++)
			{
				el = inFile.nextInt();
				Board[row][col] = el;
				if(el > 0)
				{
					setDomains(row,col,el);
				}
				setConstraints(row,col,el);
			}
		}
		
		//prints new board, each row/col/box, and the domain of every value for the board
		printBoard();
		printAllDiffs();
		for(int i=0;i < 9;i++)
		{
			for(int j=0;j < 9;j++)
			{
				//if(i < 9 && j < 9)
				//{
					if(Board[i][j] > 0)
					{
						updateDomains(i,j,Board[i][j]);
					}
				//}
			}
		}
		printDomains();
		//runSingles();
		//System.out.println("\nUPDATE TEST");
	
		while(!isWin()) {
			for(int i=0;i < 9;i++)
			{
				for(int j=0;j < 9;j++)
				{
					//if(i < 9 && j < 9)
					//{
						if(Board[i][j] > 0)
						{
							updateDomains(i,j,Board[i][j]);
						}
					//}
				}
			}
			
			printBoard();
			makeSingleEntries();
			System.out.print("\nNew ");
			printBoard();
		}
		//printDomains();
		//printBoard();
	}
}


/*
public static void main(String[] args)
{
	int el = 0;
	Scanner inFile = null;
	String inFileName = "/Users/henrithomas/Documents/workspace/AI - Sudoku/src/board.txt";
	try
	{
		inFile = new Scanner(new File(inFileName));
	}
	catch (FileNotFoundException e)
	{
		System.out.println("File " + inFileName + " not found!");
	}
    for(int i =0; i < 81;i++)
    {
    	for(int j = 0; j < 9;j++)
    	{
    		Domains[i][j] = j + 1;
    	}
    }
    
	for(int row = 0; row<9; row++)
	{
		for(int col = 0; col<9; col++)
		{
			el = inFile.nextInt();
			Board[row][col] = el;
			if(el > 0)
			{
				setDomains(row,col,el);
			}
			setConstraints(row,col,el);
		}
	}
	printBoard();
	printAllDiffs();
	printDomains();
	System.out.println("\nUPDATE TEST");
	for(int i=0;i < 9;i++)
	{
		for(int j=0;j < 9;j++)
		{
			if(i < 9 && j < 9)
			{
				if(Board[i][j] > 0)
				{
					updateDomains(i,j,Board[i][j]);
				}
			}
		}
	}
	printDomains();
	printBoard();
	makeEntries();
	System.out.print("\nNew ");
	printBoard();

}
*/