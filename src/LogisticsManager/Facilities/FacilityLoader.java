package LogisticsManager.Facilities;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FacilityLoader {
	private FacilityManager manager = new FacilityManager();
	
	FacilityLoader(FacilityManager manager){
		this.manager = manager;
		Load();
	}
	public void Load(){
		try {
            String fileName = "Facilities.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList facilities = doc.getDocumentElement().getChildNodes();
            
            
            for (int i = 0; i < facilities.getLength(); i++) {
            	ArrayList<String[]> neighbors = new ArrayList<>();
                if (facilities.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                
                String entryName = facilities.item(i).getNodeName();
                if (!entryName.equals("facilities")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }
                
                // Get a node attribute
                NamedNodeMap aMap = facilities.item(i).getAttributes();
                

                // Get a named nodes
                Element elem = (Element) facilities.item(i);
                String facilityName = elem.getElementsByTagName("name").item(0).getTextContent();
                String facilityId = elem.getElementsByTagName("id").item(0).getTextContent();
                String facilityRate = elem.getElementsByTagName("rate").item(0).getTextContent();
                String facilityCost = elem.getElementsByTagName("cost").item(0).getTextContent();
                String[] info = new String[3];
                // Get all nodes named "Book" - there can be 0 or more
                
                NodeList neighborList = elem.getElementsByTagName("neighbors");
                for (int j = 0; j < neighborList.getLength(); j++) {
                	info = new String[3];
                    if (neighborList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = neighborList.item(j).getNodeName();
                    if (!entryName.equals("neighbors")) {
                        System.err.println("Unexpected node found: " + entryName);
                        return;
                    }

                    // Get some named nodes
                    elem = (Element) neighborList.item(j);
                    String neighborId = elem.getElementsByTagName("id").item(0).getTextContent();
                    String neighborName = elem.getElementsByTagName("name").item(0).getTextContent();
                    String neighborDistance = elem.getElementsByTagName("distance").item(0).getTextContent();
                    
                    info[0] = neighborId;
                    info[1] = neighborName;
                    info[2] = neighborDistance;
                    
                    // Create a string summary of the book
                    neighbors.add(info);
                }

                // Here I would create a Store object using the data I just loaded from the XML
                Facility facility = new Facility(facilityName,facilityId,facilityRate,facilityCost,neighbors);
                manager.addFacility(facility);
                
                
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
	}
	
}