package LogisticsManager;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import LogisticsManager.Facilities.Facility;
import LogisticsManager.Facilities.FacilityManager;
import LogisticsManager.Facilities.ItemNotFoundException;
import LogisticsManager.FacilityInventory.FacilityInventoryLoader;
import LogisticsManager.Inventory.Items;
import LogisticsManager.Inventory.ItemsManager;
import LogisticsManager.Orders.OrderManager;

public class LogisticsManager {
	static LinkedHashMap<String,String> shortestPath = new LinkedHashMap<>();
	
	public static void main(String[] args) throws ItemNotFoundException{
		shortestPath.put("Santa Fe, NM","Chicago, IL");
		shortestPath.put("Atlanta, GA", "St. Louis, MO");
		shortestPath.put("Seattle, WA", "Nashville, TN");
		shortestPath.put("New York City, NY","Phoenix, AZ");
		shortestPath.put("Fargo, ND","Austin, TX");
		shortestPath.put("Denver, CO","Miami, FL");
		shortestPath.put("Austin, TX","Norfolk, VA");
		shortestPath.put("Miami, FL","Seattle, WA");
		shortestPath.put("Los Angeles, CA","Chicago, IL");
		shortestPath.put("Detroit, MI","Nashville, TN");
		
		FacilityManager fmanager = new FacilityManager();
		fmanager.Load();
		ItemsManager imanager = new ItemsManager();
		imanager.Load();
		FacilityInventoryLoader fimanager = new FacilityInventoryLoader(fmanager,imanager);
		
		for (Entry<String, String> entry : shortestPath.entrySet()){
			fmanager.printShortestPath(fmanager.get(entry.getKey()),fmanager.get(entry.getValue()));
		}
		OrderManager omanager= new OrderManager(imanager,fmanager);
		omanager.Load();
		omanager.printOrders();
		fmanager.printFacilityList();
		imanager.printItems();
		
		
	}
}
