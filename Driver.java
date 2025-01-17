/*
 * 
 * This class simulates a car driver in a simple uber app 
 * 
 * Everything has been done for you except the equals() method
 */
public class Driver
{
  private String id;
  private String name;
  private String carModel;
  private String licensePlate;
  private double wallet;
  private String type;
  private TMUberService service;
  private String address;
  private int zone;
  
  public static enum Status {AVAILABLE, DRIVING};
  private Status status;
    
  
  public Driver(String id, String name, String carModel, String licensePlate, String address)
  {
    this.id = id;
    this.name = name;
    this.carModel = carModel;
    this.licensePlate = licensePlate;
    this.status = Status.AVAILABLE;
    this.wallet = 0;
    this.type = "";

    //Creating instance variables service, address, zone
    this.service = null;
    this.address = address;
    this.zone = CityMap.getCityZone(address);
    
  }
  // Print Information about a driver
  public void printInfo()
  {
    System.out.printf("Id: %-3s Name: %-15s Car Model: %-15s License Plate: %-10s Wallet: %2.2f ", 
                      id, name, carModel, licensePlate, wallet);
    System.out.printf("Status: %-15s Address: %-20s Zone: %1d", status.toString(),address,zone);
  }
  // Getters and Setters
  public void setService (TMUberService service){
    this.service = service;
  }
  public TMUberService getService (){
    return this.service;
  }

  public String getType()
  {
    return type;
  }
  public void setType(String type)
  {
    this.type = type;
  }
  public String getId()
  {
    return id;
  }
  public void setId(String id)
  {
    this.id = id;
  }
  public String getName()
  {
    return name;
  }
  public void setName(String name)
  {
    this.name = name;
  }
  public void setAddress (String address){ //Sets the address and zone for the driver
    this.address = address;
    this.zone = CityMap.getCityZone(address);
  } 
  public String getAddress (){ //Return address
    return address; 
  }
  public int getZone (){ //Returns zone
    return zone;
  }
  public void setZone (int zone){ //Sets the zone
    this.zone = zone;
  }
  public String getCarModel()
  {
    return carModel;
  }
  public void setCarModel(String carModel)
  {
    this.carModel = carModel;
  }
  public String getLicensePlate()
  {
    return licensePlate;
  }
  public void setLicensePlate(String licensePlate)
  {
    this.licensePlate = licensePlate;
  }
  public Status getStatus()
  {
    return status;
  }
  public void setStatus(Status status)
  {
    this.status = status;
  }
  public double getWallet()
  {
    return wallet;
  }
  public void setWallet(double wallet)
  {
    this.wallet = wallet;
  }
  /*
   * Two drivers are equal if they have the same name and license plates.
   * This method is overriding the inherited method in superclass Object
   * 
   * Fill in the code 
   */
  public boolean equals(Object other)
  {
    Driver otherDriver = (Driver) other; //Casting other variable to type Driver
    //Checks whether the name and Liscense Plate of Driver is equal to the name and License Plate of other Driver 
    if (this.name.equals(otherDriver.getName()) && this.licensePlate.equals(otherDriver.getLicensePlate())){
      return true;
    }

    return false;
  }
  
  // A driver earns a fee for every ride or delivery
  public void pay(double fee)
  {
    wallet += fee;
  }


}
