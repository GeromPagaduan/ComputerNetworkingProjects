/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Weiying Zhu
 * Edited by:
 * Joshua Hoshiko
 * Gerom Pagaduan
 * CS 3700
 */ 

import java.net.*;
import java.io.*;
import java.util.*;

public class TCPMultiServerThread extends Thread {
    private Socket clientTCPSocket = null;

    public TCPMultiServerThread(Socket socket) {
		super("TCPMultiServerThread");
		clientTCPSocket = socket;
    }

    public void run() {

      System.out.println("Connecting...");
      
		try {
	 	   PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
	  		BufferedReader cSocketIn = new BufferedReader(
				    new InputStreamReader(
				    clientTCPSocket.getInputStream()));

	      String fromClient, toClient, fileName, message;
         Date currentDate = java.util.Calendar.getInstance().getTime();    //gets current date
			String[] parseClient; //array used to store split string
         
	 	   while ((fromClient = cSocketIn.readLine()) != null) {
            while (true) {
               String newLine = cSocketIn.readLine();
               if (newLine.equals("")) {
                  break;
               }
               fromClient = fromClient + " " + newLine;
            }
            System.out.println("Commands from client, new-lines removed:");
            System.out.println(fromClient);
            parseClient = fromClient.split("\\s+");    //split client's input to strings for each space
            fileName = parseClient[1].replace("/", "");  //removes / from the string
            File resource = new File(fileName);    //creates resource

            
            // returns 400 if GET not found
            if (!parseClient[0].equals("GET")) {
                message = printHeader("HTTP/1.1 400 Bad Request", currentDate, clientTCPSocket) + "\r\n" + "\r\n" + "\r\n" + "\r\n";
                cSocketOut.println(message);
            }
            
            // returns 404 if source file not found
            else if (!resource.isFile()) {
                message = printHeader("HTTP/1.1 404 File Not Found", currentDate, clientTCPSocket) + "\r\n" + "\r\n" + "\r\n" + "\r\n";
                cSocketOut.println(message);
            }
            
            // returns 200 if previous conditions aren't met
            else {
            BufferedReader fileIn = new BufferedReader(new FileReader(resource));
            message = printHeader("HTTP/1.1 200 OK", currentDate, clientTCPSocket) + "\r\n" +  printBody(resource) + "\r\n" + "\r\n" + "\r\n" + "\r\n";
            cSocketOut.println(message);
            }
            parseClient = null;
	 	   }
			
		   cSocketOut.close();
		   cSocketIn.close();
		   clientTCPSocket.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
    
    
    //Helper methods
    private String printHeader(String statusMessage, Date currentDate, Socket server) {
      return (statusMessage + "\r\n" + "Date: " + currentDate + "\r\n" + "Server: " + clientTCPSocket + "\r\n");
    }
  
    private String printBody (File resource) {
    String body, contents = "";
      try {
         BufferedReader fileIn = new BufferedReader(new FileReader(resource));
         while( (body = fileIn.readLine()) != null ) {
            contents = contents + body + "\n";
         }
      } 
      catch (IOException e) {
		    e.printStackTrace();
		}
      return contents;
      
    } // end printBody    
} //end class