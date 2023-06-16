package buildings.goods_deliverer;

import buildings.Building;
import data_structures.List;
import inventory.InventoryIndex;
import location.WorldMapTile;
import unit.Unit;
import unit.human.Human;

public class MagicProcessingFacility extends GoodsDeliverer {

	protected int[] products;
	protected WorldMapTile location;
	protected int[] assignment;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public MagicProcessingFacility(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void autoGiveAssignment() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getType() {
		return Building.MINING_FACILITY;
	}
	
	@Override
	public boolean deliverGoods(Building recipient) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean giveGoods(Unit recipient) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void completeDailyAction() {
		restockInventory();
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completeMonthlyAction() {
		restockInventory();
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		int type = goods[0];
		return type == InventoryIndex.RESOURCE
				|| type == InventoryIndex.USABLECROP
				|| type == InventoryIndex.HANDHELD_WEAPON;
	}

	@Override
	public int amountProducibleWithResources() {
		//TODO
		return -1;
	}

	@Override
	public List<int[]> getStorehouseNeeds() {
		// TODO Auto-generated method stub
		return null;
	}
}
