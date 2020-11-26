/*
 * Server App upon SMTP
 * A thread is created for each connection request from a client
 * So it can handle Multiple Client Connections at the same time
 * Weiying Zhu
 */ 

import java.net.*;
import java.io.*;

public class SMTPMultiServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSMTPSocket = null;
        boolean listening = true;

        try {
            serverSMTPSocket = new ServerSocket(5250);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5250.");
            System.exit(-1);
        }

        while (listening){
	    		new SMTPMultiServerThread(serverSMTPSocket.accept()).start();
		  }
			
        serverSMTPSocket.close();
    }
}