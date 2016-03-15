package musiclibrary;

/**
 * Track object. Has a trackName. Is Comparable. Loaded into Albums.
 * Spaces in trackName are converted to underscores.
 * @author Nico
 */
public class Track implements Comparable{
    private String trackName;
    
    public Track(String aTrack){
        trackName = aTrack;
        
        //Replace spaces with underscores
        trackName = trackName.replaceAll(" ", "_");
    }
    
    public void setTrackName(String aTrackName){
        trackName = aTrackName;
        trackName = trackName.replaceAll(" ", "_");
    }
    
    public String getTrackName(){
        return trackName;
    }
    
    @Override
    public int compareTo(Object o) {
        //Compare the FIUCourseName
        String compareName = ((Track) o).getTrackName();
        return trackName.toLowerCase().compareTo(compareName.toLowerCase());
    }
    
}
