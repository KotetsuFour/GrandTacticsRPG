package buildings.goods_deliverer;

import buildings.Building;
import inventory.InventoryIndex;
import inventory.item.Resource;
import location.WorldMapTile;
import unit.Unit;
import unit.human.Human;

public class MiningFacility extends GoodsDeliverer {

	protected boolean[] assignment;
	protected WorldMapTile location;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public MiningFacility(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		this.location = location;
		int numResources = InventoryIndex.index.get(InventoryIndex.RESOURCE).size();
		for (int q = 0; q < numResources; q++) {
			materials.add(new int[] {InventoryIndex.RESOURCE, q, 0});
		}
		assignment = new boolean[numResources];
		autoGiveAssignment();
	}
	
	@Override
	public void autoGiveAssignment() {
		for (int q = 0; q < assignment.length; q++) {
			assignment[q] = ((Resource)InventoryIndex.getElement(materials.get(q))).canBeFoundHere(location.getType());
		}
		this.continuousDelivery = true;
	}
	
	@Override
	public String getType() {
		return Building.MINING_FACILITY;
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
		//As incentive to mine different things at different facilities,
		//the more different things you mine at a single facility, the
		//harder it is to mine any individual thing
		float div = 0;
		for (int q = 0; q < assignment.length; q++) {
			if (assignment[q]) {
				div++;
			}
		}
		if (div == 0) div = 1;
		
		double minability = (location.getType().getMinability() * 10) / div; //Since minability is 0-10,
														//Multiplying by 10 makes it a percentage
		minability *= (0.0 + getCurrentHP()) / getMaximumHP(); //Affected by building's percentage HP
		
		for (int q = 0; q < materials.size(); q++) {
			if (assignment[q]) {
				Resource r = (Resource)InventoryIndex.getElement(new int[] {InventoryIndex.RESOURCE, q});
				if (!r.canBeFoundHere(location.getType())) {
					continue;
				}
				materials.get(q)[2] += minability * r.getRarity();
			}
		}
		
		if (customer != null) {
			deliverGoods(customer);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeMonthlyAction() {
		//As incentive to mine different things at different facilities,
		//the more different things you mine at a single facility, the
		//harder it is to mine any individual thing
		float div = 0;
		for (int q = 0; q < assignment.length; q++) {
			if (assignment[q]) {
				div++;
			}
		}
		if (div == 0) div = 1;
		
		//Calculate health only once to be used multiple times
		double percentHealth = percentageHealth();
		
		//Deal with common minable materials first
		double minability = (location.getType().getMinability() * 10) / div; //Since minability is 0-10,
														//Multiplying by 10 makes it a percentage
		minability *= percentHealth; //Affected by building's percentage HP

		for (int q = 0; q < materials.size(); q++) {
			if (assignment[q]) {
				Resource r = (Resource)InventoryIndex.getElement(new int[] {InventoryIndex.RESOURCE, q});
				if (!r.canBeFoundHere(location.getType())) {
					continue;
				}
				materials.get(q)[2] += minability * r.getRarity() * 30;
			}
		}
		
		if (customer != null) {
			deliverGoods(customer);
		}
	}
	
	public boolean isProducing(int idx) {
		return assignment[idx];
	}
	
	public void toggleProduction(int idx) {
		assignment[idx] = !assignment[idx];
	}
	
	public void giveAssignment(Building customer) {
		this.customer = customer;
	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		return false;
	}
}
