package a01;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private int gridSize; //number of rows and columns in the N-by-N grid
	private int timesExperimentRuns; //Amount of times the experiment will be run
	private double[] results; //array that stores the results of the experiments

	/**
	 * Performs T independent experiments on an N­by­N grid
	 * 
	 * @param N - gridSize
	 * @param T - timesExperimentRuns
	 */
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) throw new IllegalArgumentException(
				"Given N or T are less than or equal to 0(PercolationStats())");
		timesExperimentRuns= T;
		gridSize = N;
		results = new double[timesExperimentRuns];
		
		//running the percolation experiment T number of times
		for (int i = 0; i < timesExperimentRuns; i++) {
			results[i] = findPercolationThreshold();
			
		}
	}

	/**
	 * Samples mean of percolation threshold
	 * @return the mean of percolation threshold
	 */
	public double mean() {
		return StdStats.mean(results);
	}

	/**
	 * Finds the sample standard deviation of percolation threshold
	 * @return the sample standard deviation of the percolation threshold
	 */
	public double stddev() {
		if (StdStats.stddev(results) == 1) return Double.NaN;
		return StdStats.stddev(results);
	}

	/**
	 * Finds the low endpoint of 95% confidence interval
	 * 
	 * @return the low endpoint of the 95% confidence interval
	 */
	public double confidenceLow() {
		return mean() - 1.96 * stddev() / Math.sqrt(results.length);
		// TODO
	}

	/**
	 * Finds the high endpoint of 95% confidence interval
	 * 
	 * @return
	 */
	public double confidenceHigh() {
		return mean() + 1.96 * stddev() / Math.sqrt(results.length);
		// TODO
	}
	
	/**
	 * Calculates percolation threshold for each experiment
	 * @return percolationThreshold
	 */
	private double findPercolationThreshold() {
	    Percolation perc = new Percolation(gridSize);
	    int i, j;
	    int count = 0;
	    while (!perc.percolates()) {
	      do {
	        i = StdRandom.uniform(gridSize) ;
	        j = StdRandom.uniform(gridSize) ;
	      } while (perc.isOpen(i,j));
	      count++;
	      perc.open(i, j);
	    }
	    return count/(Math.pow(gridSize ,2));
	  }
	
	public static void main(String[] args) {
		//TESTING
	    PercolationStats stats = new PercolationStats(200, 50);
	    StdOut.printf("mean = %f\n", stats.mean());
	    StdOut.printf("stddev = %f\n", stats.stddev());
	    StdOut.printf("confidenceLow = %f\n", stats.confidenceLow());
	    StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
	    
	    StdOut.println();
	    
	    PercolationStats stats1 = new PercolationStats(200, 100);
	    StdOut.printf("mean = %f\n", stats1.mean());
	    StdOut.printf("stddev = %f\n", stats1.stddev());
	    StdOut.printf("confidenceLow = %f\n", stats1.confidenceLow());
	    StdOut.printf("confidenceHigh = %f\n", stats1.confidenceHigh());
	    
	    StdOut.println();
	    
	    PercolationStats stats2 = new PercolationStats(500, 120);
	    StdOut.printf("mean = %f\n", stats2.mean());
	    StdOut.printf("stddev = %f\n", stats2.stddev());
	    StdOut.printf("confidenceLow = %f\n", stats2.confidenceLow());
	    StdOut.printf("confidenceHigh = %f\n", stats2.confidenceHigh());
	    
	    
	}

}
