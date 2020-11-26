/*
 * Server App upon SMTP
 * A thread is started to handle every client SMTP connection to this server
 * Weiying Zhu
 * Edited by:
 * Gerom Pagaduan
 * Joshua Hoshiko
 * CS 3700
 */ 


import java.net.*;
import java.io.*;
import java.util.*;

public class SMTPMultiServerThread extends Thread {
    private Socket clientSMTPSocket = null;
    public SMTPMultiServerThread(Socket socket) {
		super("SMTPMultiServerThread");
		clientSMTPSocket = socket;
    }

    public void run() {
      try {
	 	   PrintWriter cSocketOut = new PrintWriter(clientSMTPSocket.getOutputStream(), true);
	  		BufferedReader cSocketIn = new BufferedReader(
				    new InputStreamReader(
				    clientSMTPSocket.getInputStream()));

         cSocketOut.println("220 cs3700a.msudenver.edu"); // Server talks to client first

	      String fromClient, toClient, fileName, message;
			String[] parseClient; // Stores input
         
         
	 	   while ((fromClient = cSocketIn.readLine()) != null) {             
                        
            // Split client's input to strings for each space
            parseClient = fromClient.split("\\s+");
            
            // Checks for QUIT
            if (parseClient[0].equals("QUIT")) {
               System.out.println(fromClient);
               cSocketOut.println(printResponse(6));
               break;
            }
            
            
            // Checks for HELO
            System.out.println(fromClient);
            while (!parseClient[0].equals("HELO")) {
               cSocketOut.println(printError(1));
            }
            cSocketOut.println(printResponse(1));
            
            
            // Checks for MAIL FROM
            fromClient = cSocketIn.readLine();
            parseClient = fromClient.split("\\s+");
            String mailFrom = parseClient[0] + " " + parseClient[1];
            System.out.println(fromClient);
            while (!mailFrom.equals("MAIL FROM:")) {
               cSocketOut.println(printError(2));
            }
            cSocketOut.println(printResponse(2));
            
            
            // Checks for RCPT TO:
            fromClient = cSocketIn.readLine();
            parseClient = fromClient.split("\\s+");
            String rcptTo = parseClient[0] + " " + parseClient[1];
            System.out.println(fromClient);
            while (!rcptTo.equals("RCPT TO:")) {
               cSocketOut.println(printError(3));
            }
            cSocketOut.println(printResponse(3));
            
            
            // Checks for DATA
            fromClient = cSocketIn.readLine();
            parseClient = fromClient.split("\\s+");
            System.out.println(parseClient[0]);            
            while (!parseClient[0].equals("DATA")) {
               cSocketOut.println(printError(4));
            }
            cSocketOut.println(printResponse(4));
            
            
            // Reads Body
            fromClient = cSocketIn.readLine();
            while (true) {
               String newLine = cSocketIn.readLine();
               fromClient = fromClient + "\r\n" + newLine;
               if (newLine.equals(".")) {
                  break;
               }
            }
            System.out.println(fromClient);
            
            
            // Message sent successful
            cSocketOut.println(printResponse(5));
         }
         System.out.println("Closing all i/o streams");
		   cSocketOut.close();
		   cSocketIn.close();
		   clientSMTPSocket.close();

         
      } 
      catch (IOException e) {
		    e.printStackTrace();
		}
    }
    
    
    // Helper methods
    private String printError(int errorNumber) {
      if (errorNumber == 1) {
         return("503 5.5.2 Send hello first");
      }
      else if (errorNumber == 2) {
         return("503 5.5.2 Need mail command");
      }
      else if (errorNumber == 3) {
         return("503 5.5.2 Need rcpt command");
      }
      else if (errorNumber == 4) {
         return("503 5.5.2 Need data command");
      }
      return "";
    } 
    

    private String printResponse(int responseNumber) {      
      if (responseNumber == 1) {
          try {
            InetAddress ip = InetAddress.getLocalHost();
            return("250 cs3700a.msudenver.edu Hello " + ip);
          }
          catch (UnknownHostException e) {
            e.printStackTrace();
          }
      }
      else if (responseNumber == 2) {
         return("250 2.1.0 Sender OK");
      }
      else if (responseNumber == 3) {
         return("250 2.1.5 Recipient OK");
      }
      else if (responseNumber == 4) {
         return("354 Start mail input; end with <CRLF>.<CRLF>");
      }
      else if (responseNumber == 5) {
         return("250 Message received and to be delivered");
      }
      else if (responseNumber == 6) {
         return("221 cs3700a.msudenver.edu closing connection");
      }
      return "";
    }
      
} // End SMTPMultiServerThread