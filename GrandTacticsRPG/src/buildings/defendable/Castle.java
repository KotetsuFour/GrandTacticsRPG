package buildings.defendable;

import buildings.Building;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.weapon.HandheldWeapon;
import location.WorldMapTile;
import reference.UnitClassIndex;
import unit.UnitClass;
import unit.human.Human;

public class Castle extends Defendable {

	private int[] mounts;
	
	public static final int MAXIMUM_ANIMAL_COUNT = 5;
	
	public static final int MAX_TRAINABLE_LEVEL = 15;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Castle(Human owner, WorldMapTile location) {
		super(owner.getName() + " Castle",
				MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		mounts = new int[UnitClass.Mount.values().length];
	}
	public Castle(String name, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, null, location);
		mounts = new int[UnitClass.Mount.values().length];
	}

	@Override
	public String getType() {
		return Building.CASTLE;
	}
	
	@Override
	public void completeDailyAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completeMonthlyAction() {
		// TODO Auto-generated method stub
		
	}
	
	public void addMount(UnitClass.Mount m, int quantity) {
		mounts[m.getId()] += quantity;
	}
	
	public int[] getMounts() {
		return mounts;
	}
	
	public boolean canOutfitOwner() {
		return owner != null
				&& getAssignedGroup() != null
				&& getAssignedGroup().containsUnit(owner);
	}
	
	public void autoAssignClass() {
		List<UnitClass> unitClasses = UnitClassIndex.getHumanClasses();
		if (owner.getUnitClass() != null) {
			return;
		}
		UnitClass best = unitClasses.get(0);
		int heuristic = Integer.MIN_VALUE;
		for (int w = 0; w < unitClasses.size(); w++) {
			UnitClass uc = unitClasses.get(w);
			if (uc.canTrainUnitWithMaterials(owner, mounts)) {
				int check = unitEffectivenessInClass(uc);
				if (check > heuristic) {
					heuristic = check;
					best = uc;
				}
			}
		}
		assignClass(best);
	}
	
	public boolean assignClass(UnitClass c) {
		if (c.canTrainUnitWithMaterials(owner, mounts)) {
			owner.assignClass(c);
			if (c.getMount() != null) {
				mounts[c.getMountType()]--;
			}
			return true;
		}
		return false;
	}
	
	private int unitEffectivenessInClass(UnitClass uc) {
		int ret = uc.generalInternalHeuristic();
		
		//TODO make sure this works.
		// It's supposed to make the heuristic decently influenced by the resources available
		for (int q = 0; q < materials.size(); q++) {
			int[] mat = materials.get(q);
			HandheldWeapon w = (HandheldWeapon)InventoryIndex.getElement(mat);
			if (uc.getProficiencyModifiers()[w.getProficiencyIndex()] >= w.getProficiencyRequirement()) {
				ret += (mat[2] * 10);
			}
		}
		if (uc.getMount() != null) {
			ret += (mounts[uc.getMountType()] * 10);
			ret += uc.getMountMovement() + (uc.getMountEvasionBonus() * 5);
		}
		
		int[] parts = owner.getBodyPartsMaximumHPGrowth();
		int[] gMods = uc.getGrowthModifiers();
		ret += (gMods[0] + (parts[Human.HEAD] / 2)) - Math.abs(gMods[0] - (parts[Human.HEAD] / 2));
		ret += (gMods[1] + (parts[Human.TORSO] / 2)) - Math.abs(gMods[1] - (parts[Human.TORSO] / 2));
		ret += ((gMods[2] + (parts[Human.RIGHT_ARM] / 2)) - Math.abs(gMods[2] - (parts[Human.RIGHT_ARM] / 2)));
		ret += (gMods[2] + (parts[Human.LEFT_ARM] / 2)) - Math.abs(gMods[2] - (parts[Human.LEFT_ARM] / 2)); //Don't multiply strength again. That's too much
		ret += (gMods[3] + (parts[Human.RIGHT_LEG] / 2)) - Math.abs(gMods[3] - (parts[Human.RIGHT_LEG] / 2));
		ret += (gMods[3] + (parts[Human.LEFT_LEG] / 2)) - Math.abs(gMods[3] - (parts[Human.LEFT_LEG] / 2));
		
		ret += ((gMods[4] + owner.getMagicGrowth()) - Math.abs(gMods[4] - owner.getMagicGrowth())) * uc.magicHeuristicMultiplier();
		ret += (gMods[5] + owner.getSkillGrowth()) - Math.abs(gMods[5] - owner.getSkillGrowth());
		ret += (gMods[6] + owner.getReflexGrowth()) - Math.abs(gMods[6] - owner.getReflexGrowth());
		ret += (gMods[7] + owner.getAwarenessGrowth()) - Math.abs(gMods[7] - owner.getSkillGrowth());
		ret += (gMods[8] + owner.getResistanceGrowth()) - Math.abs(gMods[8] - owner.getResistanceGrowth());

		return ret;
	}

