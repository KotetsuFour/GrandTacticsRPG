package inventory.staff;

import inventory.InventoryIndex;
import unit.Unit;

public class SupportStaff extends Staff {

	public SupportStaff(String name, int initialUses, int approximateWorth, int weight,
			int range, int[][] recipe) {
		super(name, initialUses, approximateWorth, weight, range, recipe);
		// TODO Auto-generated constructor stub
	}

	public void effect(Unit u) {
		//TODO
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.SUPPORT_STAFF;
	}
}
