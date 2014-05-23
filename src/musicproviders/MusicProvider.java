/**
 * Nov 25, 2013
 * MusicProvider.java
 * Daniel Pok
 * AP Java 6th
 */
package musicproviders;

import java.util.ArrayList;

import data.Track;

/**
 * @author poler_000
 *
 */
public interface MusicProvider {

	public String getProviderSideID(String artist, String track, String album);
	public String getProviderSidePlaylist(ArrayList<Track> tracks);
	
}
