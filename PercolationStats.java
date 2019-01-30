package a01;

import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	//made final because they never change once implemented
	private final int GRID_SIZE; //number of rows and columns in the N-by-N grid
	private final int TIMES_EXPERIMENT_RUNS; //Amount of times the experiment will be run
	
	private double[] percolationThreshold; //array that stores the results of the experiments

	/**
	 * Performs T independent experiments on an N­by­N grid
	 * 
	 * @param N - number of columns/rows in the grid
	 * @param T - number of times to run stats
	 */
	public PercolationStats(int N, int T) {
		//throws illegal argument if the numbers given are less than or equal to 0
		if (N <= 0 || T <= 0) throw new IllegalArgumentException("Given N or T are less than or equal to 0(PercolationStats())");
		
		TIMES_EXPERIMENT_RUNS= T;					//stores number of times experiment runs as global variable
		percolationThreshold = new double[TIMES_EXPERIMENT_RUNS];  	//stores results of percolation experiment
		int row;
		int column;
		GRID_SIZE = N * N;						//stores the grid size
		
		for (int i = 0; i < timesExperimentRuns; i++) {			//running the percolation experiment T number of times
			Percolation p = new Percolation(N);			//creates a new Percolation class with an N-by-N grid
			while(!p.percolates()) { 				//randomly opens sites until the system percolates,
				row = StdRandom.uniform(0, N);			//sets row to a random number
				column = StdRandom.uniform(0, N);		//sets column to a random number
				
				/*if the site is not already open, then we open the site
				* and increment the openedSites variable to keep track of
				* how many opened sites there are
				*/
				if(!p.isOpen(row, column)) { 
					p.open(row, column);
				}
			}
			
			/*sets the percolationThreshold array to what the threshold was for this experiment
			* in order to keep track of the threshold for each experiment to later get the
			* statistics of the tests ran
			*/
			percolationThreshold[i] = (double) p.numberOfOpenSites() / (double) GRID_SIZE;
		}
	}

	/**
	 * Samples mean of percolation threshold
	 * @return the mean of percolation threshold
	 */
	public double mean() {
		return StdStats.mean(percolationThreshold);
	}

	/**
	 * Finds the sample standard deviation of percolation threshold
	 * @return the sample standard deviation of the percolation threshold
	 */
	public double stddev() {
		if (StdStats.stddev(percolationThreshold) == 1) return Double.NaN; //if the result equals one, then it returns that the double is not a number
		return StdStats.stddev(percolationThreshold);
	}

	/**
	 * Finds the low endpoint of 95% confidence interval
	 * @return the low endpoint of the 95% confidence interval
	 */
	public double confidenceLow() {
		return mean() - ((1.96*stddev())/Math.sqrt(TIMES_EXPERIMENT_RUNS));
	}

	/**
	 * Finds the high endpoint of 95% confidence interval
	 * 
	 * @return
	 */
	public double confidenceHigh() {
		return mean() + ((1.96*stddev())/Math.sqrt(TIMES_EXPERIMENT_RUNS));
	}
}
