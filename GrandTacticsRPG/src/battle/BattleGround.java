package battle;

import java.util.HashMap;
import java.util.Set;

import affiliation.Nation;
import data_structures.List;
import location.BFTileOccupant;
import location.BattlegroundTile;
import location.WMTileOccupant;
import location.WorldMapTile;
import reference.BattlegroundTileIndex;
import ship.Ship;
import unit.Unit;
import unit.UnitGroup;

public class BattleGround {

	private WorldMapTile[] location;
	private boolean isHorizontal;
	private List<UnitGroup> combatants;
	private HashMap<BFTileOccupant, int[]> getCoordsByOccupant;
	private int barrierHealth;
	private Ship[] ships;
	private BattlegroundTile[][] map;
	
	public BattleGround(WorldMapTile[] location, boolean isHorizontal,
			WMTileOccupant[] engagingParties) {
		this.getCoordsByOccupant = new HashMap<>();
		this.location = location;
		for (int q = 0; q < location.length; q++) {
			location[q].setBattle(this);
		}
		this.isHorizontal = isHorizontal;
		this.combatants = new List<>(4, 4);
		this.ships = new Ship[2];
		//Create map to make placing units easier
		getMap();
		//TODO place inanimate objects
		if (engagingParties[0] instanceof Ship) {
			addShip(0, (Ship)engagingParties[0]);
		} else if (engagingParties[0] instanceof UnitGroup) {
			UnitGroup g = (UnitGroup)engagingParties[0];
			addUnitGroup(0, g);
			if (g.getPrisoners() != null) {
				addPrisonerGroup(0, g.getPrisoners());
			}
		}
		if (engagingParties[1] instanceof Ship) {
			addShip(1, (Ship)engagingParties[1]);
		} else if (engagingParties[1] instanceof UnitGroup) {
			UnitGroup g = (UnitGroup)engagingParties[1];
			addUnitGroup(1, g);
			if (g.getPrisoners() != null) {
				addPrisonerGroup(1, g.getPrisoners());
			}
		}
		//Close battleground after we've placed all units
		exitBattleground();
		//Increment battles in this war
		combatants.get(0).getAffiliation().getCurrentWarWith(combatants.get(1).getAffiliation()).incrementBattles();
		//TODO if this battle is happening at a special location, record its beginning in
		//history
	}
	
	private void addShip(int tile, Ship s) {
		ships[tile] = s;
		//We add the assigned group separate from the reserves and prisoners so we can
		//place the group's members on their starting tiles
		if (s.assignedGroup() != null) {
			addUnitGroup(tile, s.assignedGroup());
		}
		List<UnitGroup> undeployed = s.getAllUndeployedPassengersAndPrisoners();
		for (int q = 0; q < undeployed.size(); q++) {
			UnitGroup group = undeployed.get(q);
			group.setBattle(this);
		}
	}
	
	private void addUnitGroup(int tile, UnitGroup group) {
		if (group == null) {
			throw new IllegalArgumentException("Cannot add a null group to combatants");
		}
		//TODO autoEquip all (for AI groups, take from convoy if necessary/possible)
		//TODO set strategy
		group.setBattgroundPositions();
		combatants.add(group);
		group.setBattle(this);
		for (int q = 0; q < group.getMembers().size(); q++) {
			addUnit(tile, group.getMembers().get(q));
		}
	}
	
	private void addUnit(int tile, Unit u) {
		if (u == null) {
			throw new IllegalArgumentException("Cannot add a null unit to battleground");
		}
		//TODO if the spot is occupied, pick nearest available one
		int[] coords = {u.getBattlePositionX(), u.getBattlePositionY()};
		if (tile == 1) {
			if (isHorizontal) {
				coords[0] = (BattlegroundTileIndex.TILE_DIMENSION * 2) - coords[0];
			} else {
				int temp = coords[0];
				coords[0] = coords[1];
				coords[1] = (BattlegroundTileIndex.TILE_DIMENSION * 2) - temp;
			}
		} else if (!isHorizontal) {
			int temp = coords[0];
			coords[0] = BattlegroundTileIndex.TILE_DIMENSION - coords[1];
			coords[1] = temp;
		}
		coords = closestUnoccupiedTile(coords);
		//TODO translate coordinates to correspond to the appropriate tile
		getMap()[coords[0]][coords[1]].placeUnit(u);
		getCoordsByOccupant.put(u, coords);
	}
	
	private void addPrisonerGroup(int tile, UnitGroup prisoners) {
		if (prisoners == null) {
			throw new IllegalArgumentException("Cannot add a null group to combatants");
		}
		combatants.add(prisoners);
		prisoners.setBattle(this);
		for (int q = 0; q < prisoners.getMembers().size(); q++) {
			addPrisoner(tile, prisoners.getMembers().get(q));
		}
	}
	
