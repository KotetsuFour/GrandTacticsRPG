package unit;

import affiliation.Nation;
import battle.BattleGround;
import data_structures.List;
import data_structures.PriorityQueue;
import inventory.InventoryIndex;
import location.WMTileOccupant;
import location.WorldMapTile;
import manager.GeneralGameplayManager;
import ship.Ship;
import unit.human.Human;
import unit.monster.Monster;

public class UnitGroup implements WMTileOccupant {

	private List<Unit> members;
	private WorldMapTile location;
	private BattleGround battle;
	private int[] generalObjective; //I might not need this, depending on how the AI works
	private int[] battleGroundObjective;
	private boolean isAIControlled;
	private boolean customPositions;
	private Assignable assignedThing;
	private UnitGroup prisoners;
	
	public static final int CAPACITY = 20;
	public static final int[][] DEFAULT_BATTLE_POSITIONS = {
			{12, 8}, {12, 11}, {11, 6}, {11, 13}, {10, 4}, {10, 15}, {9, 6}, {9, 13}, {8, 8}, {8, 11},
			{10, 8}, {10, 11}, {13, 4}, {13, 15}, {11, 2}, {11, 17}, {8, 2}, {8, 17}, {8, 4}, {8, 15}
			};
	public static final int[][] DEFAULT_SMALL_SHIP_POSITIONS = {
			{10, 9}, {10, 10}, {9, 8}, {9, 11}, {11, 8}, {11, 11}, {12, 9}, {12, 10}, {10, 8}, {10, 11},
			{8, 9}, {8, 10}, {7, 8}, {7, 11}, {8, 8}, {8, 11}, {6, 9}, {6, 10}, {13, 8}, {13, 11}
	};
	public static final int[][] DEFAULT_MEDIUM_SHIP_POSITIONS = {
			{11, 9}, {11, 10}, {10, 8}, {10, 11}, {9, 7}, {9, 12}, {11, 7}, {11, 12}, {9, 9}, {9, 10},
			{12, 8}, {12, 11}, {8, 8}, {8, 11}, {7, 7}, {7, 12}, {13, 9}, {13, 10}, {7, 9}, {7, 10}
	};
	public static final int[][] DEFAULT_LARGE_SHIP_POSITIONS = {
			{13, 9}, {13, 10}, {11, 7}, {11, 12}, {9, 7}, {9, 12}, {10, 9}, {10, 10}, {12, 6}, {12, 13},
			{7, 8}, {7, 11}, {8, 6}, {8, 13}, {13, 7}, {13, 12}, {15, 7}, {15, 12}, {5, 7}, {5, 12}
	};
	
	public UnitGroup(Unit firstMember) {
		members = new List<>(CAPACITY, CAPACITY);
		members.add(firstMember); //Ensures that the group is never empty
		firstMember.assignGroup(this);
		//Initializing location isn't necessary
		//Same with battle and objectives
		
		//Groups that are not under the player's control are AI-controlled
		isAIControlled = firstMember.getAffiliation() != GeneralGameplayManager.getPlayerNation();
		
		//Add yourself to the nation's unitgroup list
		firstMember.getAffiliation().getUnitGroups().add(this);
	}
	
	public UnitGroup(List<Human> u) {
		members = new List<>(CAPACITY, CAPACITY);
		members.add(u.get(0));
		members.get(0).assignGroup(this);
		for (int q = 1; q < u.size(); q++) {
			add(u.get(q));
		}
		//Groups that are not under the player's control are AI-controlled
		isAIControlled = u.get(0).getAffiliation() != GeneralGameplayManager.getPlayerNation();
		
		//Add yourself to the nation's unitgroup list
		u.get(0).getAffiliation().getUnitGroups().add(this);
		
	}
	
	public int getLeadershipBonus(Unit unit) {
		if (members.get(0) == unit) {
			return 0;
		}
		return members.get(0).getLeadership() * 5;
	}
	
	public boolean containsUnit(Unit u) {
		return members.contains(u);
	}
	
	@Override
	public WorldMapTile getLocation() {
		return location;
	}

