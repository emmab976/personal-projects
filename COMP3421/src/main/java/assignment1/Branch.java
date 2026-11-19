package assignment1;

public class Branch {
	//handles the inventory of each branch
	
	private String name;
	private int onHand;
	
	public Branch(String name, int onHand) {
		this.name = name;
		this.onHand = onHand;
	}
	
	public int getQuantity() {
		return onHand;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name + "   " + onHand;
	}

}
