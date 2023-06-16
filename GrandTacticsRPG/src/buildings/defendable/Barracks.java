package buildings.defendable;

import buildings.Building;
import location.WorldMapTile;
import unit.human.Human;

public class Barracks extends Defendable {

	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Barracks(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getType() {
		return Building.BARRACKS;
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

}
