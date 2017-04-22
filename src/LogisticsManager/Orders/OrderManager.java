package LogisticsManager.Orders;

import java.util.ArrayList;

import LogisticsManager.Facilities.FacilityManager;
import LogisticsManager.Inventory.ItemsManager;


public class OrderManager {
	private ArrayList<Order> currentOrders = new ArrayList<>();
	private ItemsManager imanager;
	private FacilityManager fmanager;
	
	
	public OrderManager(ItemsManager imanager,FacilityManager fmanager){
		this.imanager = imanager;
		this.fmanager = fmanager;
	}
	public void Load(){
		OrderLoader load = new OrderLoader(fmanager, this,imanager);
	}
	
	public void addItem(Order orderPlaced) {
		currentOrders.add(orderPlaced);
		
	}

	public void printOrders() {
		for (Order order: currentOrders){
			order.printOrder();
		}
		
	}

}
