package inventory.staff;

import inventory.InventoryIndex;

public class StationaryStaff extends Staff {

	public StationaryStaff(String name, int initialUses, int approximateWorth, int weight,
			int range, int[][] recipe) {
		super(name, initialUses, approximateWorth, weight, range, recipe);
		// TODO Auto-generated constructor stub
	}

	public void effect() {
		//TODO
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.STATIONARY_STAFF;
	}
}
