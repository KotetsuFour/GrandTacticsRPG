package inventory;

import data_structures.List;
import inventory.item.Armor;
import inventory.item.UsableCrop;
import inventory.item.EdibleCrop;
import inventory.item.Item;
import inventory.item.Resource;
import inventory.weapon.HandheldWeapon;
import inventory.weapon.StationaryWeapon;
import inventory.weapon.Weapon;
import location.WorldMapTile.WorldMapTileType;
import reference.UnitClassIndex;

public class InventoryIndex {

	public static List<List<Item>> index;
	
	public static final int RESOURCE = 0;
	public static final int USABLE_ITEM = 1;
	public static final int ARMOR = 2;
	public static final int HANDHELD_WEAPON = 3;
	public static final int STATIONARY_WEAPON = 4;
	public static final int STATIONARY_STAFF = 5;
	public static final int OFFENSIVE_STAFF = 6;
	public static final int SUPPORT_STAFF = 7;
	public static final int TILE_STAFF = 8;
	public static final int USABLECROP = 9;
	public static final int EDIBLECROP = 10;
	
	public static void initialize() {
		index = new List<>(11, 11);
		for (int q = 0; q < index.limit(); q++) {
			index.add(new List<Item>());
		}
		generateResources();
		generateStandardWeapons();
		generateStandardArmors();
		generateCrops();
		generateEdibleCrops();
		//TODO initialize other item types
	}
	
