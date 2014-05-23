/**
 * Nov 13, 2013
 * MigrateLibrary.java
 * Daniel Pok
 * AP Java 6th
 */
package tools;

import java.io.IOException;

import data.MusicLibrary;
import data.Track;

/**
 * @author Daniel
 *
 */
public class MigrateLibrary {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		MusicLibrary lib = MusicLibrary.openLibrary("library.txt");
		MusicLibrary temp = new MusicLibrary();
		for(Track i: lib.tracks()){
			temp.addTrack(i);
		}
		MusicLibrary.saveLibrary("library.txt", temp);
		
	}

}
