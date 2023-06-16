package buildings;

import affiliation.Nation;
import data_structures.List;
import unit.human.Human;

public class ResearchCenter extends Building {

	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public ResearchCenter(String name, Human owner) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getType() {
		return Building.RESEARCH_CENTER;
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
	public void defect(Nation n) {
		// TODO maybe deal with technology tree as well as owner
		
	}

	@Override
	public List<int[]> getStorehouseNeeds() {
		// TODO Auto-generated method stub
		return null;
	}

}
