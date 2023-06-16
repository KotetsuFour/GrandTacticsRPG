package unit.monster;

import unit.UnitClass;
import unit.human.Human;

public class Wolf extends Monster {

	public static final int LEG1 = 2;
	public static final int LEG2 = 3;
	public static final int LEG3 = 4;
	public static final int LEG4 = 5;
	public static final int RIGHT_EYE = 6;
	public static final int LEFT_EYE = 7;
	public static final int MOUTH = 8;
	
	public static final String[] BODY_PARTS_STRINGS = {"Head", "Torso", "Leg 1",
			"Leg 2", "Leg 3", "Leg 4", "Right Eye", "Left Eye", "Mouth"};
	
	protected Wolf(int level, UnitClass unitClass, String name, int[] maxHPs, int magic, int skill, int reflex,
			int awareness, int resistance, int movement, int leadership, int[] maxHPGrowths, int magGrowth,
			int sklGrowth, int rfxGrowth, int awrGrowth, int resGrowth, Human master) {
		super(level, unitClass, name, maxHPs, magic, skill, reflex, awareness, resistance, movement, leadership, maxHPGrowths,
				magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth, master);
		// TODO Auto-generated constructor stub
	}

	private float percentageEyesHP() {
		int totalEyesCurrentHP = bodyPartsCurrentHP[RIGHT_EYE] + bodyPartsCurrentHP[LEFT_EYE];
		int totalEyesMaximumHP = bodyPartsMaximumHP[RIGHT_EYE] + bodyPartsMaximumHP[LEFT_EYE];
		return (float)((0.0 + totalEyesCurrentHP) / (0.0 + totalEyesMaximumHP));
	}
	
	private float percentageLegsHP() {
		int totalCurrentLegsHP = bodyPartsCurrentHP[LEG1] + bodyPartsCurrentHP[LEG2]
				+ bodyPartsCurrentHP[LEG3] + bodyPartsCurrentHP[LEG4];
		int totalMaximumLegsHP = bodyPartsMaximumHP[LEG1] + bodyPartsMaximumHP[LEG2]
				+ bodyPartsMaximumHP[LEG3] + bodyPartsMaximumHP[LEG4];
		return (float)((0.0 + totalCurrentLegsHP) / (0.0 + totalMaximumLegsHP));
	}
	
	@Override
	public boolean isUsingMagic() {
		return false;
	}

	@Override
	public int getMovement() {
		return Math.max(0, Math.round(percentageLegsHP() * movement));
	}

	@Override
	public boolean canCarryUnit() {
		//Can always carry units (think Loth Wolves)
		return true;
	}

	@Override
	public boolean canBeCarried() {
		//Can never be carried by another unit (again, think Loth Wolves)
		return false;
	}

	@Override
	public boolean canFly() {
		//Wolves can never fly. I refuse
		return false;
	}

	@Override
	public int attackStrength() {
		return bodyPartsCurrentHP[MOUTH] / 2;
	}

	@Override
	public int defense(boolean isMagicAttack, int bodyPart) {
		if (isMagicAttack) {
			return resistance;
		}
		return 0;
	}

	@Override
	public int accuracy() {
		int effectiveSkill = Math.round((percentageEyesHP() * skill));
		
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
		int effectiveAwareness = Math.round((percentageEyesHP() * awareness));
		
		int avoidance = (attackSpeed() * 2) + effectiveAwareness;
		if (group != null && this != group.getLeader()) { //Leader cannot give themselves a bonus
			avoidance += group.getLeadershipBonus(this);
		}
		if (bodyPart == 0) {
			avoidance += 15;
		} else if (bodyPart == LEG1 || bodyPart == LEG2
				|| bodyPart == LEG3 || bodyPart == LEG4) {
			avoidance += 35;
		} else if (bodyPart == RIGHT_EYE || bodyPart == LEFT_EYE) {
			avoidance += 70;
		}
		return avoidance;
	}

	@Override
	public int criticalHitAvoid() {
		return Math.round(awareness * percentageEyesHP());
	}

	@Override
	public int[] getRanges() {
		int[] ret  = {0, 1, -1, -1};
		return ret;
	}

	@Override
	public String[] getBodyPartsNames() {
		return BODY_PARTS_STRINGS;
	}

}
