package LogisticsManager.Orders;

import java.io.File;
import java.io.IOException;
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
import LogisticsManager.Inventory.Items;
import LogisticsManager.Inventory.ItemsManager;

public class OrderLoader {
	private OrderManager omanager;
	private ItemsManager imanager;
	private FacilityManager fmanager;
    
    OrderLoader(FacilityManager fmanager,OrderManager omanager,ItemsManager imanager){
    	this.omanager = omanager;
    	this.imanager = imanager;
    	this.fmanager = fmanager;
    	Load();
    }
	
	public void Load(){
    	try {
            String fileName = "order.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();
            HashMap<Items, Integer> order = new HashMap<>();
            NodeList items = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
            	order = new HashMap<>();
                if (items.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                
                String entryName = items.item(i).getNodeName();
                if (!entryName.equals("orders")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }
                
                // Get a node attribute
                NamedNodeMap aMap = items.item(i).getAttributes();
                

                // Get a named nodes
                Element elem = (Element) items.item(i);
                String OrderId = elem.getElementsByTagName("id").item(0).getTextContent();
                int OrderTime = Integer.parseInt(elem.getElementsByTagName("time").item(0).getTextContent());
                String OrderDestination = elem.getElementsByTagName("destination").item(0).getTextContent();
                // Get all nodes named "Book" - there can be 0 or more
                NodeList ItemsOrdered = elem.getElementsByTagName("items");
                for (int j = 0; j < ItemsOrdered.getLength(); j++) {
                	
                    if (ItemsOrdered.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = ItemsOrdered.item(j).getNodeName();
                    if (!entryName.equals("items")) {
                        System.err.println("Unexpected node found: " + entryName);
                        return;
                    }

                    // Get some named nodes
                    elem = (Element) ItemsOrdered.item(j);
                    String ItemId = elem.getElementsByTagName("id").item(0).getTextContent();
                    int quantity = Integer.parseInt(elem.getElementsByTagName("quantity").item(0).getTextContent());
                    Items item = imanager.get(ItemId);
                    
                    
                    
                    // Create a string summary of the book
                    order.put(item,quantity);
                }
                // Here I would create a Store object using the data I just loaded from the XML
                Order OrderPlaced = new Order(fmanager,OrderId,OrderTime,OrderDestination,order);
                
                omanager.addItem(OrderPlaced);
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException | ItemNotFoundException e) {
            e.printStackTrace();
        }
	}
}