	private void addPrisoner(int tile, Unit prisoner) {
		if (prisoner == null) {
			throw new IllegalArgumentException("Cannot add a null unit to battleground");
		}
		//TODO pick the nearest available prisoner spawn tile
		int[] coords = {0, 0};
		//TODO translate coordinates to correspond to the appropriate tile
		getCoordsByOccupant.put(prisoner, coords);
	}
	
	private int[] closestUnoccupiedTile(int[] coords) {
		//TODO
		if (getMap()[coords[0]][coords[1]].getUnit() != null) {
			return new int[] {0, 0};
		}
		return coords;
	}
	
	public BattlegroundTile[][] getMap() {
		if (map != null) {
			return map;
		}
		//These halves of the map already include any ship or building that might be on the tile
		BattlegroundTile[][] firstHalf = BattlegroundTileIndex.mapToUse(location[0]);
		BattlegroundTile[][] secondHalf = BattlegroundTileIndex.mapToUse(location[1]);
		if (isHorizontal) {
			map = new BattlegroundTile[BattlegroundTileIndex.TILE_DIMENSION * location.length][BattlegroundTileIndex.TILE_DIMENSION]; //That is, 40x20
			for (int q = 0; q < firstHalf.length; q++) {
				for (int w = 0; w < BattlegroundTileIndex.TILE_DIMENSION; w++) {
					map[q][w] = firstHalf[q][w];
				}
			}
			for (int q = firstHalf.length; q < firstHalf.length + secondHalf.length; q++) {
				for (int w = 0; w < BattlegroundTileIndex.TILE_DIMENSION; w++) {
					map[q][w] = secondHalf[q - firstHalf.length][w];
				}
			}
		} else {
			map = new BattlegroundTile[BattlegroundTileIndex.TILE_DIMENSION][BattlegroundTileIndex.TILE_DIMENSION * location.length]; //That is, 20x40
			for (int q = 0; q < BattlegroundTileIndex.TILE_DIMENSION; q++) {
				for (int w = 0; w < firstHalf.length; w++) {
					map[q][w] = firstHalf[q][w];
				}
			}
			for (int q = 0; q < BattlegroundTileIndex.TILE_DIMENSION; q++) {
				for (int w = firstHalf.length; w < firstHalf.length + secondHalf.length; w++) {
					map[q][w] = secondHalf[q][w - firstHalf.length];
				}
			}
		}
		Set<BFTileOccupant> occupants = getCoordsByOccupant.keySet();
		for (BFTileOccupant o : occupants) {
			//TODO if space is not empty, find the nearest spot
			int[] coords = getCoordsByOccupant.get(o);
			map[coords[0]][coords[1]].placeOccupant(o);
		}
		return map;
	}
	
	/**
	 * To save memory, set map to null when the battleground is not currently being
	 * accessed
	 */
	public void exitBattleground() {
		map = null;
	}
	
	public List<UnitGroup> getUnitGroupsBelongingToNation(Nation n) {
		List<UnitGroup> ret = new List<>();
		for (int q = 0; q < combatants.size(); q++) {
			UnitGroup ug = combatants.get(q);
			if (ug.getAffiliation() == n) {
				ret.add(ug);
			}
		}
		return ret;
	}
	
	public void moveUnit(Unit unit, int[] to,
			BattlegroundTile[][] map //Require the use of the map just to ensure the map is
									//currently constructed. I might decide this is unnecessary
									//once all battleground functions and AI are done
			) {
		Unit alreadyHere = map[to[0]][to[1]].getUnit();
		if (alreadyHere != null && alreadyHere != unit) {
			throw new IllegalArgumentException("There is already someone here");
		}
		int[] currentCoords = getCoordsByOccupant.get(unit);
		map[currentCoords[0]][currentCoords[1]].removeUnit();
		map[to[0]][to[1]].placeUnit(unit);
		getCoordsByOccupant.put(unit, to);
	}
	
	public void removeOccupant(BFTileOccupant o) {
		//Removal from unit group is already done in death sequence
		int[] coords = getCoordsByOccupant.get(o);
		if (o instanceof Unit) {
			Unit u = (Unit)o;
			if (map != null) {
				map[coords[0]][coords[1]].removeUnit();
			}
			if (u.getPassenger() != null) {
				BFTileOccupant pass = u.getPassenger();
				getCoordsByOccupant.put(pass, coords);
				if (map != null) {
					map[coords[0]][coords[1]].placeOccupant(pass);
				}
			}
		}
		getCoordsByOccupant.remove(o);
	}
	
	public int[] getCoordsOfUnit(BFTileOccupant o) {
		return getCoordsByOccupant.get(o);
	}
	
	public BattlegroundTile getTileAtCoords(int[] coords) {
		return map[coords[0]][coords[1]];
	}
	
	public int[] getDimensions() {
		if (isHorizontal) {
			return new int[] {BattlegroundTileIndex.TILE_DIMENSION * 2, BattlegroundTileIndex.TILE_DIMENSION};
		}
		return new int[] {BattlegroundTileIndex.TILE_DIMENSION, BattlegroundTileIndex.TILE_DIMENSION * 2};
	}
	
