package inventory;

import location.BFTileOccupant;

public class Convoy implements BFTileOccupant, Lootable {

	protected int[][] inventory;
	
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] steal() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[][] getInventory() {
		return inventory;
	}
	
}
