/**
 * Nov 10, 2013
 * TestPickMusic.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import java.io.IOException;
import java.util.ArrayList;

import music.PickMusic;

import data.MusicLibrary;
import data.Track;

/**
 * @author Daniel
 *
 */
public class TestPickMusic {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		MusicLibrary lib = MusicLibrary.openLibrary("library.txt");
		ArrayList<Track> tracks = lib.tracks();
		System.out.printf("Songs similar to: %n%s%n-----------------------------", tracks.get(0).toStringMedium());
		ArrayList<Track> similar = PickMusic.sameGenre(tracks, tracks.get(0), 1);
		similar = PickMusic.moodWithinDistance(similar, tracks.get(0), 4);
		for(Track i: similar){
			System.out.println(i.toStringMedium());
		}
	}

}
