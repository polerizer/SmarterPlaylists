/**
 * Nov 10, 2013
 * TestMoodAxes.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import data.Coordinate;
import music.MoodAxes;

/**
 * @author Daniel
 *
 */
public class TestMoodAxes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Coordinate point = MoodAxes.getCoordinate("65356"), point2 = MoodAxes.getCoordinate("43030");
		System.out.println(MoodAxes.ids);
		System.out.println(point);
		System.out.printf("X: %f Y: %f Axes: %s%n", point.x, point.y, point.axes);
		System.out.printf("Distance is: %f", point.distance(point2));
		
	}

}
