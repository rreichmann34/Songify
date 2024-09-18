import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;

public class BackendDeveloperTests {

  
/*
 * Ensures that readData throws an exception when a bad file type is given
 */
  
@Test
public void case1() {
  
  
  IterableSortedCollection<SongInterface> testing = new ISCPlaceholder<SongInterface>();
  
  Backend test = new Backend(testing);
  
  boolean works = true;
  
  
  try {
    test.readData("wrong");
    works = false;
  } catch (IOException e) {
    
  }
  

  Assertions.assertEquals(true,works);
  
}


/*
 * Tests that the getRange method produces an unempty list (Update to include specific tests
   that ensures all songs are in range once iterator is correctly implemented).
 */

@Test
public void case2() {
  
  IterableSortedCollection<SongInterface> testing = new ISCPlaceholder<SongInterface>();
  
  Backend test = new Backend(testing);
  
  
  try {
    test.readData("songs.csv");
  } catch (IOException e) {
    e.printStackTrace();
  }
  
  List<String> testList = test.getRange(60,200);
  
  
  
  Assertions.assertEquals(true, !testList.isEmpty());
  
}

/*
 * Tests that filterOldSongs method filters the list of songs returned by future calls of getRange() and 
 * fiveMostDanceable() to only include older songs.
 * 
*/
@Test
public void case3() {
  
  
  IterableSortedCollection<SongInterface> testing = new ISCPlaceholder<SongInterface>();
  
  Backend test = new Backend(testing);
  
  
  try {
    test.readData("songs.csv");
  } catch (IOException e) {
    e.printStackTrace();
  }
  
  test.getRange(60, 200);
  
  List<String> testList = test.filterOldSongs(2020);
  
  
  Assertions.assertEquals(true, !testList.isEmpty()); 
}

/*
  Tests that danceability provides a non-empty list when getRange was called
*/
@Test
public void case4() {
  
  IterableSortedCollection<SongInterface> testing = new ISCPlaceholder<SongInterface>();
  
  Backend test = new Backend(testing);
  
  
  try {
    test.readData("songs.csv");
  } catch (IOException e) {
    
  }
  catch (IllegalStateException e) {
    
  }
  
  test.getRange(110, 120);
  
  List<String> danceabilityList = test.fiveMostDanceable();
  
  
  Assertions.assertEquals(true, !danceabilityList.isEmpty());
  
}


/*
Tests the fiveMostDanceable methods throws an error when getRange() hasn't been called
*/

@Test
public void case5() {
  
  IterableSortedCollection<SongInterface> testing = new ISCPlaceholder<SongInterface>();
  
  Backend test = new Backend(testing);
  
  
  boolean throwError = true;
  
  try {
    test.readData("songs.csv");
    test.fiveMostDanceable();
    throwError = false;
  } catch (IllegalStateException e) {
    
  }
  catch (IOException e) {
    
  }
  
  
  
  
  
  Assertions.assertEquals(true, throwError);
  
}


}
