package buildings;

import affiliation.Nation;
import buildings.goods_deliverer.Storehouse;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.item.UsableCrop;
import inventory.item.UsableItem;
import inventory.staff.Staff;
import inventory.staff.SupportStaff;
import unit.human.Human;

public class Hospital extends Building {

	protected List<Human> patients;
	protected int healingPower;
	protected int curingPower;
	
	public static final int HEALING_NEEDED = 1000;
	public static final int CURING_NEEDED = 100;
	
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Hospital(String name, Human owner) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner);
		patients = new List<>();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getType() {
		return Building.HOSPITAL;
	}
	
	@Override
	public void completeDailyAction() {
		restockInventory();
		for (int q = 0; q < patients.size() && healingPower > 0; q++) {
			Human h = patients.get(q);
			int[] hps = h.getBodyPartsCurrentHP();
			for (int w = 0; w < hps.length && healingPower > 0; w++) {
				h.getStatusEffects()[Staff.INJURY] = 0;
				if (h.getCurrentHPOfBodyPart(w) < h.getMaximumHPOfBodyPart(w)) {
					h.heal(w, 2);
					healingPower -= 2;
				}
			}
		}
		cureEffects();
	}
	
	@Override
	public void completeMonthlyAction() {
		restockInventory();
		for (int q = 0; q < patients.size() && healingPower > 0; q++) {
			Human h = patients.get(q);
			int[] hps = h.getBodyPartsCurrentHP();
			for (int w = 0; w < hps.length && healingPower > 0; w++) {
				int toHeal = Math.min(h.getMaximumHPOfBodyPart(w) - h.getCurrentHPOfBodyPart(w),
						healingPower);
				h.heal(w, toHeal);
				healingPower -= toHeal;
			}
		}
		cureEffects();
	}
	
	public void restockInventory() {
		if ((healingPower < HEALING_NEEDED || curingPower < CURING_NEEDED) && owner != null) {
			List<Building> b = owner.getHome().getOtherBuildings();
			for (int q = 0; q < b.size(); q++) {
				if (b.get(q) instanceof Storehouse) {
					Storehouse s = (Storehouse)b.get(q);
					List<int[]> store = s.getMaterials();
					for (int w = 0; w < store.size(); w++) {
						int[] itemArray = store.get(w);
						if (itemArray[0] == InventoryIndex.USABLE_ITEM) {
							UsableItem item = (UsableItem) InventoryIndex.getElement(itemArray);
							//TODO find out how many you need to get healingPower and curingPower
							// up to acceptable values and take that many
						} else if (itemArray[0] == InventoryIndex.USABLECROP) {
							UsableCrop item = (UsableCrop) InventoryIndex.getElement(itemArray);
							//TODO find out how many you need to get healingPower and curingPower
							// up to acceptable values and take that many
						} else if (itemArray[0] == InventoryIndex.SUPPORT_STAFF) {
							SupportStaff item = (SupportStaff) InventoryIndex.getElement(itemArray);
							//TODO find out how many you need to get healingPower and curingPower
							// up to acceptable values and take that many
						}
					}
				}
			}
		}
	}
	
	private void cureEffects() {
		for (int q = 0; q < patients.size() && curingPower > 0; q++) {
			Human h = patients.get(q);
			int[] status = h.getStatusEffects();
			if (curingPower > 0 && status[Staff.BERSERK] > 0) {
				int cure = Math.min(curingPower, status[Staff.BERSERK]);
				status[Staff.BERSERK] -= cure;
				//TODO take off berserk list
				curingPower -= cure;
			}
			if (curingPower > 0 && status[Staff.POISON] > 0) {
				int cure = Math.min(curingPower, status[Staff.POISON]);
				status[Staff.POISON] -= cure;
				//TODO take off poison list
				curingPower -= cure;
			}
			if (curingPower > 0 && status[Staff.SLEEP] > 0) {
				int cure = Math.min(curingPower, status[Staff.SLEEP]);
				status[Staff.SLEEP] -= cure;
				//TODO take off sleep list
				curingPower -= cure;
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		return goods[0] == InventoryIndex.USABLE_ITEM
				|| (goods[0] == InventoryIndex.USABLECROP && !((UsableCrop)InventoryIndex.getElement(goods)).isUsedInBuilding())
				|| goods[0] == InventoryIndex.SUPPORT_STAFF;
	}
	
	@Override
	public boolean receiveGoods(int[] goods) {
		if (!canReceiveGoods(goods)) {
			return false;
		}
		if (goods[0] == InventoryIndex.USABLE_ITEM) {
			//TODO if it's an antidote, add the strength to curing power
			//TODO if it's a vulnerary, add the strength to healing power
		} else if (goods[0] == InventoryIndex.SUPPORT_STAFF) {
			//TODO if it's a healing staff, add the strength to healing power
			//TODO if it's a restoring staff, add the strength to curing power
		} else if (goods[0] == InventoryIndex.USABLECROP) {
			//TODO add the strength to healing power
		}
		return true;
	}

	@Override
	public void defect(Nation n) {
		// TODO deal with patients as well as owner
		
	}

	@Override
	public List<int[]> getStorehouseNeeds() {
		// TODO Auto-generated method stub
		return null;
	}

}
