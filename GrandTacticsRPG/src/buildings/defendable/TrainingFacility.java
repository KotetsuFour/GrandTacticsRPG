package buildings.defendable;

import affiliation.Nation;
import buildings.Building;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.weapon.HandheldWeapon;
import inventory.weapon.Weapon;
import location.WorldMap;
import location.WorldMapTile;
import reference.UnitClassIndex;
import unit.UnitClass;
import unit.UnitGroup;
import unit.human.Human;

public class TrainingFacility extends Defendable {

	private int[] mounts;
	private List<Human> trainees; //Just humans, as monsters cannot be trained
	
	public static final int MAX_TRAINABLE_LEVEL = 10;
	
	/**This value, when gained every day for 30 days, will bring a level 0 tier 1 unit up
	 * to level 10, with only 1 extra experience point (taking into account reduced exp
	 * gain for higher level units)
	 */
	public static final int OPTIMAL_TRAINING_PER_TURN = 38;
	public static final int MAXIMUM_ANIMAL_COUNT = 50;
	
	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public TrainingFacility(String name, Human owner, WorldMapTile location) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, owner, location);
		mounts = new int[UnitClass.Mount.values().length];
		trainees = new List<>();
	}
	
	@Override
	public String getType() {
		return Building.TRAINING_FACILITY;
	}
	
	@Override
	public void completeDailyAction() {
		
		//To relieve the player of tedious responsibility, if classes are not assigned
		//immediately after recruitment, then assume the player doesn't care and auto-assign
		autoAssignClasses();
		autoAssignAllWeapons();
		autoAssignAllArmors();
		autoAssignAllStaves();
		
		for (int q = 0; q < trainees.size(); q++) {
			Human h = trainees.get(q);
			if (h.getUnitClass() == null) {
				continue;
			}
			if (h.getLevel() < MAX_TRAINABLE_LEVEL) {
				//Doesn't really matter, but this means that higher tier units will
				//gain less experience (tier 3 and 4 units will hardly gain any experience)
				h.gainExperience(OPTIMAL_TRAINING_PER_TURN);
			}
		}
	}
	
	/**
	 * Overridden for optimization's sake. The superclass's method would also
	 * function correctly, but slower, probably
	 */
	@Override
	public void completeMonthlyAction() {
		
		//To relieve the player of tedious responsibility, if classes are not assigned
		//immediately after recruitment, then assume the player doesn't care and auto-assign
		autoAssignClasses();
		autoAssignAllWeapons();
		autoAssignAllArmors();
		autoAssignAllStaves();
		
		for (int q = 0; q < trainees.size(); q++) {
			Human h = trainees.get(q);
			if (h.getUnitClass() == null) {
				continue;
			}
			//For units above tier 1, this is much faster than the daily action method
			//I don't really mind the difference, though
			while (h.getLevel() < MAX_TRAINABLE_LEVEL) {
				h.finishLevel();
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	public boolean addUnit(Human u) {
		if (trainees.isFull() || u.getAffiliation().getArmy().isFull()) {
			return false;
		}
		if (u.getGroup() != null) {
			u.getGroup().remove(u);
		}
		if (trainees.isEmpty()) {
			new UnitGroup(u); //group automatically adds itself to the nation
		} else {
			trainees.get(0).getGroup().add(u);
		}
		u.getAffiliation().getArmy().add(u);
		trainees.add(u);
		return true;
	}
	
	public List<Human> getTrainees() {
		return trainees;
	}
	
	public void addMount(UnitClass.Mount m, int quantity) {
		mounts[m.getId()] += quantity;
	}
	
	public int[] getMounts() {
		return mounts;
	}
	
	public boolean assignWeapon(int unit, int weapon) {
		if (trainees.get(unit).receiveNewItem(materials.get(weapon))) {
			materials.get(weapon)[2]--;
			if (materials.get(weapon)[2] == 0) {
				materials.remove(weapon);
			}
			return true;
		}
		return false;
	}
	
	public boolean assignArmor(int unit, int armor) {
		if (trainees.get(unit).receiveNewArmor(armors.get(armor))) {
			armors.get(armor)[2]--;
			if (armors.get(armor)[2] == 0) {
				armors.remove(armor);
			}
			return true;
		}
		return false;
	}

	public boolean assignStaff(int unit, int staff) {
		if (trainees.get(unit).receiveNewItem(staves.get(staff))) {
			staves.get(staff)[2]--;
			if (staves.get(staff)[2] == 0) {
				staves.remove(staff);
			}
			return true;
		}
		return false;
	}

	public boolean readyToGraduate() {
		for (int q = 0; q < trainees.size(); q++) {
			if (trainees.get(q).getUnitClass() == null
					|| trainees.get(q).getLevel() < MAX_TRAINABLE_LEVEL) {
				return false;
			}
		}
		return true;
	}

	public boolean graduateUnits(WorldMap map) {
		if (trainees.isEmpty()) {
			return false;
		}
		if (location.isVacant()) {
			UnitGroup group = trainees.get(0).getGroup();
			group.autoAssignLeader();
			location.sendHere(group);
			trainees = new List<>(UnitGroup.CAPACITY, UnitGroup.CAPACITY);
			return true;
		}
		return false;
	}
	
	/**
	 * This method exists only for testing purposes
	 * @return graduated unit group
	 */
	public UnitGroup graduateUnitsForTesting() {
		UnitGroup group = new UnitGroup(trainees);
		group.autoAssignLeader();
		trainees = new List<>(UnitGroup.CAPACITY, UnitGroup.CAPACITY);
		return group;
	}

	public void autoAssignClasses() {
		for (int q = 0; q < trainees.size(); q++) {
			autoAssignClass(q);
		}
	}
	
	public void autoAssignClass(int unit) {
		List<UnitClass> unitClasses = UnitClassIndex.getHumanClasses();
		Human h = trainees.get(unit);
		if (h.getUnitClass() != null) {
			return;
		}
		UnitClass best = unitClasses.get(0);
		int heuristic = Integer.MIN_VALUE;
		for (int w = 0; w < unitClasses.size(); w++) {
			UnitClass uc = unitClasses.get(w);
			if (uc.canTrainUnitWithMaterials(h, mounts)) {
				int check = unitEffectivenessInClass(h, uc);
				if (check > heuristic) {
					heuristic = check;
					best = uc;
				}
			}
		}
		assignClass(unit, best);
	}
	
	public boolean assignClass(int unit, UnitClass c) {
		if (c.canTrainUnitWithMaterials(trainees.get(unit), mounts)) {
			trainees.get(unit).assignClass(c);
			if (c.getMount() != null) {
				mounts[c.getMountType()]--;
			}
			return true;
		}
		return false;
	}
	
	public void autoAssignSupportPartners() {
		//Just assign randomly
		//TODO maybe strategically assign partners just to make sure the player
		//doesn't have that advantage over the AI
		
		for (int q = 0; q < trainees.size(); q++) {
			Human searching = trainees.get(q);
			if (searching.canAssignSupportPartner()) {
				for (int w = q + 1; w < trainees.size(); w++) {
					Human found = trainees.get(w);
					if (found.canAssignSupportPartner()) {
						searching.assignSupportPartner(found);
						break;
					}
				}
			}
		}
	}
	
	public void autoAssignAllWeapons() {
		for (int q = 0; q < trainees.size(); q++) {
			autoAssignWeapon(q);
		}
	}
	
	public void autoAssignWeapon(int unit) {
		Human h = trainees.get(unit);
		//If the unit already has a weapon that they're somewhat proficient in, don't do this
		HandheldWeapon eq = h.getEquippedWeapon();
		if (eq != null && h.proficiencyWith(eq.getProficiencyIndex()) > 0) {
			return;
		}
		int wepHeur = Integer.MIN_VALUE;
		if (eq != null) {
			wepHeur = h.getEquipmentHeuristic(h.getInventory()[0]);
		}
		int idx = -1;
		for (int a = 0; a < materials.size(); a++) {
			int[] ie = materials.get(a);
			int test = h.getEquipmentHeuristic(ie);
			if (test > wepHeur) {
				wepHeur = test;
				idx = a;
			}
		}
		if (idx != -1) {
			assignWeapon(unit, idx);
			h.autoEquip();
		}
	}
	
	public void autoAssignAllArmors() {
		for (int q = 0; q < trainees.size(); q++) {
			autoAssignArmor(q);
		}
	}
	
	public void autoAssignArmor(int unit) {
		Human h = trainees.get(unit);
		if (h.getArmor() != null) {
			return;
		}
		int armHeur = Integer.MIN_VALUE;
		int idx = -1;
		for (int a = 0; a < armors.size(); a++) {
			Armor ie = (Armor)InventoryIndex.getElement(armors.get(a));
			int test = 0;
			if (h.getUnitClass() == null || h.getUnitClass().getMount() == null) {
				test -= (Math.max(0, (ie.getWeight() - h.armStrength())) * 10);
			}
			//Double count head and torso defense
			test += ie.getDefenseFor(0);
			test += ie.getDefenseFor(1);
			int[] defs = ie.getDefenses();
			for (int w = 0; w < defs.length; w++) {
				test += (defs[w] * 10) - h.getMaximumHPOfBodyPart(w);
			}
			if (test > armHeur) {
				armHeur = test;
				idx = a;
			}
		}
		if (idx != -1) {
			assignArmor(unit, idx);
		}
	}
	
	public void autoAssignAllStaves() {
		for (int q = 0; q < trainees.size(); q++) {
			autoAssignStaff(q);
		}
	}
	
	public void autoAssignStaff(int unit) {
		Human h = trainees.get(unit);
		if (h.proficiencyWith(Weapon.STAFF) == 0) {
			return;
		}
		//TODO
	}

	private int unitEffectivenessInClass(Human h, UnitClass uc) {
		int ret = uc.generalInternalHeuristic();
		
		//The commented code below and the one commented line in the following if block
		// Are supposed to make the heuristic decently influenced by the resources available
		// However, this is probably not a helpful approach. It is better to make an impartial
		// decision about the unit's best class, then order weapons according to what was picked
//		for (int q = 0; q < materials.size(); q++) {
//			int[] mat = materials.get(q);
//			HandheldWeapon w = (HandheldWeapon)InventoryIndex.getElement(mat);
//			if (uc.getProficiencyModifiers()[w.getProficiencyIndex()] >= w.getProficiencyRequirement()) {
//				ret += (mat[2] * 10);
//			}
//		}
		if (uc.getMount() != null) {
//			ret += (mounts[uc.getMountType()] * 10);
			ret += uc.getMountMovement() + (uc.getMountEvasionBonus() * 5);
		}
		
		int[] parts = h.getBodyPartsMaximumHPGrowth();
		int[] gMods = uc.getGrowthModifiers();
		ret += (gMods[0] + (parts[Human.HEAD] / 2)) - Math.abs(gMods[0] - (parts[Human.HEAD] / 2));
		ret += (gMods[1] + (parts[Human.TORSO] / 2)) - Math.abs(gMods[1] - (parts[Human.TORSO] / 2));
		ret += ((gMods[2] + (parts[Human.RIGHT_ARM] / 2)) - Math.abs(gMods[2] - (parts[Human.RIGHT_ARM] / 2)));
		ret += (gMods[2] + (parts[Human.LEFT_ARM] / 2)) - Math.abs(gMods[2] - (parts[Human.LEFT_ARM] / 2)); //Don't multiply strength again. That's too much
		ret += (gMods[3] + (parts[Human.RIGHT_LEG] / 2)) - Math.abs(gMods[3] - (parts[Human.RIGHT_LEG] / 2));
		ret += (gMods[3] + (parts[Human.LEFT_LEG] / 2)) - Math.abs(gMods[3] - (parts[Human.LEFT_LEG] / 2));
		
		ret += ((gMods[4] + h.getMagicGrowth()) - Math.abs(gMods[4] - h.getMagicGrowth())) * uc.magicHeuristicMultiplier();
		ret += (gMods[5] + h.getSkillGrowth()) - Math.abs(gMods[5] - h.getSkillGrowth());
		ret += (gMods[6] + h.getReflexGrowth()) - Math.abs(gMods[6] - h.getReflexGrowth());
		ret += (gMods[7] + h.getAwarenessGrowth()) - Math.abs(gMods[7] - h.getSkillGrowth());
		ret += (gMods[8] + h.getResistanceGrowth()) - Math.abs(gMods[8] - h.getResistanceGrowth());

		return ret;
	}

	@Override
	public void defect(Nation n) {
		// TODO deal with trainees as well as owner
		
	}

}
