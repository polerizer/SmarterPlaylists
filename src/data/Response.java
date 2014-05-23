/**
 * Nov 10, 2013
 * Response.java
 * Daniel Pok
 * AP Java 6th
 */
package data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import tools.StringTools;
import xml.XMLTools;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Daniel
 *
 */
public class Response implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -87915993850840076L;
	//instance variables
	private String status;
	private Document xml;
	private ArrayList<Album> albums;
	private HashMap<String, Triplet> knownTriplets;
	
	public Response(Document xml, HashMap<String, Triplet> knowns){
		this.xml = xml;
		knownTriplets = knowns;
		if(xml == null){
			status = "ERROR";
			System.err.println("Invalid XML.");
		} else{
			Element root = xml.getDocumentElement();
			ArrayList<Element> child = XMLTools.getChildrenByTag(root,  "RESPONSE");
			if(child.size() < 1){
				System.err.println("Failed to find any matching nodes.");
				status = "ERROR";
			} else{
				//takes the first RESPONSE tag
				status = child.get(0).getAttribute("STATUS");
				createAlbums(child.get(0));
			}
		}
	}
	
	private void createAlbums(Element response){
		ArrayList<Element> albumElements = XMLTools.getChildrenByTag(response, "ALBUM");
		albums = new ArrayList<Album>();
		for(Element i: albumElements){
			albums.add(new Album(i, knownTriplets));
		}
	}
	
	public String status(){
		return status;
	}
	
	public Document xml(){
		return xml;
	}
	
	public ArrayList<Album> albums(){
		return albums;
	}
	
	public HashMap<String, Triplet> knownTriplets(){
		return knownTriplets;
	}
	
	public String toString(){
		String result = "";
		result += String.format("Status: %s%n", status);
		result += String.format("Album Count: %d%n", albums.size());
		for(Album i : albums){
				result += StringTools.indentString(i.toString(), "    ") + String.format("%n");	
		}
		result += knownTriplets.toString();
		return result;
	}
}
