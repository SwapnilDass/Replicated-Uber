import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class TMUberRegistered
{
    // These variables are used to generate user account and driver ids
    private static int firstUserAccountID = 900;
    private static int firstDriverId = 700;

    // Generate a new user account id
    public static String generateUserAccountId(ArrayList<User> current) //arraylists declared in TMUberSysMng.
    {
        return "" + firstUserAccountID + current.size();
    }

    // Generate a new driver id
    public static String generateDriverId(ArrayList<Driver> current)
    {
        return "" + firstDriverId + current.size();
    }

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    // The test scripts and test outputs included with the skeleton code use these
    // users and drivers below. You may want to work with these to test your code (i.e. check your output with the
    // sample output provided). 
    public static ArrayList<Driver> loadPreregisteredDrivers (String filename) throws FileNotFoundException{

        ArrayList <Driver> driverlist  = new ArrayList<>(); //Making a new ArrayList to store Drivers
        
        FileReader file = new FileReader(filename); //Reading the fil

        Scanner scanner = new Scanner (file); //Using a scanner to read the file data
        while (scanner.hasNextLine()) {

            String name = scanner.nextLine();
            String carModel = scanner.nextLine();
            String licensePlate = scanner.nextLine();
            String address = scanner.nextLine();
            String id = generateDriverId(driverlist);

            Driver mydriver = new Driver(id, name, carModel, licensePlate, address);
            driverlist.add(mydriver); //Adding the Driver into the arrayList
        }
        scanner.close();
        return driverlist;
    }
    

    // Database of Preregistered users
    // In Assignment 2 these will be loaded from a file
    public static ArrayList<User> loadPreregisteredUsers(String filename) throws FileNotFoundException
    {
        ArrayList<User> userlist = new ArrayList<>(); //Making a new ArrayList to store Users
        FileReader file = new FileReader(filename);

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            
            String name = scanner.nextLine();
            String address = scanner.nextLine();
            int wallet = Integer.parseInt(scanner.nextLine());
            String id = generateUserAccountId(userlist);

            User myuser = new User(id, name, address, wallet);
            userlist.add(myuser); //Adding the User into the list
        }
        scanner.close();
        return userlist;
    }
}
