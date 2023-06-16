package buildings.goods_deliverer;

import affiliation.Nation;
import buildings.Building;
import data_structures.List;
import inventory.InventoryIndex;
import location.WorldMapTile;
import unit.Unit;
import unit.UnitClass.Mount;
import unit.human.Human;

public class TradeCenter extends GoodsDeliverer {

	private int[] mounts;
	private int dayCounter;
	
	/**
	 * Assignment mostly functions as normal
	 * 
	 * When assigned to transport mounts, [0] is negative mount ID - 1, and [1] is quantity
	 */
	private int[] assignment;
	private TradeCenter assignedTarget;
	
	public static final int MAXIMUM_ANIMAL_COUNT = 500;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public TradeCenter(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		this.mounts = new int[Mount.values().length];
		this.dayCounter = 0;
		//assignment and assignedTradeCenter are initially false
	}
	
	@Override
	public void autoGiveAssignment() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getType() {
		return Building.TRADE_CENTER;
	}
	
	public int[] getMounts() {
		return mounts;
	}
	
	@Override
	public boolean deliverGoods(Building recipient) {
		//TODO
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
		dayCounter++;
		if (dayCounter == 30) {
			completeMonthlyAction();
		}
	}
	
	@Override
	public void completeMonthlyAction() {
		restockInventory();
		dayCounter = 0;
		if (isSendingGoods()) {
			//If assigned to transport mounts, then [0] is negative
			if (assignment[0] < 0) {
				int mountToGive = (assignment[0] + 1) * -1;
				int given = Math.min(assignment[1], mounts[mountToGive]);
				int owed = assignment[1] - mounts[mountToGive];
				mounts[mountToGive] -= given;
				
				assignedTarget.getMounts()[mountToGive] += given;
				if (owed > 0) {
					//TODO recipient becomes upset
					assignment[1] += owed;
				}
			} else {
				for (int q = 0; q < materials.size(); q++) {
					int[] item = materials.get(q);
					if (InventoryIndex.elementsAreEqual(item, assignment)) {
						int given = Math.min(item[2], assignment[2]);
						int owed = assignment[2] - item[2];
						item[2] -= given;
						
						assignedTarget.receiveGoods(new int[] {item[0], item[1], given});
						if (owed > 0) {
							//TODO recipient becomes upset
							assignment[2] += owed;
						}
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
	public boolean canReceiveGoods(int[] goods) {
		return true;
	}

	@Override
	public void defect(Nation n) {
		// TODO maybe deal with trade relations as well as owner
	}
	
	public boolean isSendingGoods() {
		return assignment != null && assignedTarget != null;
	}

	@Override
	public List<Building> possibleRecipients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<int[]> getStorehouseNeeds() {
		if (isSendingGoods()) {
			List<int[]> needs = new List<>();
			//Always ask for items in assignment
			needs.add(assignment);
		}
		return null;
	}

}
