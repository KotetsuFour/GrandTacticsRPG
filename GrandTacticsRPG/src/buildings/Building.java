package buildings;

import affiliation.CityState;
import affiliation.Nation;
import buildings.goods_deliverer.Storehouse;
import data_structures.List;
import inventory.InventoryIndex;
import unit.human.Human;

public abstract class Building {

	protected String name;
	protected int structuralIntegrity;
	protected int maxStructuralIntegrity;
	protected int durability;
	protected int resistance;
	protected Human owner;
	protected List<int[]> materials;
	
	public static final String COLISEUM = "Coliseum";
	public static final String HOSPITAL = "Hospital";
	public static final String PORT = "Port";
	public static final String RESEARCH_CENTER = "Research Center";
	public static final String SHIPYARD = "Shipyard";
	public static final String VILLAGE = "Village";
	public static final String WARP_PAD = "Warp Pad";
	public static final String BARRACKS = "Barracks";
	public static final String CASTLE = "Castle";
	public static final String FORTRESS = "Fortress";
	public static final String PRISON = "Prison";
	public static final String TRAINING_FACILITY = "Training Facility";
	public static final String FACTORY = "Factory";
	public static final String FARM = "Farm";
	public static final String MAGIC_PROCESSING_FACILITY = "Magic Processing Facility";
	public static final String MINING_FACILITY = "Mining Facility";
	public static final String RANCH = "Ranch";
	public static final String STOREHOUSE = "Storehouse";
	public static final String TRADE_CENTER = "Trade Center";
	
	public Building(String name, int maxStructuralIntegrity, int durability, int resistance,
			Human owner) {
		this.name = name;
		this.maxStructuralIntegrity = maxStructuralIntegrity;
		this.structuralIntegrity = this.maxStructuralIntegrity;
		this.durability = durability;
		this.resistance = resistance;
		this.owner = owner;
		this.materials = new List<>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getNameAndType() {
		return String.format("%s (%s)", name, getType());
	}
	
	public abstract String getType();
	
	public Human getOwner() {
		return owner;
	}
	
	public void removeOwner(CityState cs) {
		owner = Human.completelyRandomHuman(cs);
	}
	
	public void defect(Nation n) {
		owner.defect(n);
	}
	
	public boolean takeDamage(boolean isMagicAttack, int damage) {
		if (isMagicAttack) {
			damage -= getResistance();
		} else {
			damage -= getDurability();
		}
		if (damage > 0) {
			structuralIntegrity -= damage;
			return structuralIntegrity > 0;
		}
		return true;
	}
	
	public void rename(String newName) {
		this.name = newName;
	}
	
	public abstract void completeDailyAction();
	
	public abstract void completeMonthlyAction();
	
	public int getCurrentHP() {
		return this.structuralIntegrity;
	}
	
	public int getMaximumHP() {
		return this.maxStructuralIntegrity;
	}
	
	public abstract void destroy();
	
	public boolean receiveGoods(int[] goods) {
		if (!canReceiveGoods(goods)) {
			return false;
		}
		for (int q = 0; q < materials.size(); q++) {
			int[] m = materials.get(q);
			if (InventoryIndex.elementsAreEqual(m, goods)) {
				m[2] += goods[2];
				return true;
			}
		}
		materials.add(goods.clone());
		return true;
	}
	public abstract boolean canReceiveGoods(int[] goods);
	
	public List<int[]> getMaterials() {
		return materials;
	}
	
	public int getDurability() {
		return durability;
	}
	public int getResistance() {
		return resistance;
	}
	
	public float percentageHealth() {
		return (float)(0.0 + getCurrentHP()) / getMaximumHP();
	}
	
	@Override
	public String toString() {
		return getNameAndType();
	}

	/**
	 * Gives a list of materials that the building requires from a storehouse
	 * in order to do its tasks
	 * 
	 * By default, this returns null to indicate that no materials are needed
	 * @return
	 */
	public List<int[]> getStorehouseNeeds() {
		return null;
	}
	
	/**
	 * Get needs from storehouse
	 * By default, this looks at storehouse needs and meets them as stated
	 * 
	 * Note that some buildings will have more abstract needs
	 *  and thus will override this method
	 */
	public void restockInventory() {
		List<int[]> needs = getStorehouseNeeds();
		if (needs != null && owner != null) {
			List<Building> b = owner.getHome().getOtherBuildings();
			for (int q = 0; q < b.size() && !needs.isEmpty(); q++) {
				if (b.get(q) instanceof Storehouse) {
					Storehouse s = (Storehouse)b.get(q);
					List<int[]> store = s.getMaterials();
					//TODO if you cannot find the item in any storehouses, submit a request to
					// ONE of them to get the item from another storehouse in the nation
					for (int w = 0; w < needs.size(); w++) {
						for (int e = 0; e < store.size(); e++) {
							//Once you find the item in the inventory
							if (InventoryIndex.elementsAreEqual(needs.get(w), store.get(e))) {
								int[] itemArray = needs.get(w);
								
								//Make array for transferring materials
								int[] transfer = {itemArray[0], itemArray[1], Math.min(itemArray[2], store.get(e)[2])};
								
								//Remove from needs and store the materials that are being transfered
								store.get(e)[2] -= transfer[2];
								itemArray[2] -= transfer[2];
								
								//If the need has been completely met, remove this from the list
								if (itemArray[2] == 0) {
									needs.remove(w);
									w--;
								}
								break;
							}
						}
					}
				}
			}
		}
	}
}
