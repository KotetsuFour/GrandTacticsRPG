package buildings.defendable;

import affiliation.Nation;
import buildings.Building;
import data_structures.List;
import inventory.Convoy;
import inventory.InventoryIndex;
import inventory.item.Item;
import location.WorldMapTile;
import unit.UnitGroup;
import unit.human.Human;

public class Prison extends Defendable {

	protected List<Human> prisoners;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Prison(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		// TODO Auto-generated constructor stub
		prisoners = new List<>(20, 20);
	}

	@Override
	public String getType() {
		return Building.PRISON;
	}
	
	@Override
	public void completeDailyAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completeMonthlyAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void defect(Nation n) {
		// TODO deal with prisoners as well as owner
		
	}

	public boolean canAcceptPrisonersFromGroup(UnitGroup group) {
		//Assume that group has prisoners
		return prisoners.limit() >= prisoners.size() + group.getPrisoners().size();
	}
	
	public void acceptPrisonersFromGroup(UnitGroup group) {
		//Assuming that group has prisoners
		UnitGroup captives = group.removePrisoners();
		for (int q = 0; q < captives.size(); q++) {
			//Monsters don't surrender, so assume all prisoners are humans
			Human h = (Human) captives.get(q);
			int[][] inv = h.getInventory();
			for (int idx = 0; idx < inv.length; idx++) {
				int[] itemArray = inv[idx];
				//Take all items that can be salvaged
				if (itemArray != null && canReceiveGoods(itemArray)) {
					Item item = InventoryIndex.getElement(itemArray);
					//Prisons can only receive ManufacturableItems (weapons, armor, and staves)
					//so we can assume that [2] is the item's current uses
					//This kinda feels like it's in danger of bugs though. Not super comfortable with it
					if (itemArray[2] == item.getInitialUses()) {
						//If the item is in perfect shape, keep it
						receiveGoods(new int[] {itemArray[0], itemArray[1], 1});
					}
				}
				h.getInventory()[idx] = null;
			}
			if (h.getPassenger() instanceof Convoy) {
				int[][] con = ((Convoy)h.getPassenger()).getInventory();
				for (int w = 0; w < con.length; w++) {
					if (con[w] != null && canReceiveGoods(con[w])) {
						receiveGoods(con[w]);
						con[w] = null;
					}
				}
			}
			captives.remove(h);
			prisoners.add(h);
		}
	}

}