	@Override
	public int getMovement() {
		PriorityQueue<Integer> canBeCarriedMoves = new PriorityQueue<>();
		PriorityQueue<Integer> canCarryMoves = new PriorityQueue<>();
		for (int q = 0; q < members.size(); q++) {
			Unit u = members.get(q);
			if (u.canBeCarried()) {
				canBeCarriedMoves.add(u.getMovement());
			} else { //If cannot be carried, then can carry
				canCarryMoves.add(u.getMovement());
			}
		}
		if (canCarryMoves.size() >= canBeCarriedMoves.size()) {
			return canCarryMoves.pop();
		}
		while (canCarryMoves.size() < canBeCarriedMoves.size() - 1) {
			canBeCarriedMoves.pop();
		}
		return canBeCarriedMoves.pop();
	}
	
	public boolean canFly() {
		int fliers = 0;
		int ground = 0;
		for (int q = 0; q < members.size(); q++) {
			if (members.get(q).canFly()) {
				fliers++;
			} else if (members.get(q).canCarryUnit()) {
				return false;
			} else {
				ground++;
			}
		}
		return fliers >= ground;
	}
	
	public List<Equippable> getUnitsWithItem(int[] item) {
		List<Equippable> possessors = new List<>();
		for (int q = 0; q < members.size(); q++) {
			if (members.get(q) instanceof Equippable) {
				Equippable u = (Equippable)members.get(q);
				int[][] inv = u.getInventory();
				if (inv == null) {
					continue;
				}
				for (int w = 0; w < inv.length; w++) {
					if (InventoryIndex.elementsAreEqual(inv[w], item)) {
						possessors.add(u);
						break;
					}
				}
			}
		}
		return possessors;
	}
	
	public void sendTo(WorldMapTile tile) {
		this.location = tile;
	}
	
	@Override
	public Nation getAffiliation() {
		return members.get(0).getAffiliation();
	}
	
	@Override
	public BattleGround getBattle() {
		return battle;
	}
	
	public void setBattle(BattleGround battle) {
		this.battle = battle;
		for (int q = 0; q < members.size(); q++) {
			members.get(q).incrementBattles();
		}
	}

	public void setBattleGroundObjective() {
		//TODO based on general objective, members, and surroundings
	}
	
	public int[] getBattleGroundObjective() {
		return battleGroundObjective;
	}
	
	public void setGeneralObjective(int[] obj) {
		this.generalObjective = obj;
	}
	
	public void setBattgroundPositions() {
		if (!customPositions) {
			int[][] positions = null;
			if (assignedThing instanceof Ship) {
				Ship s = (Ship) assignedThing;
				if (s.getSize() == Ship.SMALL_SIZE) {
					positions = DEFAULT_SMALL_SHIP_POSITIONS;
				} else if (s.getSize() == Ship.MEDIUM_SIZE) {
					positions = DEFAULT_MEDIUM_SHIP_POSITIONS;
				} else if (s.getSize() == Ship.LARGE_SIZE) {
					positions = DEFAULT_LARGE_SHIP_POSITIONS;
				}
			} else {
				positions = DEFAULT_BATTLE_POSITIONS;
			}
			for (int q = 0; q < members.size(); q++) {
				int[] coords = positions[q];
				members.get(q).setCoords(coords[0], coords[1]);
			}
		}
	}
	
	/**
	 * Gives the military strength of the group
	 * @return array with the following indexes:
	 * [0] = physical strength
	 * [1] = magical strength
	 * [2] = accuracy
	 * [3] = avoidance (Basic. that is, torso for units)
	 * [4] = crit rate
	 * [5] = crit avoidance (Basic. that is, torso for units)
	 * [6] = average attack speed
	 * [7] = defense (Basic. that is, torso for units)
	 * [8] = resistance
	 * [9] = head HP
	 * [10] = torso HP
	 */
	public int[] getPower() {
		int[] ret = new int[11];
		for (int q = 0; q < members.size(); q++) {
			int[] current = members.get(q).getPower();
			for (int w = 0; w < current.length; w++) {
				ret[w] += current[w];
			}
		}
		ret[6] /= Math.max(1, members.size()); //Make sure that attack speed is always the average
//		if (assignedThing != null) {
//			int[] basePower = assignedThing.getPower();
//			ret[0] += basePower[0]; //Physical
//			ret[1] += basePower[1]; //Magical
//			ret[2] += basePower[2]; //Accuracy
//			ret[4] += basePower[3]; //Crit
//			ret[7] += basePower[4]; //Defense
//			ret[8] += basePower[5]; //Resistance
//		}
		return ret;
	}

