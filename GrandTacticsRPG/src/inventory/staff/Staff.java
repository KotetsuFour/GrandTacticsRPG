package inventory.staff;

import inventory.item.ManufacturableItem;

public abstract class Staff extends ManufacturableItem {

	//minRange should always be 1
	private int maxRange;
	
	public static final int INJURY = 0;
	public static final int HEALING = 1;
	public static final int POISON = 2;
	public static final int SLEEP = 3;
	public static final int BERSERK = 4;
	
	
	public Staff(String name, int initialUses, int approximateWorth, int weight, int range, int[][] recipe) {
		super(name, initialUses, approximateWorth, weight, recipe);
		// TODO Auto-generated constructor stub
	}

	public int maxRange() {
		return maxRange;
	}

	@Override
	public String[] getInformationDisplayArray(int[] itemArray) {
		// TODO Auto-generated method stub
		return null;
	}

}