	public boolean isActive() {
		//TODO debug
		Nation side1 = null;
		int side1Quant = 0;
		Nation side2 = null;
		int side2Quant = 0;
		for (int q = 0; q < combatants.size(); q++) {
			UnitGroup group = combatants.get(q);
			if (group.getMembers().isEmpty()) {
				combatants.remove(q);
				q--;
				continue;
			}
			if (side1 == null) {
				side1 = group.getAffiliation();
				side1Quant++;
			} else if (group.getAffiliation().isAlliedWith(side1)) {
				side1Quant++;
			} else if (side2 == null) {
				side2 = group.getAffiliation();
				side2Quant++;
			} else if (group.getAffiliation().isAlliedWith(side2)) {
				side2Quant++;
			} else {
				throw new IllegalArgumentException("An illegal participant entered the battlefield\n"
						+ group.getLeader().getDisplayName() + ", representing " + group.getAffiliation().getName());
			}
		}
		
		if (side1Quant > 2 || side2Quant > 2) {
			throw new IllegalArgumentException("Rethink your logic for joining a battle. One side\n"
					+ "was able to have more than 2 groups helping it");
		}
		
		return side1Quant > 0 && side2Quant > 0;
	}
	
	public void endBattle() {
		location[0].setBattle(null);
		location[1].setBattle(null);
		
		for (int q = 0; q < combatants.size(); q++) {
			location[q].sendHere(combatants.get(q));
		}
	}
	
	public boolean canEnter(UnitGroup group) {
		int friends = 0;
		for (int q = 0; q < combatants.size(); q++) {
			if (group.getAffiliation().isAlliedWith(combatants.get(q).getAffiliation())) {
				friends++;
				if (friends > 1) {
					return false;
				}
			}
		}
		return barrierHealth <= 0;
	}
	
	public boolean canSurrender(Nation n) {
		int friends = 0;
		int idxOfAlly = -1;
		for (int q = 0; q < combatants.size(); q++) {
			if (n.isAlliedWith(combatants.get(q).getAffiliation())) {
				friends++;
				//If there is more than just one allied group, you cannot surrender
				if (friends > 1) {
					return false;
				}
				idxOfAlly = q;
			}
		}
		
		//Can surrender if you have one allied group that you directly control
		return idxOfAlly != -1 && combatants.get(idxOfAlly).getAffiliation() == n;
	}
	
	public List<UnitGroup> getCombatants() {
		for (int q = 0; q < combatants.size(); q++) {
			if (combatants.get(q).getMembers().isEmpty()) {
				combatants.remove(q);
				q--;
			}
		}
		return combatants;
	}
	
	public List<BattlegroundTile> getAllAdjacentTiles(int x, int y) {
		List<BattlegroundTile> ret = new List<>(4, 4);
		if (x != 0) {
			ret.add(map[x - 1][y]);
		}
		if (x < map.length - 1) {
			ret.add(map[x + 1][y]);
		}
		if (y != 0) {
			ret.add(map[x][y - 1]);
		}
		if (y < map[x].length - 1) {
			ret.add(map[x][y + 1]);
		}
		return ret;
	}

	public List<BattlegroundTile> getAlliedTiles(Unit u, int x, int y) {
		List<BattlegroundTile> adjacent = getAllAdjacentTiles(x, y);
		List<BattlegroundTile> ret = new List<>(4, 4);
		for (int q = 0; q < adjacent.size(); q++) {
			BattlegroundTile adj = adjacent.get(q);
			if (!(adj.isVacant())
					&& adj.getUnit().getAffiliation() == u.getAffiliation()
					&& adj.getUnit() != u) {
				ret.add(adj);
			}
		}
		return ret;
	}

	public List<BattlegroundTile> getAdjacentTilesWithCarriableAllies(Unit u, int x, int y) {
		if (!(u.canCarryUnit()) || u.getPassenger() != null) {
			return new List<>();
		}
		List<BattlegroundTile> adjacent = getAllAdjacentTiles(x, y);
		List<BattlegroundTile> ret = new List<>(4, 4);
		for (int q = 0; q < adjacent.size(); q++) {
			BattlegroundTile adj = adjacent.get(q);
			if (!(adj.isVacant())
					&& adj.getUnit().getAffiliation() == u.getAffiliation()
					&& adj.getUnit() != u
					&& adj.getUnit().canBeCarried()) {
				ret.add(adj);
			}
		}
		return ret;
	}

	public List<BattlegroundTile> getAdjacentAvailableTiles(Unit u, int x, int y) {
		List<BattlegroundTile> adjacent = getAllAdjacentTiles(x, y);
		List<BattlegroundTile> ret = new List<>(4, 4);
		for (int q = 0; q < adjacent.size(); q++) {
			BattlegroundTile adj = adjacent.get(q);
			if (adj.isVacant() || adj.getUnit() == u) {
				ret.add(adj);
			}
		}
		return ret;
	}

}