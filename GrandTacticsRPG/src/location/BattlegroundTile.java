package location;

import unit.Unit;

public class BattlegroundTile {

	private BFTileOccupant occupant;
	private Unit unit;
	private BattlegroundTileType type;
	
	public BattlegroundTile (BattlegroundTileType type) {
		this.type = type;
	}
	
	public BattlegroundTileType getType() {
		return type;
	}
	
	public int avoidanceBonus() {
		return type.getAvoidanceBonus();
	}
	
	public int moveCostOnFoot() {
		return type.getMoveCostOnFoot();
	}
	
	public int moveCostInAir() {
		return type.getMoveCostInAir();
	}
	
	public int getMoveCost(Unit u) {
		if (u.canFly()) {
			return moveCostInAir();
		}
		return moveCostOnFoot();
	}
	
	public void placeUnit(Unit u) {
		this.unit = u;
	}
	
	public void placeOccupant(BFTileOccupant bfTileOccupant) {
		if (bfTileOccupant instanceof Unit) {
			placeUnit((Unit)bfTileOccupant);
		} else if (bfTileOccupant != null) {
			occupant = bfTileOccupant;
		}
		
	}

	public Unit getUnit() {
		return unit;
	}
	public void removeUnit() {
		unit = null;
	}
	public BFTileOccupant getInanimateObjectOccupant() {
		return occupant;
	}
	
	public void removeInanimateObjectOccupant() {
		occupant = null;
	}
	
	public boolean isVacant() {
		return unit == null;
	}
	
	public enum BattlegroundTileType {
		GRASS("Grass", 1, 1, 0),
		SAND("Sand", 2, 1, 5),
		TREE("Tree", 2, 1, 20),
		THICKET("Thicket", 6, 1, 40),
		MOUNTAIN("Mountain", 4, 1, 30),
		PEAK("Peak", 4, 1, 40),
		HOUSE("House", 1, 1, 10),
		HOUSE_DOOR("Door", 1, 1, 10), //Part of a house that can be interacted with
		RUBBLE("Rubble", 2, 1, 0),
		PILLAR("Pillar", 2, 6, 20),
		GATE("Gate", 1, 1, 20),
		WALL("Wall", Integer.MAX_VALUE, 1, 0),
		SHALLOW_WATER("Shallow Water", 3, 1, 10),
		DEEP_WATER("Deep Water", Integer.MAX_VALUE, 1, 10),
		CAVE("Cave", 1, 6, 10),
		MAGMA("Molten Rock", 3, 3, 10),
		FLOOR("Floor", 1, 5, 0),
		THRONE("Throne", 1, 5, 30),
		CHEST("Chest", 1, 5, 0),
		WETLAND("Wetland", 2, 1, 5),
		WASTELAND("Wasteland", 1, 1, 0),
		ROAD("Road", 1, 1, 0),
		WARP_TILE("Warp Tile", 2, 3, 0),
		DECK("Deck", 1, 1, 0),
		DOCK("Dock", 1, 1, 5),
		SNOW("Snow", 2, 1, 5),
//		STRUCTURE("")
		;
		private String name;
		private int moveCostOnFoot;
		private int moveCostInAir;
		private int avoidanceBonus;
		private BattlegroundTileType(String name, int moveCostOnFoot, int moveCostInAir,
				int avoidanceBonus) {
			this.name = name;
			this.moveCostOnFoot = moveCostOnFoot;
			this.moveCostInAir = moveCostInAir;
			this.avoidanceBonus = avoidanceBonus;
		}
		public String getName() {
			return name;
		}
		public int getMoveCostOnFoot() {
			return moveCostOnFoot;
		}
		public int getMoveCostInAir() {
			return moveCostInAir;
		}
		public int getAvoidanceBonus() {
			return avoidanceBonus;
		}
	}
}
