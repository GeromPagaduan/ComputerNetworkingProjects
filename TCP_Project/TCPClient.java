/*
 * Client App upon TCP
 *
 * Weiying Zhu
 *
 */ 

import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
        String serverAddress;
        String command;
        String resource;
        String version;
        String host;
        String userAgent;
        
        boolean cont;
        
        int blankCounter;
        
        long timeSent;
        long timeReceived;
        long timeElapsed;

        // Prompt user for IP/DNS
        System.out.print("Please enter the DNS or IP of the server: ");
        serverAddress = sysIn.readLine();

        try {
            // time sent
            timeSent = System.nanoTime();  
            
            // establish tcp connection
            tcpSocket = new Socket(serverAddress, 5250);
            
            // time recieved
            timeReceived = System.nanoTime();
            
            // print RTT of establishing the TCP connection
            timeElapsed = (timeReceived - timeSent) / 1000000;
            System.out.println("RTT of Query: " + timeElapsed + "ms");
            
            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverAddress);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "  + serverAddress);
            System.exit(1);
        }
        
        while (true) {
            // query user for the HTTP command line and User-Agent
            System.out.print("HTTP command: ");
            command = sysIn.readLine();
            System.out.print("Resource: ");
            resource = sysIn.readLine();
            System.out.print("HTTP version: ");
            version = sysIn.readLine();
            System.out.print("Host: ");
            host = sysIn.readLine();
            System.out.print("User-Agent: ");
            userAgent = sysIn.readLine();
        
            // Construct an HTTP request out of the given information
            fromUser = constructHTTPRequest(command, resource, version, host, userAgent);
            socketOut.print(fromUser);
            socketOut.flush();
            
            // Read in input from server
            blankCounter = 0;
            while (blankCounter < 4) {
               
               // Read and print the line
               fromServer = socketIn.readLine();
               System.out.println(fromServer);
               
               // These conditionals check if there is a blank line
               // If there are 4 blanks in a row, then the loop terminates
               if (!fromServer.equals("")) {
                  blankCounter = 1;
               }
               else {
                  blankCounter++;   
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
                  System.out.println("\nShutting down connection...");
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
    
    private static String constructHTTPRequest(String command, String resource, String version, String host, String userAgent) {
         return command + " /" + resource + " " + version + "\nHost: " + host + "\nUser-Agent: " + userAgent 
         + "\r\n" + "\r\n";
    }
}