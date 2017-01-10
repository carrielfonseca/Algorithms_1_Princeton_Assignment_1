/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 *
 * @author Fabio
 */
public class PercolationStats {
    
    private int n; 
    private int trials;
    private int [] numberOfCellsOpened; // number of cells opened in each trial
    
   public PercolationStats(int n, int trials)    {   // perform trials independent experiments on an n-by-n grid
       if (n <= 0 || trials <= 0) throw new java.lang.IllegalArgumentException("the number rows and trials must be greater than 0");
       this.n = n;
       this.trials = trials;       
       this.numberOfCellsOpened = new int[trials];       
       for (int k = 0; k < trials; k++) {
           Percolation p = new Percolation(n);
           int count = 0;
           while (!p.percolates()) {
               int i = StdRandom.uniform(1, n+1);
               int j = StdRandom.uniform(1, n+1);
               if (!p.isOpen(i, j)) {
                  p.open(i, j);
                  count = count + 1;         
               }   
           }
           numberOfCellsOpened[k] = count;         
       }
   }
   public double mean()     {                     // sample mean of percolation threshold       
       double mean;    
       mean = StdStats.mean(numberOfCellsOpened)/Math.pow(n, 2);
       return mean;
   }
   public double stddev()    {                    // sample standard deviation of percolation threshold
       double sampleStadardDeviation;
       sampleStadardDeviation = StdStats.stddev(numberOfCellsOpened)/Math.pow(n, 2);
       return sampleStadardDeviation;
    }

   public double confidenceLo()  {
        double lowThreshold;
        lowThreshold = mean() - 1.96*(stddev()/Math.sqrt((trials-1)));                
        return lowThreshold;
   }
   
    public double confidenceHi()  {
        double upperThreshold;
        upperThreshold = mean() + 1.96*(stddev()/Math.sqrt((trials-1)));  
        return upperThreshold;
   }
   public static void main(String[] args) {   // test client (described below)
       System.out.println("Please enter the number of rows of the grid");
       int n = StdIn.readInt();
       System.out.println("Please enter the number of trials");
       int trials = StdIn.readInt();
       PercolationStats st = new PercolationStats(n, trials);
       System.out.println(" mean                    =  " + st.mean());
       System.out.println("stddev                   =  "  + st.stddev());
       System.out.println("95% confidence interval = "  + st.confidenceLo() + ", " + st.confidenceHi());     
   }    
}
