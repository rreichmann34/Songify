import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


/**
 * BackendPlaceholder - CS400 Project 1: iSongify
 *
 * This class doesn't really work.  The methods are hardcoded to output values
 * as placeholders throughout development.  It demonstrates the architecture
 * of the Backend class that will be implemented in a later week.
 */
public class Backend implements BackendInterface {
  
  int maxYear;
  int getRangeHigh;
  int getRangeLow;
  List<SongInterface> getRangeSongs = new ArrayList<SongInterface>();
  
  
  IterableSortedCollection<SongInterface> tree;
  
  
public Backend(IterableSortedCollection<SongInterface> tree) {
    
  
  this.tree = tree;
  
  
  
  }
  /**
   * 
   * Loads data from the .csv file referenced by filename.
   * @param filename is the name of the csv file to load data from
   * @throws IOException when there is trouble finding/reading file
   */
  @Override
  public void readData(String filename) throws IOException {
 
    File file = new File(filename); 
    Scanner scnr = new Scanner(file, "utf-8");
    
    
    //Get passed the first row which is just headings
    if(scnr.hasNext()) {
    scnr.nextLine();
    }
    int i = 0;
    while(scnr.hasNext()) {
      Song song = new Song(scnr.nextLine());
      System.out.println(i);
      i++;
      tree.insert(song);
    }
    scnr.close();
    
  }

  /**
   * Retrieves a list of song titles for songs that have a Speed (BPM)
   * within the specified range (sorted by BPM in ascending order).  If 
   * a maxYear filter has been set using filterOldSongs(), then only songs
   * on Billboard durring or before that maxYear should be included in the 
   * list that is returned by this method.
   *
   * Note that either this bpm range, or the resulting unfiltered list
   * of songs should be saved for later use by the other methods defined in 
   * this class.
   *
   * @param low is the minimum Speed (BPM) of songs in the returned list
   * @param hight is the maximum Speed (BPM) of songs in the returned list
   * @return List of titles for all songs in specified range 
   */
  @Override
  public List<String> getRange(int low, int high) {
    // TODO Auto-generated method stub
    
    this.getRangeLow = low;
    this.getRangeHigh = high;
    
    getRangeSongs.clear();
    
    Song highSong = new Song("I'm the One (feat. Justin Bieber, Quavo, Chance the Rapper & Lil Wayne)"
        + ",Coldplay,permanent wave,2013,"+ high +",45,28,-8,11,11,236,63,3,55");
    Song lowSong = new Song("I'm the One (feat. Justin Bieber, Quavo, Chance the Rapper & Lil Wayne)"
        + ",Coldplay,permanent wave,2013,"+ low +",45,28,-8,11,11,236,63,3,55");
    
    
    tree.setIterationStartPoint(highSong);
    
    Iterator<SongInterface> testing2 = tree.iterator();
    
    
    
   
    List<String> titles = new ArrayList<String>();
    while(testing2.hasNext()) {
      
      Song nextSong = (Song)testing2.next();
      
      if(nextSong.compareTo(lowSong) > -1 && maxYear !=0 && nextSong.getYear() < maxYear) {
        titles.add(nextSong.Title);
        getRangeSongs.add(nextSong);
      }
      
      else if (nextSong.compareTo(lowSong) > -1 && maxYear ==0) {
        titles.add(nextSong.Title);
        getRangeSongs.add(nextSong);
      }
      
    }
    
   
    
    return titles;
  }

  /**
   * Filters the list of songs returned by future calls of getRange() and 
   * fiveMostDanceable() to only include older songs.  If getRange() was 
   * previously called, then this method will return a list of song titles
   * (sorted in ascending order by Speed BPM) that only includes songs on
   * Billboard on or before the specified maxYear.  If getRange() was not 
   * previously called, then this method should return an empty list.
   *
   * Note that this maxYear threshold should be saved for later use by the 
   * other methods defined in this class.
   *
   * @param maxYear is the maximum year that a returned song was on Billboard
   * @return List of song titles, empty if getRange was not previously called
   */
  @Override
  public List<String> filterOldSongs(int maxYear) {
    // Checking to make sure getRange() has been called. If not, this method should return an empty list
    if(this.getRangeHigh == 0 && this.getRangeLow == 0 || getRangeSongs.isEmpty()) {
      List<String> emptyList = new ArrayList<String>();
      return emptyList;
    }

    List<String> returnList = new ArrayList<String>();
    for(int i = 0; i < getRangeSongs.size(); i++){ // loop through values in current getRangeSongs list
        if(getRangeSongs.get(i).getYear() <= maxYear){
          returnList.add(getRangeSongs.get(i).getTitle()); // add to returnList if the song's year is less than or equal to maxYear paramter
        }
    }
    return returnList;
  }

  /**
   * This method makes use of the attribute range specified by the most
   * recent call to getRange().  If a maxYear threshold has been set by
   * filterOldSongs() then that will also be utilized by this method.
   * Of those songs that match these criteria, the five most danceable will 
   * be returned by this method as a List of Strings in increasing order of 
   * speed (bpm).  Each string contains the danceability followed by a 
   * colon, a space, and then the song's title.
   * If fewer than five such songs exist, display all of them.
   *
   * @return List of five most danceable song titles and their danceabilities
   * @throws IllegalStateException when getRange() was not previously called.
   */
  @Override
  public List<String> fiveMostDanceable() {
    if(getRangeSongs.isEmpty()) {
      throw new IllegalStateException("getRange() hasn't been called");
    }
    
    List<SongInterface> sorted = new ArrayList<SongInterface>();
    
    int smallest = 0;
    for(int i = 0; i < getRangeSongs.size(); i++) {
      if(sorted.size() < 5) {
        sorted.add(getRangeSongs.get(i));
        for(int j = 0; j < sorted.size(); j++) {
          if(sorted.get(j).getDanceability() < smallest) {
            smallest = sorted.get(j);
          }
        }
      }
    }
    
    
    
    
    
    
    
    
    
    
    if(getRangeSongs.isEmpty()) {
      throw new IllegalStateException("getRange() hasn't been called");
    }
    
    List<SongInterface> sorted = new ArrayList<SongInterface>();
    
    for(int i = 0; i < getRangeSongs.size(); ++i) {
      
      for(int j = 0; j < 5; ++j) {
        
        if(sorted.isEmpty() | sorted.size() <= j - 1) {
          sorted.add(getRangeSongs.get(i));
          break;
        }
        
        else if(sorted.size() > 4 && sorted.get(j).getDanceability() >  getRangeSongs.get(i).getDanceability()) {
          if(j == 4) {
            sorted.set(4, getRangeSongs.get(i));
          }
          
          else {
            sorted.add(j, getRangeSongs.get(i));
            if(sorted.size() > 5) {
              sorted.remove(5);
            }
            
          }
          
        }
        
      }
      
      
    }

    
//    Each string contains the danceability followed by a 
//    * colon, a space, and then the song's title.
    List<String> returnList = new ArrayList<String>();
    
    for(int i = 0; i < sorted.size(); ++i) {
      int Danceability = sorted.get(i).getDanceability();
      String title = sorted.get(i).getTitle();
      
      returnList.add(Danceability + ": " + title);
      
    }
    
    
    return returnList;
  }

}