	public Assignable getAssignedThing() {
		return assignedThing;
	}
	
	public void giveAssignment(Assignable a) {
		this.assignedThing = a;
		this.location = null;
		a.assignGroup(this);
		this.customPositions = false;
	}
	
	public void removeAssignment() {
		assignedThing = null;
	}

	public boolean add(Unit u) {
		if (u.getAffiliation() != getAffiliation()) {
			return false;
		}
		//Monsters can only be grouped with other monsters with the same master
		//(it makes figuring out affiliation easier)
		if (u instanceof Monster) {
			Unit check = members.get(0);
			if (!(check instanceof Monster)
					|| ((Monster)check).getMaster() != ((Monster)u).getMaster()) {
				return false;
			}
		}
		if (members.add(u)) {
			u.assignGroup(this);
			customPositions = false;
			return true;
		}
		return false;
	}

	public void remove(Unit unit) {
		Nation n = unit.getAffiliation();
		int idx = members.remove(unit);
		if (unit.getGroup() != null) {
			unit.removeGroup();
		}
		if (idx == 0) {
			if (members.isEmpty()) {
				n.getUnitGroups().remove(this);
				if (location != null && location.getGroupPresent() == this) {
					location.removeGroupOrShip();
				}
			} else {
				autoAssignLeader();
			}
		}
	}
	
	public void toggleAIControl() {
		isAIControlled = !isAIControlled;
	}
	
	public boolean isAIControlled() {
		return isAIControlled;
	}
	
	public void exhaust() {
		for (int q = 0; q < members.size(); q++) {
			members.get(q).exhaust();
		}
	}
	
	public boolean isExhausted() {
		for (int q = 0; q < members.size(); q++) {
			if (!(members.get(q).isExhausted())) {
				return false;
			}
		}
		return true;
	}
	
	public void assignLeader(int idx) {
		members.add(0, members.remove(idx));
	}
	
	public void autoAssignLeader() {
		Unit lead = members.get(0);
		for (int q = 0; q < members.size(); q++) {
			Unit cand = members.get(q);
			if (cand.getLeadership() > lead.getLeadership()) {
				lead = cand;
			}
		}
		members.remove(lead);
		members.add(0, lead);
	}

	public List<Unit> getMembers() {
		return members;
	}
	
	public int size() {
		return members.size();
	}
	
	public boolean isFull() {
		return members.isFull();
	}
	
	public Unit get(int q) {
		return members.get(q);
	}
	
	public UnitGroup getPrisoners() {
		//Because I dunno another foolproof way to do this
		if (prisoners != null && prisoners.size() == 0) {
			prisoners = null;
		}
		return prisoners;
	}

	public UnitGroup removePrisoners() {
		UnitGroup ret = prisoners;
		prisoners = null;
		return ret;
	}

	@Override
	public Unit getLeader() {
		return members.get(0);
	}

	/**
	 * Method used for testing
	 * @return
	 */
	public String getGroupAsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Members:\n");
		for (int q = 0; q < members.size(); q++) {
			Unit u = members.get(q);
			sb.append(String.format("%s (%s)\n", u.getName(), u.getUnitClassName()));
		}
		int[] power = getPower();
		sb.append(String.format("Power:\nPhysical: %d, Magical: %d, Accuracy: %d, Avoidance: %d,\n",
				power[0], power[1], power[2], power[3]));
		sb.append(String.format("Crit Rate: %d, Crit Avoid: %d, AS: %d, DEF: %d, RES: %d\n",
				power[4], power[5], power[6], power[7], power[8]));
		sb.append(String.format("Head HP: %d, Torso HP: %d\n", power[9], power[10]));
		
		return sb.toString();
	}

}
