package LogisticsManager.FacilityInventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import LogisticsManager.Facilities.FacilityManager;
import LogisticsManager.Facilities.ItemNotFoundException;
import LogisticsManager.Inventory.ItemsManager;
import LogisticsManager.Inventory.Items;

public class FacilityInventoryLoader {
	FacilityManager fmanager = new FacilityManager();
	ItemsManager imanager = new ItemsManager();
	
	public FacilityInventoryLoader(FacilityManager fmanager,ItemsManager imanager){
		this.imanager = imanager;
		this.fmanager = fmanager;
		Load();
	}
	
	public void Load(){
		try {
            String fileName = "facilityinventory.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList facilityinventory = doc.getDocumentElement().getChildNodes();
            
            String itemId = "";
            for (int i = 0; i < facilityinventory.getLength(); i++) {
            	HashMap<Items,Integer> inventory = new HashMap<>();
                if (facilityinventory.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                
                String entryName = facilityinventory.item(i).getNodeName();
                if (!entryName.equals("inventories")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }
                
                NamedNodeMap aMap = facilityinventory.item(i).getAttributes();
                

                // Get a named nodes
                Element elem = (Element) facilityinventory.item(i);
                String Name = elem.getElementsByTagName("name").item(0).getTextContent();
                String[] info = new String[3];
                
                NodeList itemsList = elem.getElementsByTagName("items");
                for (int j = 0; j < itemsList.getLength(); j++) {
                	info = new String[3];
                    if (itemsList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = itemsList.item(j).getNodeName();
                    if (!entryName.equals("items")) {
                        System.err.println("Unexpected node found: " + entryName);
                        return;
                    }

                    // Get some named nodes
                    elem = (Element) itemsList.item(j);
                    itemId = elem.getElementsByTagName("id").item(0).getTextContent();
                    int quantity = Integer.parseInt(elem.getElementsByTagName("quantity").item(0).getTextContent());
                    Items item = imanager.get(itemId);
                    // Create a string summary of the book
                    inventory.put(item,quantity);
                }

                
                fmanager.addItem(Name,inventory);
                
                
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | ItemNotFoundException e) {
            e.printStackTrace();
        }
	}
}
