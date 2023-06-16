package unit;

import affiliation.Nation;
import location.BFTileOccupant;
import util.RNGStuff;

public abstract class Unit implements BFTileOccupant {

	public static final int MAX_LEVEL = 20;
	public static final int EXPERIENCE_TOWARDS_LEVEL = 100;
	
	protected UnitClass unitClass;
	protected int level;
	protected int experience;
	protected String name;
	protected String nickname;
	protected UnitGroup group;
	protected int[] activeEffects;
	protected BFTileOccupant passenger;
	protected int battlePositionX;
	protected int battlePositionY;
	protected int[] bodyPartsCurrentHP;
	protected int[] bodyPartsMaximumHP;
	protected int magic;
	protected int skill;
	protected int reflex;
	protected int awareness;
	protected int resistance;
	protected int movement;
	protected int leadership;
	protected int[] bodyPartsMaximumHPGrowth;
	protected int magicGrowth;
	protected int skillGrowth;
	protected int reflexGrowth;
	protected int awarenessGrowth;
	protected int resistanceGrowth;
	protected int wars;
	protected int battles;
	protected int kills;
	protected boolean important;
	protected boolean exhausted;
	
	public static final int HEAD = 0;
	public static final int TORSO = 1;
	
	public Unit(int level, UnitClass unitClass, String name, int[] maxHPs, int magic, int skill, int reflex,
			int awareness, int resistance, int movement, int leadership, int[] maxHPGrowths,
			int magGrowth, int sklGrowth, int rfxGrowth, int awrGrowth, int resGrowth) {
		this.unitClass = unitClass;
		this.level = level;
		this.experience = 0;
		this.name = name;
		this.activeEffects = new int[5];
		this.battlePositionX = -1;
		this.battlePositionY = -1;
		this.bodyPartsMaximumHP = maxHPs;
		bodyPartsCurrentHP = new int[maxHPs.length];
		for (int q = 0; q < bodyPartsCurrentHP.length; q++) {
			bodyPartsCurrentHP[q] = maxHPs[q];
		}
		this.magic = magic;
		this.skill = skill;
		this.reflex = reflex;
		this.awareness = awareness;
		this.resistance = resistance;
		this.movement = movement;
		this.leadership = leadership;
		this.bodyPartsMaximumHPGrowth = maxHPGrowths;
		this.magicGrowth = magGrowth;
		this.skillGrowth = sklGrowth;
		this.reflexGrowth = rfxGrowth;
		this.awarenessGrowth = awrGrowth;
		this.resistanceGrowth = resGrowth;
		this.wars = 0;
		this.battles = 0;
		this.kills = 0;


	}
	
	public int getLevel() {
		return level;
	}
	public int getExperience() {
		return experience;
	}
	public String getName() {
		if (nickname != null) {
			return name + " \"" + nickname + "\"";
		}
		return name;
	}
	public void setNickName(String nickname) {
		this.nickname = nickname;
	}
	
	@Override
	public String getDisplayName() {
		return name + " \"" + nickname + "\"";
	}
	public UnitClass getUnitClass() {
		return unitClass;
	}
	public BFTileOccupant getPassenger() {
		return passenger;
	}
	public int getBattlePositionX() {
		return battlePositionX;
	}
	public int getBattlePositionY() {
		return battlePositionY;
	}
	public int[] getBodyPartsCurrentHP() {
		return bodyPartsCurrentHP;
	}
	public int[] getBodyPartsMaximumHP() {
		return bodyPartsMaximumHP;
	}
	public int getCurrentHPOfBodyPart(int bodyPart) {
		return bodyPartsCurrentHP[bodyPart];
	}
	public int getMaximumHPOfBodyPart(int bodyPart) {
		return bodyPartsMaximumHP[bodyPart];
	}
	public int[] getBodyPartsMaximumHPGrowth() {
		return bodyPartsMaximumHPGrowth;
	}
	public int getGrowthRateOfBodyPart(int bodyPart) {
		return this.bodyPartsMaximumHPGrowth[bodyPart];
	}
	public int getMagic() {
		return magic;
	}
	public int getSkill() {
		return skill;
	}
	public int getReflex() {
		return reflex;
	}
	public int getAwareness() {
		return awareness;
	}
	public int getResistance() {
		return resistance;
	}
	public abstract int getMovement();
	
