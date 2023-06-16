package buildings;

import affiliation.Nation;
import unit.human.Human;

public class WarpPad extends Building {

	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public WarpPad(String name, Human owner) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getType() {
		return Building.WARP_PAD;
	}
	
	@Override
	public void completeDailyAction() {
		// TODO Auto-generated method stub
	}

	@Override
	public void completeMonthlyAction() {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		return false;
	}

	@Override
	public void defect(Nation n) {
		// TODO deal with warp network as well as owner
		
	}

}
