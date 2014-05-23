/**
 * Nov 10, 2013
 * StringTools.java
 * Daniel Pok
 * AP Java 6th
 */
package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author Daniel
 *
 */
public class StringTools {

	/**Takes an input string and appends indent before every line of the string.
	 * 
	 * @param input
	 * @param indent
	 * @return
	 */
	public static String indentString(String input, String indent){
		String result = "";
		BufferedReader sRead = new BufferedReader(new StringReader(input));
		try {
			result += String.format("%s%s", indent, sRead.readLine());
			while(sRead.ready()){
				String temp = sRead.readLine();
				if(temp == null) break;
				result += String.format("%n%s%s", indent, temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
