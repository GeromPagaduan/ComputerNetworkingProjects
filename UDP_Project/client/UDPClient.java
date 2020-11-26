/*
 * Client App upon UDP
 * Weiying Zhu
 * Edited by:
 * Joshua Hoshiko
 * Gerom Pagaduan
 * CS 3700
 */ 

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient {
    public static void main(String[] args) throws IOException {

            // creat a UDP socket
        DatagramSocket udpSocket = new DatagramSocket();

        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
        String serverAddress;
        boolean cont;
        
        // Prompt user for IP/DNS
        System.out.print("Please enter the DNS or IP of the server: ");
        serverAddress = sysIn.readLine();
        
        // Print items & ids
        System.out.println("Item ID       Item Description");
        System.out.println("0001          New Inspiron 15");
        System.out.println("0002          New Inspiron 17");
        System.out.println("0003          New Inspiron 15R");
        System.out.println("0004          New Inspiron 15z Ultrabook");
        System.out.println("0005          XPS 14 Ultrabook");
        System.out.println("0006          New XPS 12 UltrabookXPS");
        
        System.out.print("\nEnter Item ID Number: ");
        while ((fromUser = sysIn.readLine()) != null) {
            
            //validate that the user entered a number
            if (!validateDigits(fromUser)) {
                  System.out.print("\nID must be a number! Enter Item ID Number: ");
             } // end if
             else {
			 
               // send request
               InetAddress address = InetAddress.getByName(serverAddress);
			      byte[] buf = fromUser.getBytes();
               DatagramPacket udpPacket = new DatagramPacket(buf, buf.length, address, 5250);
               udpSocket.send(udpPacket);
            
               // time sent
               long timeSent = System.nanoTime();  
          
               // get response
		         byte[] buf2 = new byte[256];
               DatagramPacket udpPacket2 = new DatagramPacket(buf2, buf2.length);
               udpSocket.receive(udpPacket2);
            
               // time received
               long timeReceived = System.nanoTime();  
               long timeElapsed = timeReceived - timeSent;
  	        
               // display response
               fromServer = new String(udpPacket2.getData(), 0, udpPacket2.getLength());
               System.out.println("\nItem information From Server: " + fromServer);
               System.out.println("RTT of Query: " + timeElapsed/1000000 + "ms");
               
               //Ask user whether they want to continue
               cont = false;
               while (!cont) {
                  System.out.print("\nWould you like to continue? Enter 'yes' or 'no': ");
                  fromUser = sysIn.readLine();
			         if (fromUser.equalsIgnoreCase("yes")) {
                     cont = true;
                     System.out.print("\nEnter Item ID Number: ");
                  } // end if
                  else if (fromUser.equalsIgnoreCase("no")) {
                     System.out.println("\nShutting down connection...");
                     break;
                  } // end else if
               } // end while
               
               if (!cont)
                  break;
                  
             } // end else
            } // end while
	 	  udpSocket.close();
    } //end main    
    
    private static boolean validateDigits(String input) {
      if (input.equals("")) {
         return false;
      } // end if
      
      char[] characters = input.toCharArray();
      boolean validInput = true;
      
      for (char c : characters) {
         if (!Character.isDigit(c)) {
            validInput = false;
            break;
         } // end if
      } // end for
      return validInput;
    } // end validateDigits  
} //end class