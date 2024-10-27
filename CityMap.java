import java.util.Arrays;
import java.util.Scanner;

// The city consists of a grid of 9 X 9 City Blocks

// Streets are east-west (1st street to 9th street)
// Avenues are north-south (1st avenue to 9th avenue)

// Example 1 of Interpreting an address:  "34 4th Street"
// A valid address *always* has 3 parts.
// Part 1: Street/Avenue residence numbers are always 2 digits (e.g. 34).
// Part 2: Must be 'n'th or 1st or 2nd or 3rd (e.g. where n => 1...9)
// Part 3: Must be "Street" or "Avenue" (case insensitive)

// Use the first digit of the residence number (e.g. 3 of the number 34) to determine the avenue.
// For distance calculation you need to identify the the specific city block - in this example 
// it is city block (3, 4) (3rd avenue and 4th street)

// Example 2 of Interpreting an address:  "51 7th Avenue"
// Use the first digit of the residence number (i.e. 5 of the number 51) to determine street.
// For distance calculation you need to identify the the specific city block - 
// in this example it is city block (7, 5) (7th avenue and 5th street)
//
// Distance in city blocks between (3, 4) and (7, 5) is then == 5 city blocks
// i.e. (7 - 3) + (5 - 4) 

public class CityMap
{
  // Checks for string consisting of all digits
  // An easier solution would use String method matches()
  private static boolean allDigits(String s)
  {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;
    return  true;
  }

  // Get all parts of address string
  // An easier solution would use String method split()
  // Other solutions are possible - you may replace this code if you wish
  private static String[] getParts(String address)
  {
    String parts[] = new String[3];
    
    if (address == null || address.length() == 0)
    {
      parts = new String[0];
      return parts;
    }
    int numParts = 0;
    Scanner sc = new Scanner(address);
    
    while (sc.hasNext())
    {
      if (numParts >= 3)
        parts = Arrays.copyOf(parts, parts.length+1);

      parts[numParts] = sc.next();
      numParts++;
    }
    if (numParts == 1)
      parts = Arrays.copyOf(parts, 1);
    else if (numParts == 2)
      parts = Arrays.copyOf(parts, 2);
    return parts;
  }

  // Checks for a valid address
  public static boolean validAddress(String address)
  {
    // Fill in the code
    // Make use of the helper methods above if you wish
    // There are quite a few error conditions to check for 
    // e.g. number of parts != 3

    String[] parts = getParts(address); //Calling getParts function and assigning the parts of address into the array
    if (parts.length != 3) {
        return false;
    }
    String firstStr = parts[0]; //Storing parts of the address into each string variable
    String second = parts[1];
    String third = parts[2];

    if (firstStr.length() != 2 || !allDigits(firstStr)) { //Checks if the the first part is of length 2 and are all digits
        return false;
    }
    if (!allDigits(second.substring(0, 1))){ //second part of the address should start with a number.
      return false;
    }
    int firstInt = Integer.parseInt(firstStr.substring(0)); //Converts the first part into integer and stored in a variable
    if (firstInt < 10 || firstInt > 99) {
        return false;
    }
    if (second.length() != 3){ //Checks if the second part of address is of length 3
      return false;
    }
    int secondInt = Integer.parseInt(second.substring(0, 1)); //Converts a part of string to integer to get the first digit
    //Checks if the second part of address is valid or not
    if ((secondInt < 1 || secondInt > 9) && ((second.substring(1) != "th") || (second.substring(1) !="rd" ) || (second.substring(1) !="st" ) )) {
        return false;
    }
    //checks if the third part of address is valid. Should contain "Street" or "Avenue"
    if ((!third.equalsIgnoreCase("street")) && (!third.equalsIgnoreCase("avenue"))) {
        return false;
    }
    return true; //Returns true if the address is valid

  
  }

  // Computes the city block coordinates from an address string
  // returns an int array of size 2. e.g. [3, 4] 
  // where 3 is the avenue and 4 the street
  // See comments at the top for a more detailed explanation

  public static int[] getCityBlock(String address)
  {
    String[] parts = getParts(address); //Using the function getParts and storing the parts into an array
    int[] block = {-1, -1}; //Initializing an array block
  
    if (parts[2].equalsIgnoreCase("avenue")) { //check if the third part of address in "avenue"  
      //Parts are  converted to integer and stored into block indexes accordingly
        block[1] = Integer.parseInt(parts[0].substring(0,1)); 
        block[0] = Integer.parseInt(parts[1].substring(0,1)); 
        
    } else if (parts[2].equalsIgnoreCase("street")) {  //Codes below are executed if the third part of the address is "street"
        block[1] = Integer.parseInt(parts[1].substring(0,1)); 
        block[0] = Integer.parseInt(parts[0].substring(0,1)); 
    }
    return block; //returns the array block
  }
  
  // Calculates the distance in city blocks between the 'from' address and 'to' address
  // Hint: be careful not to generate negative distances
  
  // This skeleton version generates a random distance
  // If you do not want to attempt this method, you may use this default code
  public static int getDistance(String from, String to)
  {
    // Fill in the code or use this default code below. If you use
    // the default code then you are not eligible for any marks for this part
    
    // Math.random() generates random number from 0.0 to 0.999
    // Hence, Math.random()*17 will be from 0.0 to 16.999
    // cast the double to whole number

    if (validAddress(from) && validAddress(to)) { //Checks if from and to adresses are valid address or not

      int[] distArr1 = getCityBlock(from); //Storing parts of the address into an array
      int[] distArr2 = getCityBlock(to);
      return Math.abs(distArr2[0] - distArr1[0]) + Math.abs(distArr2[1] - distArr1[1]); //Calculating the distance
    }
    return -1; // Invalid addresses
  }

  public static int getCityZone (String address){ //Method that computes zone

    int[] cityBlock = getCityBlock(address);
    
    // if both the avenue (cityBlock[0]) and the street (cityBlock[1]) fall within the range 1-5 for avenues and 6-9 for streets
    //return 0
    if ((cityBlock[0] >= 1 && cityBlock[0] <= 5) && (cityBlock[1] >= 6 && cityBlock[1] <= 9))  { //If 
      return 0;
    }
    else if ((cityBlock[0] >= 6 && cityBlock[0] <= 9) && (cityBlock[1] >= 6 && cityBlock[1] <= 9)){
      return 1;
    }
    else if ((cityBlock[0] >= 6 && cityBlock[0] <= 9) && (cityBlock[1] >= 1 && cityBlock[1] <= 5)){
      return 2;
    }
    else if ((cityBlock[0] >= 1 && cityBlock[0] <= 5) && (cityBlock[0] >= 1 && cityBlock[0] <= 5)){
      return 3;
    }
    return -1; //else return -1
  }

}
