package buildings.goods_deliverer;

import buildings.Building;
import buildings.Village;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.item.Item;
import location.WorldMapTile;
import unit.Unit;
import unit.human.Human;

public class Farm extends GoodsDeliverer {

	protected int[] assignment;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Farm(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		autoGiveAssignment();
	}
	
	@Override
	public void autoGiveAssignment() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String getType() {
		return Building.FARM;
	}
	
	@Override
	public boolean deliverGoods(Building recipient) {
		boolean ret = false;
		for (int q = 0; q < materials.size(); q++) {
			if (materials.get(q)[2] > 0 && recipient.receiveGoods(materials.get(q).clone())) {
				materials.get(q)[2] = 0;
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public boolean giveGoods(Unit recipient) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void completeDailyAction() {
		if (assignment == null) {
			return;
		}
		// TODO probably rebalance
		float increaseBy = Math.min(Math.round(location.getType().getProliferability() * 100), assignment[2]);
		increaseBy *= percentageHealth();
		boolean accountedFor = false;
		int idxOfProduct = -1;
		for (int q = 0; q < materials.size(); q++) {
			int[] prod = materials.get(q);
			if (prod[0] == assignment[0] && prod[1] == assignment[1]) {
				prod[2] += increaseBy;
				assignment[2] -= increaseBy;
				accountedFor = true;
				idxOfProduct = q;
				break;
			}
		}
		if (!accountedFor) {
			idxOfProduct = materials.size();
			materials.add(new int[] {assignment[0], assignment[1], Math.round(increaseBy)});
			assignment[2] -= increaseBy;
		}
		
		//TODO after updating UI, remove conditional for whether customer is null
		if (continuousDelivery || (customer != null && materials.get(idxOfProduct)[2] == assignment[2])) {
			deliverGoods(customer);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void completeMonthlyAction() {
		if (assignment == null) {
			return;
		}
		// TODO probably rebalance
		float increaseBy = Math.min(Math.round(location.getType().getProliferability() * 3000), assignment[2]);
		increaseBy *= (0.0 + getCurrentHP()) / getMaximumHP();
		boolean accountedFor = false;
		int idxOfProduct = -1;
		for (int q = 0; q < materials.size(); q++) {
			int[] prod = materials.get(q);
			if (prod[0] == assignment[0] && prod[1] == assignment[1]) {
				prod[2] += increaseBy;
				assignment[2] -= increaseBy;
				accountedFor = true;
				idxOfProduct = q;
				break;
			}
		}
		if (!accountedFor) {
			idxOfProduct = materials.size();
			materials.add(new int[] {assignment[0], assignment[1], Math.round(increaseBy)});
			assignment[2] -= increaseBy;
		}
		
		//TODO after updating UI, remove conditional for whether customer is null
		if (continuousDelivery || (customer != null && materials.get(idxOfProduct)[2] == assignment[2])) {
			deliverGoods(customer);
		}
	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		return false;
	}
	
	public void giveAssignment(int[] a) {
		this.assignment = a;
	}
	
	public void giveAssignment(boolean continuous, int[] a, Building destination) {
		this.continuousDelivery = continuous;
		this.assignment = a;
		this.customer = destination;
	}
	
	public int[] getAssignment() {
		return assignment;
	}
	
	public List<Item> possibleProducts() {
		List<Item> ret = new List<>();
		
		List<Item> use = InventoryIndex.index.get(InventoryIndex.USABLECROP);
		for (int q = 0; q < use.size(); q++) {
			ret.add(use.get(q));
		}
		
		List<Item> edi = InventoryIndex.index.get(InventoryIndex.EDIBLECROP);
		for (int q = 0; q < edi.size(); q++) {
			ret.add(edi.get(q));
		}
		
		return ret;
	}

	public List<Building> possibleRecipients() {
		List<Building> ret = new List<>();
		List<Building> b = location.getOwner().getOtherBuildings();
		for (int q = 0; q < b.size(); q++) {
			Building check = b.get(q);
			for (int w = 0; w < materials.size(); w++) {
				if (check.canReceiveGoods(materials.get(w))) {
					ret.add(check);
					break;
				}
			}
		}
		List<Village> v = location.getOwner().getResidentialAreas();
		for (int q = 0; q < v.size(); q++) {
			Building check = v.get(q);
			for (int w = 0; w < materials.size(); w++) {
				if (check.canReceiveGoods(materials.get(w))) {
					ret.add(check);
					break;
				}
			}
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
				break;
			}
		}
		List<Village> v = location.getOwner().getResidentialAreas();
		for (int q = 0; q < v.size(); q++) {
			Building check = v.get(q);
			if (check.canReceiveGoods(itemArray)) {
				ret.add(check);
				break;
			}
		}
		
		return ret;
	}
}
