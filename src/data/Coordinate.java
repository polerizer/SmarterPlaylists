/**
 * Nov 10, 2013
 * Coordinate.java
 * Daniel Pok
 * AP Java 6th
 */
package data;

import java.io.Serializable;

/**
 * @author Daniel
 *
 */
public class Coordinate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7168300625728262059L;
	public double x;
	public double y;
	public String axes;
	
	public Coordinate(double x,double y, String axes){
		this.x = x;
		this.y = y;
		this.axes = axes;
	}
	
	public Coordinate(){
		x = 0;
		y= 0;
		axes = "";
	}
	
	//find the distance normalized to 10x10 coordinates
	public double distance(Coordinate destination){
		double x1, x2, y1, y2;
		if(axes.equals("5x5")){
			x1 = 2*x - 0.5;
			y1 = 2*y - 0.5;
		} else{
			x1 = x;
			y1 = y;
		}
		if(destination.axes.equals("5x5")){
			x2 = 2*destination.x - 0.5;
			y2 = 2*destination.y - 0.5;
		} else{
			x2 = destination.x;
			y2 = destination.y;
		}
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	public String toString(){
		return String.format("(%f,%f) %s", x, y, axes);
	}
}
