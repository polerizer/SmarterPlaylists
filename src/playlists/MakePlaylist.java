/**
 * Nov 10, 2013
 * MakePlaylist.java
 * Daniel Pok
 * AP Java 6th
 */
package playlists;

import java.util.ArrayList;
import java.util.Random;

import music.PickMusic;

import data.Coordinate;
import data.Track;

/**
 * @author Daniel
 *
 */
public class MakePlaylist {

	public static ArrayList<Track> makePlaylist(Track start, Track end, int total, ArrayList<Track> tracks){
		ArrayList<Track> playlist = new ArrayList<Track>();
		ArrayList<Track> unplayed = new ArrayList<Track>();
		for(Track i: tracks){unplayed.add(i);}
		unplayed.remove(start);
		unplayed.remove(end);
		ArrayList<Track> candidates;
		Coordinate target;
		playlist.add(start);
		Random rnd = new Random();
		for(int i = 1; i < total - 1; i++){
			candidates = mergeArrayLists(PickMusic.bpmWithinDistance(unplayed, playlist.get(i -1), 10), PickMusic.sameGenre(unplayed, playlist.get(i-1)));
			target = MoodFunction.getCoordinate(start.coordinate(), end.coordinate(), i, total);
			if(candidates.size() == 0){
				candidates = PickMusic.moodWithinDistance(unplayed, target, 100);
			}
			if(candidates.size() == 0){
				candidates = unplayed;
			}
			candidates = PickMusic.moodWithinDistance(candidates, target, 15);
			Track next;
			if(candidates.size() < 3){
				next = candidates.get(0);
			} else{
				next = candidates.get(rnd.nextInt(3));
			}
			
			playlist.add(next);
			unplayed.remove(next);
		}
		
		playlist.add(end);
		return playlist;
	}
	
	public static ArrayList<Track> mergeArrayLists(ArrayList<Track> one, ArrayList<Track> two){
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track i: one){
			result.add(i);
		}
		for(Track i:two){
			if(!result.contains(i)){
				result.add(i);
			}
		}
		return result;
	}
}
