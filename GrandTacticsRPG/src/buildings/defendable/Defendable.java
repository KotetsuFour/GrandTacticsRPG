package buildings.defendable;

import buildings.Building;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.weapon.StationaryWeapon;
import location.WorldMapTile;
import unit.Assignable;
import unit.UnitGroup;
import unit.human.Human;

public abstract class Defendable extends Building implements Assignable {

	private UnitGroup assignedGroup;
	private List<StationaryWeapon> defenses;
	protected List<int[]> armors;
	protected List<int[]> staves;
	protected WorldMapTile location;

	public Defendable(String name, int maxStructuralIntegrity, int durability, int resistance,
			Human owner, WorldMapTile location) {
		super(name, durability, durability, durability, owner);
		this.armors = new List<>();
		this.staves = new List<>();
		this.defenses = new List<>();
		this.location = location;
	}
	
	@Override
	public void assignGroup(UnitGroup group) {
		this.assignedGroup = group;
	}
	
	@Override
	public UnitGroup getAssignedGroup() {
		if (assignedGroup != null && assignedGroup.getMembers().isEmpty()) {
			assignedGroup = null;
		}
		return assignedGroup;
	}
	
	@Override
	public boolean dismissAssignedGroup() {
		if (!(location.isVacant())) {
			return false;
		}
		location.sendHere(assignedGroup);
		assignedGroup.removeAssignment();
		assignedGroup = null;
		return true;
	}
	
	@Override
	public List<StationaryWeapon> getDefenses() {
		return defenses;
	}
	@Override
	public void placeStationaryWeapon(StationaryWeapon w) {
		defenses.add(w);
		//TODO assign a position for the weapon
	}
	
	public List<int[]> getArmors() {
		return armors;
	}
	
	public List<int[]> getStaves() {
		return staves;
	}
	
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
	@Override
	public int[] getPower() {
		int[] ret = new int[6];
		for (int q = 0; q < defenses.size(); q++) {
			StationaryWeapon s = defenses.get(q);
			if (s.isMagic()) {
				ret[1] = s.getMight();
			} else {
				ret[0] = s.getMight();
			}
			ret[2] = s.getHit();
			ret[3] = s.getCrit();
			ret[4] = s.getDefense();
			ret[5] = s.getResistance();
		}
		ret[4] += getDurability();
		ret[5] += getResistance();
		return ret;
	}
	
	@Override
	public boolean canReceiveGoods(int[] goods) {
		int type = goods[0];
		return type == InventoryIndex.HANDHELD_WEAPON
				|| type == InventoryIndex.STATIONARY_WEAPON
				|| type == InventoryIndex.ARMOR
				|| type == InventoryIndex.OFFENSIVE_STAFF
				|| type == InventoryIndex.STATIONARY_STAFF
				|| type == InventoryIndex.TILE_STAFF
				|| type == InventoryIndex.SUPPORT_STAFF;
	}
	
	@Override
	public boolean receiveGoods(int[] goods) {
		if (!canReceiveGoods(goods)) {
			return false;
		}
		if (goods[0] == InventoryIndex.STATIONARY_WEAPON) {
			StationaryWeapon w = (StationaryWeapon)InventoryIndex.getElement(goods);
			for (int q = 0; q < goods[2]; q++) {
				placeStationaryWeapon(w.clone());
			}
			return true;
		} else if (goods[0] == InventoryIndex.HANDHELD_WEAPON) {
			for (int q = 0; q < materials.size(); q++) {
				int[] m = materials.get(q);
				if (InventoryIndex.elementsAreEqual(m, goods)) {
					m[2] += goods[2];
					return true;
				}
			}
			materials.add(goods.clone());
			return true;
		} else if (goods[0] == InventoryIndex.ARMOR) {
			for (int q = 0; q < armors.size(); q++) {
				int[] m = armors.get(q);
				if (InventoryIndex.elementsAreEqual(m, goods)) {
					m[2] += goods[2];
					return true;
				}
			}
			armors.add(goods.clone());
			return true;
		} else if (goods[0] == InventoryIndex.SUPPORT_STAFF
				|| goods[0] == InventoryIndex.OFFENSIVE_STAFF
				|| goods[0] == InventoryIndex.STATIONARY_STAFF
				|| goods[0] == InventoryIndex.TILE_STAFF) {
			for (int q = 0; q < staves.size(); q++) {
				int[] m = staves.get(q);
				if (InventoryIndex.elementsAreEqual(m, goods)) {
					m[2] += goods[2];
					return true;
				}
			}
			staves.add(goods.clone());
			return true;
		}
		return false;
	}



}
