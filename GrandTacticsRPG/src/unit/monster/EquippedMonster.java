package unit.monster;

import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.item.Item;
import inventory.staff.Staff;
import inventory.weapon.HandheldWeapon;
import inventory.weapon.StationaryWeapon;
import inventory.weapon.Weapon;
import unit.Equippable;
import unit.UnitClass;
import unit.human.Human;

public abstract class EquippedMonster extends Monster implements Equippable {

	protected int[][] inventory;
	
	protected int[] armor;
	
	protected int[] proficiency;
	
	public static final int RIGHT_ARM = 2;
	public static final int LEFT_ARM = 3;
	
	protected EquippedMonster(int level, UnitClass unitClass, String name, int[] maxHPs, int magic, int skill, int reflex,
			int awareness, int resistance, int movement, int leadership, int[] maxHPGrowths, int magGrowth,
			int sklGrowth, int rfxGrowth, int awrGrowth, int resGrowth, Human master) {
		super(level, unitClass, name, maxHPs, magic, skill, reflex, awareness, resistance, movement, leadership, maxHPGrowths,
				magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth, master);
		// TODO Auto-generated constructor stub
	}
	
	public Item getEquippedItem() {
		//TODO remove try-catch after creating index;
		try {
			return InventoryIndex.getElement(inventory[0]);
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public boolean isUsingMagic() {
		Item i = getEquippedItem();
		return i instanceof Weapon && ((Weapon) i).isMagic();
	}
	
	@Override
	public void useWeapon(boolean hit) {
		// TODO Auto-generated method stub
	}

	@Override
	public int[] getRanges() {
		int wepIdx = 0;
		int wepRng = 1;
		int stfIdx = -1;
		int stfRng = -1;
		for (int q = 0; q < inventory.length; q++) {
			Item i = InventoryIndex.getElement(inventory[q]);
			if (i instanceof HandheldWeapon) {
				int check = ((HandheldWeapon)i).maxRange();
				if (inventory[wepIdx] == null || check > wepRng) {
					wepRng = check;
					wepIdx = q;
				}
			} else if (i instanceof Staff) {
				int check = ((Staff)i).maxRange();
				if (check > stfRng) {
					stfRng = check;
					stfIdx = q;
				}
			}
		}
		int[] ret = {wepIdx, wepRng, stfIdx, stfRng};
		return ret;
	}

	@Override
	public int[][] getInventory() {
		return inventory;
	}
	
	@Override
	public int attackStrength() {
		Item i = getEquippedItem();
		if (i instanceof Weapon) {
			Weapon w = (Weapon) i;
			if (w.isMagic()) {
				return magic + w.getMight();
			}
			return armStrength() + w.getMight();
		}
		return armStrength();
	}

	@Override
	public int armStrength() {
		//Average arms HP / 3 (Just divide by 6 to account for the / 2 and the / 3. Don't change it, future me!)
		return Math.round((float)((0.0 + bodyPartsCurrentHP[RIGHT_ARM] + bodyPartsCurrentHP[LEFT_ARM]) / 6));
	}
	
	@Override
	public int defense(boolean isMagicAttack, int bodyPart) {
		if (isMagicAttack) {
			return resistance;
		}
		Armor a = getArmor();
		if (a == null) {
			return 0;
		}
		return a.getDefenseFor(bodyPart);
	}

	@Override
	public abstract int getBaseAccuracy();
	
	@Override
	public int getBaseCrit() {
		int crit = skill;
		return crit;
	}
	
	@Override
	public int accuracy() {
		int accuracy = getBaseAccuracy();
		
		Item i = getEquippedItem();
		if (i instanceof Weapon) {
			Weapon w = (Weapon) i;
			accuracy += w.getHit();
			accuracy += Math.min(10, proficiency[w.getProficiencyIndex()] - w.getProficiencyRequirement());
		}
		//TODO manage weapon triangle advantage in the external battle manager
		return accuracy;
	}
	
	@Override
	public int criticalHitRate() {
		int crit = getBaseCrit();
		Item i = getEquippedItem();
		if (i instanceof Weapon) {
			Weapon w = (Weapon) i;
			crit += w.getCrit();
			crit += Math.min(10, proficiency[w.getProficiencyIndex()] - w.getProficiencyRequirement());
		}
		return crit;
	}

	@Override
	public int criticalHitAvoid() {
		int avoid = awareness;
		return avoid;
	}
	
	@Override
	public boolean canUseBallista() {
		return armStrength() > 10;
	}

	@Override
	public boolean canUse(StationaryWeapon weapon) {
		if (weapon.isMagic()) {
			return canUseMagicTurrets();
		}
		return canUseBallista();
	}

	@Override
	public boolean canUseMagicTurrets() {
		return magic > 0;
	}

	@Override
	public Armor getArmor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroyArmor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getArmorName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWeaponName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandheldWeapon getEquippedWeapon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void autoEquip() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getEquipmentHeuristic(int[] item) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void equip(int idx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean receiveNewArmor(int[] armor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean receiveNewItem(int[] item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int proficiencyWith(int type) {
		return proficiency[type];
	}

}