	public int getLeadership() {
		return leadership;
	}
	public int getMagicGrowth() {
		return magicGrowth;
	}
	public int getSkillGrowth() {
		return skillGrowth;
	}
	public int getReflexGrowth() {
		return reflexGrowth;
	}
	public int getAwarenessGrowth() {
		return awarenessGrowth;
	}
	public int getResistanceGrowth() {
		return resistanceGrowth;
	}
	public int[] getStatusEffects() {
		return activeEffects;
	}
	public int getKills() {
		return kills;
	}
	public int getBattles() {
		return battles;
	}
	public int getWars() {
		return wars;
	}
	public void incrementKills() {
		kills++;
	}
	public void incrementKills(int victims) {
		kills += victims;
	}
	public void incrementBattles() {
		battles++;
	}
	public void incrementWars() {
		wars++;
	}
	public boolean isImportant() {
		return important;
	}
	public void toggleImportance() {
		important = !important;
	}
	public void exhaust() {
		this.exhausted = true;
	}
	public void restoreEnergy() {
		this.exhausted = false;
	}
	public boolean isExhausted() {
		return this.exhausted;
	}
	
	/**
	 * Increments the kill count of the conflict between this unit's nation and
	 * the killer's nation
	 * @param dfd
	 */
	public void wasKilledBy(Unit killer) {
		if (getAffiliation() != null && killer.getAffiliation() != null) {
			getAffiliation().getCurrentWarWith(killer.getAffiliation()).incrementKills(this);
		}
	}
	public boolean isEnemyOf(BFTileOccupant o) {
		if (o == null || !(o instanceof Unit)) {
			return false;
		}
		return getAffiliation().isAtWarWith(((Unit)o).getAffiliation());
	}

	public abstract void defect(Nation n);
	
	public boolean isAlive() {
		return bodyPartsCurrentHP[HEAD] > 0 && bodyPartsCurrentHP[TORSO] > 0;
	}
	public void kill() {
		bodyPartsCurrentHP[HEAD] = 0;
	}
	public void heal(int bodyPart, int amount) {
		bodyPartsCurrentHP[bodyPart] = Math.min(getCurrentHPOfBodyPart(bodyPart) + amount,
				getMaximumHPOfBodyPart(bodyPart));
	}
	public abstract void deathSequence();
	
	public UnitGroup getGroup() {
		return group;
	}
	
	public String getUnitClassName() {
		if (unitClass == null) {
			return "Not Assigned";
		}
		return unitClass.getName();
	}
	
	public void assignGroup(UnitGroup unitGroup) {
		if (group != null) {
			group.remove(this);
		}
		this.group = unitGroup;
	}
	
	/**
	 * Just a handy method for fixing stupid mistakes. DO NOT use this as the normal way
	 * to remove a group from a unit
	 */
	public void removeGroup() {
		this.group = null;
	}
	
	public abstract Nation getAffiliation();
	
	public abstract boolean canCarryUnit();
	
	public abstract boolean canBeCarried();
	
	public abstract int attackSpeed();
	
	public abstract boolean canFly();
	
	public abstract int attackStrength();
	
	public abstract int defense(boolean isMagicAttack, int bodyPart);
	
	public abstract int getBaseAccuracy();

	public abstract int getBaseCrit();

	public abstract int accuracy();
	
	public abstract int avoidance(int bodyPart);
	
	public abstract int criticalHitRate();

	public abstract int criticalHitAvoid();
	
	public abstract boolean isUsingMagic();

	public abstract String[] getBodyPartsNames();

	/**
	 * Return an array with the following indexes:
	 * [0] = index of longest range weapon in inventory (0 if no inventory or inventory
				has no weapons)
	 * [1] = range of longest range weapon in inventory (custom if no inventory, 1 if inventory
				has no weapons)
	 * [2] = index of longest range staff in inventory (-1 if no inventory or no staves)
	 * [3] = range of longest range staff in inventory (-1 if no staves)
	 * @return
	 */
	public abstract int[] getRanges();

