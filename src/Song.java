
public class Song implements SongInterface {

  String Title = "";
  String Artist;
  String Genre;
  int Year;
  int BPM;
  int Danceability;
  int Loudness;
  int Liveness;
  int Energy;
  
  
  public Song(String row) {
    
    String[] newRow = row.split(",");
    int rowLeng = newRow.length;
    
    
    //Create title
    if(rowLeng > 14) {
      
      for(int i = 0; rowLeng - 1 - i >= 13; ++i) {
        Title += newRow[i];
      }
      
    }
    
    int startPoint = rowLeng - 13;
    if(rowLeng == 14) {
      Title = newRow[0];
    }
    Artist = newRow[startPoint];
    Genre = newRow[startPoint + 1];
    Year = Integer.parseInt(newRow[startPoint + 2]);
    BPM = Integer.parseInt(newRow[startPoint + 3]);
    Energy = Integer.parseInt(newRow[startPoint + 4]);
    Danceability = Integer.parseInt(newRow[startPoint + 5]);
    Loudness = Integer.parseInt(newRow[startPoint + 6]);
    Liveness = Integer.parseInt(newRow[startPoint + 7]);
    
   

  }
  
  
  
  @Override
  public int compareTo(SongInterface o) {
    
    if(this.BPM > o.getBPM()) {
      return 1;
    }
    
    else if(this.BPM < o.getBPM()) {
      return -1;
    }
    
    else if(this.BPM == o.getBPM()) {
      return 0;
    }
    
    return 0;
  }

  @Override
  public String getTitle() {
    
    return Title;
  }

  @Override
  public String getArtist() {
    
    return Artist;
  }

  @Override
  public String getGenres() {
    
    return Genre;
  }

  @Override
  public int getYear() {
    
    return Year;
  }

  @Override
  public int getBPM() {
    
    return BPM;
  }

  @Override
  public int getEnergy() {
    
    return Energy;
  }

  @Override
  public int getDanceability() {
    
    return Danceability;
  }

  @Override
  public int getLoudness() {
    
    return Loudness;
  }

  @Override
  public int getLiveness() {
   
    return Liveness;
  }

  
  
  
}
