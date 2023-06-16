package buildings;

import data_structures.List;
import ship.Ship;
import unit.human.Human;

public class Port extends Building {

	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	private List<Ship> ships;
	
	public Port(String name, Human owner) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner);
		ships = new List<>(4, 4);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getType() {
		return Building.PORT;
	}
	
	public boolean isFull() {
		return ships.isFull();
	}

	@Override
	public void completeDailyAction() {
		restockInventory();
		// TODO Auto-generated method stub
	}

	@Override
	public void completeMonthlyAction() {
		restockInventory();
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<int[]> getStorehouseNeeds() {
		// TODO Auto-generated method stub
		return null;
	}

}
