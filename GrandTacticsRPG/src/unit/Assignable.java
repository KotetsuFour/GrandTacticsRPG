package unit;

import data_structures.List;
import inventory.weapon.StationaryWeapon;

public interface Assignable {

	/**
	 * Gives power of the assignable thing
	 * @return an array with indexes:
	 * [0] = physical strength
	 * [1] = magical strength
	 * [2] = accuracy
	 * [3] = critRate
	 * [4] = defense
	 * [5] = resistance
	 */
	public int[] getPower();
	
	public void assignGroup(UnitGroup group);
	
	public UnitGroup getAssignedGroup();
	
	public boolean dismissAssignedGroup();

	public String getName();

	List<StationaryWeapon> getDefenses();
	
	void placeStationaryWeapon(StationaryWeapon w);
}
