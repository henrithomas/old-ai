import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;
public class Graphs
{
	
	public static boolean checkDirected(int[][] matrix)
	{
		for(int i = 0; i<matrix.length; i++)
		{
			for(int j = i; j<matrix[0].length;)
			{
				if(matrix[i][j] != matrix[j][i])
				{
					System.out.println("This graph is not directed.");
					return true;
				}
			}
		}
		return false;
	}
	
	public static void checkForLoop(int[][] matrix)
	{
		int[] array = new int[matrix.length];
		for(int i = 0; i< matrix.length; i++)
		{
            if(checkForLoopR(matrix, i, array))
            {
                System.out.println("LOOP FOUND");
                System.exit(0);
            }
		}
		System.out.println("LOOP NOT FOUND");
	}
	public static boolean checkForLoopR(int[][] matrix2, int k, int[] check)
	{
		boolean exit = false;
		if(check[k] == 1)
		{
			return true;
		}
		else 
		{
			int i = 0;
			check[k] = 1;
			while(i<matrix2.length && !exit)
			{
				if(matrix2[k][i] == 1)
				{
					exit = checkForLoopR(matrix2,i,check);
				}
                i++;
			}	
		}
		check[k] = 0;
		return exit;
	} 
	
	public static void main(String[] args)
	{
		int[][] AdjacencyM;
		int el = 0, size = 0;
		Scanner keyboard = new Scanner(System.in);
		Scanner inFile = null;
		
		System.out.println("What matrix size?");
		size = keyboard.nextInt();
		AdjacencyM = new int[size][size];
		boolean done = false;
		do
		{
			System.out.println("Enter file name: ");
			String inFileName = keyboard.next();
			//System.out.println("The file " + inFileName + " has the following lines:");
			
			try
			{
				inFile = new Scanner(new File(inFileName));
				done = true;
			}
			catch (FileNotFoundException e)
			{
				System.out.println("File " + inFileName + " not found!");
				System.out.println("Let\'s try again...");
			}
		}
		while(!done);
		for(int i = 0; i<size; i++)
		{
			for(int j = 0; j<size; j++)
			{
				el = inFile.nextInt();
				AdjacencyM[i][j] = el;
			}
		}
        checkForLoop(AdjacencyM);
		keyboard.close();
	}
}
