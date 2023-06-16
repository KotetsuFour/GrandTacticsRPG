package unit.monster;

import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.item.Item;
import inventory.weapon.Weapon;
import unit.UnitClass;
import unit.human.Human;

public class Gorgon extends EquippedMonster {
	
	public static final int RIGHT_EYE = 4;
	public static final int LEFT_EYE = 5;
	public static final int TAIL = 6;

	public static final String[] BODY_PARTS_STRINGS = {"Head", "Torso", "Right Arm",
			"Left Arm", "Right Eye", "Left Eye", "Tail"};
	
	protected Gorgon(int level, UnitClass unitClass, String name, int[] maxHPs, int magic, int skill, int reflex,
			int awareness, int resistance, int movement, int leadership, int[] maxHPGrowths, int magGrowth,
			int sklGrowth, int rfxGrowth, int awrGrowth, int resGrowth, Human master) {
		super(level, unitClass, name, maxHPs, magic, skill, reflex, awareness, resistance, movement, leadership, maxHPGrowths,
				magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth, master);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMovement() {
		//If no mount, Movement is the percentage of the unit's tail's health
		float percentMove =
				(float) ((0.0 + bodyPartsCurrentHP[TAIL]) / (0.0 + bodyPartsMaximumHP[TAIL]));
		return Math.max(0, Math.round(percentMove * movement));
	}

	@Override
	public boolean canCarryUnit() {
		//Can never carry unit
		return false;
	}

	@Override
	public boolean canBeCarried() {
		//Can always be carried
		return true;
	}

	@Override
	public int attackSpeed() {
		int encumberment = 0;
		Armor a = getArmor();
		if (a != null) {
			encumberment += a.getWeight() - armStrength();
		}
		Item i = InventoryIndex.getElement(inventory[0]);
		if (i instanceof Weapon) {
			encumberment += ((Weapon) i).getWeight();
		}
		encumberment = Math.max(0, encumberment);
		return Math.max(0, reflex - encumberment);
	}

	@Override
	public boolean canFly() {
		//Can never fly
		return false;
	}

	@Override
	public int getBaseAccuracy() {
		int currentArmsHP = bodyPartsCurrentHP[RIGHT_ARM] + bodyPartsCurrentHP[LEFT_ARM];
		int maxArmsHP = Math.max(1, bodyPartsMaximumHP[RIGHT_ARM] + bodyPartsMaximumHP[LEFT_ARM]);
		double percentageArmsHP = (0.0 + currentArmsHP) / maxArmsHP;
		int currentEyesHP = bodyPartsCurrentHP[RIGHT_EYE] + bodyPartsCurrentHP[LEFT_EYE];
		int maxEyesHP = Math.max(1, bodyPartsMaximumHP[RIGHT_EYE] + bodyPartsMaximumHP[LEFT_EYE]);
		double percentageEyesHP = (0.0 + currentEyesHP) / maxEyesHP;

		int effectiveSkill = Math.round((float)(percentageArmsHP * percentageEyesHP * skill));
		
		int maxHeadHP = Math.max(1, bodyPartsMaximumHP[HEAD]);
		double percentageHeadHP = (0.0 + bodyPartsMaximumHP[HEAD]) / maxHeadHP;
		int effectiveAwareness = Math.round((float)(percentageHeadHP * awareness));
		
		int accuracy = (effectiveSkill * 2) + effectiveAwareness;
		
		if (group != null && this != group.getLeader()) { //Leader cannot give themselves a bonus
			accuracy += group.getLeadershipBonus(this);
		}
		return accuracy;
	}

	@Override
	public int avoidance(int bodyPart) {
		int currentEyesHP = bodyPartsCurrentHP[RIGHT_EYE] + bodyPartsCurrentHP[LEFT_EYE];
		int maxEyesHP = Math.max(1, bodyPartsMaximumHP[RIGHT_EYE] + bodyPartsMaximumHP[LEFT_EYE]);
		double percentageEyesHP = (0.0 + currentEyesHP) / maxEyesHP;
		int effectiveAwareness = Math.round((float)(percentageEyesHP * awareness));
		
		int avoidance = (attackSpeed() * 2) + effectiveAwareness;
		if (group != null && this != group.getLeader()) { //Leader cannot give themselves a bonus
			avoidance += group.getLeadershipBonus(this);
		}
		if (bodyPart == 0) {
			avoidance += 15;
		} else if (bodyPart == RIGHT_ARM || bodyPart == LEFT_ARM) {
			avoidance += 30;
		} else if (bodyPart == RIGHT_EYE || bodyPart == LEFT_EYE) {
			avoidance += 70;
		} else if (bodyPart == TAIL) {
			avoidance += 10;
		}
		return avoidance;
	}

	@Override
	public String[] getBodyPartsNames() {
		return BODY_PARTS_STRINGS;
	}

}
