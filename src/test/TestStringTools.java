/**
 * Nov 10, 2013
 * TestStringTools.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import tools.StringTools;

/**
 * @author Daniel
 *
 */
public class TestStringTools {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String test = "It is now morning\nBut I left so much undone\nThat I will not sleep.";
		String indent = "|----->";
		System.out.println(StringTools.indentString(test, indent));
	}

}
