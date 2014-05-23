/**
 * Nov 10, 2013
 * TestPlaylists.java
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

import playlists.MakePlaylist;

import data.MusicLibrary;
import data.Track;

/**
 * @author Daniel
 *
 */
public class TestPlaylists {

	/** 46065291-37D924FED39030A60F811C74DB5B0A27 hey ya!
	 *  74344893-4A544F7B5E8923685E072304033DC704 danny boy
	 */
	
	static String //start_id = "160022621-B249E2706D1605D0A747216D126C497A"; //you'll always be my best friend - relient k
				  //start_id = "29249444-B042CCE55FB561BB78DD7BD219B7601C"; //she will be loved - maroon 5
				  //start_id = "4833958-4FF68C2FE83A5CEABFB679E319CFDEF7"; //Leaving Las Vegas - Various artists including nicholas cage
				  start_id = "68770882-1DD94DBB30089C7923412001941FE189"; //Since U Been Gone - Kelly Clarkson
	static String //end_id = "139368190-5C35658E01E51F3B0E7D0DF3C489DD5B"; //in the end - linkin park
				  //end_id = "17575191-AA19DDCB05D066A4ED587B09950583E9"; //smooth criminal - alien ant farm
				  //end_id = "290711702-B5B97C51FE8F54AF39E7981E48049826"; //Nothing Suits Me Like a Suit - Neil Patrick Harris and HIMYM Cast
				  end_id = "160232375-1F2A71773BC6315EB896A2B0504DB2CD"; //Lollipop - Lil Wayne
	
	public static void main(String[] args) {
		MusicLibrary lib;
		try {
			lib = MusicLibrary.openLibrary("library.txt");
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
			return;
		}
		ArrayList<Track> tracks = lib.tracks();
		Track start = null;
		Track end = null;
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		PrintStream cout = System.out;
		try {
			while(start == null){
				cout.print("Start ID: ");
				String input = cin.readLine();
				start = lib.find(input);
			}
			while(end == null){
				cout.print("End ID: ");
				String input = cin.readLine();
				end = lib.find(input);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf("Playlist from %n%s%n-----------------------------%n to: %n%s%n-----------------------------%n", start.toStringMedium(), end.toStringMedium());
		ArrayList<Track> playlist = MakePlaylist.makePlaylist(start, end, 25, tracks);
		for(Track i: playlist){
			//System.out.println(i.toStringWithMoreInfo());
			System.out.println(i.toStringMedium());
		}
		new PlaylistViewer(playlist);
	}

	public static Track findTrack(String id, ArrayList<Track> tracks){
		for(Track i: tracks){
			if(i.id().equals(id)){
				return i;
			}
		}
		return null;
	}
}
