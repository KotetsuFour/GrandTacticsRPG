package unit.monster;

import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.item.Item;
import inventory.weapon.Weapon;
import unit.UnitClass;
import unit.human.Human;

public class Skeleton extends EquippedMonster {
	
	public static final int RIGHT_LEG = 4;
	public static final int LEFT_LEG = 5;
	public static final int MOUNT = 6;

	public static final String[] BODY_PARTS_STRINGS = {"Head", "Torso", "Right Arm",
			"Left Arm", "Right Leg", "Left Leg", "Mount"};
	
	protected Skeleton(int level, UnitClass unitClass, String name, int[] maxHPs, int magic, int skill, int reflex,
			int awareness, int resistance, int movement, int leadership, int[] maxHPGrowths, int magGrowth,
			int sklGrowth, int rfxGrowth, int awrGrowth, int resGrowth, Human master) {
		super(level, unitClass, name, maxHPs, magic, skill, reflex, awareness, resistance, movement, leadership, maxHPGrowths,
				magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth, master);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMovement() {
		//If unit has a mount, Movement is the percentage of the mount's health
		if (bodyPartsCurrentHP[MOUNT] > 0) {
			float percentMove = (float) ((0.0 + bodyPartsCurrentHP[MOUNT]) / (0.0 + bodyPartsMaximumHP[MOUNT]));
			return Math.round(percentMove * unitClass.getMountMovement());
		}
		//If no mount, Movement is the percentage of the unit's legs' health
		float percentMove =
				(float) ((0.0 + bodyPartsCurrentHP[RIGHT_LEG] + bodyPartsCurrentHP[LEFT_LEG]) / (0.0 + bodyPartsMaximumHP[RIGHT_LEG] + bodyPartsMaximumHP[LEFT_LEG]));
		return Math.max(0, Math.round(percentMove * movement));
	}

	@Override
	public boolean canCarryUnit() {
		//Can carry a unit if the unit has a mount and the mount is alive
		return unitClass.getMountType() > -1 && bodyPartsCurrentHP[MOUNT] > 0;
	}

	@Override
	public boolean canBeCarried() {
		//Can be carried if there is no mount or their mount is dead (i.e. when canCarryUnit is false)
		return ! canCarryUnit();
	}

	@Override
	public int attackSpeed() {
		Item i = InventoryIndex.getElement(inventory[0]);
		int encumberment = 0;
		if (bodyPartsCurrentHP[MOUNT] <= 0) { //Mounted units aren't encumbered by armor
			Armor a = getArmor();
			if (a != null) {
				encumberment += a.getWeight() - armStrength();
			}
		}
		if (i instanceof Weapon) {
			encumberment += ((Weapon) i).getWeight();
		}
		encumberment = Math.max(0, encumberment);
		return Math.max(0, reflex - encumberment);
	}
	
	@Override
	public boolean canFly() {
		//Can fly if they have a flying mount and the mount is alive
		return unitClass.mountCanFly() && bodyPartsCurrentHP[MOUNT] > 0;
	}

	@Override
	public int getBaseAccuracy() {
		int currentArmsHP = bodyPartsCurrentHP[RIGHT_ARM] + bodyPartsCurrentHP[LEFT_ARM];
		int maxArmsHP = Math.max(1, bodyPartsMaximumHP[RIGHT_ARM] + bodyPartsMaximumHP[LEFT_ARM]);
		double percentageArmsHP = (0.0 + currentArmsHP) / maxArmsHP;
		
		int effectiveSkill = Math.round((float)(percentageArmsHP * skill));
		
		int maxHeadHP = Math.max(1, bodyPartsMaximumHP[TORSO]);
		double percentageHeadHP = (0.0 + bodyPartsMaximumHP[TORSO]) / maxHeadHP;
		int effectiveAwareness = Math.round((float)(percentageHeadHP * awareness));
		
		int accuracy = (effectiveSkill * 2) + effectiveAwareness;
		
		if (group != null && this != group.getLeader()) { //Leader cannot give themselves a bonus
			accuracy += group.getLeadershipBonus(this);
		}
		return accuracy;
	}

	@Override
	public int avoidance(int bodyPart) {
		int effectiveAwareness = awareness;
		
		int maxMountHP = Math.max(1, bodyPartsCurrentHP[MOUNT]);
		double percentageMountHP = (0.0 + bodyPartsCurrentHP[MOUNT]) / maxMountHP;
		int effectiveMountEvasion = 0;
		if (unitClass != null && unitClass.getMount() != null) {
			effectiveMountEvasion = Math.round((float)(percentageMountHP * unitClass.getMountEvasionBonus()));
		}
		
		int avoidance = (attackSpeed() * 2) + effectiveAwareness + effectiveMountEvasion;
		if (group != null && this != group.getLeader()) { //Leader cannot give themselves a bonus
			avoidance += group.getLeadershipBonus(this);
		}
		if (bodyPart == 0) {
			avoidance += 15;
		} else if (bodyPart == 2 || bodyPart == 3) {
			avoidance += 30;
		} else if (bodyPart == 4 || bodyPart == 5) {
			avoidance += 45;
		}
		return avoidance;
	}

	@Override
	public String[] getBodyPartsNames() {
		return BODY_PARTS_STRINGS;
	}

}
