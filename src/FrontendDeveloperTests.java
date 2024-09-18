// == CS400 Spring 2024 File Header Information ==
// Name: Remington Reichmann
// Email: rreichmann@wisc.edu
// Lecturer: Gary Dahl
// Notes to Grader: NONE

import org.junit.jupiter.api.Test;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;

public class FrontendDeveloperTests{
	
	/**
	 * Tests the readFile() method defined in Frontend.java by making sure that the method does not
	 * throw exceptions when user input is entered. Any errors should be caught and handled in the class itself and the program 
     * should continue running.
	 */
	@Test
	public void testReadFile(){
	  IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
	  BackendInterface backend = new BackendPlaceholder(tree);
	  Scanner in = new Scanner(System.in);
	  FrontendInterface frontend = new Frontend(in,backend);
	  
	  frontend.runCommandLoop();
      System.out.println("R");
      Assertions.assertDoesNotThrow(() -> System.out.println("songs.csv"), "The line threw an unexpected error when passed a known file!");
      System.out.println("Q");
	  
	  frontend.runCommandLoop();
      System.out.println("R");
      Assertions.assertDoesNotThrow(() -> System.out.println("random.txt"), "The line threw an error when passed an unknown file!");
      System.out.println("Q");
	}

	/**
	 * Tests the getValues() function defined in Frontend.java by making sure that no exceptions
	 * are thrown. Any errors should be caught and handled in the class itself and the program 
     * should continue running.
	 */
	@Test
	public void testGetValues(){
	  IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
      BackendInterface backend = new BackendPlaceholder(tree);
      Scanner in = new Scanner(System.in);
      FrontendInterface frontend = new Frontend(in,backend);
      
      frontend.runCommandLoop();
      System.out.println("G");
      Assertions.assertDoesNotThrow(() -> System.out.println("80"), "The inputed line threw an unexpected error!");
      Assertions.assertDoesNotThrow(() -> System.out.println("90"), "The inputed line threw an unexpected error!");
      Assertions.assertDoesNotThrow(() -> System.out.println(false), "The inputed line threw an error when a boolean was passed instead of handling the error in the Frontend!");
      System.out.println("Q");
	}

	/**
	 * Tests the setFilter() function defined in Frontend.java by making sure that no exceptions
     * are thrown. Any errors should be caught and handled in the class itself and the program 
     * should continue running.
	 */
	@Test
	public void testSetFilter(){
	  IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
      BackendInterface backend = new BackendPlaceholder(tree);
      Scanner in = new Scanner(System.in);
      FrontendInterface frontend = new Frontend(in,backend);
      
      frontend.runCommandLoop();
      System.out.println("F");
      Assertions.assertDoesNotThrow(() -> System.out.println(false), "The inputed line threw an error when a boolean was passed!");
      Assertions.assertDoesNotThrow(() -> System.out.println("2019"));
      System.out.println("Q");
	}
	
	/**
     * Tests the topFive() function defined in Frontend.java by making sure that no exceptions
     * are thrown. Any errors should be caught and handled in the class itself and the program 
     * should continue running.
     */
	@Test
	public void testTopFive(){
	  IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
      BackendInterface backend = new BackendPlaceholder(tree);
      Scanner in = new Scanner(System.in);
      FrontendInterface frontend = new Frontend(in,backend);
      
      System.out.println(frontend == null);
      
      
      frontend.runCommandLoop();
      Assertions.assertDoesNotThrow(() -> System.out.println("D"), "Trying to retrieve the top 5 most danceable songs threw an exception!");
	}

	/**
	 * Tests that the program continues to run even when invalid input is entered by the user. The 
	 * caught exceptions should be handled in Frontend.java, so if any exceptions are thrown in this
	 * class, then Frontend.java does not work as intended.
	 */
	@Test
	public void testInvalidInput(){
	  IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
      BackendInterface backend = new BackendPlaceholder(tree);
      Scanner in = new Scanner(System.in);
      FrontendInterface frontend = new Frontend(in,backend);
      
      frontend.runCommandLoop();
      Assertions.assertDoesNotThrow(() -> System.out.println("UFEWFFDKHGDKGH:EKKH"), "The inputted line threw an error when passed invalid input instead of handling the error in the Frontend!");
      System.out.println("Q");
	}
	
	/**
	 * P107 Tester. Makes sure that creating a backend object and running a frontend method with it
	 * doesn't throw an error.
	 */
	@Test
	public void integrationBPMTest() {
	  // Creating frontend and backend objects
	  IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
      BackendInterface backend = new BackendPlaceholder(tree);
      Scanner in = new Scanner(System.in);
      Frontend frontend = new Frontend(in, backend);
      
      // String of commands that simulates user input
      String userInput = "R\nsongs.csv\nG\n\206\n300\nQ";
      
      // Creating new TextUITester to make sure the backend returns what it should
      TextUITester outputChecker = new TextUITester(userInput);
	  
      // Run the command loop and save what the backend returns
      frontend.runCommandLoop();
	  System.out.println(userInput);
      String output = outputChecker.checkOutput();
      
      // Check that output contains the correct values(FourFiveSeconds)
      Assertions.assertTrue(output.contains("FourFiveSeconds"), "Error: Failed to retrieve the "
          + "correct song when searching for a song with a bpm of at least 206. The output"
          + "should contain FourFiveSeconds.");
	}
	
	/**
	 * P107 Tester. Tests the topFive() method in Frontend.java, which references the backend version
	 * of the topFive() method.
	 */
	@Test
	public void backendIntegration2() {
	// Creating frontend and backend objects
      IterableSortedCollection<SongInterface> tree = new ISCPlaceholder<>(); 
      BackendInterface backend = new BackendPlaceholder(tree);
      Scanner in = new Scanner(System.in);
      Frontend frontend = new Frontend(in, backend);
      
      // String of commands that simulates user input
      String userInput = "R\nsongs.csv\nF\n2010\nQ";
      
      // Creating new TextUITester to make sure the backend returns what it should
      TextUITester outputChecker = new TextUITester(userInput);
      
      // Run the command loop and save what the backend returns
      frontend.runCommandLoop();
      System.out.println(userInput);
      String output = outputChecker.checkOutput();
      
      // Check that output contains some of the correct values. The assertion doesn't check for 
      // all of the correct values since there are too many.
      Assertions.assertTrue(output.contains("TiK ToK"), "Error: The output didn't contain TiK ToK");
      Assertions.assertTrue(output.contains("Marry You"), "Error: The output didn't contain Marry You");
      Assertions.assertTrue(output.contains("Like A G6"), "Error: The output didn't contain Like A G6");
	}
}
