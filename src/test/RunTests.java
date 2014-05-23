/**
 * Nov 9, 2013
 * RunTests.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author Daniel
 *
 */
public class RunTests {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		PrintStream cout = System.out;
		cout.print("Host: ");
		String host = cin.readLine();
		cout.print("Port: ");
		int port = Integer.parseInt(cin.readLine());
		cout.println("Attempting to bind to " + host + " on port " + port);
		TCPConnection socket = new TCPConnection(host, port);
		BufferedReader sin = socket.getInputStream();
		PrintWriter sout = socket.getOutputStream();
		String input = "";
		boolean client = true;
		cout.println("Beginning with client side: ");
		while(socket.isReady()){
			if(client){
				cout.print("Client> ");
				input = cin.readLine();
				if(input.length() <= 0){
					client = false;
					sout.println();
				} else{
					sout.println(input);
				}
			} else{
				input = sin.readLine();
				if(!sin.ready()){
					client = true;
				} else{
					cout.print("Server> ");
					cout.println(input);
				}
			}
		}
		socket.close();
	}

}
