/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 *
 * @author Fabio
 */

public class Percolation {   
    private boolean[][] grid; //grid cell is false if it is closed and true if it is open
    private int n; //grid dimension (number of rows or columns) 
    private int virtualCellTop, virtualCellBottom; 
    private WeightedQuickUnionUF quickUnion;
   
    public Percolation(int n) {     // create n-by-n grid, with all sites blocked
        if (n <= 0) throw new java.lang.IllegalArgumentException("the number rows must be greater than 0");
        this.grid = new boolean[n+1][n+1];
        this.n = n;
        this.virtualCellTop= (int) (Math.pow(n,2));
        this.virtualCellBottom = (int) (Math.pow(n,2)+1);
        this.quickUnion = new WeightedQuickUnionUF((int) (Math.pow(n,2)+2)); //union objects has the grid and the 2 virtual cells at the bottm and top
        for(int i = 1; i < n+1; i++) {
            for(int j = 0; j < n+1; j++) {
                grid[i][j] = false;   
            }
        }  
        // connects the virtual cell at the top with the first row
        for (int i = 0; i < n; i++) {
           quickUnion.union(virtualCellTop, i); 
        }
        for (int i = 1; i <= n; i++) {
           quickUnion.union(virtualCellBottom, (int) Math.pow(n, 2) - i); 
        }      
    }   
    
   public void open(int i, int j)  {   // open site (row i, column j) if it is not open already 
         if (i <= 0 || i > n|| j <= 0|| j > n) throw new IndexOutOfBoundsException("row or col index out of bounds");
         grid[i][j] = true;
         for (int row = i-1;row <= (i+1); row++ ) {
             for (int col = j-1; col <= (j+1); col++) {
                 if ((row == i || col == j) && row >= 1 && row <= n && col >= 1 && col <= n) {
                     if (isOpen(row, col)) {
                         quickUnion.union(mapGridToInteger(i,j), mapGridToInteger(row, col));
                     } 
                 }
             }
         }
   }
   
   public boolean isOpen(int i, int j)  {   // is site (row i, column j) open?
       if (i <= 0 || i > n|| j <= 0|| j > n) throw new IndexOutOfBoundsException("row or col index out of bounds");
       boolean b;
       b = grid[i][j];
       return b;       
   }
   public boolean isFull(int i, int j)  {   // is site (row i, column j) full?
       if (i <= 0 || i > n|| j <= 0|| j > n) throw new IndexOutOfBoundsException("row or col index out of bounds");
       boolean b;      
       b = (quickUnion.connected(virtualCellTop, mapGridToInteger(i, j)) && isOpen(i, j));
       return b;
   }
   
   public boolean percolates() {       
       boolean b = false;       
       if (quickUnion.connected(virtualCellTop, virtualCellBottom)) { 
           if (checkFirstRowIsOpen() && checkLastRowIsOpen()) {
               b = true;
           }
       }
       else {
           b = false;
       }
    return b;   
   }
   
   
   private int mapGridToInteger(int i, int j) {   // return the integer that identifies each cell in the grid
      int x;
       x = (i-1)*n + j -1;
       return x;
   }
   
   private boolean checkFirstRowIsOpen() {
       boolean b = false;
       for (int j = 1; j <= n; j++) {
            if (isOpen(1,j)) {
               b = true;
               break;
            }
       }    
       return b;
   }
   
   private boolean checkLastRowIsOpen() {
       boolean b = false;
       for (int j = 1; j <= n; j++) {
            if (isOpen(n,j)) {
               b = true;
               break;
            }
       }    
       return b;
   }
   public static void main(String[] args)  {
      int n = 2;
      int count = 0;      
      Percolation p = new Percolation(n);      
       while(!p.percolates()) {
         int i = StdRandom.uniform(1, n+1);
         int j = StdRandom.uniform(1, n+1);
         if (!p.isOpen(i, j)) {
         p.open(i, j);
         count = count + 1;         
         }        
      }      
       System.out.println("Percolates with count equals: " + count);         
   }
}
