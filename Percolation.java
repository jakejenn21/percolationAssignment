package a01;
/**
 * @author - Annie Ruiz
 * This program uses Monte Carlo's simulation to estimate the value of percolation
 * threshold given a N-by-N grid */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	//INSTANCE VARS
	private boolean[][] isGridSiteOpen;
	private WeightedQuickUnionUF quickUnion;
	private WeightedQuickUnionUF backWash;
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

		LENGTH = N; 							// assigning the length to N
		totSites = N * N; 						// assigning the total sites to N-by-N
		openSites = 0;							//assigns openSites to 0, since there's no open sites
		isGridSiteOpen = new boolean[N][N]; 				// creates a 2D grid for the sites, immediately initializes to false
		virtualTop = totSites + 1; 					// adds one extra site to the grid
		virtualBottom = totSites + 2; 					// adds a second extra site to the grid
		
	/* quickUnion WeightedQuickUnionUF will be attached to the virtualBottom 
	* and virtualTop sites and will check if the site percolates, the backWash 
	* WeightedQuickUnionUF will be only attached to the top site, and
	* will prevent backwash by never being attached to the virtualBottom,
	* therefore stopping all the open bottom sites from being unionized
	* to the other open bottom sites through the virtualBottom site.
	*/ 
		quickUnion = new WeightedQuickUnionUF(totSites + 3); 		// three are added to include the virtual sites
		backWash = new WeightedQuickUnionUF(totSites + 1);		// one extra to have sites be between 0 to N-1
	}


	/**
	 * Opens site (row i, column j) if it is not open already opened
	 * 
	 * @param i - row number
	 * @param j - column number
	 */
	public void open(int i, int j) {
		checkIfOutOfBounds(i, j);					// calls the method created to check if out of bounds

		if (!isOpen(i, j)) { 						// if the site is not already open, then open it
			isGridSiteOpen[i][j] = true; 				// sets the boolean site to true, signifying the spot as open
			openSites++;						// increments openSites to simplify numberOfOpenSites() method
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
		return isGridSiteOpen[i][j]; 						// returns whether the boolean grid spot is open or not
	}

	/**
	 * Checks if site (row i, column j) is full 
	 * full site is an open site that can be connected to the top row
	 * via a chain of neighboring (left, right, up,
	 * down) open sites.
	 * 
	 * @param i - row number
	 * @param j - column number
	 * @return - whether the site is full or not
	 */
	public boolean isFull(int i, int j) {
		checkIfOutOfBounds(i, j); 						// calls the method created to check if out of bound
		return backWash.connected(xyToOneD(i, j), virtualTop);	
		//uses the backWash WeightedQuickUnionUf since this one isn't attached to the virtualBottom
	}

	/**
	 * Checks if the system percolates
	 * The system percolates when the bottom and the 
	 * top are connected.
	 * 
	 * @return whether the N-by-N grid is connected from the bottom to the top
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
		//sets the site given to a One dimensional number
		int gridSpotOneDNum = xyToOneD(i, j);

		//UNIONIZING TOP
		if (i == 0) {				// this would be the top row, so we attach it to the virtual top
			quickUnion.union(gridSpotOneDNum, virtualTop);
			backWash.union(gridSpotOneDNum, virtualTop);}	
		else { 						
			if (isOpen(i-1, j)) {		//unionizing this site to the site above if it is already open
				quickUnion.union(gridSpotOneDNum, xyToOneD(i - 1, j));
				backWash.union(gridSpotOneDNum, xyToOneD(i - 1, j));} 
		}

		//UNIONIZING BOTTOM
		if (i == LENGTH-1) {			// this would be the bottom row, so we attach it to the virtual bottom
			quickUnion.union(gridSpotOneDNum, virtualBottom);} //we exclude the backWash from unionizing with the virtualBottom	
		else { 
			if (isOpen(i+1, j)) {		//unionizing this site to the site below if it is open
				quickUnion.union(gridSpotOneDNum, xyToOneD(i + 1, j));
				backWash.union(gridSpotOneDNum, xyToOneD(i + 1, j));
			}
		}
		
		//UNIONIZING RIGHT
		if (j < LENGTH-1 && isOpen(i, j+1)) {	// attaching the right side, if it isn't the right edge and is already open
			quickUnion.union(gridSpotOneDNum, xyToOneD(i, j + 1));
			backWash.union(gridSpotOneDNum, xyToOneD(i, j + 1));}
		
		//UNIONIZING LEFT
		if (j > 0 && isOpen(i, j-1)) {	//attaching the left side, if it isn't the left edge and is already open
			quickUnion.union(gridSpotOneDNum, xyToOneD(i, j - 1));
			backWash.union(gridSpotOneDNum, xyToOneD(i, j - 1));}
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
		return (LENGTH * i) + j + 1;
	}

	/**
	 * Simple method to check if the isOpen(), isFull(), or Open() is out of bounds
	 * 
	 * @param i - row number
	 * @param j - column number
	 */
	private void checkIfOutOfBounds(int i, int j) {
		if (i < 0 || i >= LENGTH)
			throw new IndexOutOfBoundsException("row index " + i + " must be between 0 and " + (LENGTH - 1));

		if (j < 0 || j >= LENGTH)
			throw new IndexOutOfBoundsException("column index " + j + " must be between 0 and " + (LENGTH - 1));
	}
	
	/**
	 * Calculates number of opened sites
	 * @return numberOfOpenSites
	 */
	public int numberOfOpenSites() {
		return openSites;
	}
	
	}
	
