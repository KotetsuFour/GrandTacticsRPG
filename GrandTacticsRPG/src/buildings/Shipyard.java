package buildings;

import data_structures.List;
import inventory.InventoryIndex;
import inventory.item.UsableCrop;
import unit.human.Human;

public class Shipyard extends Building {

	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Shipyard(String name, Human owner) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getType() {
		return Building.SHIPYARD;
	}
	
	@Override
	public void completeDailyAction() {
		restockInventory();
		// TODO Auto-generated method stub
	}

	@Override
	public void completeMonthlyAction() {
		restockInventory();
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		int type = goods[0];
		return type == InventoryIndex.RESOURCE
				|| type == InventoryIndex.STATIONARY_WEAPON
				|| (type == InventoryIndex.USABLECROP && ((UsableCrop)InventoryIndex.getElement(goods)).isUsedInBuilding());
	}

	@Override
	public List<int[]> getStorehouseNeeds() {
		// TODO Auto-generated method stub
		return null;
	}

}
