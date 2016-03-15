package musiclibrary;

import java.util.Comparator;

/**
 * Comparator for Albums. Compares the artistNames
 * @author Nico
 */
public class artistNameComparator implements Comparator<Album> {
    @Override
    public int compare(Album a, Album b){
        return a.getArtistName().toLowerCase().compareTo(b.getArtistName().toLowerCase());
    }
}