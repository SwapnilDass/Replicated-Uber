import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import javax.management.Query;

//import Driver.Status;

/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private Map <String , User> users;
  private ArrayList <Driver> drivers;

  private Queue<TMUberService>[] serviceRequests; 

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  //These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    users   = new TreeMap<String , User>(); //Making a Map for users
    drivers = new ArrayList<Driver>(); //Making a map for drivers

    serviceRequests = new Queue[4]; //Create an array to hold the queues of service Requests
    for (int i=0; i<4 ; i++){ //Iterate over each element
      serviceRequests[i] = new LinkedList<>();  //Initialize each element of the Service Request array with a new Linked list
    }
    
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  String errMsg = null;

  public String getErrorMessage()
  {
    return errMsg;
  }
  
  // Given user account id, find user in list of users
  // Return null if not found

  public void setUsers (ArrayList<User> usersList){ //Method to put Users into the map
    for (User user : usersList){
      users.put(user.getAccountId() , user); //User ID is the key, User is the value
    }
  }
  public void setDrivers (ArrayList<Driver> driverList){
    this.drivers = driverList;
  }

  public User getUser(String accountId) //Returns the User from the MAP
  {
    // Fill in the code

    for (User myuser : users.values()){ //Loops over the values in MAP
      if (myuser.getAccountId().equals(accountId)){
        return (myuser);
      }
    }
    return null;
  }
  
  // Check for duplicate user
  private boolean userExists(User user)
  {
    // Fill in the code
    for (User currentUser : users.values()){
      if (currentUser.equals(user)){
        return true;
      }
    }
    return false;
  }
  
 // Check for duplicate driver
  private boolean driverExists(Driver driver)
  {
    // Fill in the code
    for (Driver currentDriver : drivers){ //Looping through drivers arraylist
      //Returns true if driver exists
      if (currentDriver.equals(driver)){
        return true;
      }
    }
    return false;
  }
  
  // Given a user, check if user ride/delivery request already exists in service requests
  private boolean existingRequest(TMUberService req)
  {
    // Fill in the code
    
    for (Queue<TMUberService> queue : serviceRequests){
      for (TMUberService service : queue){
        if (service.equals(req)){
          return true;
        }
      }
    }
    return false;
  }

  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  //Method to get the Zone of the Driver
  //Returns Zone

  public int getDriverZone (String id){
    int zone = -1;
    for (Driver myDriver : drivers){
      if (myDriver.getId().equals(id)){
        zone = myDriver.getZone();
      }
    }
    return zone;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  private Driver getAvailableDriver()
  {
    // Fill in the code
    for (int i=0 ; i<drivers.size() ; i++){ //Looping through drivers arraylist
      //Returns the available driver
      if (drivers.get(i).getStatus() == Driver.Status.AVAILABLE ){
        return drivers.get(i);
      }
    }
    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers() {
    System.out.println();
    
    // Create a list of users to sort them
    List<User> userList = new ArrayList<>(users.values());

    //This line sorts our userlist based on AccountID
    Collections.sort(userList, (u1, u2) -> Integer.parseInt(u1.getAccountId()) - Integer.parseInt(u2.getAccountId()));

    // Print the sorted list of users
    int index = 0;
    for (User user : userList) {
        index++;
        System.out.printf("%-2s. ", index);
        user.printInfo();
        System.out.println();
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    // Fill in the code
    System.out.println();

    for (int i=0 ; i<drivers.size() ;i++){

      int index = i+1;
      System.err.printf("%-2s", index);
      drivers.get(i).printInfo();
      System.out.println();
      System.out.println();
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    // Fill in the code
    System.out.println();

    for (int zone=0 ; zone<serviceRequests.length; zone++){

      int index = 0;
      System.out.println("ZONE " + zone);
      System.out.println( ("======"));
      System.out.println();

      Queue<TMUberService> queue = serviceRequests[zone];

      if (queue != null){
        for (TMUberService service : queue){
          index ++;
          System.err.printf("%-2s", index);
          service.printInfo();
          System.out.println();
          System.out.println();
        }
      }
    }
  }

  // Add a new user to the system
  public void registerNewUser(String name, String address, double wallet) throws UserExistsException
  {
    // Fill in the code. Before creating a new user, check paramters for validity
    // See the assignment document for list of possible erros that might apply
    // Write the code like (for example):
    // if (address is *not* valid)
    // {
    //    set errMsg string variable to "Invalid Address "
    //    return false
    // }
    // If all parameter checks pass then create and add new user to array list users
    // Make sure you check if this user doesn't already exist!

    if (name == null || name == ""){  //If name is invalid throw exception
      throw new IllegalArgumentException("Invalid User Name");
      
    }
    if (!(CityMap.validAddress(address))){  //If address invalid throw exception
      throw new IllegalArgumentException("Invalid Address");
      
    }
    if (!(wallet >= 0)){  //if wallet balance invalid throw exception
      throw new IllegalArgumentException("Invalid Wallet Balance");
      
    }
    User myUserReg = null;
    ArrayList <User> userList = new ArrayList<>(users.values()); //Initializes userList with the values in the users MAP

    myUserReg = new User(TMUberRegistered.generateUserAccountId(userList), name, address, wallet); //Creating a User object named "myUserReg"
    
    if ((userExists(myUserReg))){ // if the user to be registered does not exist in the users list then it is added to the list of users
      throw new UserExistsException("User Already exists"); //Throws exception if User already exists
      
    }
    users.put(TMUberRegistered.generateUserAccountId(userList), myUserReg); //Add the User into the MAP 
  
  }
  // Add a new driver to the system
  public void registerNewDriver(String name, String carModel, String carLicencePlate, String address) throws DriverExistsException
  {
    // Fill in the code - see the assignment document for error conditions
    // that might apply. See comments above in registerNewUser
    if (name == null || name == ""){
      throw new InvalidUserNameException("Invalid User Name");
      
    }
    if (address == null || address == ""){
      throw new InvalidAddressException("Invalid Driver Address");
      
    }
    if (carLicencePlate == null || carLicencePlate == ""){
      throw new InvalidWalletException("Invalid Car Licence Plate");
      
    }
    if (carModel == null || carModel == ""){
      throw new InvalidCarModelException ("Invalid Car Model");
    }
    Driver myDriverReg = null;
    myDriverReg = new Driver(TMUberRegistered.generateDriverId(drivers), name, carModel, carLicencePlate, address); //Creating a Driver object named "myDriverReg"

    if ((driverExists(myDriverReg))){
      throw new DriverExistsException("Driver Already Exists"); //Throws exception if Driver exists
      
    }
    drivers.add(myDriverReg); //Add the Driver into arrayList drivers
    
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public void requestRide(String accountId, String from, String to) throws RuntimeException {
    // Check for valid parameters
    // Use the account id to find the user object in the list of users
    // Get the distance for this ride
    // Note: distance must be > 1 city block!
    // Find an available driver
    // Create the TMUberRide object
    // Check if existing ride request for this user - only one ride request per user at a time!
    // Change driver status (not done)
    // Add the ride request to the list of requests
    // Increment the number of rides for this user

    if (getUser(accountId) == null) { // Using getUser function to check if the user exists
        throw new UserNotFoundException("User Account not Found"); // throws exception if User not found
    }
    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) { // Checks if from and to are valid address or not
        throw new InvalidAddressException("Address not valid");
    } else {
        User myUser = getUser(accountId); // Getting the user with the given accountID and stored into myUser variable

        int mydistance = CityMap.getDistance(from, to); // Calculates the distance
        if (mydistance > 1) {

            TMUberRide otherRide = new TMUberRide(from, to, myUser, mydistance, getRideCost(mydistance)); // Creating a new object of type TMUberRide and initializing it

            if (existingRequest(otherRide)) { // Checks if the otherRide already exists in the system or not
                throw new ExistingReqException("Ride Already Exists for the User"); // Throws exception if ride already exists for User
            }
            if (otherRide.getUser().getWallet() >= getRideCost(mydistance)) { // Checks if User have enough funds to carry out the service
                int zone = CityMap.getCityZone(from);

                serviceRequests[zone].offer(otherRide); // Adding this ride request to the list serviceRequests
                myUser.addRide(); // Adding the number of rides of the user
            } else {
                throw new InsufficientFundsException("Insufficient Funds"); // throws exception
            }
        } 
        else {
          throw new InsufficientDistException("Insufficient Distance"); // throws exception
        }
    }
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public void requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId) throws RuntimeException
  {
    // See the comments above and use them as a guide
    // For deliveries, an existing delivery has the same user, restaurant and food order id
    // Increment the number of deliveries the user has had
    if (getUser(accountId) == null){
      throw new UserNotFoundException("User Account not Found"); //throws exception if User not found
      
    }
    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)){
      throw new InvalidAddressException("Address not valid");
      
    }
    else{
      User myUser = getUser(accountId);

      int mydistance = CityMap.getDistance(from, to);
      if (mydistance > 1){


        //Creating a new object of type TMUberDelivery and initializing it
        TMUberDelivery otherRide = new TMUberDelivery(from, to, myUser, mydistance, getDeliveryCost(mydistance), restaurant, foodOrderId);
        otherRide.getServiceType();

        if (existingRequest(otherRide)){ //Checks if delivery request already exists or not
          throw new ExistingReqException ("User Already Has Delivery Request at Restaurant " + otherRide.getRestaurant() + " with Food Order ID " + otherRide.getFoodOrderId());//throws exception if 
            
        }
        if (otherRide.getUser().getWallet() >=  getRideCost(mydistance)){ //Checks if User have enough balance to carry out the service
          int zone = CityMap.getCityZone(from); //gets the zone of the address

          serviceRequests[zone].offer(otherRide);// Adds the ride into the queue
          myUser.addDelivery();// Adding the number of deliveries of that user
            
        }
        else{
          throw new NoDriverException("No Drivers Available");
        }
      }
      else{   
        throw new InsufficientDistException("Insufficient Distance");
      }
    }
  }



  

  // Cancel an existing service request. 
  // parameter int request is the index in the serviceRequests array list


  public void cancelServiceRequest (int zone , int request) throws RuntimeException{

    if (zone<0 || zone >= serviceRequests.length){
      throw new InvalidZoneException ("Invalid Zone");
    }
    if (request < 1 ){
      throw new InvalidRequestException ("Invalid Request");
    }
    if (request > serviceRequests[zone].size()){
      throw new EmptyZoneException ("No Requests for that zone"); //throws exception if there are no requests for that zone
    }
    Queue <TMUberService> myQueue = serviceRequests[zone]; //Making a Queue  and initializing it with the Queue stored at index 'zone' in the serviceRequests array. 
    TMUberService removedService = null;

    for (int i=0 ; i<request ; i++){ //Looping over the number of requests
      removedService = myQueue.poll(); //Removing each request from the queue

      if (removedService == null){
        throw new EmptyQueueException ("Queue is empty"); //Throws exception if queue is empty
      }
      if (i != request-1){ //Checks if the user wanted to remove that particular request
        myQueue.offer(removedService);//Adds the request back into Queue if User did not want to remove that request
      }
    }

    if (removedService.getServiceType().equalsIgnoreCase("RIDE")){
      removedService.getUser().minusRide(); //Decreamenting the number of rides
    }
    else{
      removedService.getUser().minusDelivery();
    }

  }




  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public void dropOff(String driverID) throws RuntimeException
  {
    // See above method for guidance
    // Get the cost for the service and add to total revenues
    // Pay the driver
    // Deduct driver fee from total revenues
    // Change driver status
    // Deduct cost of service from user
    boolean driverFound = false;

    for (Driver currDriver : drivers) { //Loops over the drivers list
      if (currDriver.getId().equals(driverID)){
        driverFound = true; //Returns true if driver found
        if (currDriver.getStatus().equals(Driver.Status.DRIVING)){ //Checks the status of the driver

          TMUberService myservice = null;
          String serviceType = currDriver.getType();

          //if (serviceType.equals("RIDE")){
          myservice = currDriver.getService();
          
          /** 
          else if (serviceType.equals("DELIVERY")){
            myservice = currDriver.getService();
          }
          **/

          double mycost = myservice.getCost(); //getting the cost of the service
          totalRevenue += mycost; //Adding the cost into the revenue

          currDriver.pay(currDriver.getWallet() + (PAYRATE*mycost)); //Paying the driver

          User myUser = myservice.getUser();
          myUser.payForService(mycost); //Making the User pay for the service
          totalRevenue -= PAYRATE * mycost;
          currDriver.setStatus(Driver.Status.AVAILABLE); //Changing the status of the Driver
          
          //Setting up the new Zone for the Driver after dropoff
          currDriver.setAddress(myservice.getTo());
          int newZone = CityMap.getCityZone(myservice.getTo());
          currDriver.setZone(newZone);
        }
        else{
          throw new DriverNotDrivingException ("Driver is not Driving"); //throws exception if driver not driving
        }
      }
    }
    //throws exception if driver not founbd
    if (!driverFound){
      throw new NoDriverException("Driver not found"); 
    }

  }
  //This method makes the Driver drive to a different address and sets up the zone
  public void DriveTo (String driverID , String Address){

    Boolean driverFound = false;
    Boolean addressvalid = false;
    // Creating a DriveTo method

    for (Driver myDriver : drivers){
      if (myDriver.getId().equals(driverID)){
        driverFound = true;

        if (myDriver.getStatus().equals(Driver.Status.AVAILABLE)){
          if (CityMap.validAddress(Address)){
            addressvalid = true;

            myDriver.setAddress(Address);// sets the address
            int newZone = CityMap.getCityZone(Address); //gets the new zone

            myDriver.setZone(newZone); //sets the new zone
          }
          if (!addressvalid){
            throw new InvalidAddressException("Invalid Address");
          }
        }
        else{
          throw new NoDriverAvailableException ("Driver is not Available");
        }
      }
    }
    if (!driverFound){
      throw new DriverExistsException("Driver not Found");
    }
  }

  // This pickup method allows us to pick up a User requesting a ride
  public void pickup (String driverID) throws RuntimeException{

    Boolean pickeup = false;
    for (Driver mydriver : drivers){
      if (mydriver.getId().equals(driverID)){

        Driver currdriver = mydriver;
        int zone = CityMap.getCityZone(currdriver.getAddress());

        Queue <TMUberService> serviceQueue = serviceRequests[zone]; //Making a Queue  and initializing it with the Queue stored at index 'zone' in the serviceRequests array.

        if (serviceQueue == null || serviceQueue.isEmpty()){
          throw new NoServiceReqException("No Service Requests for zone " + zone); //Throws exception if there are no requests for the zone

        }
        TMUberService service = serviceQueue.poll(); //Removes the request from the Queue
        currdriver.setService(service); //Sets the service

        currdriver.setStatus(Driver.Status.DRIVING); //Changes the status of the driver
        currdriver.setAddress(service.getFrom()); //sets the new address for the driver

        pickeup = true;
      } 
    }
    //executes this code if User was not PickedUp by the driver
    if (!pickeup){
      throw new NoDriverException("Driver not Found"); 
    }
     
  }

  // Sort users by name
  // Then list all users
  public void sortByUserName()
  {
    Collection <User> userValues = users.values();// Gets the User objects from the users map
    ArrayList <User> userList = new ArrayList<>(userValues);//Creats an arrayList from the values obtained from map
    Collections.sort(userList , new NameComparator());//Sorting the ArrayList using comparator

    for (User myuser : userList){
      myuser.printInfo();
      System.out.println();
    }

  }

  // Helper class for method sortByUserName
  private class NameComparator implements Comparator<User>
  {
    //Compares two User objects based on their names
    public int compare (User u1 , User u2){
      return u1.getName().compareTo(u2.getName());
    }
  }

  // Sort users by number amount in wallet
  // Then list all users
  public void sortByWallet()
  {
    Collection <User> userValues = users.values();
    ArrayList <User> userlList = new ArrayList<>(userValues);
    Collections.sort(userlList , new UserWalletComparator() );

    for (User myuser : userlList){
      myuser.printInfo();
      System.out.println();
    }
  }
  // Helper class for use by sortByWallet
  private class UserWalletComparator implements Comparator<User>
  {
    //Compares two User objects based on their wallet balance
    public int compare (User u1 , User u2){
      return Double.compare(u1.getWallet(), u2.getWallet());
    }
  }

  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  public void sortByDistance() {
    for (Queue<TMUberService> serviceQueue : serviceRequests) {//Iterate over each queue of serviceRequests

      List<TMUberService> serviceList = new ArrayList<>(serviceQueue); //Create a list from the current service Queue
      Collections.sort(serviceList, new DistanceComparator());//Sorts the service using DiistanceComparator
      serviceQueue.clear();
      serviceQueue.addAll(serviceList);//Clears and adds the sorted serviced back
        
      for (TMUberService myser : serviceQueue) {
        myser.printInfo();
        System.out.println();
      }
    }
  }
  
  //Creating helper class for use by sortByDistance
  private class DistanceComparator implements Comparator<TMUberService>{

    //Compares two TMUberService objects based on their distances
    public int compare (TMUberService s1 , TMUberService s2){
      return Integer.compare(s1.getDistance(), s2.getDistance());
    }
  }

}

//Defining the exceptions

class UserExistsException extends RuntimeException{
  public UserExistsException (String message){
      super(message);
  }
}

class DriverExistsException extends RuntimeException{
  public DriverExistsException (String message){
    super(message);
  }
}
class UserNotFoundException extends RuntimeException{
  public UserNotFoundException (String message){
    super(message);
  }
}
class ExistingReqException extends RuntimeException{
  public ExistingReqException (String message){
    super(message);
  }
}
class InsufficientFundsException extends RuntimeException{
  public InsufficientFundsException (String message){
    super(message);
  }
}
class NoDriverException extends RuntimeException{
  public NoDriverException (String message){
    super(message);
  }
}
class InsufficientDistException extends RuntimeException{
  public InsufficientDistException (String message){
    super(message);
  }
}
class NoServiceReqException extends RuntimeException{
  public NoServiceReqException (String message){
    super(message);
  }
}
class NoRequestException extends RuntimeException{
  public NoRequestException (String message){
    super (message);
  }
}
class InvalidUserNameException extends RuntimeException{
  public InvalidUserNameException (String message){
    super(message);
  }
}
class InvalidAddressException extends RuntimeException{
  public InvalidAddressException (String message){
    super(message);
  }
}
class InvalidWalletException extends RuntimeException{
  public InvalidWalletException (String message){
    super(message);
  }
}
class InvalidZoneException extends RuntimeException{
  public InvalidZoneException (String message){
    super(message);
  }
}
class InvalidRequestException extends RuntimeException{
  public InvalidRequestException (String message){
    super(message);
  }
}
class EmptyQueueException extends RuntimeException{
  public EmptyQueueException (String message){
    super(message);
  }
}
class InvalidCarModelException extends RuntimeException{
  public InvalidCarModelException (String message){
    super(message);
  }
}
class DriverNotDrivingException extends RuntimeException{
  public DriverNotDrivingException (String message){
    super(message);
  }
}
class NoDriverAvailableException extends RuntimeException{
  public NoDriverAvailableException (String message){
    super(message);
  }
}
class EmptyZoneException extends RuntimeException{
  public EmptyZoneException (String message){
    super(message);
  }
}

