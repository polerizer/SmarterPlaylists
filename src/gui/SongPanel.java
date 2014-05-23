/**
 * Nov 10, 2013
 * SongPanel.java
 * Daniel Pok
 * AP Java 6th
 */
package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import data.Track;

/**
 * @author Daniel
 *
 */
public class SongPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7151758910576135069L;

	/**
	 * 
	 */
	public SongPanel(Track track) {
		super();
		String text = "";
		text += String.format("%s | ", track.title());
		if(track.artist() == null || track.artist().isEmpty()){
			text += String.format("By: %s", track.album().artist());
		} else{
			text += String.format("By: %s", track.artist());			
		}
		text += String.format(" | On: %s", (track.album() != null? track.album().title() : ""));
		this.add(new JLabel(text));
		this.invalidate();
	}
}
