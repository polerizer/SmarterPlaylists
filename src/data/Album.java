/**
 * Nov 9, 2013
 * Album.java
 * Daniel Pok
 * AP Java 6th
 */
package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;

import tools.StringTools;
import xml.XMLTools;

/**
 * @author Daniel
 *
 */
public class Album implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3295478788744901835L;
	//instance variables
	private String id, artist, title;
	private Integer date, trackCount; 
	private ArrayList<Triplet> artistOrigin, artistType, artistEra, genre;
	private ArrayList<Track> tracks;
	private HashMap<String, Triplet> knownTriplets;
	
	//constructor that builds Album objects from album elements
	public Album(Element element, HashMap<String, Triplet> known){
		//set all the values to defaults (null)
		id = null;
		artist = null;
		title = null;
		date = null;
		trackCount = null;
		artistOrigin = null;
		artistType = null;
		artistEra = null;
		genre = null;
		tracks = null;
		knownTriplets = known;
		
		//begin populating fields that are not empty
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "GN_ID");
			id = temp.get(0).getFirstChild().getNodeValue();
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "ARTIST");
			artist = temp.get(0).getFirstChild().getNodeValue();
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "TITLE");
			title = temp.get(0).getFirstChild().getNodeValue();
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "DATE");
			date = Integer.parseInt(temp.get(0).getFirstChild().getNodeValue());
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "TRACK_COUNT");
			trackCount = Integer.parseInt(temp.get(0).getFirstChild().getNodeValue());
		} catch(Exception ex){}
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "ARTIST_ORIGIN");
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
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "ARTIST_TYPE");
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
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "ARTIST_ERA");
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
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "GENRE");
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
		
		//populate tracks
		try{
			ArrayList<Element> temp = XMLTools.getChildrenByTag(element, "TRACK");
			tracks = new ArrayList<Track>();
			for(Element i : temp){
				tracks.add(new Track(i, this, knownTriplets));
			}
		} catch (Exception ex){}
	}
	
	public String id(){
		return id;
	}
	public String title(){
		return title;
	}
	public String artist(){
		return artist;
	}
	public Integer trackCount(){
		return trackCount;
	}
	public Integer date(){
		return date;
	}
	public ArrayList<Triplet> artistOrigin(){
		return artistOrigin;
	}
	public ArrayList<Triplet> artistType(){
		return artistType;
	}
	public ArrayList<Triplet> artistEra(){
		return artistEra;
	}
	public ArrayList<Triplet> genre(){
		return genre;
	}
	public ArrayList<Track> tracks(){
		return tracks;
	}
	public HashMap<String, Triplet> knownTriplets(){
		return knownTriplets;
	}
	
	/**Updates fields in this Album with properties of the update album, preferring the update album's properties. Note that the track list won't be merged.
	 * 
	 * @param update The album to draw information from to update this one
	 */
	public void mergeAlbums(Album update){
		if(update == null) return;
		
		if(update.trackCount() != null) trackCount = update.trackCount();
		if(update.title() != null) title = update.title();
		if(update.artist() != null) artist = update.artist();
		if(update.id() != null) id = update.id();
		if(update.artistOrigin() != null) artistOrigin = update.artistOrigin();
		if(update.artistType() != null) artistType = update.artistType();
		if(update.artistEra() != null) artistEra = update.artistEra();
		if(update.date() != null) date = update.date();
		if(update.genre() != null) genre = update.genre();
		
		//note this doens't merge the albums' track lists
	}
	
	public String toString(){
		String result = "";
		result += String.format("Album:%n");
		if(title != null && !title.isEmpty()) result += String.format("Title: %s%n", title); //Title
		if(artist != null && !artist.isEmpty()) result += String.format("Artist: %s%n", artist); //Artist
		result += String.format("Track Count: %d%n", tracks.size()); //Track Count
		if(id != null && !id.isEmpty()) result += String.format("ID: %s%n", id); //ID
		if(date != null) result += String.format("Date: %d%n", date); //Date
		if(artistOrigin != null && artistOrigin.size() > 0){
			result += "Artist Origin:" + String.format("%n");
			for(Triplet i : artistOrigin){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(artistType != null && artistType.size() > 0){
			result += "Artist Type:" + String.format("%n");
			for(Triplet i : artistType){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(artistEra != null && artistEra.size() > 0){
			result += "Artist Era:" + String.format("%n");
			for(Triplet i : artistEra){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}
		if(genre != null && genre.size() > 0){
			result += "Genre:" + String.format("%n");
			for(Triplet i : genre){
				result += StringTools.indentString(String.format("%s (%s)", i.text(), i.toString()), "    ") + String.format("%n");
			}
		}

		result += String.format("Tracks:%n");
		for(Track i : tracks){
				result += StringTools.indentString(i.toString(), "    ") + String.format("%n");	
		}
		return result;
	}
}
