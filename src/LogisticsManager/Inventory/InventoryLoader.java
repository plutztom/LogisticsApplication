package LogisticsManager.Inventory;

import java.io.File;
import java.io.IOException;
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

public class InventoryLoader {
    private ItemsManager manager = new ItemsManager();
    
    InventoryLoader(ItemsManager manager){
    	this.manager = manager;
    	Load();
    }
    public void Load(){
    	try {
            String fileName = "items.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();
            
            NodeList items = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                if (items.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                
                String entryName = items.item(i).getNodeName();
                if (!entryName.equals("items")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }
                
                // Get a node attribute
                NamedNodeMap aMap = items.item(i).getAttributes();
                

                // Get a named nodes
                Element elem = (Element) items.item(i);
                String ItemId = elem.getElementsByTagName("id").item(0).getTextContent();
                String ItemCost = elem.getElementsByTagName("price").item(0).getTextContent();
                // Get all nodes named "Book" - there can be 0 or more

                // Here I would create a Store object using the data I just loaded from the XML
                Items Item = new Items(ItemId,Integer.parseInt(ItemCost));
                manager.addItem(Item);
                
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
	}
}
