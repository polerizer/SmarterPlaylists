/**
 * Nov 10, 2013
 * MoodFunction.java
 * Daniel Pok
 * AP Java 6th
 */
package playlists;

import data.Coordinate;

/**
 * @author Daniel
 *
 */
public class MoodFunction {

	/**
	 * 
	 * @param start mood
	 * @param end mood
	 * @param num current position on curve (i.e. which number point)
	 * @param total total points including start and end
	 * @return
	 */
	public static Coordinate getCoordinate(Coordinate start, Coordinate end, int num, int total){
		//linear curve
		double dx = end.x - start.x, dy = end.y - start.y;
		return new Coordinate(start.x  + dx * num / total, start.y + dy * num / total, "10x10");
	}
	
}
