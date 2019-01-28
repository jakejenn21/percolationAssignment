package a01;

import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private int gridSize; //number of rows and columns in the N-by-N grid
	private int timesExperimentRuns; //Amount of times the experiment will be run
	private double[] results; //array that stores the results of the experiments

	/**
	 * Performs T independent experiments on an N­by­N grid
	 * 
	 * @param N
	 * @param T
	 */
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) throw new IllegalArgumentException("Given N or T are less than or equal to 0(PercolationStats())");
		
		timesExperimentRuns= T;
		results = new double[T];
		
		//running the percolation experiment T number of times
		for (int i = 0; i < timesExperimentRuns; i++) {
			Percolation p = new Percolation(N);
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
		if (StdStats.stddev(results) == 1) return Double.NaN; //if the result equals one, then it returns that the double is not a number
		return StdStats.stddev(results);
	}

	/**
	 * Finds the low endpoint of 95% confidence interval
	 * 
	 * @return the low endpoint of the 95% confidence interval
	 */
	public double confidenceLow() {
		return 0d;
		// TODO
	}

	/**
	 * Finds the high endpoint of 95% confidence interval
	 * 
	 * @return
	 */
	public double confidenceHigh() {
		return 0d;
		// TODO
	}
}
