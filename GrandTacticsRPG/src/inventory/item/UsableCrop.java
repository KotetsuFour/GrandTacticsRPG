package inventory.item;

import inventory.InventoryIndex;

public class UsableCrop extends Item {

	protected boolean usedInBuilding;
	
	public UsableCrop(String name, int initialUses, int approximateWorth, int weight,
			boolean usedInBuilding) {
		super(name, initialUses, approximateWorth, weight);
		this.usedInBuilding = usedInBuilding;
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.USABLECROP;
	}

	public boolean isUsedInBuilding() {
		return usedInBuilding;
	}

	@Override
	public String[] getInformationDisplayArray(int[] itemArray) {
		// TODO Auto-generated method stub
		return null;
	}
}
