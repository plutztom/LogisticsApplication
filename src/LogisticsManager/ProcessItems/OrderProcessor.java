package LogisticsManager.ProcessItems;


import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.io.PrintWriter;

import LogisticsManager.Inventory.Items;
import LogisticsManager.Facilities.*;

public class OrderProcessor {
	private HashMap<Items,Integer> items;
	private HashMap<Items,Integer> info = new HashMap<>();
	private String orderId;
	private int orderTime;
	private HashMap<Items,Integer> lastDay = new HashMap<>();
	private HashMap<Items,Integer> firstDay = new HashMap<>();
	private String orderDestination;
	private FacilityManager fmanager;
	private int totalFacilities = 0;
	ArrayList<Integer> possibleEndDates = new ArrayList<>();
	ArrayList<Integer> possibleStartDates = new ArrayList<>();
	PrintWriter writer;
	
	
	public OrderProcessor(FacilityManager fmanager,HashMap<Items,Integer> items,String orderId,int orderTime, String orderDestination){
		this.fmanager = fmanager;
		this.items = items;
		this.orderId = orderId;
		this.orderTime = orderTime;
		this.orderDestination = orderDestination;
		
		
	}
	public HashMap<Items,Integer> Process(){
		try{
		    writer = new PrintWriter(orderId+".txt", "UTF-8");
		} catch (Exception e) {
		   e.printStackTrace();
		}
		for (Entry<Items,Integer> entry : items.entrySet()){
			info.put(entry.getKey(), findFacility(entry.getKey(),entry.getValue()));
		}
		writer.close();
		return info;
	}
	
	private int findFacility(Items item,int quantity){
		writer.println("\n\nItem ID: "+item.getId()+", "+"Quantity: "+quantity);
		writer.println("Logistics Details:");
		HashMap<Facility,Double> viableOptions = new HashMap<>();
		int stillNeed = quantity;
		ArrayList<Facility> flist = fmanager.getList();
		for (Facility facility:flist){
			if (facility.getQuantity(item) > 0){
				try {
					double dist = fmanager.getShortestPath(facility,fmanager.get(orderDestination));
					viableOptions.put(facility, dist);
					
				} catch (ItemNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		while (stillNeed > 0){
			int req = 0;
			Facility key = Collections.max(viableOptions.entrySet(), (entry1, entry2) -> (((int)entry1.getValue().doubleValue()) - ((int)entry2.getValue().doubleValue()))).getKey();
			if (stillNeed < key.getQuantity(item)){
				req = stillNeed;
			}
			else{
				req = key.getQuantity(item);
			}
			writer.println("\n\nName: "+key.getName()+" ("+req+" of "+quantity+")");
			stillNeed -= key.getQuantity(item);
			int end = key.requestItems(item,req,orderTime);
			possibleEndDates.add(end);
			try {
				possibleStartDates.add((int)fmanager.getShortestPath(key, fmanager.get(orderDestination)));
			} catch (ItemNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			totalFacilities++;
			viableOptions.remove(key);
			writer.println("   Processing Start: Day "+orderTime);
			writer.println("   Processing End: Day "+end);
			try {
				writer.println("   Travel Start: Day "+(orderTime+(int)fmanager.getShortestPath(key, fmanager.get(orderDestination))/(8*50)));
			} catch (ItemNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				writer.println("   Travel End: Day "+(end+((int)fmanager.getShortestPath(key, fmanager.get(orderDestination))/(8*50))));
			} catch (ItemNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lastDay.put(item,Collections.max(possibleEndDates));
		firstDay.put(item, Collections.min(possibleStartDates));
		possibleEndDates.clear();
		possibleStartDates.clear();
		
		
		return totalFacilities;
	}
	public int getLastDay(Items item){
		return lastDay.get(item);
	}
	public int maxLast(){
		return Collections.max(lastDay.values());
	}
	public int minFirst(){
		int min = Collections.min(firstDay.values());
		min = min/(8*50);
		min += orderTime;
		return min;
	}
	public int getFirstDay(Items item){
		return orderTime + (firstDay.get(item))/(8*50);
		
	}
}
