package musiclibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Gets a filename from the user that is a catalog of albums. Creates albums
 * with the information and stores them in an ArrayList. Presents menu to user
 * for searching the catalog for an album or for an artist and prints
 * information about the album if found or all of the albums by the artist if
 * found. Also allows user to input information for a new album which is added
 * to the ArrayList and written to the catalog file.
 *
 * @author Nico
 */
public class MusicLibrary {

    public static String fileName;

    /**
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        //ArrayList for storing albums
        ArrayList<Album> albums = new ArrayList();

        //Makes a scanner with a filename from the user
        Scanner fileScanner = getScanner();

        //Is set to false if the program ran to completion
        boolean tryAgain = true;

        //While tryAgain
        do {

            /**
             * Go through the Scanner. For each line, gather the tokens, create
             * a new album with them, and then add it to albums.
             */
            try {
                while (fileScanner.hasNextLine()) { //Do something on each line (each album)

                    //Temporary variables that will be used to create an Album object later
                    String artistName;
                    String albumName;
                    ArrayList<Track> tracks = new ArrayList();

                    //Create a scanner with the line
                    Scanner lineScanner = new Scanner(fileScanner.nextLine());

                    //Get the artistName
                    artistName = lineScanner.next();

                    //Get the albumName
                    albumName = lineScanner.next();

                    //All the remaining tokens are song titles
                    while (lineScanner.hasNext()) {

                        //Create a Track with the next token
                        Track track = new Track(lineScanner.next());

                        //Add the Track to tracks
                        tracks.add(track);

                    }

                    //Put everything together in an album
                    Album album = new Album(albumName, artistName, tracks);

                    //Put the album into albums
                    albums.add(album);

                } //Done with the line

                //Print all albums sorted by albumName
                printAlbumsByAlbumName(albums);

                //Print dividing line of asterisks
                System.out.println("*******************************************\n");

                //Print all albums sorted by artistName
                printAlbumsByArtistName(albums);

                //Menu
                String input;
                do {
                    System.out.println("Choose an option (a,b,c,d)");
                    System.out.println("a) Search by Album Title");
                    System.out.println("b) Search by Artist");
                    System.out.println("c) Add album to catalog");
                    System.out.println("d) Quit");

                    //Use getMenuInput() to get an input that we know what to do with.
                    input = getMenuInput();

                    switch (input) {
                        case "a":
                            albumSearch(albums);
                            break;
                        case "b":
                            artistSearch(albums);
                            break;
                        case "c":
                            addAlbum(albums, albums);
                            break;
                        default: ;
                            break;
                    }

                    //Repeat menu if input is "d"
                } while (!input.equals("d"));

                //Program ran successfully. End do/while loop.
                tryAgain = false;
                
            } catch (NoSuchElementException e) { //If something in the file is amiss..
                System.out.println("File is unacceptably corrupt");
                fileScanner = getScanner();
            }
        } while (tryAgain); //If exception was caught, try again after getting new scanner.

    }

    /**
     * Searches the Album ArrayList for a specified album. Takes input from the
     * user and uses binarySearch to find it and then print information about
     * that album.
     *
     * @param albums
     */
    public static void albumSearch(ArrayList<Album> albums) {

        //Sort ArrayList
        Collections.sort(albums);

        System.out.println("Enter an album to search for");

        //Get input from the user
        String albumName = getStringInput();

        //Replace spaces with underscores
        albumName = albumName.replaceAll(" ", "_");

        ArrayList<Track> dummyTracks = new ArrayList();

        Album dummyAlbum = new Album(albumName, "", dummyTracks);

        int index;

        index = Collections.binarySearch(albums, dummyAlbum);

        if (index >= 0) { //Something was found. Print info.
            System.out.println("Found album!");
            System.out.println("Album name: " + albums.get(index).getAlbumName());
            System.out.println("Artist name: " + albums.get(index).getArtistName());
            System.out.println("Tracks: ");
            for (int i = 0; i < albums.get(index).getTracks().size(); i++) {
                System.out.println("\t" + albums.get(index).getTracks().get(i).getTrackName());
            }

        } else { //Nothing was found
            System.out.println("Did not find album: \"" + albumName + "\".");
        }

    }

    /**
     * Searches the Album ArrayList for albums by a specified artist. Prints all
     * of the albums with an artistName that matches the artistName input by the
     * user.
     *
     * @param albums
     */
    public static void artistSearch(ArrayList<Album> albums) {

        //Sort the ArrayList
        Collections.sort(albums, new artistNameComparator());

        System.out.println("Enter an artist to search for");

        //Get the artist to search for
        String artistName = getStringInput();

        //Replace spaces with underscores
        artistName = artistName.replaceAll(" ", "_");

        ArrayList<Track> dummyTracks = new ArrayList();

        Album dummyAlbum = new Album("", artistName, dummyTracks);

        int index;
        index = Collections.binarySearch(albums, dummyAlbum, new artistNameComparator());

        if (index >= 0) { //Something was found

            System.out.println("Found albums by artist \"" + artistName + "\"!");

            //Find the first album in the list by the artist
            String current;
            String previous;
            String next;

            do {
                //Artist of the current album
                current = albums.get(index).getArtistName();

                if (index == 0) { //There's nothing before this album. Don't try to get it.
                    previous = "1234567890"; //Dummy value
                } else { //Get the previous album's artistName
                    previous = albums.get(index - 1).getArtistName();
                }

                //If they match, the previous album is by the same artist
                if (previous.equals(current)) {
                    index--; //Move up
                }

                //If they match, we moved up and should get/compare the previous artist again.
            } while (previous.equals(current));

            /**
             * Keep printing album at index and keep incrementing index/printing
             * until album at next index is by different artist or reached end
             */
            do {

                //Print the current album's info
                System.out.println("Album: " + albums.get(index).getAlbumName());
                System.out.println("Tracks: ");
                for (int i = 0; i < albums.get(index).getTracks().size(); i++) {
                    System.out.println("\t" + albums.get(index).getTracks().get(i).getTrackName());
                }

                //Current album's artist
                current = albums.get(index).getArtistName();

                //Get next album's artist
                if (index == albums.size() - 1) { //There's nothing after. Don't try to get it.
                    next = "1234567890"; //Dummy value
                } else { //Get next album's artist
                    next = albums.get(index + 1).getArtistName();
                }

                //If they match, next album is by same artist
                if (next.equals(current)) {
                    index++;
                }

                //If they match, we moved down and should print/compare again
            } while (next.equals(current));
        } else { //No albums by that artist were found
            System.out.println("No albums by artist \"" + artistName + "\" found.");
        }
    }

    /**
     * Gets information from user to make a new album object, add it to the
     * album ArrayLists, resorts them, and writes data to the file in use.
     *
     * @param albums the ArrayList sorted by album name
     * @param albums2 the ArrayList sorted by artist name
     * @throws IOException
     */
    public static void addAlbum(ArrayList<Album> albums, ArrayList<Album> albums2) throws IOException {

        //Temporary variables that will be used to create an Album object
        String artistName;
        String albumName;
        ArrayList<Track> tracks = new ArrayList();

        //Get album and artist name
        System.out.println("Enter the name of the album to add to the catlog file: ");
        albumName = getStringInput();
        System.out.println("Enter the artist: ");
        artistName = getStringInput();

        String input;
        boolean repeat = true; //For repeating Track creation

        do {
            System.out.println("Enter a track to add to the album or \"done\" to finish");

            input = getStringInput();

            switch (input) {
                case "done": //User is done. Set repeat to false.
                    repeat = false;
                    break;
                default: ; //Create a Track with the input and add it to tracks.
                    Track track = new Track(input);
                    tracks.add(track);
                    break;
            }

            //If repeat is still true, repeat.
        } while (repeat);

        //Create Album
        Album album = new Album(albumName, artistName, tracks);

        //Add the Albums to both ArrayLists
        albums.add(album);
        albums2.add(album);

        //Resort them
        Collections.sort(albums);
        Collections.sort(albums2, new artistNameComparator());

        //Write the data to the file. fileName is static and was assigned in getScanner()
        FileWriter fw = new FileWriter(fileName, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.print("\n" + album.toString());
        pw.close();

    }

    /**
     * Asks the user for a string and repeats until the string is either
     * "a","b","c",or "d".
     *
     * @return
     */
    public static String getMenuInput() {
        String input;
        Scanner in = new Scanner(System.in);
        try {
            input = in.next();
            //Check if it's not any of the valid options. Recursive.
            if (!(input.equals("a")
                    || input.equals("b")
                    || input.equals("c")
                    || input.equals("d"))) {
                System.out.println("Not a valid option. Choose a,b,c,d.");
                input = getMenuInput(); //Try again.
            }

        } catch (NoSuchElementException e) {

            System.out.println("You did not enter a valid option. Choose a,b,c,d.");

            input = getMenuInput(); //Try again.

        }

        return input;

    }

    /**
     * Gets string input from the user.
     *
     * @return input
     */
    public static String getStringInput() {
        String input;
        Scanner in = new Scanner(System.in);
        try {
            input = in.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("Enter a string");
            input = getStringInput(); //Try again.
        }
        return input;
    }

    /**
     * Gets a filename from System.in. Creates a file with that filename.
     * Creates a scanner for that file.
     *
     * @return A scanner made from a file, made from a filename from System.in
     * @throws FileNotFoundException
     */
    public static Scanner getScanner() throws FileNotFoundException {

        //Get file name
        Scanner in = new Scanner(System.in);
        System.out.println("Enter filename:");
        fileName = in.nextLine();

        //Create file and scanner
        File file = new File(fileName);
        Scanner fileScan;
        try {
            fileScan = new Scanner(file);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. Try again."); //File doesn't exist.
            fileScan = getScanner(); //Try again.
        }

        return fileScan;

    }

    /**
     * Prints all of the Albums in albums sorted by albumName. Sorts albums and
     * prints them one by one.
     *
     * @param albums
     */
    public static void printAlbumsByAlbumName(ArrayList<Album> albums) {

        System.out.println("Printing albums sorted by album name...\n");

        //Sort albums by name
        Collections.sort(albums);

        //Display all the albums
        for (int i = 0; i < albums.size(); i++) {

            System.out.println("Album: \t\t" + albums.get(i).getAlbumName());
            System.out.println("Artist: \t" + albums.get(i).getArtistName());
            System.out.print("Tracks: \t");
            //Display all the tracks (They are alread alphabetically sorted)
            for (int j = 0; j < albums.get(i).getTracks().size(); j++) {

                //We have the tracks ArrayList, let's print the tracks
                if (j == 0) { //Formating magic. No tabs for first track printed.
                    System.out.println(albums.get(i).getTracks().get(j).getTrackName());
                }
                System.out.println("\t\t" + albums.get(i).getTracks().get(j).getTrackName());
            }

            System.out.println();
        }
    }

    /**
     * Prints all of the Albums in albums sorted by artistName. Sorts albums by
     * artist name and then prints all of the albums by an artist, and then all
     * of the albums by the next artist, until all albums have been printed.
     *
     * @param albums
     */
    public static void printAlbumsByArtistName(ArrayList<Album> albums) {

        System.out.println("Printing albums sorted by artist name...\n");

        //Sort albums by artistName
        Collections.sort(albums, new artistNameComparator());

        String artist = "null";

        //Display all the albums. Go through each album.
        for (int i = 0; i < albums.size(); i++) {

            //Check if the current album's artist is the same as the previous album's
            //If it is different, print a dividing line and the new artist's name
            if (!artist.equals(albums.get(i).getArtistName())) {
                System.out.println("-------------------------"
                        + albums.get(i).getArtistName()
                        + "-------------------------\n");
                //Make the new artist the current artist.
                artist = albums.get(i).getArtistName();
            }

            //Print the current album's info
            System.out.println("Album: \t\t" + albums.get(i).getAlbumName());
            System.out.print("Tracks: \t\n");
            //Display all the tracks (They are already alphabetically sorted)
            for (int j = 0; j < albums.get(i).getTracks().size(); j++) {

                //We have the tracks ArrayList, let's print the tracks
                System.out.println("\t\t" + albums.get(i).getTracks().get(j).getTrackName());
            }
            System.out.println();
        }

    }

}
