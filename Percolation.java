package a01;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	
	private boolean[][] grid;
	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF backwash;
	
	
	private final int VIRTUAL_TOP; // virtual top-site
	private final int VIRTUAL_BOTTOM;   // virtual bottom-site
    private int gridSize;
    private int numOfOpenSites ;
	/**
	 * Creates NbyN grid, with all sites blocked.
	 * @param N size of the grid
	 */
	public Percolation(int N) {
		
	   if (N <= 0) throw new IllegalArgumentException("N must be more than zero");
	
	   gridSize = N;
	   numOfOpenSites = 0;
		// creates grid N x N
	   grid = new boolean[N][N];
	   
	   // initialize top and bottom virtual sites
	   VIRTUAL_TOP = 0;
	   VIRTUAL_BOTTOM = ((N) * (N))+ 1;
	 
	   
	   // initialize WeightedQuickUnionUF objects
	   uf = new WeightedQuickUnionUF(N * N + N);
	   backwash = new WeightedQuickUnionUF(N* N + N);
	   
	   }
	   
	
	/**
	 * Opens site(row i, column j) if it is not open already.
	 * @param if site(row i, column j) is open.
	 * @param i row of the grid
	 * @param j j column of the grid
	 */
	public void open(int i, int j) {
		
		checkIfOutOfBounds(i,j);
		
		if(!isOpen(i,j)) {
			//set cell to open
			grid[i][j]= true;
			numOfOpenSites++;
			//unionize surrounding sides
			unionizeSurroundingSides(i,j);
		}	
	}		
	
	/**
	 * Checks if site(row i, column j) is open.
	 * @param i row of the grid
	 * @param j column of the grid
	 * @return
	 */
	public boolean isOpen(int i, int j) {
		checkIfOutOfBounds(i,j);
		return grid[i][j];
		
	}
	
	/**
	 * Checks if site (row i, column j) is full.
	 * @param i row of the grid
	 * @param j column of the grid
	 * @return
	 */
	public boolean isFull(int i, int j) {
		checkIfOutOfBounds(i, j);
		if(isOpen(i,j)) {
			int index = xyToOneD(i,j);
			return backwash.connected(index, VIRTUAL_TOP);
		}else 
			return false;
		
	}
	
	/**
	 * Checks whether the system percolates.
	 * @return
	 */
	public boolean percolates() {
		return uf.connected(VIRTUAL_TOP, VIRTUAL_BOTTOM);
		
	}
	
	private int xyToOneD(int i, int j) {
        if (i < 0 || i > gridSize || j < 0 || j > gridSize)
            throw new IndexOutOfBoundsException("Illegal parameter value.");
        return ((i * gridSize ) + j + 1) ; 
    }
	/**
	 * 
	 * @param i row of the grid
	 * @param j column of the grid
	 */
	private void unionizeSurroundingSides(int i, int j) {
	int gridSpotOneDNum = xyToOneD(i, j);
	

		if (i == 0) { // union virtual top
			uf.union(VIRTUAL_TOP,gridSpotOneDNum);
			backwash.union(VIRTUAL_TOP,gridSpotOneDNum);
        }
        if (i == (gridSize-1)) { // union virtual bottom
            
        	uf.union(gridSpotOneDNum, VIRTUAL_BOTTOM);
        }

        // union possible neighbor(s)
        if (i > 0 && isOpen(i - 1, j)) {
            uf.union(gridSpotOneDNum, xyToOneD(i - 1, j));
            backwash.union(gridSpotOneDNum, xyToOneD(i - 1, j));
            
        }
        if (i < (gridSize-1) && isOpen(i + 1, j)) {
            uf.union(gridSpotOneDNum, xyToOneD(i + 1, j));
            backwash.union(gridSpotOneDNum, xyToOneD(i + 1, j));
        }
        if (j > 0 && isOpen(i, j - 1)) {
            uf.union(gridSpotOneDNum, xyToOneD(i, j - 1));
            backwash.union(gridSpotOneDNum, xyToOneD(i, j - 1));
        }
        if (j < (gridSize-1) && isOpen(i, j + 1)) {
            uf.union(gridSpotOneDNum, xyToOneD(i, j + 1));
            backwash.union(gridSpotOneDNum, xyToOneD(i, j + 1));
        }
		
	}
	
	
	/**
	 * Simple method to check if the isOpen(), isFull(), or Open() is out of bounds
	 * @param i  row of the grid
	 * @param j column of the grid
	 */
	private void checkIfOutOfBounds(int i, int j) {
		if (i < 0 || i >= gridSize) throw new IndexOutOfBoundsException
		("row index " + i + " must be between 0 and " + (gridSize-1));
		
		if (j < 0 || j >= gridSize) throw new IndexOutOfBoundsException
		("column index " + j + " must be between 0 and " + (gridSize-1));
	}
	/**
	 * Calculates number of opened sites
	 * @return numberOfOpenSites
	 */
	public int numberOfOpenSites() {
		return numOfOpenSites;
	}
	
	}
	
