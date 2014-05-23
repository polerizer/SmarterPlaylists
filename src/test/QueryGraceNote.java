/**
 * Nov 9, 2013
 * QueryGraceNote.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;

import org.w3c.dom.Document;

import data.Response;
import data.Triplet;

import radams.gracenote.webapi.GracenoteException;
import radams.gracenote.webapi.GracenoteWebAPI;

/**
 * @author Daniel
 *
 */
public class QueryGraceNote {

	/**
	 * @param args
	 */
	private static String clientID  = ""; // Put your clientID here.
	private static String clientTag = ""; // Put your clientTag here.
	private static String userID = ""; // api.register();
	public static void main(String[] args)
	{
		String command = "";
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		PrintStream cout = System.out;

		GracenoteWebAPI api;
		try {
			api = new GracenoteWebAPI(clientID, clientTag, userID);
		} catch (GracenoteException e1) {
			e1.printStackTrace();
			return;
		}
		cout.println("Gracenote Web API Console Interface");
		cout.println("-----------------------------------");
		cout.println("Supported Commands: ");
		cout.println("\t'search': search by artists, albumns, or track names");
		cout.println("\t'fetch': fetches a record by its Gracenote ID");
		cout.println("\t'exit': quits this shell");

		while(!command.equals("exit")){
			cout.print(">>> ");
			try
			{
				command = cin.readLine();

				if(command.equals("search")){
					cout.print("Artist: ");
					String artist = cin.readLine();
					cout.print("Albumn: ");
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
					Response response = new Response(results, new HashMap<String, Triplet>());
					cout.println(response);

				} else if(command.equals("fetch")){
					cout.print("ID: ");
					String gn_id = cin.readLine();
					cout.print("Want all the data? (y/n): ");
					boolean extended = cin.readLine().equals("y");
					// Once you have the userID, you can search for tracks, artists or albums easily.
					System.out.println("Fetching...");
					Document results = api.fetchAlbum(gn_id, extended, true);
					Response response = new Response(results, new HashMap<String, Triplet>());
					cout.println(response);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GracenoteException ex){
				ex.printStackTrace();
			}
		}


	}
}
