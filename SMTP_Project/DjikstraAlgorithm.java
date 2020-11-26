import java.net.*;
import java.io.*;
import java.util.*;

public class DjikstraAlgorithm {
   
   public static void main(String args[]) { 
    
      ArrayList<Integer> N = new ArrayList(); // Contains source nodes; list of travelled path
      ArrayList<String> Y = new ArrayList();  // Cost matrix for the current node we're on
      ArrayList<Integer> D = new ArrayList(); // Default distance vector (contains costs of edges between source to i)
      ArrayList<Integer> P = new ArrayList(); // Predecessor vector (contains previous nodes)
      String c = ""; //previous node
    
      // Initialization
      N.set(0, 0);
      int sourceNode = N.get(0);
      for (int i = 0; i < D.size(); i++){  // Replace D.size with n
         if (D.get(i) != -1) { // If i is adjacent to sourceRouter
            P.set(i, sourceNode); // Sets previous node of index to source node
         }
         else {
            D.set(i, -1); // If not adjacent, cost is infinity.
            P.set(i, -1);
         }
      } // end for loop
   
   
      // LOOPING FOR OPERATIONS
      // Loop to find minimum in distance array D
      for (int count = 2; count < n; count++){
         double minimum = D.get(1);
         int minimumIndex; // Holds the index (node) of the current minimum
         if (D.get(count) < minimum && !N.contains(D.get(count) && D.get(count) != -1)){  // If it's the minimum and node hasn't been visited yet
           minimum = D.get(count);  // Sets new min
           minimumIndex = count;
           N.add(count);
           Y.add("{" + P.get(count) + "," + count + "}"); // Adds path to Y. ex: {u,x} in the form of {0,3}
         
         // Updates D and P
         for (int count = 0; count < D.size; count++){
            //set new D equal to the row of minimumIndex on the matrix
            //set new P equal to the new row's costs
            if(minimum + D.get(i) < D.get(i)){
               D.get(i) = minimum + D.get(i);
            }
         } // End for loop
      
      
      } // end for loop
      
   } // end main
   
}