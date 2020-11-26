import java.io.*;
import java.net.*;
import java.util.*;

//The Item class contains information for products including the item ID, amount in inventory, description, and price
//Joshua Hoshiko
//Gerom Pagaduan
//CS 3700

public class Item {
   public int id; 
   private int inventory;
   private String description;
   private double price;

      //For testing 
//    public static void main(String[] args) {      
//       Item itemOne = new Item(000001, 157, "New Inspirion 15", 379.99);
//       Item itemTwo = new Item(000002, 128, "New Inspirion 17", 449.99);
//       Item itemThree = new Item(000003, 202, "New Inspirion 15R", 549.99);
//       Item itemFour = new Item(000004, 315, "New Inspirion 15z Ultrabook", 749.99);
//       Item itemFive = new Item(000005, 261, "XPS 14 Ultrabook", 999.99);
//       Item itemSix = new Item(000006, 178, "New XPS 12 UltrabookXPS", 1199.99);
//       
//       Item[] itemList = {itemOne, itemTwo, itemThree, itemFour, itemFive, itemSix};
//       
//       for(int i=0; i<=5; i++) {
//          System.out.println(itemList[i].toString());
//       }
//    }

   public Item() {
      this.id = 0;
      this.inventory = 0;
      this.description = "";
      this.price = 0.0;
   }
   
   public Item(int id, int inventory, String description, double price) {
      this.id = id;
      this.inventory = inventory;
      this.description = description;
      this.price = price;
   }
   
   public String toString() {
      return "\nID: " + id + "\n" + "Inventory: " + inventory + "\n" 
                       + "Description: " + description + "\n"
                       + "Price: " + price;
   }
   
}