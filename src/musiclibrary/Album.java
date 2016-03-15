package musiclibrary;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * An album object. Has albumName, artistName, ArrayList<Track>
 * Spaces are converted to underscores in the variables.
 * @author Nico
 */
public class Album implements Comparable<Album>{
    
    private String albumName;
    private String artistName;
    private ArrayList<Track> tracks;
    
    public Album(String albumName, String artistName, ArrayList<Track> tracks){
        
        this.albumName = albumName;
        this.artistName = artistName;
        this.tracks = tracks;
        
        //Replace spaces with underscores
        this.albumName = albumName.replaceAll(" ", "_");
        this.artistName = artistName.replaceAll(" ", "_");
        
        //Sort the tracks alphabetically
        Collections.sort(this.tracks);
        
    }
    
    public void setAlbumName(String albumName){
        this.albumName = albumName;
        this.albumName = albumName.replaceAll(" ", "_");
    }
    
    public String getAlbumName(){
        return albumName;
    }
    
    public void setArtistName(String artistName){
        this.artistName = artistName;
        this.artistName = artistName.replaceAll(" ", "_");
    }
    
    public String getArtistName(){
        return artistName;
    }
    
    public void setTracks(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ArrayList<Track> getTracks(){
        return tracks;
    }

    @Override
    public int compareTo(Album o) {
        //Compare the FIUCourseName
        String compareName = (o).getAlbumName();
        return albumName.toLowerCase().compareTo(compareName.toLowerCase());
    }
    
    @Override
    public String toString(){
        String theString = artistName +" "+ albumName;
        for (Track track : tracks) {
            theString = theString+" " + track.getTrackName();
        }
        
        return theString;
    }
    
}
