/**
 * Nov 9, 2013
 * Track.java
 * Daniel Pok
 */
package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import music.MoodAxes;

import org.w3c.dom.Element;

import tools.StringTools;
import xml.XMLTools;

/**Track.java
 * Class representing a single Track.
 * Has fields for the track's id, artist, title, trackNumber, bpm, artist origin, artist type, artist era, genre, mood, tempo, album, and mood coordinate
 * There are no setters because the metadata for a track is not meant to be changed. To change track data, merge it with an updated version of the track using the mergeTracks(...) function.
 * Consequently, the getters are named as the variables with parentheses (i.e. to get the id, call id(). to get artist, call artist())
 * The get[variable]() functions (getArtist(), getArtistOrigin(), getArtistType(), getArtistEra(), getGenre()) get values that can be in either the Track or its parent Album by first looking
 * in the track's variables then looking into the album's. This lets you get the artist easily if you don't care whether there was a track artist different from the album artist, etc.
 * The four string methods (toString, toStringShort, and toStringMedium) give three different levels and formats for strings representing the track. String short gives a quick, one-line
 * description of track, artist, and album, string medium gives that info plus bpm, genre and mood coordinates. String long gives all info it can find in the track, pulling from the parent album
 * as needed. toString gives any information it can find in this track only.
 * 
 * @author Daniel Pok
 *
 */
public class Track implements Serializable{

	/** Serialization ID
	 * 
	 */
	private static final long serialVersionUID = -3149437799060858763L;
	
	//instance variables
	private String id, artist, title;
	private Integer trackNumber, bpm; 
	private ArrayList<Triplet> artistOrigin, artistType, artistEra, genre, mood, tempo;
	private Album album;
	private Coordinate coordinate;

