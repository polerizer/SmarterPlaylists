/**
 * Nov 12, 2013
 * TrackTools.java
 * Daniel Pok
 * AP Java 6th
 */
package tools;

import java.util.ArrayList;

import data.Track;

/**
 * @author Daniel
 *
 */
public class TrackTools {


	public static ArrayList<Track> findTracks(String artist, String album, String title, ArrayList<Track> tracks){
		if(tracks == null || tracks.size() == 0){
			return null;
		} else{
			ArrayList<Track> result = new ArrayList<Track>();
			for(Track i: tracks){
				if(artist == null || artist.isEmpty() || (i.getArtist() != null && i.getArtist().toUpperCase().contains(artist.toUpperCase()))){
					if(album == null || album.isEmpty() || (i.album() != null && i.album().title().toUpperCase().contains(album.toUpperCase()))){
						if(title == null || title.isEmpty() || (i.title() != null && i.title().toUpperCase().contains(title.toUpperCase()))){
							result.add(i);
						}
					}
				}
			}
			return result;
		}
	}
	
}