	/**
	 * Gives the power of the unit
	 * @return array with the following indexes:
	 * [0] = physical strength
	 * [1] = magical strength
	 * [2] = accuracy
	 * [3] = avoidance (Basic. that is, torso)
	 * [4] = crit rate
	 * [5] = crit avoidance (Basic. that is, torso)
	 * [6] = average attack speed
	 * [7] = defense (Basic. that is, torso)
	 * [8] = resistance
	 * [9] = head HP
	 * [10] = torso HP
	 */
	public int[] getPower() {
		int[] ret = new int[11];
		if (isUsingMagic()) {
			ret[1] = attackStrength();
		} else {
			ret[0] = attackStrength();
		}
		ret[2] = accuracy();
		ret[3] = avoidance(1);
		ret[4] = criticalHitRate();
		ret[5] = criticalHitAvoid();
		ret[6] = attackSpeed();
		ret[7] = defense(false, 1);
		ret[8] = defense(true, 1);
		ret[9] = getCurrentHPOfBodyPart(0);
		ret[10] = getCurrentHPOfBodyPart(1);
		return ret;
	}

	public abstract int getMorale();
	
	/**
	 * Take damage, then tell if unit is still alive
	 * @param isMagicAttack
	 * @param bodyPart
	 * @param attackPower
	 * @return
	 */
	public boolean takeDamage(boolean isMagicAttack, int bodyPart, int attackPower) {
		attackPower -= defense(isMagicAttack, bodyPart);
		bodyPartsCurrentHP[bodyPart] -= attackPower;
		return isAlive();
	}
	
	public boolean takeCriticalDamage(boolean isMagicAttack, int bodyPart, int attackPower) {
		attackPower -= (defense(isMagicAttack, bodyPart) / 2); //Only half the defense counts
		//Damage is done to HP, maxHP, and growth rate
		bodyPartsCurrentHP[bodyPart] -= attackPower;
		bodyPartsMaximumHP[bodyPart] -= attackPower;
		bodyPartsMaximumHPGrowth[bodyPart] -= attackPower;
		//If you lose all maxHP, you lose that part entirely and it cannot be trained
		if (bodyPartsMaximumHP[bodyPart] <= 0) {
			bodyPartsMaximumHPGrowth[bodyPart] = 0;
		}
		return isAlive();
	}
	
	public double percentHealthOfPart(int part) {
		//Notify the caller if this body part has been lost
		if (this.bodyPartsMaximumHP[part] <= 0) {
			return -1;
		}
		return (0.0 + bodyPartsCurrentHP[part]) / bodyPartsMaximumHP[part];
	}
	
	public int effectiveLevel() {
		if (unitClass == null) {
			return 0;
		}
		return level + (20 * (unitClass.getTier() - 1));
	}
	
	/**
	 * Manage EXP gain and return whether or not a level-up occurred.
	 * @param exp
	 * @return
	 */
	public abstract boolean gainExperience(int exp);
	
	/**
	 * Because there are so many units, level-ups are silent. We don't display the change in
	 * stats, only that a level-up occurred. Therefore, we can have a protected method to handle
	 * this.
	 * 
	 * Whether this unit is a human or monster, this protocol for updating body parts
	 * and other stats should work, as it is generic
	 */
	protected void levelUp() {
		
		level++;
		
		for (int q = 0; q < bodyPartsMaximumHPGrowth.length; q++) {
			//If growth rate is over 100%, always increment according to growth / 100
			bodyPartsMaximumHP[q] += bodyPartsMaximumHPGrowth[q] / 100;
			bodyPartsCurrentHP[q] += bodyPartsMaximumHPGrowth[q] / 100;
			//Then check remainder to see if you will increment based on RNG
			if (bodyPartsMaximumHPGrowth[q] % 100 > RNGStuff.random0To99()) {
				bodyPartsMaximumHP[q]++;
				bodyPartsCurrentHP[q]++;
			}
		}
		//Again, if growth rate is over 100%, always increment according to growth / 100
		magic += magicGrowth / 100;
		//Then check remainder to see if you will increment based on RNG
		if (magicGrowth % 100 > RNGStuff.random0To99()) {
			magic++;
		}
		skill += skillGrowth / 100;
		if (skillGrowth % 100 > RNGStuff.random0To99()) {
			skill++;
		}
		reflex += reflexGrowth / 100;
		if (reflexGrowth % 100 > RNGStuff.random0To99()) {
			reflex++;
		}
		awareness += awarenessGrowth / 100;
		if (awarenessGrowth % 100 > RNGStuff.random0To99()) {
			awareness++;
		}
		resistance += resistanceGrowth / 100;
		if (resistanceGrowth % 100 > RNGStuff.random0To99()) {
			resistance++;
		}
	}
	
	public void setCoords(int x, int y) {
		this.battlePositionX = x;
		this.battlePositionY = y;
	}

}
