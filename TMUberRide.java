/*
 * 
 * This class simulates an ride service for a simple Uber app
 * 
 * A TMUberRide is-a TMUberService with some extra functionality
 */
public class TMUberRide extends TMUberService
{
  private int numPassengers;
  private boolean requestedXL;
  
  public static final String TYPENAME = "RIDE";
  
  // Constructor to initialize all inherited and new instance variables 
  public TMUberRide(String from, String to, User user, int distance, double cost)
  {
    // Fill in the code - make use of the super method
    super(from, to, user, distance, cost, TYPENAME); //calling constructor of the superclass TMUberService
    //Initializing numPassengers and requestedXL;
    this.numPassengers =0;
    //this.requestedXL = false;

  }
  
  public String getServiceType()
  {
    return TYPENAME;
  }

  public int getNumPassengers()
  {
    return numPassengers;
  }

  public void setNumPassengers(int numPassengers)
  {
    this.numPassengers = numPassengers;
  }

  public boolean isRequestedXL()
  {
    return requestedXL;
  }

  public void setRequestedXL(boolean requestedXL)
  {
    this.requestedXL = requestedXL;
  }
  public int compareTo(TMUberService other) {
    TMUberRide otherRide = (TMUberRide) other;
    return Integer.compare(this.getDistance(), otherRide.getDistance());
  }

}



