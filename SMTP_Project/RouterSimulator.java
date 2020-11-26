import java.io.*;
import java.util.*;

public class RouterSimulator {
   public static void main(String[] args) throws IOException{
      // User/file input variable
      BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
      BufferedReader fileIn;
      String fromUser;
      String fileName = "topo.txt";
      
      // Information storing variables
      int n;
      int cost[][];
      int tempCost[][];
      String[] lineList;
      int rowCounter= 0;
      boolean validInputFile;
   
      // First, get the n value from the user
      System.out.println("Please enter the number of routers, 'n', in the network.");
      System.out.println("'n' must be greater than or equal to 2 amd non-negative.");
   
      // Get valid input for n
      while (true) {
         System.out.print("Please enter a value for n: ");
         fromUser = sysIn.readLine();
         if (validateDigits(fromUser)) {
            if(Integer.parseInt(fromUser) >= 2) {
               break;
            }
            else {
               System.out.println("Input must be a number greater than or equal to 2!");
            }
         }
         else {
            System.out.println("Input must be a number and non-negative!");
         }
      }
      n = Integer.parseInt(fromUser);
      
      // create an empty cost matrix from n
      cost = new int[n][n];
      // First, fill the cost array with '-1' to symbolize infinities and '0' for nodes going to themselves
      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++){
            if (i == j) {
               cost[i][j] = 0;
            }
            else {
               cost[i][j] = -1;
            }
         }
      }
      
      while(true) {
         validInputFile = true;
         // reset the cost matrix to be be filled
         tempCost = cost;
         // Try and open the topology file and add weights to the cost array
         try {
            fileIn = new BufferedReader(new FileReader(fileName));
            String line = fileIn.readLine();
            while(line != null) {
               lineList = line.split("\\s+");
               rowCounter++;
               if(Integer.parseInt(lineList[0]) >= n || Integer.parseInt(lineList[0]) < 0) {
                  System.out.println("\nError with first router number: " 
                     + lineList[0] + " in " + fileName + " at line: " + rowCounter);
                  validInputFile = false;
                  break;
               }
               if(Integer.parseInt(lineList[1]) >= n || Integer.parseInt(lineList[1]) < 0) {
                  System.out.println("\nError with second router number: " 
                     + lineList[1] + " in " + fileName + " at line: " + rowCounter);
                  validInputFile = false;
                  break;
               }
               if(Integer.parseInt(lineList[2]) < 0) {
                  System.out.println("\nError with edge weight: " 
                     + lineList[2] + " in " + fileName + " at line: " + rowCounter);
                  validInputFile = false;
                  break;
               }
               tempCost[Integer.parseInt(lineList[0])][Integer.parseInt(lineList[1])] = Integer.parseInt(lineList[2]);
               tempCost[Integer.parseInt(lineList[1])][Integer.parseInt(lineList[0])] = Integer.parseInt(lineList[2]);
               line = fileIn.readLine();
            }
            fileIn.close();
         }
         catch (IOException e) {
            System.out.println("\nError opening " + fileName);
            validInputFile = false;
         }
         
         if (!validInputFile) {
            System.out.print("Please enter a new input file name: ");
            fileName = sysIn.readLine();
         }
         else {
            break;
         }
      }
      
      // Update cost matrix with valid values
      // Prints cost matrix as well
      cost = tempCost;
      tempCost = null;
      System.out.println("\nCost matrix:");
      for(int i = 0; i < n; i++) {
         for(int j = 0; j < n; j++){
            System.out.print(cost[i][j] + " ");
         }
         System.out.println();
      }
      
      // Djiktra's Algorithm
      // Initialization
      ArrayList<Integer> N = new ArrayList<Integer>(); // Contains source nodes; list of travelled path
      ArrayList<String> Y = new ArrayList<String>();  // Connecting paths      
      ArrayList<Integer> D = new ArrayList<Integer>(); // Best Path from node to origin
      ArrayList<Integer> P = new ArrayList<Integer>(); // Predecessor vector (contains previous nodes)
            
      N.add(0);
      for (int i = 0; i < n; i++){
         P.add(-1);
         D.add(cost[0][i]);
         if (D.get(i) != -1) { // If i is adjacent to sourceRouter
            P.set(i, 0); // Sets previous node of index to source node
         }
      }
      
      System.out.println("\nThis is iteration: 0");
      System.out.println("Distance Vector:" + Arrays.toString(D.toArray()));   
      System.out.println("Predecessor Vector: " + Arrays.toString(P.toArray()));
      System.out.println("N' : " + Arrays.toString(N.toArray()));  
      System.out.println("Y' : " + Arrays.toString(Y.toArray()));  
      
      for (int count = 0; count < n; count++) {
         int k = findMin(D, N);
         if (k == -1) {
            break;
         }
         N.add(k);
         Y.add("{" + P.get(k) + "," + k + "}");
         
 
         for (int counter = 0; counter < n; counter++) {
            if(D.get(counter) == -1 && cost[counter][k] != -1 && !N.contains(counter)) {
               D.set(counter, D.get(k) + cost[counter][k]);
               P.set(counter, k);
            }
            else if(cost[counter][k] != -1 && D.get(k) + cost[counter][k] < D.get(counter) && !N.contains(counter)) {
               D.set(counter, D.get(k) + cost[counter][k]);
               P.set(counter, k); //gets latest minimum value
            }
         }
         
         System.out.println("\nThis is iteration: " + (count+1) );
         System.out.println("Distance Vector:" + Arrays.toString(D.toArray()));   
         System.out.println("Predecessor Vector: " + Arrays.toString(P.toArray()));
         System.out.println("N' : " + Arrays.toString(N.toArray()));  
         System.out.println("Y' : " + Arrays.toString(Y.toArray()));  
      }

      // Forwarding Table
      System.out.printf("\n             Forwarding Table\n");
      System.out.printf("%-30.30s  %-30.30s%n", "Destination", "Link");
      for (int num = 0; num < n - 1; num++) {
         System.out.printf("%-30.30s  %-30.30s%n", N.get(num+1), Y.get(num));
      }
   }
      
   
   // Helper Methods
   // Validates that the input n is valid
   private static boolean validateDigits(String input) {
      if (input.equals("")) {
         return false;
      }
      
      char[] characters = input.toCharArray();
      boolean validInput = true;
      
      for (char c : characters) {
         if (!Character.isDigit(c)) {
            validInput = false;
            break;
         }
      }
      return validInput;
    }
       
     private static int findMin(ArrayList<Integer> D, ArrayList<Integer> N) {
      int minimumIndex = -1;
      int minimum = -1;
      
      for(int i = 0; i < D.size(); i++) {
         if (!N.contains(i) && D.get(i) != -1) {
            minimum = D.get(i);
            minimumIndex = i;
            break;
         }
      }
      
      if (minimum == -1) {
            return minimumIndex;
      }
      for (int i = 1; i < D.size(); i++){
         if (D.get(i) <= minimum && !N.contains(i) && D.get(i) != -1) {
            minimum = D.get(i);
            minimumIndex = i;
         }
      }
      return minimumIndex;
     }

   
}