	public static Item getElement(int[] reference) {
		//TODO make sure index is constructed by the time this is accessed so no
		//exceptions have to be thrown
		if (reference == null) {
			return null;
		}
		try {
			return index.get(reference[0]).get(reference[1]);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public static int[] newInstanceOfItem(int[] reference) {
		Item item = getElement(reference);
		int[] ret = {reference[0], reference[1], item.getInitialUses()};
		return ret;
	}
	
	public static boolean elementsAreEqual(int[] i1, int[] i2) {
		return i1[0] == i2[0] && i1[1] == i2[1];
	}
	
	public static boolean moveItemToInventory(List<int[]> inv, int[] item) {
		for (int q = 0; q < inv.size(); q++) {
			if (elementsAreEqual(inv.get(q), item)) {
				inv.get(q)[2] += item[2];
				item[2] = 0;
				return true;
			}
		}
		inv.add(item.clone());
		item[2] = 0;
		return false;
	}
	
	private static void addItem(int type, Item i) {
		i.setSpecificItemId(index.get(type).size());
		index.get(type).add(i);
	}
	
	private static void addResource(Resource r) {
		addItem(RESOURCE, r);
	}
	private static void addHandheldWeapon(HandheldWeapon w) {
		addItem(HANDHELD_WEAPON, w);
	}
	private static void addStationaryWeapon(StationaryWeapon w) {
		addItem(STATIONARY_WEAPON, w);
	}
	private static void addArmor(Armor a) {
		addItem(ARMOR, a);
	}
	private static void addUsableCrop(UsableCrop c) {
		addItem(USABLECROP, c);
	}
	private static void addEdibleCrop(EdibleCrop c) {
		addItem(EDIBLECROP, c);
	}
	
	private static void generateResources() {
		//TODO fix stats
		//worth, weight, strength, durability
		//TODO probably rebalance rarities
		Resource stone = new Resource("Stone", 0, 0, 0, 0, 2, null);
		addResource(stone);
		Resource clay = new Resource("Clay", 0, 0, 0, 0, 1, new WorldMapTileType[] {WorldMapTileType.SWAMP, WorldMapTileType.SNOWY_PLAIN});
		addResource(clay);
		Resource bronze = new Resource("Bronze", 0, 0, 0, 0, (float)0.4, null);
		addResource(bronze);
		Resource iron = new Resource("Iron", 0, 0, 0, 0, (float)0.3, null);
		addResource(iron);
		Resource steel = new Resource("Steel", 0, 0, 0, 0, (float)0.2, null);
		addResource(steel);
		Resource silver = new Resource("Silver", 0, 0, 0, 0, (float)0.08, null);
		addResource(silver);
		Resource gold = new Resource("Gold", 0, 0, 0, 0, (float)0.02, null);
		addResource(gold);
		Resource obsidian = new Resource("Obsidian", 0, 0, 0, 0, (float)0.1, new WorldMapTileType[] {WorldMapTileType.MOUNTAIN, WorldMapTileType.SNOWY_MOUNTAIN});
		addResource(obsidian);
		Resource glass = new Resource("Glass", 0, 0, 0, 0, (float)0.2, new WorldMapTileType[] {WorldMapTileType.DESERT});
		addResource(glass);
	}
	private static void generateStandardWeapons() {
		//TODO make recipes and adjust stats as appropriate
		int e = 20;
		int d = 40;
		int c = 90;
		int b = 135;
		int a = 200;
		int s = 290;
		/**
		 * Proficiency index is
		 * 0 for sword
		 * 1 for lance
		 * 2 for axe
		 * 3 for bow
		 * 4 for knife
		 * 5 for ballista
		 * 6 for anima
		 * 7 for light
		 * 8 for dark
		 */
		//proficiencyRequirement, minRange, maxRange, might, hit, crit, isMagic, proficiencyIndex,
		//recipe, initialUses, approximateWorth, weight
		HandheldWeapon ironSword = new HandheldWeapon("Iron Sword", d, 1, 6, 80, 0, false, Weapon.SWORD,
				new int[][] {}, 50, 0, 3);
		addHandheldWeapon(ironSword);
		HandheldWeapon ironLance = new HandheldWeapon("Iron Lance", d, 1, 12, 80, 0, false, Weapon.LANCE,
				new int[][] {}, 50, 0, 12);
		addHandheldWeapon(ironLance);
		HandheldWeapon ironAxe = new HandheldWeapon("Iron Axe", d, 1, 14, 70, 0, false, Weapon.AXE,
				new int[][] {}, 50, 0, 18);
		addHandheldWeapon(ironAxe);
		HandheldWeapon ironBow = new HandheldWeapon("Iron Bow", d, 3, 10, 70, 0, false, Weapon.BOW,
				new int[][] {}, 50, 0, 8);
		addHandheldWeapon(ironBow);
		HandheldWeapon ironKnife = new HandheldWeapon("Iron Knife", d, 2, 2, 65, 0, false, Weapon.KNIFE,
				new int[][] {}, 50, 0, 2);
		addHandheldWeapon(ironKnife);
		StationaryWeapon ironBallista = new StationaryWeapon("Iron Ballista", d, 3, 10, 25, 60, 0, false, Weapon.BALLISTA,
				new int[][] {}, 50, 0, 30, 20, 0, 10);
		addStationaryWeapon(ironBallista);
		HandheldWeapon fire = new HandheldWeapon("Fire", e, 2, 10, 70, 0, true, Weapon.ANIMA,
				new int[][] {}, 50, 0, 12);
		addHandheldWeapon(fire);
		HandheldWeapon light = new HandheldWeapon("Light", e, 2, 5, 80, 0, true, Weapon.LIGHT,
				new int[][] {}, 50, 0, 5);
		addHandheldWeapon(light);
		HandheldWeapon dark = new HandheldWeapon("Dark", e, 2, 14, 60, 0, true, Weapon.DARK,
				new int[][] {}, 50, 0, 12);
		addHandheldWeapon(dark);
	}
	
	private static void generateStandardArmors() {
		//TODO redo this with actual stats and recipes
		int[] ironDefs = {0, 5, 0, 0, 0, 0, 0, 0, 0};
		Armor ironChestplate = new Armor("Iron Chestplate", 50, 0, 5, ironDefs,
				UnitClassIndex.HUMAN, new int[][] {}, null);
		addArmor(ironChestplate);
	}
	
	private static void generateCrops() {
		UsableCrop wood = new UsableCrop("Wood", 0, 0, 0, true);
		addUsableCrop(wood);
		UsableCrop herb = new UsableCrop("Medicinal Herb", 0, 0, 0, false);
		addUsableCrop(herb);
	}
	
	private static void generateEdibleCrops() {
		EdibleCrop grain = new EdibleCrop("Grain", 0, 0, 0);
		addEdibleCrop(grain);
		EdibleCrop fruit = new EdibleCrop("Fruit", 0, 0, 0);
		addEdibleCrop(fruit);
		EdibleCrop veg = new EdibleCrop("Vegetable", 0, 0, 0);
		addEdibleCrop(veg);
	}
	
}
