/**
 * Nov 9, 2013
 * XMLTest.java
 * Daniel Pok
 * AP Java 6th
 */
package test;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import data.Response;

import xml.XMLTools;
import data.Triplet;

/**
 * @author Daniel
 *
 */
public class XMLTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		PrintStream cout = System.out;
		
		try
		{
			// Get and parse into a document
			//DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = XMLTools.makeXML(new File("test.xml"));//db.parse(new InputSource(new FileReader(new File("test.xml"))));
			Response resp = new Response(doc, new HashMap<String, Triplet>());
			cout.println(resp);
			//cout.println(resp.albums());
			//cout.println(resp.knownTriplets());
//			NodeList nl = doc.getDocumentElement().getChildNodes();
//			cout.println(nl.getLength());
//			for(int i = 0; i < nl.getLength(); i++){
//				cout.println(nl.item(i).getFirstChild().getNodeValue());
//			}
//			for(Element i: getChildrenByTag((Element) doc.getDocumentElement().getElementsByTagName("RESPONSE").item(0), "")){
//				cout.printf("%s: %s%n", i.getTagName(), i.getFirstChild().getNodeValue());
//			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
