package buildings.goods_deliverer;

import buildings.Building;
import buildings.defendable.Castle;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.item.Item;
import inventory.item.ManufacturableItem;
import inventory.item.UsableItem;
import inventory.weapon.Weapon;
import location.WorldMapTile;
import unit.Unit;
import unit.human.Human;

public class Factory extends GoodsDeliverer {

	protected int[] products;
	protected int[] assignment;
	protected List<int[]> neededIngredients;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Factory(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		products = new int[3];
		//assignment and neededIngredients are initially null
	}

	@Override
	public void autoGiveAssignment() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getType() {
		return Building.FACTORY;
	}
	
	@Override
	public boolean deliverGoods(Building b) {
		if (b.receiveGoods(products)) {
			assignment[2] -= products[2];
			products[2] = 0;
			if (!continuousDelivery) {
				if (assignment[2] == 0) {
					assignment = null;
					neededIngredients = null;
					products = new int[3];
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean giveGoods(Unit recipient) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void completeDailyAction() {
		restockInventory();
		int amountToMake = Math.min(amountProducibleWithResources(), 10);
		if (amountToMake <= 0) {
			return;
		}
		amountToMake = Math.round(amountToMake * percentageHealth());
		if (!continuousDelivery) {
			amountToMake = Math.min(amountToMake, assignment[2] - products[2]);
		}
		if (products == null) {
			products = new int[] {assignment[0], assignment[1], amountToMake};
		} else {
			products[2] += amountToMake;
		}
		
		if (continuousDelivery || products[2] == assignment[2]) {
			deliverGoods(customer);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeMonthlyAction() {
		restockInventory();
		int amountToMake = Math.min(amountProducibleWithResources(), 300);
		if (amountToMake <= 0) {
			return;
		}
		amountToMake = Math.round(amountToMake * percentageHealth());
		if (!continuousDelivery) {
			amountToMake = Math.min(amountToMake, assignment[2] - products[2]);
		}
		if (products == null) {
			products = new int[] {assignment[0], assignment[1], amountToMake};
		} else {
			products[2] += amountToMake;
		}
		
		if (continuousDelivery || products[2] == assignment[2]) {
			deliverGoods(customer);
		}
	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		int type = goods[0];
		return type == InventoryIndex.RESOURCE
				|| type == InventoryIndex.USABLECROP;
	}
	
	@Override
	public boolean receiveGoods(int[] goods) {
		if (!canReceiveGoods(goods)) {
			return false;
		}
		//Deal with needed ingredients first
		if (neededIngredients != null) {
			for (int q = 0; q < neededIngredients.size(); q++) {
				int[] need = neededIngredients.get(q);
				if (InventoryIndex.elementsAreEqual(goods, need)) {
					need[2] -= goods[2];
					if (need[2] <= 0) {
						neededIngredients.remove(q);
						if (neededIngredients.isEmpty()) {
							neededIngredients = null;
						}
					}
					break;
				}
			}
		}
		return super.receiveGoods(goods.clone());
	}
	
	@Override
	public int amountProducibleWithResources() {
		if (assignment == null) {
			return -1;
		}
		Item item = InventoryIndex.getElement(assignment);
		int[][] recipe = ((ManufacturableItem)item).getRecipe();
		int amountToMake = Integer.MAX_VALUE;
		for (int q = 0; q < recipe.length; q++) {
			int[] need = recipe[q];
			boolean exists = false;
			for (int w = 0; w < materials.size(); w++) {
				int[] check = materials.get(w);
				if (InventoryIndex.elementsAreEqual(check, need)) {
					amountToMake = Math.min(amountToMake, check[2] / need[2]);
					exists = true;
					break;
				}
			}
			if (!exists) {
				amountToMake = 0;
				break;
			}
		}
		return amountToMake;
	}

	public void giveAssignment(int[] a) {
		this.assignment = a;
		products[0] = assignment[0];
		products[1] = assignment[1];
		products[2] = 0;
		
		neededIngredients = new List<>();
		
		Item item = InventoryIndex.getElement(a);
		int[][] recipe = ((ManufacturableItem)item).getRecipe();
		for (int q = 0; q < recipe.length; q++) {
			int[] ingredient = recipe[q];
			for (int w = 0; w < materials.size(); w++) {
				int[] avail = materials.get(w);
				if (InventoryIndex.elementsAreEqual(ingredient, avail)) {
					int needed = (ingredient[2] * assignment[2]) - avail[2];
					if (needed > 0) {
						neededIngredients.add(new int[] {ingredient[0], ingredient[1], needed});
					}
					break;
				}
			}
		}
	}
	
	public void giveAssignment(boolean continuous, int[] a, Building destination) {
		this.continuousDelivery = continuous;
		this.assignment = a;
		this.customer = destination;
		products[0] = assignment[0];
		products[1] = assignment[1];
		products[2] = 0;
		
		neededIngredients = new List<>();
		
		Item item = InventoryIndex.getElement(a);
		int[][] recipe = ((ManufacturableItem)item).getRecipe();
		for (int q = 0; q < recipe.length; q++) {
			int[] ingredient = recipe[q];
			for (int w = 0; w < materials.size(); w++) {
				int[] avail = materials.get(w);
				if (InventoryIndex.elementsAreEqual(ingredient, avail)) {
					int needed = (ingredient[2] * assignment[2]) - avail[2];
					if (needed > 0) {
						neededIngredients.add(new int[] {ingredient[0], ingredient[1], needed});
					}
					break;
				}
			}
		}
	}
	
	public int[] getAssignment() {
		return assignment;
	}
	
	public List<Item> possibleProducts() {
		List<Item> ret = new List<>();
		
		//Can produce non-magic handheld weapons
		List<Item> wep = InventoryIndex.index.get(InventoryIndex.HANDHELD_WEAPON);
		for (int q = 0; q < wep.size(); q++) {
			Weapon w = (Weapon)wep.get(q);
			if (!w.isMagic()) {
				ret.add(w);
			}
		}
		
		//Can produce non-magic stationary weapons
		List<Item> stn = InventoryIndex.index.get(InventoryIndex.STATIONARY_WEAPON);
		for (int q = 0; q < stn.size(); q++) {
			Weapon w = (Weapon)stn.get(q);
			if (!w.isMagic()) {
				ret.add(w);
			}
		}
		
		//Can produce armor
		List<Item> arm = InventoryIndex.index.get(InventoryIndex.ARMOR);
		for (int q = 0; q < arm.size(); q++) {
			Armor w = (Armor)arm.get(q);
			ret.add(w);
		}
		
		//Can produce usable items such as medicines
		List<Item> use = InventoryIndex.index.get(InventoryIndex.USABLE_ITEM);
		for (int q = 0; q < use.size(); q++) {
			UsableItem w = (UsableItem)use.get(q);
			ret.add(w);
		}
		
		return ret;
	}
	
	public int[] getProducts() {
		return products;
	}
	
	@Override
	public List<Building> possibleRecipients() {
		List<Building> ret = new List<>();
		List<Building> b = location.getOwner().getOtherBuildings();
		for (int q = 0; q < b.size(); q++) {
			Building check = b.get(q);
			if (check != this && check.canReceiveGoods(products)) {
				ret.add(check);
			}
		}
		List<Castle> c = location.getOwner().getNobleResidences();
		for (int q = 0; q < c.size(); q++) {
			ret.add(c.get(q));
		}
		return ret;
	}

	@Override
	public List<Building> possibleRecipientsOfItem(Item item) {
		List<Building> ret = new List<>();
		List<Building> b = location.getOwner().getOtherBuildings();
		int[] itemArray = new int[] {item.getGeneralItemId(), item.getSpecificItemId()};
		for (int q = 0; q < b.size(); q++) {
			Building check = b.get(q);
			if (check != this && check.canReceiveGoods(itemArray)) {
				ret.add(check);
			}
		}
		List<Castle> c = location.getOwner().getNobleResidences();
		for (int q = 0; q < c.size(); q++) {
			ret.add(c.get(q));
		}
		return ret;
	}
	
	@Override
	public List<int[]> getStorehouseNeeds() {
		return neededIngredients;
	}
}
