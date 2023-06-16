package inventory.item;

import inventory.InventoryIndex;

public class UsableItem extends ManufacturableItem {

	public UsableItem(String name, int initialUses, int approximateWorth, int weight, int[][] recipe) {
		super(name, initialUses, approximateWorth, weight, recipe);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getGeneralItemId() {
		return InventoryIndex.USABLE_ITEM;
	}

	@Override
	public String[] getInformationDisplayArray(int[] itemArray) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
