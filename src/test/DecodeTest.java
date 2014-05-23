/**
 * Nov 26, 2013
 * DecodeTest.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author poler_000
 *
 */
public class DecodeTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		PrintStream cout = System.out;
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		
		String line = "";
		while(!line.equals("exit")){
			cout.print("Enter Text to Decode: ");
			line = cin.readLine();
			cout.println(decode(line));
		}

	}
	
	public static String decode(String str) throws UnsupportedEncodingException{
		return URLDecoder.decode(str, "UTF-8");
	}

}
