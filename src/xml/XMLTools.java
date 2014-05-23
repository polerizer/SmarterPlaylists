/**
 * Nov 10, 2013
 * XMLTools.java
 * Daniel Pok
 * AP Java 6th
 */
package xml;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * @author Daniel
 *
 */
public class XMLTools {

	public static Document makeXML(String text){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(text)));
			return doc;
		} catch (Exception ex){
			System.err.println("Failed to parse string as XML.");
			return null;
		}
	}
	
	public static Document makeXML(File file){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new FileReader(file)));
			return doc;
		} catch (Exception ex){
			System.err.println("Failed to parse file as XML.");
			return null;
		}
	}
	
	public static ArrayList<Element> getChildrenByTag(Element xml, String tag){
		ArrayList<Element> list = new ArrayList<Element>();
		if(!xml.hasChildNodes()) return list;
		Node child = xml.getFirstChild();
		while(child != null){
			if(child instanceof Element && ((Element) child).getTagName().equals(tag)){
				list.add((Element) child);
			}
			child = child.getNextSibling();			
		}

		return list;		
	}
}
