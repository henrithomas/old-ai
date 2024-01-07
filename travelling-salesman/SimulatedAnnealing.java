import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealing 
{	
	static void setupCosts(int[][] Costs,int numCities)
	{
		Random r = new Random();
		int cost = 0;
		int low = 100;
		int high = 2501;
		for(int i = 0; i < numCities; i++)
		{
			for(int j = i; j < numCities; j++)
			{
				if(j == i) {Costs[i][j] = 0;} 
				else
				{
					cost = r.nextInt(high - low) + low;
					Costs[i][j] = cost;
					Costs[j][i] = cost;
				}
			}
		}	
	}
	static int[] generatePath(int numCities)
	{
		boolean[] chosen = new boolean[numCities]; 
		boolean found = false;
		int index = 0;
		int[] path = new int[numCities + 1];
		for(int i = 0; i < numCities; i++)
		{
			found = false;
			while(!found)
			{
				index = (int)(numCities * Math.random());
				if(chosen[index] == false)
				{ 
					found = true;
					chosen[index] = true;
				} 
			}
			path[i] = index;
		}
		path[numCities] = path[0];
		return path;
	}
	
	static int[] swap(int[] array, int numCities, Random rand)
	{
		int[] path = Arrays.copyOf(array, array.length);
		boolean idxCheck = false;
		while(!idxCheck)
		{
			int idx1 = rand.nextInt(numCities - 1) + 1;
			int idx2 = rand.nextInt(numCities - 1) + 1;
			if(idx1 == idx2)
			{
				idxCheck = false;
			}
			else 
			{
				idxCheck = true;
				int temp1 = path[idx1];
				int temp2 = path[idx2];
				path[idx2] = temp1;
				path[idx1] = temp2;
			}
		}
		return path;
	}
	
	static int pathCost(int[] path, int[][] costMat, int numCities)
	{
		int cost = 0; 
		for(int i = 0; i < numCities; i++)
		{
			cost += costMat[path[i]][path[i + 1]];
		}
		return cost;
	}
	
	static double acceptance(int currCost, int newCost, double temp)
	{
		if(currCost - newCost > 0)
		{
			return 1.0;
		}
		return Math.exp((currCost - newCost) / temp);
	}
	
	static void printPath(int[] array)
	{
		System.out.println("Path:");
		for(int j = 0; j < array.length; j++)
		{ System.out.print(array[j] + " "); }
		System.out.println();
	}
	
	public static void main(String[] args)
	{
		int NUM_CITIES = 10;
		int currCost = 0;
		int nextCost = 0;
		int bestCost = 0;
		int BestPath[] = new int[NUM_CITIES + 1]; 
		int current[]= new int[NUM_CITIES + 1];
		int next[]= new int[NUM_CITIES + 1];
		int[][] TravelCosts = new int[NUM_CITIES][NUM_CITIES];
		double T = 30000; 
		double cooling = 0.0001;
		double accept = 0.0;
		Random r = new Random();
		
	    setupCosts(TravelCosts, NUM_CITIES);
	    System.out.println("Cost adjacency matrix:");
		for(int i = 0; i < NUM_CITIES; i++)
		{
			for(int j = 0; j < NUM_CITIES; j++)
			{
				System.out.print(TravelCosts[i][j] + "\t");
			}
			System.out.println();
		}
		for(int i = 0; i < 10; i++)
		{
			T = 10000;
			current = generatePath(NUM_CITIES);
			BestPath = current;
			currCost = pathCost(current, TravelCosts, NUM_CITIES);
			System.out.println("\nInitial Cost: " + currCost);
			int initialCost = currCost;
		
			while(T>1)
			{		
				next = swap(current,NUM_CITIES,r);
				nextCost = pathCost(next, TravelCosts, NUM_CITIES);
				accept = acceptance(currCost, nextCost, T);
			
				if(accept > Math.random())
				{
					current = next;
				}
		
				currCost = pathCost(current, TravelCosts, NUM_CITIES);
				bestCost = pathCost(BestPath,TravelCosts, NUM_CITIES);
		
				if(currCost < bestCost)
				{
					BestPath = current;
				}
		
				T *= 1 - cooling;
			}
		
			bestCost = pathCost(BestPath,TravelCosts, NUM_CITIES);
			double initialCostD = (double)initialCost;
			double bestCostD = (double)bestCost;
			double improvement = 0.0;
			improvement = Math.round((Math.abs(initialCostD - bestCostD) / initialCostD) * 100);
			System.out.println("Final path cost: "+ bestCost);
			System.out.println("Path improvement: " + improvement + "%\n");
		}
	}
}
