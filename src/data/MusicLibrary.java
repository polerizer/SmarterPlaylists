/**
 * Nov 9, 2013
 * MusicLibrary.java
 * Daniel Pok
 * AP Java 6th
 */
package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Daniel
 *
 */
public class MusicLibrary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7077782193433295711L;
	//variables
	private HashMap<String, Triplet> triplets;
	private ArrayList<Track> tracks;
	private HashMap<String, Track> tracksById;

	public MusicLibrary(HashMap<String, Triplet> triplets){
		this.triplets = triplets;
		tracks = new ArrayList<Track>();
		tracksById = new HashMap<String, Track>();
	}

	public MusicLibrary(){
		triplets = new HashMap<String, Triplet>();
		tracks = new ArrayList<Track>();
		tracksById = new HashMap<String, Track>();
	}

	public HashMap<String, Track> tracksById(){
		return tracksById;
	}

	public ArrayList<Track> tracks(){
		return tracks;
	}

	public HashMap<String, Triplet> triplets(){
		return triplets;
	}
	
	public int size(){
		return tracks.size();
	}
	
	public void addTrack(Track track){
		String id = track.id();
		if(id != null && tracksById.containsKey(id)){
			tracksById.get(id).mergeTracks(track);
		} else{
			tracks.add(track);
			tracksById.put(id, track);
		}
	}

	public void addResponse(Response response){
		if(response.albums() != null){
			for(Album i: response.albums()){
				for(Track j: i.tracks()){
					addTrack(j);
				}
			}	
		} 
	}
	
	public Track find(String id){
		return tracksById.get(id);
	}
	
	/**Removes the track with the selected ID from the library and return the track
	 * 
	 * @param id String ID of the track to be removed
	 * @return the track removed or null if no track was found with that ID
	 */
	public Track delete(String id){
		if(id != null && tracksById.containsKey(id)){
			Track result = tracksById.get(id);
			tracks.remove(result);
			tracksById.remove(id);
			return result;
		} else{
			return null;
		}
	}
	
	public static void saveLibrary(String fileName, MusicLibrary lib) throws IOException{
		FileOutputStream fout = new FileOutputStream(fileName);
		ObjectOutputStream oout = new ObjectOutputStream(fout);
		oout.writeObject(lib);
		oout.close();
		fout.close();
	}

	public static MusicLibrary openLibrary(String fileName) throws IOException, ClassNotFoundException{
		FileInputStream fin = new FileInputStream(fileName);
		ObjectInputStream oin = new ObjectInputStream(fin);
		MusicLibrary lib = (MusicLibrary) oin.readObject();
		oin.close();
		fin.close();
		return lib;
	}
	
	public String idsToString(){
		if(tracksById == null) return "";
		String result = "";
		for(String id: tracksById.keySet()){
			Track track = tracksById.get(id);
			result += String.format("ID: %s%nTitle: %s%nArtist: %s%nAlbum: %s%n", id, track.title(), track.artist(), track.album().title());
		}
		return result;
	}
	
	public String toString(){
		String result = "";
		for(Track i: tracks){
			result += String.format("%s%n", i);
		}
		return result;
	}
}
