package buildings.goods_deliverer;

import buildings.Building;
import buildings.Village;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.item.Item;
import inventory.staff.Staff;
import inventory.weapon.Weapon;
import location.WorldMapTile;
import unit.Unit;
import unit.human.Human;

public class Storehouse extends GoodsDeliverer {

	protected List<int[]> weaponStorage;
	protected List<int[]> armorStorage;
	protected List<int[]> staffStorage;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Storehouse(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void autoGiveAssignment() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String getType() {
		return Building.STOREHOUSE;
	}
	
	@Override
	public boolean deliverGoods(Building recipient) {
		//Never give all goods to a single building
		return false;
	}

	public boolean deliverGoods(Building recipient, List<int[]> order) {
		//TODO give order to building
		return false;
	}
	
	@Override
	public boolean giveGoods(Unit recipient) {
		//Never give all goods to one person
		return false;
	}

	public boolean giveGoods(Unit recipient, List<int[]> order) {
		//TODO give order to recipient
		return false;
	}
	
	@Override
	public void completeDailyAction() {
		List<Building> b = location.getOwner().getOtherBuildings();
		List<Village> v = location.getOwner().getResidentialAreas();
		//Castles don't matter here
		for (int q = 0; q < b.size(); q++) {
			provideNeedsOfBuilding(b.get(q));
		}
		for (int q = 0; q < v.size(); q++) {
			provideNeedsOfBuilding(v.get(q));
		}
	}
	
	public void provideNeedsOfBuilding(Building build) {
		List<int[]> needs = build.getStorehouseNeeds();
		if (needs != null) {
			for (int w = 0; w < needs.size(); w++) {
				int[] need = needs.get(w);
				//TODO materials should probably be a map or something so that
				//this is more efficient. We'll see how it runs first though
				Item i = InventoryIndex.getElement(need);
				List<int[]> check = null;
				if (i instanceof Weapon) {
					check = weaponStorage;
				} else if (i instanceof Armor) {
					check = armorStorage;
				} else if (i instanceof Staff) {
					check = staffStorage;
				} else {
					check = materials;
				}
				for (int e = 0; e < check.size(); e++) {
					if (InventoryIndex.elementsAreEqual(need, check.get(e))) {
						int numToSend = Math.min(need[2], check.get(e)[2]);
						int[] delivery = new int[] {need[0], need[1], numToSend};
						check.get(e)[2] -= numToSend;
						build.receiveGoods(delivery);
						break;
					}
				}
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeMonthlyAction() {
		//Exact same as monthly action
		completeDailyAction();
	}

	@Override
	public boolean receiveGoods(int[] goods) {
		Item i = InventoryIndex.getElement(goods);
		List<int[]> check = null;
		if (i instanceof Weapon) {
			check = weaponStorage;
		} else if (i instanceof Armor) {
			check = armorStorage;
		} else if (i instanceof Staff) {
			check = staffStorage;
		} else {
			check = materials;
		}
		for (int q = 0; q < check.size(); q++) {
			int[] m = check.get(q);
			if (InventoryIndex.elementsAreEqual(m, goods)) {
				m[2] += goods[2];
				return true;
			}
		}
		check.add(goods.clone());
		return true;
	}
	
	@Override
	public boolean canReceiveGoods(int[] goods) {
		return true;
	}

	@Override
	public List<Building> possibleRecipients() {
		// TODO Auto-generated method stub
		return null;
	}

	public void submitRequest(int[] need) {
		// TODO Auto-generated method stub
	}

}
