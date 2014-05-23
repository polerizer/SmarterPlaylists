/**
 * Nov 10, 2013
 * PlaylistViewer.java
 * Daniel Pok
 * AP Java 6th
 */
package gui;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import data.Track;

/**
 * @author Daniel
 *
 */
public class PlaylistViewer{

	private ArrayList<Track> playlist;
	private JFrame frame;
	private Container panel;
	
	public PlaylistViewer(ArrayList<Track> tracks){
		playlist = tracks;
		displayStuff();
	}
	
	private void displayStuff(){
        //Create and set up the window.
        frame = new JFrame("Playlist");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = frame.getContentPane();
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        panel.add(myPanel);
        for(Track i : playlist){
        	myPanel.add(new SongPanel(i));        	
        }
        frame.invalidate();

        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	
}
