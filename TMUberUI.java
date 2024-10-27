import java.io.FileNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

// Simulation of a Simple Command-line based Uber App 

// This system supports "ride sharing" service and a delivery service

public class TMUberUI
{
  public static void main(String[] args)
  {
    // Create the System Manager - the main system code is in here 

    TMUberSystemManager tmuber = new TMUberSystemManager();
    
    Scanner scanner = new Scanner(System.in);
    System.out.print(">");

    // Process keyboard actions
    while (scanner.hasNextLine())
    {
      String action = scanner.nextLine();

      if (action == null || action.equals("")) 
      {
        System.out.print("\n>");
        continue;
      }
      // Quit the App
      else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
        return;
      // Print all the registered drivers
      else if (action.equalsIgnoreCase("DRIVERS"))  // List all drivers
      {
        tmuber.listAllDrivers(); 
      }
      // Print all the registered users
      else if (action.equalsIgnoreCase("USERS"))  // List all users
      {
        tmuber.listAllUsers(); 
      }
      // Print all current ride requests or delivery requests
      else if (action.equalsIgnoreCase("REQUESTS"))  // List all requests
      {
        tmuber.listAllServiceRequests(); 
      }
      // Register a new driver
      else if (action.equalsIgnoreCase("REGDRIVER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        
        String address = "";  //Address as a new parameter
        System.out.println("Address: ");
        if (scanner.hasNextLine()){
          address = scanner.nextLine();
        }
        
        String carModel = "";
        System.out.print("Car Model: ");
        if (scanner.hasNextLine())
        {
          carModel = scanner.nextLine();
        }
        String license = "";
        System.out.print("Car License: ");
        if (scanner.hasNextLine())
        {
          license = scanner.nextLine();
        }
        /** 
        String address = "";
        if (scanner.hasNextLine()){
          address = scanner.nextLine();
        }
        **/
        try{
          tmuber.registerNewDriver(name, carModel, license, address);
          System.out.printf("Driver: %-15s Car Model: %-15s License Plate: %-10s Address: %-15s", name, carModel, license,address);
        }
        catch (RuntimeException e){
          System.out.println(e.getMessage());
        }
      }
      // Register a new user
      else if (action.equalsIgnoreCase("REGUSER")) 
      {
        String name = "";
        System.out.print("Name: ");
        if (scanner.hasNextLine())
        {
          name = scanner.nextLine();
        }
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        double wallet = 0.0;
        System.out.print("Wallet: ");
        if (scanner.hasNextDouble())
        {
          wallet = scanner.nextDouble();
          scanner.nextLine(); // consume nl!! Only needed when mixing strings and int/double
        }
        
        try{
          tmuber.registerNewUser(name,address,wallet);
          System.out.printf("User: %-15s Address: %-15s Wallet: %2.2f", name, address, wallet);
        }
        catch ( RuntimeException e){
          System.out.println(e.getMessage());
        }
      }
      // Request a ride
      else if (action.equalsIgnoreCase("REQRIDE")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestRide() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        String accountId = ""; //Initializing a Variable
        System.out.println("AccountID: "); //Taking Input from the user
        if (scanner.hasNextLine()){ //Using scanner check for next line
          accountId = scanner.nextLine(); // Storing the data in the nextline to the variable using scanner
        }
        String FromAddress = "";
        System.out.println("From Address: ");
        if (scanner.hasNextLine()){
          FromAddress = scanner.nextLine();
        }
        String ToAddress = "";
        System.out.println("To Address");
        if (scanner.hasNextLine()){
          ToAddress = scanner.nextLine();
        }
        try{
          tmuber.requestRide(accountId, FromAddress, ToAddress);
          System.out.printf("Ride For:%15s From:%15s To:%15s", tmuber.getUser(accountId).getName(),FromAddress,ToAddress);
        }
        catch (RuntimeException e){
          System.out.println(e.getMessage());
        }
      }
      // Request a food delivery
      else if (action.equalsIgnoreCase("REQDLVY")) 
      {
        // Get the following information from the user (on separate lines)
        // Then use the TMUberSystemManager requestDelivery() method properly to make a ride request
        // "User Account Id: "      (string)
        // "From Address: "         (string)
        // "To Address: "           (string)
        // "Restaurant: "           (string)
        // "Food Order #: "         (string)
        String accountId = "";  //Initializing a Variable
        System.out.println("AccountID: "); //Taking Input from the user
        if (scanner.hasNextLine()){  //Using scanner check for next line
          accountId = scanner.nextLine(); // Storing the data in the nextline to the variable using scanner
        }
        String FromAddress = "";
        System.out.println("From Address: ");
        if (scanner.hasNextLine()){
          FromAddress = scanner.nextLine();
        }
        String ToAddress = "";
        System.out.println("To Address");
        if (scanner.hasNextLine()){
          ToAddress = scanner.nextLine();
        }
        String restaurant =  "";
        System.out.println("Restaurant");
        if (scanner.hasNextLine()){
          restaurant = scanner.nextLine();
        }
        String foodorder = "";
        System.out.println("Food Order");
        if (scanner.hasNextLine()){
          foodorder = scanner.nextLine();
        }
        try{
          tmuber.requestDelivery(accountId, FromAddress, ToAddress, restaurant, foodorder);
          System.out.printf("Ride For:%15s From:%15s To:%15s", tmuber.getUser(accountId).getName(),FromAddress,ToAddress);
        }
        catch (RuntimeException e){
          System.out.println(e.getMessage());
        }
      }
      // Sort users by name
      else if (action.equalsIgnoreCase("SORTBYNAME")) 
      {
        tmuber.sortByUserName();
        
      }
      // Sort users by number of ride they have had
      else if (action.equalsIgnoreCase("SORTBYWALLET")) 
      {
        tmuber.sortByWallet();
      }
      // Sort current service requests (ride or delivery) by distance
      else if (action.equalsIgnoreCase("SORTBYDIST")) 
      {
        tmuber.sortByDistance();
      }
      // Cancel a current service (ride or delivery) request
      else if (action.equalsIgnoreCase("CANCELREQ")) 
      {
        int zone = -1;
        System.out.print("Zone: "); //Asks the User to input the Zone number
        if (scanner.hasNextInt())
        {
          zone = scanner.nextInt(); // reads the zone
          scanner.nextLine(); 
        }
        int request = -1;
        System.out.println("Request #:"); //Asks the User to input the request
        if (scanner.hasNextInt()){
          request = scanner.nextInt(); // Reads the request
          scanner.nextLine();
        }
        try{
          tmuber.cancelServiceRequest(zone , request); //Cancels the request using cancelServiceRequest function
          System.out.println("Service request #" + request + " cancelled"); //Prints this statement if request cancelled
        }  
        catch (RuntimeException e){ //Catches RuntimeExceptions
          System.out.println(e.getMessage()); //Print out the message
        }
      }

      // Drop-off the user or the food delivery to the destination address
      else if (action.equalsIgnoreCase("DROPOFF")) 
      {
        String id = "";
        System.out.print("Driver ID: "); //Asks for the Driver ID 
        if (scanner.hasNext())
        {
          id = scanner.next(); //Reads the ID
          scanner.nextLine(); // consume nl
        }
        try{
          tmuber.dropOff(id); //dropoff function used to dropoff
          System.out.println("Driver " + id + " dropping off");
        }  
        catch (RuntimeException e){
          System.out.println(e.getMessage());
        }
      }
      // Get the Current Total Revenues
      else if (action.equalsIgnoreCase("REVENUES")) 
      {
        System.out.println("Total Revenue: " + tmuber.totalRevenue);
      }

      //PICKUP
      //Picks Up user from a zone
      else if (action.equalsIgnoreCase("PICKUP")){
        System.out.println("Driver ID: "); //Asks for the driver ID

        
        String id = "";
        if (scanner.hasNextLine()){
          id = scanner.nextLine();
          
        }
        int zone = tmuber.getDriverZone(id); //Getting the zone of the driver
        
        try{  
          tmuber.pickup(id); //Picks up using pickup() function
          System.out.println("Driver " + id + " Picking Up in Zone " + zone);
        }
        catch (RuntimeException e){
          System.out.println(e.getMessage());
        }
        
      }
      //DRIVETO
      //Function that allows us to drive a driver to another destination

      else if (action.equalsIgnoreCase("DRIVETO")) {
        System.out.println("Driver ID: ");
        String id = "";
        if (scanner.hasNextLine()) {
            id = scanner.nextLine();
        }
        // Consume newline character
        
    
        System.out.println("Address: "); //Asks for the desired address
        String addr = "";
        if (scanner.hasNextLine()) {
            addr = scanner.nextLine(); //Reads the address
        }
        try{
          tmuber.DriveTo(id, addr); //Uses DriveTo() function
          System.out.println("Driver " + id + " now in zone " + CityMap.getCityZone(addr)); //getCityZonne(add) gets the zone of the new address
        }
        catch (RuntimeException e){
          System.out.println(e.getMessage());
        }
    }
      // Unit Test of Valid City Address 
      else if (action.equalsIgnoreCase("ADDR")) 
      {
        String address = "";
        System.out.print("Address: ");
        if (scanner.hasNextLine())
        {
          address = scanner.nextLine();
        }
        System.out.print(address);
        if (CityMap.validAddress(address))
          System.out.println("\nValid Address"); 
        else
          System.out.println("\nBad Address"); 
      }
      // Unit Test of CityMap Distance Method
      else if (action.equalsIgnoreCase("DIST")) 
      {
        String from = "";
        System.out.print("From: ");
        if (scanner.hasNextLine())
        {
          from = scanner.nextLine();
        }
        String to = "";
        System.out.print("To: ");
        if (scanner.hasNextLine())
        {
          to = scanner.nextLine();
        }
        System.out.print("\nFrom: " + from + " To: " + to);
        System.out.println("\nDistance: " + CityMap.getDistance(from, to) + " City Blocks");
      }
      
      //System.out.print("\n>");
      //LOADUSERS
      //LOADS the list of users from a textfile

      else if (action.equalsIgnoreCase("LOADUSERS")){
        System.out.println("User File:"); //Asks us to input the name of file

        if (scanner.hasNextLine()){
          String file = scanner.nextLine(); //Reads the file name
          try{
            ArrayList<User> userlist = TMUberRegistered.loadPreregisteredUsers(file); //Stores the names in a list using loadPreregisteredUsers(file) function
            tmuber.setUsers(userlist); // Sets the users
            System.out.println("Users loaded."); //Users loaded 
          }
          catch (FileNotFoundException e){
            System.out.println(file + " Not Found");
          }
        }
      }
      
      //LOADDRIVERS
      //Loads the list of drivers from a text file

      else if (action.equalsIgnoreCase("LOADDRIVERS")){
        System.out.println("Driver File:");

        if (scanner.hasNextLine()){
          String file = scanner.nextLine();
          try{
            ArrayList<Driver> driverlist = TMUberRegistered.loadPreregisteredDrivers(file);
            tmuber.setDrivers(driverlist);
            System.out.println("Drivers loaded.");
          }
          catch (FileNotFoundException e){
            System.out.println(file + " Not Found");
          }
        }
      } 
    } 
    scanner.close();
  }
}