	public boolean assignWeapon(int weapon) {
		if (owner.receiveNewItem(materials.get(weapon))) {
			materials.get(weapon)[2]--;
			if (materials.get(weapon)[2] == 0) {
				materials.remove(weapon);
			}
			return true;
		}
		return false;
	}
	public void autoAssignWeapon() {
		int wepHeur = Integer.MIN_VALUE;
		int idx = -1;
		for (int a = 0; a < materials.size(); a++) {
			int[] ie = materials.get(a);
			int test = owner.getEquipmentHeuristic(ie);
			if (test > wepHeur) {
				wepHeur = test;
				idx = a;
			}
		}
		if (idx != -1) {
			assignWeapon(idx);
		}
	}
	
	public boolean assignArmor(int armor) {
		if (owner.receiveNewArmor(armors.get(armor))) {
			armors.get(armor)[2]--;
			if (armors.get(armor)[2] == 0) {
				armors.remove(armor);
			}
			return true;
		}
		return false;
	}
	public void autoAssignArmor() {
		int armHeur = Integer.MIN_VALUE;
		int idx = -1;
		for (int a = 0; a < armors.size(); a++) {
			Armor ie = (Armor)InventoryIndex.getElement(armors.get(a));
			int test = 0;
			if (owner.getUnitClass() == null || owner.getUnitClass().getMount() == null) {
				test -= (Math.max(0, (ie.getWeight() - owner.armStrength())) * 10);
			}
			//Double count head and torso defense
			test += ie.getDefenseFor(0);
			test += ie.getDefenseFor(1);
			int[] defs = ie.getDefenses();
			for (int w = 0; w < defs.length; w++) {
				test += (defs[w] * 10) - owner.getMaximumHPOfBodyPart(w);
			}
			if (test > armHeur) {
				armHeur = test;
				idx = a;
			}
		}
		if (idx != -1) {
			assignArmor(idx);
		}
	}
	
	public boolean assignStaff(int staff) {
		if (owner.receiveNewItem(staves.get(staff))) {
			staves.get(staff)[2]--;
			if (staves.get(staff)[2] == 0) {
				staves.remove(staff);
			}
			return true;
		}
		return false;
	}
	public void autoAssignStaff() {
		//TODO
	}

	public String trainOwner() {
		if (owner == null) {
			return "No owner to train";
		}
		if (getAssignedGroup() == null || !getAssignedGroup().containsUnit(owner)) {
			return "The owner of the castle is not currently present";
		}
		if (owner.getUnitClass() == null) {
			return "Cannot train owner until their class has been assigned via the \"Outfit\" menu";
		}
		if (location.getBattle() != null) {
			return "Cannot train while there is a battle occuring here";
		}
		if (owner.getLevel() >= MAX_TRAINABLE_LEVEL) {
			return "Cannot train owner above level " + MAX_TRAINABLE_LEVEL;
		}
		int exp = Math.max(1, 50 - owner.effectiveLevel());
		owner.gainExperience(50);
		return owner.getName() + " gained " + exp + " experience!";
	}
	
}
