package buildings.goods_deliverer;

import buildings.Building;
import buildings.defendable.Castle;
import buildings.defendable.TrainingFacility;
import data_structures.List;
import inventory.item.Item;
import location.WorldMapTile;
import unit.Unit;
import unit.UnitClass.Mount;
import unit.human.Human;
import util.RNGStuff;

public class Ranch extends GoodsDeliverer {

	protected int products;
	protected int assignedAnimal;
	protected int assignedAmount;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Ranch(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		products = 0;
		assignedAnimal = -1;
		assignedAmount = -1;
		autoGiveAssignment();
	}
	
	@Override
	public void autoGiveAssignment() {
		Mount[] ms = Mount.values();
		int type = 0;
		double heur = -1;
		for (int q = 0; q < ms.length; q++) {
			double check = ms[q].getRearingFactor(location.getType());
			if (check > heur) {
				type = q;
				heur = check;
			}
		}
		List<Building> b = location.getOwner().getOtherBuildings();
		Building dest = null;
		for (int q = 0; q < b.size(); q++) {
			Building cur = b.get(q);
			if (cur instanceof TrainingFacility && cur != customer) {
				if (customer == null || customer instanceof Castle
						|| (customer instanceof TradeCenter/**TODO AND TradeCenter does not need animals*/)
						|| (customer instanceof TrainingFacility && ((TrainingFacility)customer).getMounts()[type] < 20)) {
					dest = cur;
				}
			} else if (cur instanceof TradeCenter && cur != customer) {
				if (customer == null || customer instanceof Castle
						|| (customer instanceof TradeCenter/**TODO AND TradeCenter does not need animals*/)
						|| (customer instanceof TrainingFacility /**TODO AND cur does not need animals*/)) {
					dest = cur;
				}
			}
		}
		giveAssignment(true, type, 0, dest);
	}
	
	@Override
	public String getType() {
		return Building.RANCH;
	}
	
	@Override
	public boolean deliverGoods(Building recipient) {
		if (recipient instanceof TrainingFacility) {
			TrainingFacility tf = (TrainingFacility)recipient;
			int amount = Math.min(products, TrainingFacility.MAXIMUM_ANIMAL_COUNT - tf.getMounts()[assignedAnimal]);
			tf.getMounts()[assignedAnimal] += amount;
			assignedAmount -= amount;
			products -= amount;
			return true;
		}
		if (recipient instanceof Castle) {
			Castle c = (Castle)recipient;
			int amount = Math.min(products, Castle.MAXIMUM_ANIMAL_COUNT - c.getMounts()[assignedAnimal]);
			c.getMounts()[assignedAnimal] += amount;
			assignedAmount -= amount;
			products -= amount;
			return true;
		}
		if (recipient instanceof TradeCenter) {
			TradeCenter tc = (TradeCenter)recipient;
			int amount = Math.min(products, TradeCenter.MAXIMUM_ANIMAL_COUNT - tc.getMounts()[assignedAnimal]);
			tc.getMounts()[assignedAnimal] += amount;
			assignedAmount -= amount;
			products -= amount;
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
		//TODO maybe rebalance
		if (products < assignedAmount) {
			Mount m = Mount.values()[assignedAnimal];
			float growthFactor = m.getRearingFactor(location.getType());
			growthFactor *= (percentageHealth() / 30);
			if (RNGStuff.random0To99() < growthFactor * 100) {
				products++;
			}
		}
		
		if (continuousDelivery || (assignedAnimal == products && customer != null)) {
			deliverGoods(customer);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void completeMonthlyAction() {
		//TODO maybe rebalance
		if (products < assignedAmount) {
			Mount m = Mount.values()[assignedAnimal];
			float growthFactor = m.getRearingFactor(location.getType());
			growthFactor *= percentageHealth();
			products += Math.min(assignedAmount - products, (growthFactor * 20));
		}
		
		if (continuousDelivery || (assignedAnimal == products && customer != null)) {
			deliverGoods(customer);
		}
	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		return false;
	}

	@Override
	public int amountProducibleWithResources() {
		//TODO
		return -1;
	}

	@Override
	public List<Building> possibleRecipients() {
		List<Building> ret = new List<>();
		List<Building> b = location.getOwner().getOtherBuildings();
		for (int q = 0; q < b.size(); q++) {
			Building check = b.get(q);
			if (check instanceof TradeCenter || check instanceof TrainingFacility) {
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
		//This building doesn't produce items, and all buildings that accept any animals
		//accept all animals, so this method does the same as possibleRecipients
		return possibleRecipients();
	}
	
	public int[] getAssignment() {
		if (assignedAnimal < 0) {
			return null;
		}
		return new int[] {assignedAnimal, assignedAmount};
	}
	
	public void giveAssignment(int type, int amount) {
		this.assignedAnimal = type;
		this.assignedAmount = amount;
	}
	
	public void giveAssignment(boolean continuous, int type, int amount, Building destination) {
		this.continuousDelivery = continuous;
		this.assignedAnimal = type;
		this.assignedAmount = amount;
		this.customer = destination;
	}

	public int getNumAnimals() {
		return products;
	}

	@Override
	public List<int[]> getStorehouseNeeds() {
		// TODO Auto-generated method stub
		return null;
	}

}
