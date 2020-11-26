/*
 * Client App upon TCP
 *
 * Weiying Zhu
 * Edited by:
 * Joshua Hoshiko
 * Gerom Pagaduan
 * CS 3700
 */ 

import java.io.*;
import java.net.*;
import java.util.*;

public class SMTPClient {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
        String serverAddress;
        String senderEmail;
        String recieverEmail;
        String subject;
        String message;
        
        boolean cont;
        
        int blankCounter;
        
        long timeSent;
        long timeReceived;
        long timeElapsed;

        // Prompt user for IP/DNS
        System.out.print("Please enter the DNS or IP of the server: ");
        serverAddress = sysIn.readLine();

        try {
            // establish tcp connection
            tcpSocket = new Socket(serverAddress, 5250);
            
            // Initialize read/write
            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            
            // Read in input from server for 200 OK
            fromServer = socketIn.readLine();
            System.out.println("\r\n" + fromServer);
          
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverAddress);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "  + serverAddress);
            System.exit(1);
        }
        
        while (true) {
            // query user for the HTTP command line and User-Agent
            System.out.print("\r\nYour email address: ");
            senderEmail = sysIn.readLine();
            System.out.print("Destination email address: ");
            recieverEmail = sysIn.readLine();
            System.out.print("Subject: ");
            subject = sysIn.readLine();
            System.out.println("Enter your message. Please enter a single '.' when done: ");
            message = "";
            while (true){
               fromUser = sysIn.readLine();
               message = message + "\r\n" + fromUser;
               if (fromUser.equals(".")) {
                  System.out.println("Initiating sending sequence...\r\n");
                  break;
               }
            }
            
            // Step 1: HELO
            while (true) {
               // time sent
               timeSent = System.nanoTime();
               //Send HELO command
               socketOut.println("HELO cs3700a.msudenver.edu");
               // Wait for response
               fromServer = socketIn.readLine();
               // time recieved
               timeReceived = System.nanoTime();
               // Print response
               System.out.println(fromServer);
               // print RTT
               timeElapsed = (timeReceived - timeSent) / 1000000;
               System.out.println("RTT of Query: " + timeElapsed + "ms\r\n");
               if (fromServer.substring(0, 3).equals("250")) {
                  break;
               }
               else {
                  System.out.println("Error sending HELO command.");
                  break;
               }
            }
            
            // Step 2: MAIL FROM
            while (true) {
               // time sent
               timeSent = System.nanoTime();
               //Send MAIL FROM command
               socketOut.println("MAIL FROM: " + senderEmail);
               // Wait for response
               fromServer = socketIn.readLine();
               // time recieved
               timeReceived = System.nanoTime();
               // Print response
               System.out.println(fromServer);
               // print RTT
               timeElapsed = (timeReceived - timeSent) / 1000000;
               System.out.println("RTT of Query: " + timeElapsed + "ms\r\n");
               if (fromServer.substring(0, 3).equals("250")) {
                  break;
               }
               System.out.print("Please re-enter your email address: ");
               senderEmail = sysIn.readLine();
            }
            
            // Step 3: RCPT TO 
            while (true) {
               // time sent
               timeSent = System.nanoTime();
               //Send RCPT TO command
               socketOut.println("RCPT TO: " + recieverEmail);
               // Wait for response
               fromServer = socketIn.readLine();
               // time recieved
               timeReceived = System.nanoTime();
               // Print response
               System.out.println(fromServer);
               // print RTT
               timeElapsed = (timeReceived - timeSent) / 1000000;
               System.out.println("RTT of Query: " + timeElapsed + "ms\r\n");
               if (fromServer.substring(0, 3).equals("250")) {
                  break;
               }
               System.out.print("Please re-enter destination email address: ");
               recieverEmail = sysIn.readLine();
            }
            
            // Step 4: DATA 
            while (true) {
               // time sent
               timeSent = System.nanoTime();
               // Send DATA
               socketOut.println("DATA");
               // Wait for response
               fromServer = socketIn.readLine();
               // time recieved
               timeReceived = System.nanoTime();
               // Print response
               System.out.println(fromServer);
               // print RTT
               timeElapsed = (timeReceived - timeSent) / 1000000;
               System.out.println("RTT of Query: " + timeElapsed + "ms\r\n");
               // Display what is being sent in the body
               System.out.println("\r\nThe following email contents have been sent:");
               System.out.println("DATA" + "\r\nTo: " + recieverEmail + "\r\nFrom: " + senderEmail 
                                   + "\r\nSubject: " + subject + "\r\n\r\n" + message + "\r\n");
               
               // time sent
               timeSent = System.nanoTime();
               // Send Body
               socketOut.println("\r\nTo: " + recieverEmail + "\r\nFrom: " + senderEmail 
                                      + "\r\nSubject: " + subject + "\r\n" + message);
               // Wait for response
               fromServer = socketIn.readLine();
               // time recieved
               timeReceived = System.nanoTime();
               // Print response
               System.out.println(fromServer);
               // print RTT
               timeElapsed = (timeReceived - timeSent) / 1000000;
               System.out.println("RTT of Query: " + timeElapsed + "ms\r\n");
               
               if (fromServer.substring(0, 3).equals("250")) {
                  break;
               }
               else {
                  System.out.println("Error sending DATA command.");
                  break;
               }
            }
            
            //Ask user whether they want to continue
            cont = false;
            while (!cont) {
               System.out.print("\nWould you like to continue? Enter 'yes' or 'no': ");
               fromUser = sysIn.readLine();
			      if (fromUser.equalsIgnoreCase("yes")) {
                  cont = true;
               } 
               else if (fromUser.equalsIgnoreCase("no")) {
                  System.out.println("\nShutting down connection...\r\n");
                  socketOut.println("QUIT");
                  fromServer = socketIn.readLine();
                  System.out.println(fromServer);
                  break;
               } 
            } 
               
            if (!cont)
               break;
            
        }


        socketOut.close();
        socketIn.close();
        sysIn.close();
        tcpSocket.close();
    }
}