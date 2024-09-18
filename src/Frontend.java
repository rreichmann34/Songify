// == CS400 Spring 2024 File Header Information ==
// Name: Remington Reichmann
// Email: rreichmann@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: NONE

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface{
  
  private String min = "min";
  private String max = "max";
  private String year = "none";
  private Scanner in;
  private BackendInterface backend;
  
  /**
   * Constructor for a basic Frontend. It takes in a scanner and references the Backend. Most of the
   * methods in this class depend on retrieving information from the backend. This class's main
   * function is to handle user input.
   * 
   * @param in a scanner used for user input
   * @param backend the class that does most of the actual information getting for this class
   */
  public Frontend(Scanner in, BackendInterface backend) {
    this.in = in;
    this.backend = backend;
  }
  
  /**
   * Runs this program. If the user enters "Q", then the program will quit and stop running. There
   * are other methods that are referenced when the user inputs a certain command. This method also
   * handles any inputs that don't work.
   */
  public void runCommandLoop() {
    String input = null;
    while(input == null || !input.equals("Q")) { // This loop runs as long as the user does not 
      try {                                      // enter "Q"   
        displayMainMenu();
        input = in.nextLine();
        
        if(input.equals("R")) {
          readFile();
        }
        else if(input.equals("G")) {
          getValues();
        }
        else if(input.equals("F")) {
          setFilter();
        }
        else if(input.equals("D")) {
          topFive();
        }
        else if(!input.equals("Q")) {
          throw new Exception("Make sure you capitalize the letter of your command and only enter a single letter");
        } 
      } catch(Exception e) { // Makes sure that the user does not enter an invalid command
        System.out.println(e.getMessage());
      }
    }
  }
  
  /**
   * Displays the main menu and prints it to the console.
   */
  public void displayMainMenu() {
    String menu = """
        
        ~~~ Command Menu ~~~
            [R]ead Data
            [G]et Songs by Speed BPM [min - max]
            [F]ilter Old Songs (by Max Year: none)
            [D]isplay Five Most Danceable
            [Q]uit
        Choose command:""";
    menu=menu.replace("min",min).replace("max",max).replace("none",year); // Replace the values in 
    System.out.print(menu + " ");                                         // menu with the proper  
  }                                                                       // values  
  
  /**
   * "R" Case: The user is prompted to enter a file name. This method then calls the backend method
   * to load in a file. 
   * 
   * @throws IOException if the file cannot be found
   */
  public void readFile() {
    System.out.println("Enter file name: ");
    String file = in.nextLine();
    
    try {
      backend.readData(file); 
    } catch(IOException e) { // Catches an error and exits the method if an invalid file path is entered
      System.out.println(e.getMessage());
    }
  }
  
  /**
   * Gets songs by calling the backend version of this method. The parameters for the backend method
   * are recieved by first gathering user input. The method will continue to try to get user input
   * until they enter a valid bpm.
   */
  public void getValues() {
    System.out.println("Enter min speed BMP: ");
    int x = 0;
    boolean valid1 = false;
    // This loop continues until the user enters a valid min bpm
    while(!valid1) {  
      try {
        x = Integer.parseInt(in.nextLine());
        valid1 = true;
        } catch(Exception e) {
          System.out.println("Error: The min speed BPM must be an integer.");
        }
    }
    System.out.println("Enter max speed BPM: ");
    int y = 0;
    boolean valid2= false;
    // This loop continues until the user enters a valid min bpm
    while(!valid2) {
      try {
        y = Integer.parseInt(in.nextLine());
        valid2 = true;
        } catch(Exception e) {
          System.out.println("Error: The max speed BPM must be an integer.");
        }
    }
    
    // Set the string variables to the valid bpms
    min = x + "";
    max = y + "";
    
    List<String> values = backend.getRange(Integer.parseInt(min), Integer.parseInt(max));
    
    // Print out whatever the backend returns
    System.out.println(values.size() + " songs found between " + min + " - " + max + ": ");
    for(int i = 0; i < values.size(); i++) {
      System.out.println(values.get(i));
    }
  }
  
  /**
   * Gets songs by calling the backend version of this method. The parameter for the backend method 
   * is recieved by first gathering user input. The method will continue to try to get user input
   * until something valid is entered.
   */
  public void setFilter() {
    System.out.println("Enter max year: ");
    int y = 0;
    boolean valid = false;
    // This loop continues until a valid year is entered
    while(!valid) {
      try {
        y = Integer.parseInt(in.nextLine());
        valid = true;
      } catch(Exception e) {
        System.out.println("Error: The year must be an integer.");
        valid = false;
      }
    }
    
    // Set the year to the valid user input
    year = y + "";
    
    List<String> values = backend.filterOldSongs(Integer.parseInt(year));
    
    // Print out whatever the backend returns
    System.out.println(values.size() + " songs found between " + min + " - " + max + " from before " + year + ": ");
    for(int i = 0; i < values.size(); i++) {
      System.out.println(values.get(i));
    }
  }
  
  /**
   * Calls the backend version of this method to get and then print out the top 5 most danceable
   * songs.
   */
  public void topFive() {
    List<String> values = backend.fiveMostDanceable();
    
    // Print out whatever the backend returns
    for(int i = 0; i < values.size(); i++) {
      System.out.print(5 - i + ")");
      System.out.println(values.get(i));
    }
  }
}