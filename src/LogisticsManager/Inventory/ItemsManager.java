package LogisticsManager.Inventory;

import java.util.ArrayList;

import LogisticsManager.Facilities.Facility;
import LogisticsManager.Facilities.ItemNotFoundException;

public class ItemsManager {
	ArrayList<Items> ItemList = new ArrayList<>();
	
	
	public void Load(){
		InventoryLoader load = new InventoryLoader(this);
	}
	
	public void addItem(Items item) {
		ItemList.add(item);
	}
	
	public void printItems(){
		System.out.println("\nItem Catalog: ");
		System.out.println("Item Id"+new String(new char[4]).replace("\0", " ")+"Cost");
		for (Items item: ItemList){
			System.out.println(item.getId()+" :"+new String(new char[9-item.getId().length()]).replace("\0", " ")+"$"+item.getCost());
		}
		System.out.println();
	}

	public Items get(String itemId) throws ItemNotFoundException {
		for (Items temp : ItemList){
				if (temp.getId().equals(itemId)){
					return temp;
				}
			}
			throw new ItemNotFoundException();
	}

}
