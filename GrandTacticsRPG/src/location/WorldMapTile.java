package location;

import affiliation.CityState;
import affiliation.Nation;
import battle.BattleGround;
import buildings.Building;
import javafx.scene.paint.Color;
import ship.Ship;
import unit.UnitGroup;

public class WorldMapTile {

	private WorldMapTileType type;
	private Building building;
	private BattleGround battle;
	private CityState owner;
	private int magicPotency;
	private int magicType;
	private WMTileOccupant groupPresent;
	
	public WorldMapTile(WorldMapTileType type, int magicPotency, int magicType) {
		this.type = type;
		this.magicPotency = magicPotency;
		this.magicType = magicType;
	}
	
	public boolean isVacant() {
		return groupPresent == null;
	}
	
	public void sendHere(UnitGroup group) {
		if (group.getLocation() != null) {
			group.getLocation().removeGroupOrShip();
		}
		this.groupPresent = group;
		group.sendTo(this);
	}
	
	public int getMoveCost(UnitGroup group) {
		if (group.canFly()) {
			return 1;
		}
		return type.moveCostOnFoot();
	}
	
	public int getMoveCost(Ship group) {
		if (type == WorldMapTileType.DEEP_WATER) {
			return 1;
		} else if (type == WorldMapTileType.SHALLOW_WATER) {
			return 2;
		}
		return Integer.MAX_VALUE;
	}

	public void removeGroupOrShip() {
		this.groupPresent = null;
	}
	
	public WMTileOccupant getGroupPresent() {
		return groupPresent;
	}
	
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building b) {
		this.building = b;
	}
	
	public CityState getOwner() {
		return owner;
	}
	public void setOwner(CityState owner) {
		this.owner = owner;
	}
	public String getMagicTypeAsString() {
		if (magicType == 0) {
			return "Earth";
		}
		if (magicType == 1) {
			return "Light";
		}
		if (magicType == 2) {
			return "Dark";
		}
		return "None";
	}
	public int getMagicPotency() {
		return magicPotency;
	}

	public void setBattle(BattleGround battle) {
		this.battle = battle;
		if (groupPresent instanceof UnitGroup) {
			removeGroupOrShip();
		}
	}

	public BattleGround getBattle() {
		return battle;
	}

	public Nation getAffiliation() {
		if (owner == null) {
			return null;
		}
		return owner.getNation();
	}
	
	public WorldMapTileType getType() {
		return type;
	}

	public enum WorldMapTileType {
		PLAIN("Plain", 1, 1, 6, Color.LAWNGREEN),
		DESERT("Desert", 2, 0.2, 7, Color.YELLOW),
		FOREST("Forest", 2, 7, 6, Color.FORESTGREEN),
		DENSE_FOREST("Dense Forest", 5, 0.5, 4, Color.DARKGREEN),
		MOUNTAIN("Mountain", 4, 5, 10, Color.MEDIUMPURPLE),
		SHALLOW_WATER("Shallow Water", 5, 0.3, 1, Color.LIGHTBLUE),
		DEEP_WATER("Deep Water", Integer.MAX_VALUE, 0, 0, Color.BLUE),
		SNOWY_PLAIN("Snowy Plain", 1, 0.3, 5, Color.WHITE),
		SNOWY_MOUNTAIN("Snowy Mountain", 4, 0.3, 8, Color.CADETBLUE),
		SWAMP("Swamp", 4, 0.5, 4, Color.DARKOLIVEGREEN),
		WASTELAND("Wasteland", 1, 0, 5, Color.TAN),
		GLACIER("Glacier", 2, 0.2, 2, Color.ALICEBLUE)
		;
		
		private String name;
		private int moveCost;
		private double proliferability;
		private int minability;
		private Color displayColor;
		private WorldMapTileType(String name, int moveCost, double proliferability,
				int minability, Color displayColor) {
			this.name = name;
			this.moveCost = moveCost;
			//It is easier to grow certain animals and plants depending on the climate
			//0 means nothing can be grown. Otherwise...
			//Multiple of 2 means temperate, multiple of 3 is cold, 5 is hot, 7 is dry,
			//11 is wet, 13 is elevated, 17 is dark
			this.proliferability = proliferability;
			this.minability = minability;
			this.displayColor = displayColor;
		}
		
		public String getName() {
			return name;
		}
		
		public int moveCostOnFoot() {
			return moveCost;
		}
		
		public int getMinability() {
			return minability;
		}
		public double getProliferability() {
			return proliferability;
		}
		public Color getDisplayColor() {
			return displayColor;
		}
	}
	
}
