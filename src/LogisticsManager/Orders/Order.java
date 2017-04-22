package LogisticsManager.Orders;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import LogisticsManager.Facilities.FacilityManager;
import LogisticsManager.Inventory.Items;
import LogisticsManager.ProcessItems.OrderProcessor;


public class Order {
	private String orderId;
	private int orderTime;
	private String orderDestination;
	private int cost = 0;
	private HashMap<Items,Integer> order;
	private HashMap<Items,Integer> process;
	FacilityManager fmanager;
	OrderProcessor orderProcess;

	public Order(FacilityManager fmanager,String orderId, int orderTime, String orderDestination, HashMap<Items, Integer> order) {
		this.orderId = orderId;
		this.orderTime = orderTime;
		this.orderDestination = orderDestination;
		this.order = order;
		this.fmanager = fmanager;
		orderProcess = new OrderProcessor(fmanager,order,orderId,orderTime,orderDestination);
		process = orderProcess.Process();
		
		}
	
	public void printOrder(){
		DecimalFormat df = new DecimalFormat("#,###");
		System.out.println("Order Id:     "+orderId);
		System.out.println("Order Time:   Day "+orderTime);
		System.out.println("Destination:  "+orderDestination);
		System.out.println("List of Order Items: ");
		for (Entry<Items, Integer> entry : order.entrySet()){
			System.out.println("•  Item Id:"+new String(new char[10-entry.getKey().getId().length()]).replace("\0", " ")+entry.getKey().getId()+"     Quantity: "+entry.getValue());
			cost+=(entry.getKey().getCost()*entry.getValue());
		}
		System.out.println("\nProcessing Solution: ");
		System.out.println("Total Cost:   $"+df.format(cost));
		System.out.println("First Delivery Day: "+orderProcess.minFirst());
		System.out.println("Last Delivery Day: "+orderProcess.maxLast());
		System.out.println("Order Items: ");
		System.out.println("Item ID    Quantity    Cost    # Sources Used    First Day    Last Day");
		for (Entry<Items, Integer> entry : process.entrySet()){
			System.out.println(entry.getKey().getId()+"       "+order.get(entry.getKey()).intValue()+"      $"+df.format(entry.getKey().getCost()*(order.get(entry.getKey()).intValue()))+"       "+entry.getValue()+"              "+orderProcess.getFirstDay(entry.getKey())+"             "+orderProcess.getLastDay(entry.getKey()));
		}
		System.out.println("\n-----------------------------------------------------------------------------");
		
	}

}
