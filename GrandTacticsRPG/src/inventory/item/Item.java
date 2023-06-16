package inventory.item;

public abstract class Item {

	protected String name;
	protected int initialUses;
	protected int approximateWorth;
	protected int specificItemId;
	protected int weight;
	
	public Item(String name, int initialUses, int approximateWorth, int weight) {
		this.name = name;
		this.initialUses = initialUses;
		this.approximateWorth = approximateWorth;
		this.weight = weight;
	}
	
	public String getName() {
		return name;
	}
	public int getInitialUses() {
		return initialUses;
	}
	public int getApproximateWorth() {
		return approximateWorth;
	}
	public int getSpecificItemId() {
		return specificItemId;
	}
	public int getWeight() {
		return weight;
	}
	public void setSpecificItemId(int id) {
		this.specificItemId = id;
	}
	
	public abstract int getGeneralItemId();
	
	@Override
	public String toString() {
		return getName();
	}

	public abstract String[] getInformationDisplayArray(int[] itemArray);
}
