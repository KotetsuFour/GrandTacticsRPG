package inventory.item;

import inventory.InventoryIndex;

public class EdibleCrop extends Item {

	public EdibleCrop(String name, int initialUses, int approximateWorth, int weight) {
		super(name, initialUses, approximateWorth, weight);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.EDIBLECROP;
	}

	@Override
	public String[] getInformationDisplayArray(int[] itemArray) {
		// TODO Auto-generated method stub
		return null;
	}

}
