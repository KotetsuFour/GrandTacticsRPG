package inventory.staff;

import inventory.InventoryIndex;
import location.BattlegroundTile;

public class TileStaff extends Staff {

	public TileStaff(String name, int initialUses, int approximateWorth, int weight,
			int range, int[][] recipe) {
		super(name, initialUses, approximateWorth, weight, range, recipe);
		// TODO Auto-generated constructor stub
	}

	public void effect(BattlegroundTile target) {
		//TODO
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.TILE_STAFF;
	}
}
