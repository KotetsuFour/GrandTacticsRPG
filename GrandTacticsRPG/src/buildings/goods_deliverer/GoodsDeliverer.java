package buildings.goods_deliverer;

import buildings.Building;
import data_structures.List;
import inventory.item.Item;
import location.WorldMapTile;
import location.WorldMapTile.WorldMapTileType;
import unit.Unit;
import unit.human.Human;

public abstract class GoodsDeliverer extends Building {
	
	protected Building customer;
	protected WorldMapTile location;
	protected boolean continuousDelivery;

	public GoodsDeliverer(String name, int maxStructuralIntegrity, int durability,
			int resistance, Human owner, WorldMapTile location) {
		super(name, maxStructuralIntegrity, durability, resistance, owner);
		this.location = location;
		this.continuousDelivery = false;
	}

	public abstract boolean deliverGoods(Building recipient);
	
	public abstract boolean giveGoods(Unit recipient);
	
	/**
	 * Returns the amount of products producible using the current allotted resources
	 * Returns -1 by default for buildings that do not need resources to make products
	 * @return amount producible, or -1 if there is no assignment
	 */
	public int amountProducibleWithResources() {
		return -1;
	}
	
	/**
	 * Gives all of the buildings that this GoodsDeliverer is able to send its products to
	 * 
	 * By default, this includes all buildings within its own city that can receive its goods
	 * 
	 * @return all buildings this building can deliver its goods to
	 */
	public List<Building> possibleRecipients() {
		List<Building> ret = new List<>();
		List<Building> b = location.getOwner().getOtherBuildings();
		for (int q = 0; q < b.size(); q++) {
			Building check = b.get(q);
			for (int w = 0; w < materials.size(); w++) {
				if (check != this && check.canReceiveGoods(materials.get(w))) {
					ret.add(check);
					break;
				}
			}
		}
		return ret;
	}
	
	public List<Building> possibleRecipientsOfItem(Item item) {
		List<Building> ret = new List<>();
		List<Building> b = location.getOwner().getOtherBuildings();
		int[] itemArray = new int[] {item.getGeneralItemId(), item.getSpecificItemId()};
		for (int q = 0; q < b.size(); q++) {
			Building check = b.get(q);
			if (check != this && check.canReceiveGoods(itemArray)) {
				ret.add(check);
				break;
			}
		}
		return ret;
	}
	
	public WorldMapTileType getTerrainType() {
		return location.getType();
	}
	
	public boolean isDeliveringContinuously() {
		return continuousDelivery;
	}
	
	public Building getCustomer() {
		return customer;
	}

	public abstract void autoGiveAssignment();
}
