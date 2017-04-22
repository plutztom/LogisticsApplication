package LogisticsManager.Facilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import LogisticsManager.Inventory.Items;

public class Facility {
	
	private  String name;
	private  String id;
	private  int rate;
	private  int cost;
	private  ArrayList<String[]> neighbors = new ArrayList<>();
	private HashMap<Items, Integer> Inventory = new HashMap<>();
	private LinkedHashMap<Integer,Integer> Schedule = new LinkedHashMap<>(); 
	
	Facility(String name, String id, String rate, String cost,ArrayList<String[]> neighbors){
		this.neighbors = neighbors;
		//System.out.print(this.neighbors.toString());
		this.id = id;
		this.cost = Integer.parseInt(cost);
		this.rate = Integer.parseInt(rate);
		this.name = name;
		addSchedule();
	}
	Facility(){
		
	}
	public int getQuantity(Items item){
		if (Inventory.containsKey(item)){
			return Inventory.get(item).intValue();
		}
		else{
			return 0;
		}
	}
	
	public int completeJob(Items item,int quantity, int startDate){
		int endDate = 0;
		for (Entry<Integer,Integer> entry : Schedule.entrySet()){
			if (entry.getKey() >= startDate){
				int total = entry.getValue();
				if (total <= quantity){
					Schedule.put(entry.getKey(), entry.getValue()-total);
					quantity -= total;
				}
				else{
					Schedule.put(entry.getKey(), 0);
					quantity = 0;
				}
				
			}
			if (quantity == 0){
				endDate = entry.getKey();
				break;
			}
			
		}
		return endDate;
	}
	
	public int requestItems(Items item, int quantity,int startDate) {
		
		int endDate = completeJob(item,quantity,startDate);
		Inventory.put(item, Inventory.get(item).intValue()-quantity);
		return endDate;
		
	}
	
	public void printFacility(){
		
		System.out.println(name);
		System.out.println(new String(new char[name.length()]).replace("\0", "-"));
		System.out.println("Rate per Day: "+rate);
		System.out.println("Cost per Day: "+cost+"\n");
		System.out.println("Direct Links: ");
		for (String[] info:neighbors){
			System.out.print(info[1]+" ("+(Double.parseDouble(info[2])/(8*50))+"d);  ");
		}
		System.out.println("\n\nActive Inventory:");
		System.out.println("  Item ID       Quantity");
		for (HashMap.Entry<Items, Integer> entry : Inventory.entrySet()) {
		    Items key = entry.getKey();
		    String name = key.getId();
		    Integer value = entry.getValue();
		    System.out.println("  "+name+new String(new char[14-name.length()]).replace("\0", " ")+value);
		}
		System.out.println("\nDepleted (Used-Up) Inventory: None \nSchedule: ");
		System.out.print("Day:         ");
		for (Entry<Integer, Integer> entry : Schedule.entrySet()){
			System.out.print(entry.getKey());
			if (entry.getKey().toString().length() == 1){
				System.out.print("    ");
			}
			else{
				System.out.print("   ");
			}
		}
		System.out.println();
		System.out.print("Available:   ");
		for (Entry<Integer, Integer> entry : Schedule.entrySet()){
			if (entry.getValue().toString().length() == 1){
				System.out.print(entry.getValue()+"    ");
			}
			else{
				System.out.print(entry.getValue()+"   ");
			}
			
		}
		System.out.println("\n");
		System.out.println("-------------------------------------------------------------------------");
		
	}

	public ArrayList<String[]> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<String[]> neighbors) {
		this.neighbors = neighbors;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	public int getCost(){
		return cost;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return Integer.parseInt(id);
	}
	public void addInventory(HashMap<Items, Integer> inventory2) {
		Inventory = inventory2;		
	}
	private void addSchedule(){
		//this is placeholder
		for (int i = 1; i < 31; i++){
			Schedule.put(i, 10);
		}
	}
	
}
