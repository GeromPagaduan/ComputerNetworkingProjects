/*
 * Server App upon UDP
 * Weiying Zhu
 * Edited by:
 * Joshua Hoshiko
 * Gerom Pagaduan
 * CS 3700
 */ 
 
import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {
    public static void main(String[] args) throws IOException {
	 
	     DatagramSocket udpServerSocket = null;
        BufferedReader in = null;
		  DatagramPacket udpPacket = null, udpPacket2 = null;
		  String fromClient = null, toClient = null;
        boolean morePackets = true;
        
        //Initialize Items
        Item itemOne = new Item(000001, 157, "New Inspirion 15", 379.99);
        Item itemTwo = new Item(000002, 128, "New Inspirion 17", 449.99);
        Item itemThree = new Item(000003, 202, "New Inspirion 15R", 549.99);
        Item itemFour = new Item(000004, 315, "New Inspirion 15z Ultrabook", 749.99);
        Item itemFive = new Item(000005, 261, "XPS 14 Ultrabook", 999.99);
        Item itemSix = new Item(000006, 178, "New XPS 12 UltrabookXPS", 1199.99);
        
        int item;
        
        //Initialize array for items.
        Item[] itemList = {itemOne, itemTwo, itemThree, itemFour, itemFive, itemSix};

		  byte[] buf = new byte[256];
	
		  udpServerSocket = new DatagramSocket(5250);
        
        System.out.println("Starting Server...\nWaiting for client...");
		  	  
        while (morePackets) {
            try {

                // receive UDP packet from client
                udpPacket = new DatagramPacket(buf, buf.length);
                udpServerSocket.receive(udpPacket);

					 fromClient = new String(
					 		udpPacket.getData(), 0, udpPacket.getLength());
                     
                // Determine which item information to send
                // then generate a response
                item = Integer.parseInt(fromClient);
                if (item > itemList.length || item <= 0) {
                  toClient = "**Item not found**";
                }
                else {
                  for (int i = 0; i < itemList.length; i++){
                     if (itemList[i].id == item) {
                        toClient = itemList[i].toString();
                        break;
                     }
                  }
                }
					 
					 // send the response to the client at "address" and "port"
                InetAddress address = udpPacket.getAddress();
                int port = udpPacket.getPort();
					 byte[] buf2 = toClient.getBytes();
                udpPacket2 = new DatagramPacket(buf2, buf2.length, address, port);
                udpServerSocket.send(udpPacket2);
					 
            } catch (IOException e) {
                e.printStackTrace();
					 morePackets = false;
            }
        }
		  
        udpServerSocket.close();

    }
}