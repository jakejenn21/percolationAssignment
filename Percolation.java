package a01;
/**
 * @author - Annie Ruiz
 * This program uses Monte Carlo's simulation to estimate the value of percolation
 * threshold given a N-by-N grid */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	//METHODS
	private boolean[][] grid;
	private WeightedQuickUnionUF quickUnion;
	private final int LENGTH;
	private int totSites;
	private int openSites; // keeps track of open sites
	private static int virtualTop;
	private static int virtualBottom;

	/**
	 * Creates N-by-N grid, with all sites blocked Has two extra nodes as the
	 * virtual top and the virtual bottom These are used to more easily determine
	 * the percolation state
	 * 
	 * @param N: the size of the grid
	 * @throws IllegalArgumentException if N <= 0
	 */
	public Percolation(int N) {
		// throws exception if an invalid number is given
		if (N <= 0) throw new IllegalArgumentException("Given N is less than or equal to 0 (Percolation())");

		LENGTH = N; 									// assigning the length to N
		totSites = N * N; 								// assigning the total sites to N-by-N
		openSites = 0;									//assigns openSites to 0, since there's no open sites
		grid = new boolean[N][N]; 						// creates a 2D grid for the sites, immediately initializes to false
		virtualTop = totSites + 1; 					// adds one extra site to the grid
		virtualBottom = totSites + 2; 					// adds a second extra site to the grid
		quickUnion = new WeightedQuickUnionUF(totSites + 3); 	// three are added to include the virtual sites
		backWash = new WeightedQuickUnionUf(totSites + 1);		//adds the backWash quick union
	}


	/**
	 * Opens site (row i, column j) if it is not open already Opens
	 * 
	 * @param i - row number
	 * @param j - column number
	 */
	public void open(int i, int j) {
		checkIfOutOfBounds(i, j);						// calls the method created to check if out of bounds

		if (!isOpen(i, j)) { 							// if the site is not already open, then open it
			grid[i][j] = true; 							// changes the boolean to true
			openSites++;
			unionizeSurroundingSides(i, j); 			// unionizes the one site to all the surrounding ones
		}
	}

	/**
	 * Checks if site (row i, column j) is open
	 * 
	 * @param i - row number
	 * @param j - column number
	 * @return - whether the site spot is true or false, (open or unopen)
	 */
	public boolean isOpen(int i, int j) {
		checkIfOutOfBounds(i, j); 						// calls the method created to check if out of bound
		return grid[i][j]; 								// returns whether the boolean grid spot was set to true or false
	}

	/**
	 * Checks if site (row i, column j) is full A full site is an open site that can
	 * be connected to the top row via a chain of neighboring (left, right, up,
	 * down) open sites.
	 * 
	 * @param i - row number
	 * @param j - column number
	 * @return - whether the site is full or not
	 */
	public boolean isFull(int i, int j) {
		checkIfOutOfBounds(i, j); 						// calls the method created to check if out of bound
		return quickUnion.connected(xyToOneD(i, j), virtualTop);
	}

	/**
	 * Checks if the system percolate
	 * 
	 * @return
	 */
	public boolean percolates() {
		return quickUnion.connected(virtualBottom, virtualTop);
	}

	/**
	 * Unionizes the grid site to the surrounding sides when the site is opened
	 * @param i - row number
	 * @param j - column number
	 */
	private void unionizeSurroundingSides(int i, int j) {
		int gridSpotOneDNum = xyToOneD(i, j);

		//UNIONIZING TOP
		if (i == 0) {quickUnion.union(gridSpotOneDNum, virtualTop);}		// this would be the top row, so we attach it to the virtual top
		else { 						
			if (isOpen(i-1, j)) {
				quickUnion.union(gridSpotOneDNum, xyToOneD(i - 1, j));} //unionizing this site to the site above if it is open
		}

		//UNIONIZING BOTTOM
		if (i == LENGTH-1) {
			quickUnion.union(gridSpotOneDNum, virtualBottom); }	// this would be the bottom row, so we attach it to the virtual bottom
		else { 
			if (isOpen(i+1, j)) {
				quickUnion.union(gridSpotOneDNum, xyToOneD(i + 1, j));}//unionizing this site to the site below if it is open
		}
		
		//UNIONIZING LEFT AND RIGHT
		if (j < LENGTH-1 && isOpen(i, j+1)) {
			quickUnion.union(gridSpotOneDNum, xyToOneD(i, j + 1)); }// attaching the right side, if it isn't the right edge and is already open
		if (j > 0 && isOpen(i, j-1)) {	
			quickUnion.union(gridSpotOneDNum, xyToOneD(i, j - 1)); }// attaching the left side, if it isn't the left edge and is already open
	}

	/**
	 * Changes the 2D grid numbers to a 1D number 
	 * if (0,0) - num is 0 
	 * if (0,1) - num is 1 
	 * if (1,0) - num is 10 
	 * if (1,1) - num is 11
	 * 
	 * @param i - row number
	 * @param j - column number
	 * @return corresponding number to the 2d grid
	 */
	private int xyToOneD(int i, int j) {
		return (LENGTH+1 * i) + j;
	}

	/**
	 * Simple method to check if the isOpen(), isFull(), or Open() is out of bounds
	 * 
	 * @param i
	 * @param j
	 */
	private void checkIfOutOfBounds(int i, int j) {
		if (i < 0 || i >= LENGTH)
			throw new IndexOutOfBoundsException("row index " + i + " must be between 0 and " + (LENGTH - 1));

		if (j < 0 || j >= LENGTH)
			throw new IndexOutOfBoundsException("column index " + j + " must be between 0 and " + (LENGTH - 1));
	}

	public int numberOfOpenSites() {
		return openSites;
	}

}
