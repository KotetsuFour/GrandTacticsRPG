package ship;

import affiliation.Nation;
import battle.BattleGround;
import data_structures.List;
import inventory.weapon.StationaryWeapon;
import location.WMTileOccupant;
import location.WorldMapTile;
import manager.GeneralGameplayManager;
import unit.Assignable;
import unit.Unit;
import unit.UnitGroup;

public class Ship implements WMTileOccupant, Assignable {

	private WorldMapTile location;
	protected int bluePrint;
	protected Hull hull;
	protected Helm helm;
	protected ShipType type;
	protected int size;
	protected List<StationaryWeapon> weapons;
	protected UnitGroup assignedUnitGroup;
	protected List<ShipBarracks> barracks;
	protected List<ShipStorage> storage;
	protected List<ShipPrison> prison;
	protected BattleGround battle;
	protected boolean isImportant;
	protected boolean hasPlayerNationUnit;
	
	public static final int SMALL_SIZE = 0;
	public static final int MEDIUM_SIZE = 1;
	public static final int LARGE_SIZE = 2;
	
	public Ship(ShipType type) {
		this.hull = new Hull(type.getMaxHP());
		this.helm = new Helm(type.getMaxHP());
		this.weapons = new List<>();
		barracks = new List<>();
		storage = new List<>();
		prison = new List<>();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setBluePrint(int bluePrint) {
		this.bluePrint = bluePrint;
	}
	public int getShipPartHP(int shipPart) {
		return getShipPart(shipPart).getCurrentHP();
	}

	public boolean partIsIntact(int shipPart) {
		return getShipPartHP(shipPart) >= 0;
	}

	public boolean isDestroyed() {
		//Check for hull HP
		return !partIsIntact(0);
	}
	public int getDefense(boolean isMagicAttack) {
		if (isMagicAttack) {
			return type.getResistance();
		}
		return type.getDefense();
	}

	public void takeDamage(boolean magic, int shipPart, int might) {
		int damage = Math.max(0, might - getDefense(magic));
		getShipPart(shipPart).takeDirectDamage(damage);
	}
	public int avoidance(int shipPart) {
		if (shipPart == 0 || shipPart == 1) {
			//If hull or helm, avoidance is better when more ship parts
			//Otherwise, avoidance is 0
			return (barracks.size() + storage.size()) * 20;
		}
		return 0;
	}
	
	@Override
	public WorldMapTile getLocation() {
		return location;
	}

	@Override
	public int getMovement() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getShipDescription() {
		if (assignedUnitGroup == null) {
			return "Unoccupied Ship";
		}
		return assignedUnitGroup.getLeader().getName() + "'s Ship";
	}

	@Override
	public UnitGroup getAssignedGroup() {
		if (assignedUnitGroup != null && assignedUnitGroup.getMembers().isEmpty()) {
			assignedUnitGroup = null;
		}
		return assignedUnitGroup;
	}

	public void assignGroup(UnitGroup group) {
		this.assignedUnitGroup = group;
		setStartingPositions(group);
	}
	
	@Override
	public boolean dismissAssignedGroup() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Unit getLeader() {
		if (assignedUnitGroup == null) {
			return null;
		}
		return assignedUnitGroup.getLeader();
	}
	
	@Override
	public List<StationaryWeapon> getDefenses() {
		return weapons;
	}
	
	@Override
	public void placeStationaryWeapon(StationaryWeapon w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Nation getAffiliation() {
		if (assignedUnitGroup == null) {
			return null;
		}
		return assignedUnitGroup.getAffiliation();
	}

	public boolean assignGroupToBarracks(UnitGroup group) {
		for (int q = 0; q < barracks.size(); q++) {
			if (barracks.get(q).getPassengers() == null) {
				barracks.get(q).assignGroup(group);
				return true;
			}
		}
		return false;
	}
	
	private void setStartingPositions(UnitGroup group) {
		//TODO set the starting battle positions of the unit group
		//so that they are always physically on the ship, in optimal spots
	}
	
	private ShipPart getShipPart(int part) {
		if (part == 0) {
			return hull;
		}
		if (part == 1) {
			return helm;
		}
		part -= 2;
		if (part < barracks.size()) {
			return barracks.get(part);
		}
		part -= barracks.size();
		if (part < storage.size()) {
			return storage.get(part);
		}
		part -= storage.size();
		return prison.get(part);
	}
	
	@Override
	public BattleGround getBattle() {
		return battle;
	}
	
	/**
	 * Gives all units in assigned group and all units below deck
	 * @return
	 */
	public List<Unit> getAllPassengers() {
		List<Unit> ret = new List<>();
		isImportant = false;
		hasPlayerNationUnit = false;
		List<Unit> toAdd = assignedUnitGroup.getMembers();
		for (int q = 0; q < toAdd.size(); q++) {
			Unit u = toAdd.get(q);
			ret.add(u);
			if (u.isImportant()) {
				isImportant = true;
			} else if (u.getAffiliation() == GeneralGameplayManager.getPlayerNation()) {
				hasPlayerNationUnit = true;
			}
		}
		for (int q = 0; q < barracks.size(); q++) {
			toAdd = barracks.get(q).getUnitsCurrentlyInBarracks();
			if (toAdd != null) {
				for (int w = 0; w < toAdd.size(); w++) {
					Unit u = toAdd.get(q);
					ret.add(u);
					if (u.isImportant()) {
						isImportant = true;
					} else if (u.getAffiliation() == GeneralGameplayManager.getPlayerNation()) {
						hasPlayerNationUnit = true;
					}
				}
			}
		}
		for (int q = 0; q < prison.size(); q++) {
			toAdd = prison.get(q).getPrisoners().getMembers();
			if (toAdd != null) {
				for (int w = 0; w < toAdd.size(); w++) {
					Unit u = toAdd.get(q);
					ret.add(u);
					if (u.isImportant()) {
						isImportant = true;
					} else if (u.getAffiliation() == GeneralGameplayManager.getPlayerNation()) {
						hasPlayerNationUnit = true;
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * Gives all units (in the ship or otherwise, allied or enemy) who are physically
	 * aboard the ship and cannot escape if the ship sinks
	 * @return
	 */
	public List<Unit> getPassengersAboard() {
		// TODO Auto-generated method stub
		//TODO while getting passengers, if one of them is important or part of the player's
		//nation, mark the ship as such
		List<Unit> ret = new List<>();
		//TODO add all units in battle who are physically on the ship
		for (int q = 0; q < barracks.size(); q++) {
			List<Unit> check = barracks.get(q).getUnitsCurrentlyInBarracks();
			for (int w = 0; w < check.size(); w++) {
				Unit u = check.get(w);
				//Only need to record one, really
				if (u.isImportant()) {
					this.isImportant = true;
				} else if (u.getAffiliation() == GeneralGameplayManager.getPlayerNation()) {
					this.hasPlayerNationUnit = true;
				}
				ret.add(u);
			}
		}
		return ret;
	}
	
	public List<UnitGroup> getPrisoners() {
		List<UnitGroup> ret = new List<>();
		for (int q = 0; q < prison.size(); q++) {
			ret.add(prison.get(q).getPrisoners());
		}
		return ret;
	}
	
	public List<UnitGroup> getAllUndeployedPassengersAndPrisoners() {
		List<UnitGroup> ret = new List<>();
		for (int q = 0; q < barracks.size(); q++) {
			ret.add(barracks.get(q).getPassengers());
		}
		for (int q = 0; q < prison.size(); q++) {
			ret.add(prison.get(q).getPrisoners());
		}
		return ret;
	}

	public void destructionSequence() {
		// TODO remove ship from WorldMapTile and BattleGround
	}

	public boolean isImportant() {
		return isImportant;
	}

	public boolean hasPlayerNationUnit() {
		return hasPlayerNationUnit;
	}
	public UnitGroup assignedGroup() {
		return assignedUnitGroup;
	}

	
	public Ship clone() {
		Ship ret = new Ship(type);
		for (int q = 0; q < weapons.size(); q++) {
			ret.weapons.add(weapons.get(q).clone());
		}
		for (int q = 0; q < barracks.size(); q++) {
			ret.barracks.add((ShipBarracks)barracks.get(q).clone());
		}
		for (int q = 0; q < storage.size(); q++) {
			ret.storage.add((ShipStorage)storage.get(q).clone());
		}
		for (int q = 0; q < prison.size(); q++) {
			ret.prison.add((ShipPrison)prison.get(q).clone());
		}
		return ret;
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
		for (int q = 0; q < weapons.size(); q++) {
			StationaryWeapon s = weapons.get(q);
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
		ret[4] += type.getDefense();
		ret[5] += type.getResistance();
		return ret;
	}

	public int getSize() {
		return size;
	}
	
	private abstract class ShipPart {
		private int currentHP;
		public ShipPart(int hp) {
			this.currentHP = hp;
		}
		public int getCurrentHP() {
			return currentHP;
		}
		public void takeDirectDamage(int damage) {
			currentHP -= damage;
		}
		public abstract ShipPart clone();
	}
	private class Hull extends ShipPart {
		public Hull(int hp) {
			super(hp);
		}
		@Override
		public ShipPart clone() {
			return new Hull(type.getMaxHP());
		}
	}
	private class Helm extends ShipPart {
		public Helm(int hp) {
			super(hp);
		}
		@Override
		public ShipPart clone() {
			return new Helm(type.getMaxHP());
		}
	}
	private class ShipBarracks extends ShipPart {
		private UnitGroup passengers;
		private List<Unit> passengersCurrentlyInBarracks;
		private List<Unit> deployedPassengers;
		public ShipBarracks(int hp) {
			super(hp);
			passengersCurrentlyInBarracks = new List<>();
			deployedPassengers = new List<>();
		}
		@Override
		public ShipPart clone() {
			return new ShipBarracks(type.getMaxHP());
		}
		public List<Unit> getDeployedUnits() {
			return deployedPassengers;
		}
		public List<Unit> getUnitsCurrentlyInBarracks() {
			return passengersCurrentlyInBarracks;
		}
		public UnitGroup getPassengers() {
			return passengers;
		}
		public void assignGroup(UnitGroup group) {
			this.passengers = group;
			passengersCurrentlyInBarracks.addAll(group.getMembers());
		}
		public Unit deployUnit(int idx) {
			Unit ret = passengersCurrentlyInBarracks.remove(idx);
			deployedPassengers.add(ret);
			return ret;
		}
		public void returnAllDeployedUnitsToBarracks() {
			while (!(deployedPassengers.isEmpty())) {
				Unit u = deployedPassengers.remove(deployedPassengers.size() - 1);
				if (u.isAlive()) {
					passengersCurrentlyInBarracks.add(u);
				}
			}
		}
	}
	private class ShipPrison extends ShipPart {
		private UnitGroup prisoners;
		public ShipPrison(int hp) {
			super(hp);
		}
		@Override
		public ShipPart clone() {
			return new ShipPrison(type.getMaxHP());
		}
		public UnitGroup getPrisoners() {
			return prisoners;
		}
	}
	private class ShipStorage extends ShipPart {
		private List<int[]> materials;
		private List<int[]> weapons;
		private List<int[]> armor;
		private int currentHP;
		public ShipStorage(int hp) {
			super(hp);
			materials = new List<>();
			weapons = new List<>();
			armor = new List<>();
		}
		@Override
		public ShipPart clone() {
			return new ShipStorage(type.getMaxHP());
		}
	}
	
	public enum ShipType {
		//TODO initialize values
		WOOD(0, 0, 0, 0, 0, 0, 0, 0, 0),
		STONE(0, 0, 0, 0, 0, 0, 0, 0, 0),
		CLAY(0, 0, 0, 0, 0, 0, 0, 0, 0),
		BRONZE(0, 0, 0, 0, 0, 0, 0, 0, 0),
		IRON(0, 0, 0, 0, 0, 0, 0, 0, 0),
		STEEL(0, 0, 0, 0, 0, 0, 0, 0, 0),
		SILVER(0, 0, 0, 0, 0, 0, 0, 0, 0),
		GOLD(0, 0, 0, 0, 0, 0, 0, 0, 0),
		OBSIDIAN(0, 0, 0, 0, 0, 0, 0, 0, 0),
		GLASS(0, 0, 0, 0, 0, 0, 0, 0, 0)
		;
		private int maxHP;
		private int defense;
		private int resistance;
		private int smallCapacity;
		private int mediumCapacity;
		private int largeCapacity;
		private int smallMovement;
		private int mediumMovement;
		private int largeMovement;
		private ShipType(int maxHP, int defense, int resistance, int smallCapacity,
				int mediumCapacity, int largeCapacity, int smallMovement, int mediumMovement,
				int largeMovement) {
			this.maxHP = maxHP;
			this.defense = defense;
			this.resistance = resistance;
			this.smallCapacity = smallCapacity;
			this.mediumCapacity = mediumCapacity;
			this.largeCapacity = largeCapacity;
			this.smallMovement = smallMovement;
			this.mediumMovement = mediumMovement;
			this.largeMovement = largeMovement;
		}
		public int getMaxHP() {
			return maxHP;
		}
		public int getDefense() {
			return defense;
		}
		public int getResistance() {
			return resistance;
		}
		public int getSmallCapacity() {
			return smallCapacity;
		}
		public int getMediumCapacity() {
			return mediumCapacity;
		}
		public int getLargeCapacity() {
			return largeCapacity;
		}
		public int getSmallMovement() {
			return smallMovement;
		}
		public int getMediumMovement() {
			return mediumMovement;
		}
		public int getLargeMovement() {
			return largeMovement;
		}
	}

}