	/*##################################CONSTRUCTORS#########################################################
	 * 	
	#########################################################################################################*/
	/**Default constructor for Track, makes a new track with the given fields
	 * 
	 * @param track The XML element containing the track
	 * @param album the album object that this track is a member of
	 * @param knownTriplets the hashmap of all known triplets in the library
	 */
	public Track(Element track, Album album, HashMap<String, Triplet> knownTriplets){
		//set all the values to defaults (null)
		id = null;
		artist = null;
		title = null;
		artistOrigin = null;
		artistType = null;
		artistEra = null;
		genre = null;
		mood = null;
		tempo = null;
		this.album = album;
		trackNumber = null;
		coordinate = null;

		//begin populating fields that are not empty
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "GN_ID");
			id = temp.get(0).getFirstChild().getNodeValue();
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "ARTIST");
			artist = temp.get(0).getFirstChild().getNodeValue();
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "TITLE");
			title = temp.get(0).getFirstChild().getNodeValue();
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "TRACK_NUM");
			trackNumber = Integer.parseInt(temp.get(0).getFirstChild().getNodeValue());
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "ARTIST_ORIGIN");
			if(temp.size() > 0){
				artistOrigin = new ArrayList<Triplet>();
				for(Element i : temp){
					Triplet triplet = new Triplet(i);
					if(knownTriplets.containsKey(triplet.id())){
						triplet = knownTriplets.get(triplet.id());
					} else {
						knownTriplets.put(triplet.id(), triplet);
					}
					artistOrigin.add(triplet);
				}
			}
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "ARTIST_TYPE");
			if(temp.size() > 0){
				artistType = new ArrayList<Triplet>();
				for(Element i : temp){
					Triplet triplet = new Triplet(i);
					if(knownTriplets.containsKey(triplet.id())){
						triplet = knownTriplets.get(triplet.id());
					} else {
						knownTriplets.put(triplet.id(), triplet);
					}
					artistType.add(triplet);
				}
			}
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "ARTIST_ERA");
			if(temp.size() > 0){
				artistEra = new ArrayList<Triplet>();
				for(Element i : temp){
					Triplet triplet = new Triplet(i);
					if(knownTriplets.containsKey(triplet.id())){
						triplet = knownTriplets.get(triplet.id());
					} else {
						knownTriplets.put(triplet.id(), triplet);
					}
					artistEra.add(triplet);
				}
			}
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "GENRE");
			if(temp.size() > 0){
				genre = new ArrayList<Triplet>();
				for(Element i : temp){
					Triplet triplet = new Triplet(i);
					if(knownTriplets.containsKey(triplet.id())){
						triplet = knownTriplets.get(triplet.id());
					} else {
						knownTriplets.put(triplet.id(), triplet);
					}
					genre.add(triplet);
				}
			}
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "MOOD");
			if(temp.size() > 0){
				mood = new ArrayList<Triplet>();
				for(Element i : temp){
					Triplet triplet = new Triplet(i);
					if(knownTriplets.containsKey(triplet.id())){
						triplet = knownTriplets.get(triplet.id());
					} else {
						knownTriplets.put(triplet.id(), triplet);
					}
					mood.add(triplet);
				}
			}
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(track, "TEMPO");
			if(temp.size() > 0){
				tempo = new ArrayList<Triplet>();
				for(Element i : temp){
					Triplet triplet = new Triplet(i);
					if(knownTriplets.containsKey(triplet.id())){
						triplet = knownTriplets.get(triplet.id());
					} else {
						knownTriplets.put(triplet.id(), triplet);
					}
					tempo.add(triplet);
				}
			}
		} catch(Exception ex){}
		double x = 0, y = 0;
		for(Triplet i : mood){
			Coordinate coord = MoodAxes.getCoordinate(i.id());
			if(coord.axes.equals("5x5")){
				x = coord.x * 2 - 0.5;
				y = coord.y * 2 - 0.5;
			} else{
				x = coord.x;
				y = coord.y;
			}
		}
		if(x == 0 && y == 0){
			coordinate = null;
		} else{
			coordinate = new Coordinate(x, y, "10x10");
		}

		if(tempo.size() >= 3){
			bpm = Integer.parseInt(tempo.get(2).text().substring(0,tempo.get(2).text().length() - 1));
		} else{
			bpm = null;
		}
	}

	/*##################################GETTERS#########################################################
	 * 	
	#########################################################################################################*/
	public String id(){
		return id;
	}
	public String title(){
		return title;
	}
	public String artist(){
		return artist;
	}

	public String getArtist(){
		if(artist != null && ! artist.isEmpty()){
			return artist;
		} else if(album != null){
			return album.artist();
		} else{
			return null;
		}
	}
	public Integer trackNumber(){
		return trackNumber;
	}
	public ArrayList<Triplet> artistOrigin(){
		return artistOrigin;
	}

	public ArrayList<Triplet> getArtistOrigin(){
		if(artistOrigin != null && artistOrigin.size() > 0){
			return artistOrigin;
		} else if(album != null){
			return album.artistOrigin();
		} else{
			return null;
		}
	}

	public ArrayList<Triplet> artistType(){
		return artistType;
	}

	public ArrayList<Triplet> getArtistType(){
		if(artistType != null && artistType.size() > 0){
			return artistType;
		} else if(album != null){
			return album.artistType();
		} else{
			return null;

		}
	}

	public ArrayList<Triplet> artistEra(){
		return artistEra;
	}
	
	public ArrayList<Triplet> getArtistEra(){
		if(artistEra != null && artistEra.size() > 0){
			return artistEra;
		} else if(album != null){
			return album.artistEra();
		} else{
			return null;

		}
	}
	
	public ArrayList<Triplet> genre(){
		return genre;
	}
	
	public ArrayList<Triplet> getGenre(){
		if(genre != null && genre.size() > 0){
			return genre;
		} else if(album != null){
			return album.genre();
		} else{
			return null;

		}
	}
	
	public ArrayList<Triplet> mood(){
		return mood;
	}
	public ArrayList<Triplet> tempo(){
		return tempo;
	}
	public Album album(){
		return album;
	}

	public Coordinate coordinate(){
		return coordinate;
	}

	public Integer bpm(){
		return bpm;
	}


	/*##################################MUTATOR FUNCTIONS#########################################################
	 * 	
	#########################################################################################################*/
	
	/**Merges updates from one track onto the current one, where fields from the update track take precedence, and calls mergeAlbum on their albums
	 * 
	 * @param update Track to draw update data from
	 */
	public void mergeTracks(Track update){
		if(update == null) return;

		if(update.trackNumber() != null) trackNumber = update.trackNumber();
		if(update.title() != null) title = update.title();
		if(update.artist() != null) artist = update.artist();
		if(update.id() != null) id = update.id();
		if(update.artistOrigin() != null) artistOrigin = update.artistOrigin();
		if(update.artistType() != null) artistType = update.artistType();
		if(update.artistEra() != null) artistEra = update.artistEra();
		if(update.tempo() != null) tempo = update.tempo();
		if(update.bpm() != null) bpm = update.bpm();
		if(update.genre() != null) genre = update.genre();
		if(update.mood() != null) mood = update.mood();
		if(update.coordinate() != null) coordinate = update.coordinate();
		if(update.album() != null && album != null) album.mergeAlbums(update.album());
	}

	
	/*##################################STRING FUNCTIONS#########################################################
	 * 	
	#########################################################################################################*/
	
	/**Creates a string representing all the info about this track that it can find in this track only.
	 * 
	 * @return A string with the track's number, title, artist + all artist info, album, mood, bmp, tempo, genre, and coordinates only from this track
	 */
	public String toString(){
		String result = "";
		result += String.format("-------------------------%n");
		result += String.format("Track:%d %n", trackNumber);
		if(title != null && !title.isEmpty()) result += String.format("Title: %s%n", title); //Title
		if(artist != null && !artist.isEmpty()) result += String.format("Artist: %s%n", artist); //Artist
		if(album != null) result += String.format("Album: %s%n", album.title());
		if(id != null && !id.isEmpty()) result += String.format("ID: %s%n", id); //ID
		if(artistOrigin() != null && artistOrigin.size() > 0){
			result += "Artist Origin:" + String.format("%n");
			for(Triplet i : artistOrigin){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(artistType != null && artistType.size() > 0){
			result += "Artist Type:" + String.format("%n");
			for(Triplet i :artistType){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(artistEra != null && artistEra.size() > 0){
			result += "Artist Era:" + String.format("%n");
			for(Triplet i : artistEra){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(tempo != null && tempo.size() > 0){
			result += "Tempo:" + String.format("%n");
			for(Triplet i : tempo){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(bpm != null){
			result += "Approximate BPM: " + bpm.toString() + String.format("%n");
		}
		if(genre != null && genre.size() > 0){
			result += "Genre:" + String.format("%n");
			for(Triplet i : genre){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(mood != null && mood.size() > 0){
			result += "Mood:" + String.format("%n");
			for(Triplet i : mood){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(coordinate != null){
			result += "Mood Coordinate: " + coordinate.toString() + String.format("%n");
		}
		return result;
	}
	
	/**Creates a string representing all the info about this track that it can find, looking into the parent album for missing fields.
	 * 
	 * @return A string with the track's number, title, artist + all artist info, album, mood, bmp, tempo, genre, and coordinates including data from the album if necessary
	 */
	public String toStringLong(){
		String result = "";
		result += String.format("-------------------------%n");
		result += String.format("Track:%d %n", trackNumber);
		if(title != null && !title.isEmpty()) result += String.format("Title: %s%n", title); //Title
		if(getArtist() != null && !getArtist().isEmpty()) result += String.format("Artist: %s%n", getArtist()); //Artist
		if(album != null) result += String.format("Album: %s%n", album.title());
		if(id != null && !id.isEmpty()) result += String.format("ID: %s%n", id); //ID
		if(getArtistOrigin() != null && getArtistOrigin().size() > 0){
			result += "Artist Origin:" + String.format("%n");
			for(Triplet i : getArtistOrigin()){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(getArtistType() != null && getArtistType().size() > 0){
			result += "Artist Type:" + String.format("%n");
			for(Triplet i : getArtistType()){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(getArtistEra() != null && getArtistEra().size() > 0){
			result += "Artist Era:" + String.format("%n");
			for(Triplet i : getArtistEra()){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(tempo != null && tempo.size() > 0){
			result += "Tempo:" + String.format("%n");
			for(Triplet i : tempo){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(bpm != null){
			result += "Approximate BPM: " + bpm.toString() + String.format("%n");
		}
		if(getGenre() != null && getGenre().size() > 0){
			result += "Genre:" + String.format("%n");
			for(Triplet i : getGenre()){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(mood != null && mood.size() > 0){
			result += "Mood:" + String.format("%n");
			for(Triplet i : mood){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(coordinate != null){
			result += "Mood Coordinate: " + coordinate.toString() + String.format("%n");
		}
		return result;
	}

	/**Gets a String representing the track's title, number, artist, album title, ID, bpm, genre, and mood coordinate if they exist
	 * 
	 * @return String with the the track's number, title, artist, album title, id, bpm, genre, and mood coordinate
	 */
	public String toStringMedium(){
		String result = "";
		result += String.format("Track %d: %s%n", trackNumber, title);
		if(album != null & album.title() != null) result += String.format("Album: %s%n", album.title());
		if(getArtist() != null && !getArtist().isEmpty()) result += String.format("Artist: %s%n", getArtist()); //Artist
		if(id != null && !id.isEmpty()) result += String.format("ID: %s%n", id); //ID
		if(bpm != null){
			result += "Approximate BPM: " + bpm.toString() + String.format("%n");
		}
		if(getGenre() != null && getGenre().size() > 0){
			result += "Genre:" + String.format("%n");
			for(Triplet i : getGenre()){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(coordinate != null){
			result += "Mood Coordinate: " + coordinate.toString() + String.format("%n");
		}
		return result;
	}

	/**Gets a string representing the track's title, artist, album, and id if they exist
	 * 
	 * @return String in the format "'Song Title' by 'Artist' on 'Album' (ID: id)"
	 */
	public String toStringShort(){
		String mTitle = title, mArtist = getArtist(), mAlbum = album.title(), mId = id;
		if(mTitle == null) title = "";
		if(mArtist == null) mArtist = "";
		if(mAlbum == null) mAlbum = "";
		if(mId == null) mId = "";
		return String.format("'%s' by '%s' on '%s' (ID: %s)", mTitle, mArtist, mAlbum, mId);
	}
}
