package inventory.item;

import inventory.InventoryIndex;
import location.WorldMapTile.WorldMapTileType;

public class Resource extends Item {

	private int strength;
	
	private int durability;
	
	private float rarity;
	
	private WorldMapTileType[] whereToFind;
	
	public Resource(String name, int approximateWorth, int weight, int strength, int durability,
			float rarity, WorldMapTileType[] whereToFind) {
		super(name, 0, approximateWorth, weight);
		// TODO Auto-generated constructor stub
		this.strength = strength;
		this.durability = durability;
		this.rarity = rarity;
		this.whereToFind = whereToFind;
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.RESOURCE;
	}
	
	/**
	 * Gives the factor for daily chance of finding the resource
	 * @return
	 */
	public float getRarity() {
		return rarity;
	}
	
	/**
	 * Gives specific environments where this can be found, or null if it can be found anywhere
	 * @return
	 */
	public WorldMapTileType[] getPlacesToFind() {
		return whereToFind;
	}
	
	public boolean canBeFoundHere(WorldMapTileType type) {
		if (whereToFind == null) {
			return true;
		}
		for (int q = 0; q < whereToFind.length; q++) {
			if (type == whereToFind[q]) {
				return true;
			}
		}
		return false;
	}
	
	public int getStrength() {
		return strength;
	}
	
	public int getDurability() {
		return durability;
	}

	@Override
	public String[] getInformationDisplayArray(int[] itemArray) {
		// TODO Auto-generated method stub
		return null;
	}
}
