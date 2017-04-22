package LogisticsManager.Inventory;

public class Items {
	private String id;
	private int cost;
	
	Items(String id,int cost){
		this.id = id;
		this.cost = cost;
	}
	public int getCost(){
		return cost;
	}
	public String getId(){
		return id;
	}
}
