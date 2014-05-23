/**
 * Nov 9, 2013
 * TCPConnection.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Daniel
 *
 */
public class TCPConnection {

	//Instance Variables
	private String hostName; //the host name this socket is connected to
	private int portNumber; //the port on the host that this socket is connected to
	private Socket mSocket; //the socket object
	private PrintWriter sOut; //the printwriter that writes to the socket
	private BufferedReader sIn; //the bufferedreader that reads from the socket
	private boolean ready; //whether the socket is properly bound to the host and port and both streams are opened
	
	public TCPConnection(String host, int port){
		hostName = host;
		portNumber = port;
		ready = false;
		
		try{
			mSocket = new Socket(hostName, portNumber);
			sOut = new PrintWriter(mSocket.getOutputStream(), true);
			sIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			ready = mSocket.isBound() && mSocket.isConnected() &&  !mSocket.isClosed();
		} catch(UnknownHostException ex){
			quietClose(mSocket);
			System.err.println(String.format("Unable to connect to %s on port %d.", hostName, portNumber));
		} catch(IOException ex){
			quietClose(mSocket);
			quietClose(sOut);
			quietClose(sIn);
			System.err.println("Failed to connect and establish streams.");
		}
	}
	
	//closes a stream if it's not null
	private void quietClose(Closeable obj){
		try{
			if(obj != null){
				obj.close();
			}
		} catch(Exception ex){
			//do nothing
		}
	}
	
	//returns whether the socket is ready
	public boolean isReady(){
		ready = mSocket != null && mSocket.isBound() && mSocket.isConnected() && !mSocket.isClosed();
		return ready;
	}
	
	public PrintWriter getOutputStream(){
		return sOut;
	}
	
	public BufferedReader getInputStream(){
		return sIn;
	}
	
	//reads a line from the stream if it's available, or null if not
	public String readLine(){
		try {
			if(ready && sIn != null && sIn.ready()){
				try {
					return sIn.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Stream had an error.");
					return "";
				}
			} else{
				return null;
			}
		} catch (IOException e) {
			System.err.println("Stream was not ready.");
			return null;
		}
	}
	
	//writes to a the socket
	public void println(String contents){
		if(ready && sOut != null){
			sOut.println(contents);
		} else{
			return;
		}
	}
	
	public void print(String contents){
		if(ready && sOut != null){
			sOut.print(contents);
		} else{
			return;
		}
	}
	
	public void printf(String format, Object... args){
		if(ready && sOut != null){
			sOut.printf(format, args);
		} else{
			return;
		}
	}
	
	public void close(){
		quietClose(sIn);
		quietClose(sOut);
		quietClose(mSocket);
	}
}
