import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TSP {
	Scanner keyboard = new Scanner(System.in);
	
	static int number_of_cities = 10;
	static int[][] cost_matrix = new int[number_of_cities][number_of_cities];
	int[] global_minimum_path = new int[number_of_cities + 1];
	int global_minimum_cost = 999999999;
	
	public void hill_climbing()
	{
		int number_of_generations = 100;
		int population_size = 50;
		int[] local_minimum_path = new int[number_of_cities + 1];
		int local_minimum_cost = 0;
		
		Random r = new Random();
		
		setup_cost_matrix(r);
		
		for(int i = 0; i < number_of_cities; i++)
		{
			for(int j = 0; j < number_of_cities; j++)
			{
				System.out.print(cost_matrix[i][j] + "\t");
			}
			System.out.println();
		}
		
		int[] parent = new int[number_of_cities + 1];
		int[] child = new int[number_of_cities + 1];
		
		randomize_path(parent);
		
		local_minimum_path = Arrays.copyOf(parent, parent.length);
		local_minimum_cost = path_cost(parent);
		
		System.out.println("***Initial Path***");
		print_path(local_minimum_path);

		while(number_of_generations >= 0)
		{
			System.out.println();
			System.out.println("**Generation " +number_of_generations+ "**");
			System.out.print("Generation Parent ");
			print_path(parent);
			while(population_size >= 0)
			{
				System.out.print("Swap " +population_size+ " : ");
				child = swap(parent, r);
				
				if(path_cost(child) < local_minimum_cost)
				{
					local_minimum_path = Arrays.copyOf(child, child.length);
					local_minimum_cost = path_cost(child);
					
					System.out.println();
					System.out.println("New Local Minimum found!");
					print_path(local_minimum_path);
					
					System.out.println();
				}
				population_size--;
			}
			
			if(local_minimum_cost < global_minimum_cost)
			{
				global_minimum_path = Arrays.copyOf(local_minimum_path, local_minimum_path.length);
				global_minimum_cost = local_minimum_cost;
				
				System.out.println();
				System.out.println("New Global Minimum found!");
				print_path(global_minimum_path);
				System.out.println();
			}
			population_size = 50;
			local_minimum_cost = path_cost(parent);
			number_of_generations--;
			randomize_path(parent);
		}
		System.out.println("*******Final Global Minimum*******");
		print_path(global_minimum_path);	
	}
	
	static void setup_cost_matrix(Random rand)
	{
		int cost = 0;
		int low = 100;
		int high = 2501;
		for(int i = 0; i < number_of_cities; i++)
		{
			for(int j = i; j < number_of_cities; j++)
			{
				if(j == i) {cost_matrix[i][j] = 0;} 
				else
				{
					cost = rand.nextInt(high) + low;
					cost_matrix[i][j] = cost;
					cost_matrix[j][i] = cost;
				}
			}
		}	
	}
	
	public void randomize_path(int[] parent_input)
	{
		boolean[] city_visited = new boolean[number_of_cities];
		int index = 0;
		boolean found = false;
		
		for(int i = 0; i < number_of_cities; i++)
		{
			found = false;
			while(!found)
			{
				index = (int)(number_of_cities*Math.random());
				
				if(city_visited[index] == false)
				{
					found = true;
					city_visited[index] = true;
				}
			}
			parent_input[i] = index;
		}
		parent_input[number_of_cities] = parent_input[0];
	}
	
	public int[] swap(int[] parent_input, Random rand)
	{
		int[] child = Arrays.copyOf(parent_input, parent_input.length);
		boolean check_index = false;
		
		while(!check_index)
		{
			int index_1 = rand.nextInt(number_of_cities - 1) + 1;
			int index_2 = rand.nextInt(number_of_cities - 1) + 1;
			if(index_1 == index_2)
			{
				check_index = false;
			}
			
			else 
			{
				check_index = true;
			//	System.out.println("Swap index 1: " + index_1 + "\nSwap index 2: " + index_2);
				int temp1 = child[index_1];
				int temp2 = child[index_2];
				child[index_2] = temp1;
				child[index_1] = temp2;
			}
		}
		print_path(child);
		
		return child;
	}
	
	static int path_cost(int[] path)
	{
		int cost = 0; 
		for(int i = 0; i < number_of_cities; i++)
		{
			cost += cost_matrix[path[i]][path[i + 1]];
		}
		return cost;
	}
	
	static void print_path(int[] array)
	{
		System.out.print("Path: ");
		for(int j = 0; j < array.length; j++)
		{
			System.out.print(array[j] + " ");
		}
		System.out.print("  |  Cost: " +path_cost(array));
		System.out.println();
	}
	
	public static void main(String[] Args) throws FileNotFoundException
	{
		TSP tsp = new TSP();
		PrintStream out = new PrintStream(new FileOutputStream("run11.txt"));
		System.setOut(out);
		tsp.hill_climbing();
	}
}