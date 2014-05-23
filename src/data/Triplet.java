/**
 * Nov 9, 2013
 * Triplet.java
 * Daniel Pok
 * AP Java 6th
 */
package data;

import java.io.Serializable;

import org.w3c.dom.Element;

/**
 * @author Daniel
 *
 */
public class Triplet implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1867510177978584593L;
	//member variables, subscribing classes can only read
	private int ord;
	private String id;
	private String text;
	private AddressObject address;
	
	public Triplet(int ord, String id, String text){
		this.ord = ord;
		this.id = id;
		this.text = text;
		address = new AddressObject();
	}
	
	public Triplet(Element element) throws Exception{
		String order = element.getAttribute("ORD");
		String ID = element.getAttribute("ID");
		String elementText = element.getFirstChild().getNodeValue();
		ord = Integer.parseInt(order);
		id = ID;
		text = elementText;
		address = new AddressObject();
	}
	
	public int ord(){
		return ord;
	}
	
	public String id(){
		return id;
	}
	
	public String text(){
		return text;
	}
	
	public Object address(){
		return address;
	}
	
	public String toString(){
		return String.format("{ORD: %d, ID: %s, Text: %s}", ord, id, text);
	}
}
