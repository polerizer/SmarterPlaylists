/**
 * Nov 10, 2013
 * MoodAxes.java
 * Daniel Pok
 * AP Java 6th
 */
package music;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xml.XMLTools;
import data.Coordinate;

/**The two mood axes are Energy and Positivity. [x: Calm --> Energetic] [y: Dark --> Positive]
 * @author Daniel
 *
 */
public class MoodAxes {

	public static HashMap<String,Coordinate> ids;

	private static void initialize(){
		Document moodXml = XMLTools.makeXML(new File("MoodGridMap.xml"));
		//System.out.println(moodXml + String.format("Size: %d nodes%n", moodXml.getDocumentElement().getChildNodes().getLength()));
		ids = new HashMap<String, Coordinate>();
		NodeList moodElements = moodXml.getElementsByTagName("ITEM");
		//System.out.println(moodElements.getLength());
		for(int i = 0; i < moodElements.getLength(); i++){
			try{
				if(moodElements.item(i) instanceof Element){
					Element mood = (Element) moodElements.item(i);
					//System.out.println("Casted to element!");
					//System.out.println(mood.getNodeName());
					String id = mood.getAttribute("ID");
					//System.out.println("ID = " + id);
					//System.out.println(mood.getFirstChild().getNextSibling());
					Element grid = (Element) mood.getFirstChild().getNextSibling();
					//System.out.println("Cased grid to element!");
					//System.out.println(grid.getNodeName());
					String type = grid.getAttribute("TYPE");
					//System.out.println("Type = " + type);
					Element x = (Element) grid.getFirstChild().getNextSibling();
					//System.out.println(x);
					Element y = (Element) grid.getLastChild().getPreviousSibling();
					//System.out.println(y);
					//System.out.println(x.getNodeName() + y.getNodeName());
					//System.out.println(x.getFirstChild().getNodeValue() + y.getFirstChild().getNodeValue());
					Coordinate coord = new Coordinate(Double.parseDouble(x.getFirstChild().getNodeValue()), Double.parseDouble(y.getFirstChild().getNodeValue()), type);
					//System.out.printf("%f %f %s", coord.x, coord.y, coord.axes);
					ids.put(id, coord);
					//System.out.print(ids);
				}
			} catch(Exception ex){
				ex.printStackTrace();
			}

		}
	}
	
	public static Coordinate getCoordinate(String id){
		if(ids == null){
			initialize();
		}
		return ids.get(id);
	}

}
