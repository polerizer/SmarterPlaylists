/**
 * Nov 10, 2013
 * QueryAndAddToLibrary.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import gui.PlaylistViewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import musicproviders.YouTubeProvider;
import musicproviders.YouTubeProvider.CredentialPack;

import org.w3c.dom.Document;

import playlists.MakePlaylist;

import data.MusicLibrary;
import data.Response;
import data.Track;
import data.Triplet;

import radams.gracenote.webapi.GracenoteException;
import radams.gracenote.webapi.GracenoteWebAPI;
import tools.TrackTools;

/**
 * @author Daniel
 *
 */
public class QueryAndAddToLibrary {

	/**
	 * @param args
	 */
	private static String clientID  = ""; // Put your clientID here.
	private static String clientTag = ""; // Put your clientTag here.
	private static String userID = ""; // api.register();
	private static MusicLibrary lib = new MusicLibrary();
	private static HashMap<String, Triplet> triplets = lib.triplets();
	private static String command = "";
	private static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	private static PrintStream cout = System.out;

	public static void main(String[] args)
	{

		GracenoteWebAPI api;
		try {
			api = new GracenoteWebAPI(clientID, clientTag, userID);
		} catch (GracenoteException e1) {
			e1.printStackTrace();
			return;
		}
		cout.println("Gracenote Web API Console Interface with Libary Storage");
		cout.println("-----------------------------------");
		cout.println("Supported Commands: ");
		cout.println("\t'search': search by artists, albumns, or track names");
		cout.println("\t'fetch': fetches a record by its Gracenote ID");
		cout.println("\t'exit': quits this shell");
		cout.println("\t'print': prints tracks in library");
		cout.println("\t'save'': saves the libary to a file");
		cout.println("\t'load': loads a library from a file");
		cout.println("\t'size': states the size of the library");
		cout.println("\t'find': locate a record in the library");
		cout.println("\t'delete': delete the track with the given ID from the library");
		cout.println("\t'ids': prints out the id/track pairs of all the tracks in the library");
		cout.println("\t'new': creates a new library, discarding the current one");
		cout.println("\t'playlist': makes a playlist");

		while(!command.equals("exit")){
			cout.print(">>> ");
			try
			{
				String raw_line = cin.readLine();
				String[] parameters = raw_line.split(" ");
				command = (parameters.length > 0 ? parameters[0] : "");
				if(command.equals("search")){
					cout.print("Artist: ");
					String artist = cin.readLine();
					cout.print("Album: ");
					String album = cin.readLine();
					cout.print("Track Name: ");
					String track = cin.readLine();
					cout.print("Want all the data? (y/n): ");
					boolean extended = cin.readLine().equals("y");
					cout.print("Want only the best result? (y/n): ");
					boolean best = cin.readLine().equals("y");

					String request = api.makeQuery(artist, album, track, extended, best);
					cout.println("Query: " + request);

					// Once you have the userID, you can search for tracks, artists or albums easily.
					System.out.println("Searching...");
					Document results = api.searchTrack(artist, album, track, extended, best);
					Response response = new Response(results, triplets);
					if(response.albums() != null){
						lib.addResponse(response);
						cout.println(response);	
					} else{
						cout.println("Found no albums");
					}


				} else if(command.equals("fetch")){
					String gn_id = null;
					if(parameters.length > 1){
						gn_id = parameters[1];
					} else{
						cout.print("ID: ");
						gn_id = cin.readLine();
					}
					cout.print("Want all the data? (y/n): ");
					boolean extended = cin.readLine().equals("y");
					// Once you have the userID, you can search for tracks, artists or albums easily.
					System.out.println("Fetching...");
					Document results = api.fetchAlbum(gn_id, extended, true);
					Response response = new Response(results, triplets);
					if(response.albums() != null){
						lib.addResponse(response);
						cout.println(response);	
					} else{
						cout.println("Found no albums");
					}
				} else if(command.equals("print")){
					System.out.println(lib);
				} else if(command.equals("ids")){
					System.out.println(lib.idsToString());
				} else if(command.equals("save")){
					String fileName = null;
					if(parameters.length > 1){
						fileName = parameters[1];
					} else{
						cout.print("Output file name: ");
						fileName = cin.readLine();
					}
					try
					{
						MusicLibrary.saveLibrary(fileName, lib);
						cout.println("File saved.");
					}catch(IOException i)
					{
						i.printStackTrace();
					}
				} else if(command.equals("load")){
					String fileName = null;
					if(parameters.length > 1){
						fileName = parameters[1];
					} else{
						cout.print("Input file name: ");
						fileName = cin.readLine();
					}
					try
					{
						lib = MusicLibrary.openLibrary(fileName);
						cout.println("File read.");
					}catch(IOException i)
					{
						i.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if(command.equals("size")){
					cout.printf("The library is %d tracks large.%n", lib.size());
				} else if(command.equals("find")){
					String id = null;
					Track result = null;
					if(parameters.length > 1){
						id = parameters[1];
						result = lib.find(id);
					} else{
						result = findTrack();
					}
					if(result != null){
						cout.println(result.toStringLong());
					}
				} else if(command.equals("delete")){
					String input = null;
					if(parameters.length > 1){
						input = parameters[1];
					} else{
						cout.print("ID: ");
						input = cin.readLine();
					}
					Track result = lib.delete(input);
					if(result == null){
						cout.println("No such entry found.");
					} else{
						cout.println("Deleted the following entry from the library: ");
						cout.println(result);
					}
				} else if(command.equals("new")){
					cout.print("Sure you want to discard the current library?(y/n): ");
					String input = cin.readLine();
					if(input.equals("y")){
						lib = new MusicLibrary();
						triplets = lib.triplets();
					} else{
						cout.println("Action canceled.");
					}
				} else if(command.equals("playlist")){
					Track start = null, end = null;
					cout.println("Find Start Track:");
					while(start == null){
						start = findTrack();
					}
					cout.printf("Selected %s%n", start.toStringShort());
					cout.println("Find End Track:");
					while(end == null){
						end = findTrack();
					}
					cout.printf("Selected %s%n%n", end.toStringShort());
					System.out.printf("Playlist from %n%s%n-----------------------------%n to: %n%s%n-----------------------------%n", start.toStringMedium(), end.toStringMedium());
					ArrayList<Track> playlist = MakePlaylist.makePlaylist(start, end, 25, lib.tracks());
					for(Track i: playlist){
						//System.out.println(i.toStringWithMoreInfo());
						System.out.println(i.toStringMedium());
					}
					new PlaylistViewer(playlist);
				} else if(command.equals("test-youtube")){
					try {
						Track start = null, end = null;
						cout.println("Find Start Track:");
						while(start == null){
							start = findTrack();
						}
						cout.printf("Selected %s%n", start.toStringShort());
						cout.println("Find End Track:");
						while(end == null){
							end = findTrack();
						}
						cout.printf("Selected %s%n%n", end.toStringShort());
						System.out.printf("Playlist from %n%s%n-----------------------------%n to: %n%s%n-----------------------------%n", start.toStringMedium(), end.toStringMedium());

						Integer num = null;
						while(num == null){
							try{
								cout.print("Number of Tracks: ");
								String line = cin.readLine();
								num = Integer.parseInt(line);
							} catch(Exception ex){
								num = null;
							}
						}

						ArrayList<Track> playlist = MakePlaylist.makePlaylist(start, end, num, lib.tracks());
						YouTubeProvider youtube = null;
						String client_secret;
						String client_id;
						cout.print("Load Credentials (y/n)? ");
						String line = cin.readLine();
						if(line.equals("y")){
							cout.print("File name: ");
							line = cin.readLine();
							try{
								CredentialPack credPack = YouTubeProvider.loadCredential(line);
								cout.println("Successfully loaded credentials.");
								client_id = credPack.client_id;
								client_secret = credPack.client_secret;
								youtube = new YouTubeProvider("SmarterPlaylists", client_id, client_secret, credPack);
							}catch(Exception ex){
								ex.printStackTrace();
								cout.println("Unable to load credentials.");
								continue;
							}
						} else{
							cout.print("Client ID: ");
							client_id = cin.readLine();
							cout.print("Client Secret: ");
							client_secret = cin.readLine();
							youtube = new YouTubeProvider("SmarterPlaylists", client_id, client_secret);
							cout.print("Save Credentials (y/n)? ");
							line = cin.readLine();
							if(line.equals("y")){
								cout.print("File name: ");
								line = cin.readLine();
								youtube.saveCredential(line);
								cout.println("Saved file.");
							}
						}

						//ArrayList<String> ids = youtube.getIds(playlist);
						//for(int i = 0;i < ids.size(); i++){
						//	System.out.println(playlist.get(i).toStringShort() + " (YT: " + ids.get(i) + ")");
						//}
						for(Track trk : playlist){
							cout.println(trk.toStringShort());
						}
						String playlistId = youtube.getProviderSidePlaylist(playlist, false, true);
						System.out.println("Playlist ID: " + playlistId);
						System.out.println("Playlist URL: " + "http://www.youtube.com/playlist?list=" + playlistId);
						new PlaylistViewer(playlist);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (GracenoteException ex){
				ex.printStackTrace();
			}
		}
	}

	private static Track findTrack(){
		try{
			Track track = null;
			cout.print("ID (* for all): ");
			String input = cin.readLine();
			if (input.equals("*")){
				return selectTrack(lib.tracks());
			}
			track = lib.find(input);
			if(track == null){
				cout.println("Can't find that ID. Try by search.");
				cout.print("Artist: ");
				String artist = cin.readLine();
				cout.print("Album: ");
				String album = cin.readLine();
				cout.print("Track Title: ");
				String title = cin.readLine();
				if(artist.isEmpty() && album.isEmpty() && title.isEmpty()){
					return null;
				}
				ArrayList<Track> candidates = TrackTools.findTracks(artist, album, title, lib.tracks());
				if(candidates == null || candidates.size() == 0){
					track = null;
					cout.println("Couldn't find any tracks matching that description.");
				} else if(candidates.size() == 1){
					track = candidates.get(0);
				} else{
					track = selectTrack(candidates);
				}
			}
			return track;
		} catch(Exception e){
			return null;
		}
	}
	private static Track selectTrack(ArrayList<Track> tracks){
		Track track = null;
		cout.println("Here are the tracks matching your search: ");
		for(int i = 0; i < tracks.size(); i++){
			Track j = tracks.get(i);
			cout.printf("Result #%d: %s%n", i, j.toStringShort());
		}
		cout.println();
		cout.print("Please select a track. Result number?: ");
		Integer resultNumber = null;
		try{
			resultNumber = Integer.parseInt(cin.readLine());
		} catch(Exception ex){}
		if(resultNumber == null || resultNumber >= tracks.size() || resultNumber < 0){
			cout.println("No track selected.");
		} else{
			track = tracks.get(resultNumber);
		}
		return track;
	}
}
