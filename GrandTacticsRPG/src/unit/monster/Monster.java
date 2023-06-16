package unit.monster;

import affiliation.Nation;
import unit.Unit;
import unit.UnitClass;
import unit.human.Human;

public abstract class Monster extends Unit {
	
	protected Human master;
	
	protected Monster(int level, UnitClass unitClass, String name, int[] maxHPs, int magic, int skill, int reflex,
			int awareness, int resistance, int movement, int leadership, int[] maxHPGrowths, int magGrowth,
			int sklGrowth, int rfxGrowth, int awrGrowth, int resGrowth, Human master) {
		super(level, unitClass, name, maxHPs, magic, skill, reflex, awareness, resistance, movement, leadership, maxHPGrowths,
				magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth);
		this.master = master;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean gainExperience(int exp) {
		//Gain experience only if you're not already max level
		if (level != Unit.MAX_LEVEL) {
			exp -= effectiveLevel();
			exp /= 3; //Monsters gain less experience
			exp = Math.max(1, Math.min(Unit.EXPERIENCE_TOWARDS_LEVEL, exp)); //Max 100, Min 1
			experience += exp;
			//If you got enough experience for a level,
			if (experience >= Unit.EXPERIENCE_TOWARDS_LEVEL) {
				//Level up
				level++;
				//Adjust experience appropriately
				if (level == MAX_LEVEL) {
					experience = 0;
				} else {
					experience -= EXPERIENCE_TOWARDS_LEVEL;
				}
				//We don't need a level-up animation, considering there are so many units
				//So just adjust the stats silently here
				levelUp();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getMorale() {
		return 100; //Monsters are always willing to fight
	}
	
	@Override
	public void deathSequence() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Nation getAffiliation() {
		if (master.isAlive()) {
			return master.getAffiliation();
		}
		return null;
	}
	public Human getMaster() {
		return master;
	}
	
	@Override
	public void defect(Nation n) {
		group.remove(this);
		group = null;
		//Just need to remove from group. The master controls the affiliation
		//No need to add or remove yourself to/from an army. The Nation contructor does this
	}
	
	/**
	 * Attack speed is just reflex for unequipped monsters
	 * Method is overridden by EquippedMonster
	 * @return
	 */
	@Override
	public int attackSpeed() {
		return reflex;
	}
	
	/**
	 * Overridden by EquippedMonster
	 */
	@Override
	public int getBaseAccuracy() {
		return 0;
	}
	
	/**
	 * Overridden by EquippedMonster
	 */
	@Override
	public int getBaseCrit() {
		return 0;
	}
	
	/**
	 * Unequipped monsters use pure skill as crit
	 * This is overridden by EquippedMonters
	 */
	@Override
	public int criticalHitRate() {
		return skill;
	}

}
