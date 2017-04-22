package LogisticsManager.Facilities;

import java.util.ArrayList;
import java.util.HashMap;

import LogisticsManager.Inventory.Items;

public class FacilityManager {
	private ArrayList<Facility> facilityList = new ArrayList<Facility>();
	private Graph g = new Graph();
	
	
	public void Load(){
		FacilityLoader load = new FacilityLoader(this);
	}
	
	
	public void addFacility(Facility facility){
		facilityList.add(facility);
		g.getVertex(facility.getName());
		for (String[] neighbors : facility.getNeighbors()){
			g.addEdge(facility.getName(),neighbors[1],Double.parseDouble(neighbors[2]));
		}
	}
	
	public void printShortestPath(Facility start, Facility end){
		System.out.println(start.getName()+" ---> "+end.getName());
		System.out.print("•  ");
		g.dijkstra(start.getName());
		g.printPath(end.getName());
		System.out.println("\n");
	}
	public double getShortestPath(Facility start, Facility end){
		g.dijkstra(start.getName());
		double dist = g.distPath(end.getName());
		return dist;
	}
	public Facility get(String name) throws ItemNotFoundException{
		for (Facility temp : facilityList){
			if (temp.getName().equals(name)){
				return temp;
			}
		}
		throw new ItemNotFoundException();
	}
	public void printFacilityList(){
		for (Facility facility: facilityList){
			facility.printFacility();
		}
	}
	public ArrayList<Facility> getList(){
		ArrayList<Facility> temp = facilityList;
		return temp;
	}

	public void addItem(String name, HashMap<Items, Integer> inventory) {
		try {
			Facility facility = get(name);
			facility.addInventory(inventory);
		} catch (ItemNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
