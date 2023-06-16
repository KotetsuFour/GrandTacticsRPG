package unit.human;

import java.util.Random;

import affiliation.CityState;
import affiliation.Nation;
import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.item.Item;
import inventory.staff.Staff;
import inventory.weapon.HandheldWeapon;
import inventory.weapon.StationaryWeapon;
import inventory.weapon.Weapon;
import javafx.scene.paint.Color;
import unit.Equippable;
import unit.Unit;
import unit.UnitClass;
import util.RNGStuff;

public class Human extends Unit implements Equippable {
	
	public enum Interest {
		GARDENING("Gardening"),
		HORSEBACK_RIDING("Horseback Riding"),
		STUDYING("Studying"),
		COOKING("Cooking"),
		HUNTING("Hunting"),
		COLLECTING("Collecting"),
		PAINTING("Painting"),
		WRITING("Writing"),
		READING("Reading"),
		PLAYING_MUSIC("Playing Music"),
		WRITING_MUSIC("Writing Music"),
		SCIENTIFIC_DISCOVERY("Scientific Discovery"),
		ANIMALS("Animals"),
		STRATEGY_GAMES("Strategy Games"),
		ADVENTURE("Adventure"),
		CARPENTRY("Carpentry"),
		PRACTICAL_JOKES("Practical Jokes"),
		KNITTING("Knitting"),
		SWIMMING("Swimming"),
		PERFORMING("Performing"),
		SCULPTING("Sculpting"),
		SPORTS("Sports"),
		HIKING("Hiking"),
		TRAVELING("Traveling")
		;
		
		private String displayName;
		private Interest(String displayName) {
			this.displayName = displayName;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	}
	
	public enum CombatTrait {
		ACCURACY("Accuracy", 5),
		AVOIDANCE("Avoidance", 5),
		CRITRATE("Critical Hit Rate", 5),
		CRITAVOID("Security", 5),
		ATTACKPOWER("Attack Power", 5),
		;
		private String displayName;
		private int supportDividend;
		private CombatTrait(String displayName, int supportDividend) {
			this.displayName = displayName;
			this.supportDividend = supportDividend;
		}
		public String getDisplayName() {
			return displayName;
		}
		public int getSupportDividend() {
			return supportDividend;
		}
	}
	
	public static final int RIGHT_ARM = 2;
	public static final int LEFT_ARM = 3;
	public static final int RIGHT_LEG = 4;
	public static final int LEFT_LEG = 5;
	public static final int RIGHT_EYE = 6;
	public static final int LEFT_EYE = 7;
	public static final int MOUNT = 8;
	
	public static final String[] BODY_PARTS_STRINGS = {"Head", "Torso", "Right Arm",
			"Left Arm", "Right Leg", "Left Leg", "Right Eye", "Left Eye", "Mount"};
	
	protected int age;
	protected boolean isMortal;
	protected boolean gender; //True for female, false for male
	protected int nationalism;
	protected int militarism;
	protected int altruism;
	protected int familism;
	protected int confidence;
	protected int tolerance;
	protected Interest[] interests;
	protected Interest[] disinterests;
	protected Demeanor demeanor;
	protected CombatTrait valuedTrait;
	protected int morale; //Integer from 0-100
	protected int[] appearance;
	protected int[][] inventory;
	protected int[] armor;
	/**
	 * proficiency indexes are...
	 * 0 for sword
	 * 1 for lance
	 * 2 for axe
	 * 3 for bow
	 * 4 for knife
	 * 5 for ballista
	 * 6 for anima
	 * 7 for light
	 * 8 for dark
	 * 9 for staff
	 */
	protected int[] proficiency;
	protected CityState home;
	protected Nation affiliation;
	protected Human supportPartner;
	protected boolean isParent;
	protected int relationshipWithPlayer;
	protected int relationshipWithSupportPartner;

	public Human(String name, boolean gender, int[] maxHPs, int magic, int skill, int reflex,
			int awareness, int resistance, int movement, int leadership, int[] maxHPGrowths,
			int magGrowth, int sklGrowth, int rfxGrowth, int awrGrowth, int resGrowth,
			int age, int[] personalValues, int[] appearance, Interest[] interests,
			Interest[] disinterests, Demeanor demeanor, CombatTrait valuedTrait, CityState home) {
		super(0, null, //Start out at level 0, with no class
				name, maxHPs, magic, skill, reflex,
				awareness, resistance, movement, leadership, maxHPGrowths,
				magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth);
		//Specifically human values
		this.gender = gender;
		this.age = age;
		this.isMortal = true; //No one is born immortal
		this.nationalism = personalValues[0];
		this.militarism = personalValues[1];
		this.altruism = personalValues[2];
		this.familism = personalValues[3];
		this.confidence = personalValues[4];
		this.tolerance = personalValues[5];
		this.appearance = appearance;
		this.interests = interests;
		this.disinterests = disinterests;
		this.demeanor = demeanor;
		this.valuedTrait = valuedTrait;
		//TODO initialize morale
		this.inventory = new int[3][];
		this.proficiency = new int[10];
		this.home = home;
		this.affiliation = home.getNation();
		this.relationshipWithPlayer = 0;
		this.relationshipWithSupportPartner = 0;
	}
	
	public void assignSupportPartner(Human partner) {
		if (supportPartner != null || partner.supportPartner != null
				|| isParent || partner.isParent) {
			throw new IllegalArgumentException("Cannot assign a new support partner");
		}
		this.supportPartner = partner;
		partner.supportPartner = this;
		calculateInitialSupportRelationship();
		partner.calculateInitialSupportRelationship();
	}
	
	private void calculateInitialSupportRelationship() {
		Human p = supportPartner;
		int love = 0;
		int hate = 0;
		for (int q = 0; q < interests.length; q++) {
			for (int w = 0; w < p.interests.length; w++) {
				if (interests[q] == p.interests[w]) {
					love += 10;
				}
			}
		}
		for (int q = 0; q < disinterests.length; q++) {
			for (int w = 0; w < p.interests.length; w++) {
				if (disinterests[q] == p.interests[w]) {
					hate += 10;
				}
			}
		}
		relationshipWithSupportPartner = love - hate;
		int natDiff = Math.abs(nationalism - p.getNationalism());
		if (natDiff <= 10) {
			relationshipWithSupportPartner += 10;
		} else {
			relationshipWithSupportPartner -= natDiff / tolerance;
		}
		int milDiff = Math.abs(militarism - p.getMilitarism());
		if (milDiff <= 10) {
			relationshipWithSupportPartner += 10;
		} else {
			relationshipWithSupportPartner -= milDiff / tolerance;
		}
		int altDiff = Math.abs(altruism - p.getAltruism());
		if (altDiff <= 10) {
			relationshipWithSupportPartner += 10;
		} else {
			relationshipWithSupportPartner -= altDiff / tolerance;
		}
		int famDiff = Math.abs(familism - p.getFamilism());
		if (famDiff <= 10) {
			relationshipWithSupportPartner += 10;
		} else {
			relationshipWithSupportPartner -= famDiff / tolerance;
		}
		if (p.getDemeanor().getRarity() > 0) {
			//Rare demeanors are generally less likeable.
			//Maybe I'll change this so the Demeanor enum specifies likeability
			relationshipWithSupportPartner -= 10;
		}
	}
	public void alterSupportRelationship(int amount) {
		//I've decided that relationship can be as positive or negative as possible
		//It doesn't have to be bounded by -100 and 100
		relationshipWithSupportPartner += amount;
	}
	public int getRelationshipWithSupportPartner() {
		return relationshipWithSupportPartner;
	}
	public int getRelationshipWithPlayer() {
		return relationshipWithPlayer;
	}
	/**
	 * This is used to determine if this unit is married to their support partner and
	 * is able to reproduce.
	 * @return
	 */
	public boolean mayReproduce() {
		
		//Must have a support partner to potentially be their spouse
		if (supportPartner == null) {
			return false;
		}
		Human p = supportPartner;
		
		//Must not already have a child. Only allowing one child ensures that there are
		//no uncles, aunts, cousins, etc. for someone to marry. Also, not allowing parents
		//to re-marry prevents them from marrying their children, grandchildren, etc.
		if (isParent || p.isParent) {
			return false;
		}
		
		//Let's not have any significant age gaps.
		//Sure they're all adults, but I just don't feel like it
		//Also, since Humans can be instantiated at max age 30, it makes
		//sense that you'd be at least 50 by the time we meet your adult kid
		//The lore logic falls apart if you consider a couple that enlisted at
		//30, retired by 50, and introduced a kid at 50 who enlisted at 30
		//(that'd mean the child was born ten years before his parents met)
		//But whatever. We never explicitly say they didn't know each other before
		//enlisting. Maybe they did and had a kid before marriage
		if (Math.abs(age - p.age) > 10 || Math.min(age, p.age) < 50) {
			return false;
		}
		
		//Must be married to support partner
		return marriedToSupportPartner();
	}
	
	public boolean marriedToSupportPartner() {
		//Obviously, no support partner means no romantic relationship
		if (supportPartner == null) {
			return false;
		}
		
		//Must be opposite genders
		if (gender == supportPartner.getGender()) {
			return false;
		}
		
		//Must be mutually in love (which might as well be a marriage, since support
		//partners are already exclusively together for life)
		return relationshipWithSupportPartner >= 90 && supportPartner.relationshipWithSupportPartner >= 90;
	}
	
	/**
	 * Assuming this is a legal action, this person and their support partner
	 * have a child
	 * @return
	 */
	public Offspring reproduce() {
		isParent = true;
		supportPartner.isParent = true;
		return Offspring.getChild(this, supportPartner);
	}
	
	public boolean canAssignSupportPartner() {
		return supportPartner == null && !(isParent);
	}
	
	public void assignClass(UnitClass unitClass) {
		this.unitClass = unitClass;
		int[] gMods = unitClass.getGrowthModifiers();
		bodyPartsMaximumHPGrowth[HEAD] += gMods[0]; //gMods[0] is for the head
		bodyPartsMaximumHPGrowth[TORSO] += gMods[1]; //gMods[1] is for the torso
		bodyPartsMaximumHPGrowth[RIGHT_ARM] += gMods[2]; //gMods[2] is for the arms
		bodyPartsMaximumHPGrowth[LEFT_ARM] += gMods[2];
		bodyPartsMaximumHPGrowth[RIGHT_LEG] += gMods[3]; //gMods[3] is for the legs
		bodyPartsMaximumHPGrowth[LEFT_LEG] += gMods[3];
//		bodyPartsMaximumHPGrowth[RIGHT_EYE] += 0;        Eyes do not change
//		bodyPartsMaximumHPGrowth[LEFT_EYE] += 0;
		bodyPartsMaximumHPGrowth[MOUNT] = unitClass.initializeMountGrowth();
		bodyPartsMaximumHP[MOUNT] = unitClass.initializeMountHealth();
		bodyPartsCurrentHP[MOUNT] = bodyPartsMaximumHP[MOUNT]; //Mount starts at full health
		
		//The rest of the growths are as follows
		magicGrowth += gMods[4];
		skillGrowth += gMods[5];
		reflexGrowth += gMods[6];
		awarenessGrowth += gMods[7];
		resistanceGrowth += gMods[8];
		
		for (int q = 0; q < proficiency.length; q++) {
			proficiency[q] += unitClass.getProficiencyModifiers()[q];
		}
		if (unitClass.getTier() > 1) {
			int[] sMods = unitClass.getStatModifiers();
			bodyPartsMaximumHP[HEAD] += sMods[0]; //sMods[0] is for the head
			bodyPartsMaximumHP[TORSO] += sMods[1]; //sMods[1] is for the torso
			bodyPartsMaximumHP[RIGHT_ARM] += sMods[2]; //sMods[2] is for the arms
			bodyPartsMaximumHP[LEFT_ARM] += sMods[2];
			bodyPartsMaximumHP[RIGHT_LEG] += sMods[3]; //sMods[3] is for the legs
			bodyPartsMaximumHP[LEFT_LEG] += sMods[3];
//			bodyPartsMaximumHP[RIGHT_EYE] += 0;        Eyes do not change
//			bodyPartsMaximumHP[LEFT_EYE] += 0;
			bodyPartsMaximumHP[MOUNT] = unitClass.initializeMountGrowth(); //Mount is upgraded
			bodyPartsMaximumHP[MOUNT] = unitClass.initializeMountHealth();
			bodyPartsCurrentHP[MOUNT] = bodyPartsMaximumHP[MOUNT]; //New mount gets full health
			//The rest is as follows
			magic += sMods[4];
			skill += sMods[5];
			reflex += sMods[6];
			awareness += sMods[7];
			resistance += sMods[8];
			movement += sMods[9];
			leadership += sMods[10];
		}
	}
	
	public int getBaseMovement() {
		return movement;
	}

	@Override
	public int getMovement() {
		//If unit has a mount, Movement is the percentage of the mount's health
		if (bodyPartsCurrentHP[MOUNT] > 0) {
			float percentMove = (float) ((0.0 + bodyPartsCurrentHP[MOUNT]) / (0.0 + bodyPartsMaximumHP[MOUNT]));
			return Math.max(0, Math.round(percentMove * unitClass.getMountMovement()));
		}
		//If no mount, Movement is the percentage of the unit's legs' health
		float percentMove =
				(float) ((0.0 + bodyPartsCurrentHP[RIGHT_LEG] + bodyPartsCurrentHP[LEFT_LEG]) / (0.0 + bodyPartsMaximumHP[RIGHT_LEG] + bodyPartsMaximumHP[LEFT_LEG]));
		return Math.max(0, Math.round(percentMove * movement));
	}

	@Override
	public boolean canCarryUnit() {
		//Can carry a unit if the unit has a mount and the mount is alive
		return unitClass != null && bodyPartsCurrentHP[MOUNT] > 0;
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
			Weapon w = (Weapon)i;
			encumberment += w.getWeight() - armStrength();
		}
		encumberment = Math.max(0, encumberment);
		return Math.max(0, reflex - encumberment);
	}

	@Override
	public boolean canFly() {
		//Can fly if they have a flying mount and the mount is alive
		return unitClass != null &&
				unitClass.mountCanFly() && bodyPartsCurrentHP[MOUNT] > 0;
	}

	@Override
	public int attackStrength() {
		Item i = getEquippedItem();
		if (i instanceof Weapon) {
			Weapon w = (Weapon) i;
			if (w.isMagic()) {
				return magic + w.getMight() + getSupportRNGBonus(CombatTrait.ATTACKPOWER);
			}
			return armStrength() + w.getMight() + getSupportRNGBonus(CombatTrait.ATTACKPOWER);
		}
		return armStrength() + getSupportRNGBonus(CombatTrait.ATTACKPOWER);
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
		
		accuracy += getSupportRNGBonus(CombatTrait.ACCURACY);
		if (group != null && this != group.getLeader()) { //Leader cannot give themselves a bonus
			accuracy += group.getLeadershipBonus(this);
		}
		return accuracy;
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
	public int avoidance(int bodyPart) {
		int currentEyesHP = bodyPartsCurrentHP[RIGHT_EYE] + bodyPartsCurrentHP[LEFT_EYE];
		int maxEyesHP = Math.max(1, bodyPartsMaximumHP[RIGHT_EYE] + bodyPartsMaximumHP[LEFT_EYE]);
		double percentageEyesHP = (0.0 + currentEyesHP) / maxEyesHP;
		int effectiveAwareness = Math.round((float)(percentageEyesHP * awareness));
		
		int effectiveMountEvasion = 0;
		if (unitClass != null && unitClass.getMount() != null) {
			int maxMountHP = Math.max(1, bodyPartsCurrentHP[MOUNT]);
			double percentageMountHP = (0.0 + bodyPartsCurrentHP[MOUNT]) / maxMountHP;
			effectiveMountEvasion = Math.round((float)(percentageMountHP * unitClass.getMountEvasionBonus()));
		}
		
		int avoidance = (attackSpeed() * 2) + effectiveAwareness + effectiveMountEvasion;
		if (group != null && this != group.getLeader()) { //Leader cannot give themselves a bonus
			avoidance += group.getLeadershipBonus(this);
		}
		avoidance += getSupportRNGBonus(CombatTrait.AVOIDANCE);
		if (bodyPart == 0) {
			avoidance += 15;
		} else if (bodyPart == RIGHT_ARM || bodyPart == LEFT_ARM) {
			avoidance += 30;
		} else if (bodyPart == RIGHT_LEG || bodyPart == LEFT_LEG) {
			avoidance += 45;
		} else if (bodyPart == RIGHT_EYE || bodyPart == LEFT_EYE) {
			avoidance += 70;
		}
		return avoidance;
	}
	
	private int getSupportRNGBonus(CombatTrait ct) {
		if (supportPartner != null &&
				supportPartner.getGroup() != null
				&& getGroup().getBattle() != null
				&& getGroup().getBattle() == supportPartner.getGroup().getBattle()
				&& supportPartner.getValuedTrait() == ct) {
			return Math.max(-20, Math.min(20, relationshipWithSupportPartner / ct.getSupportDividend()));
		}
		return 0;
	}
	
	@Override
	public int getBaseCrit() {
		int crit = skill + getSupportRNGBonus(CombatTrait.CRITRATE);
		return crit;
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
		return Math.max(0, crit);
	}

	@Override
	public int criticalHitAvoid() {
		int avoid = awareness + getSupportRNGBonus(CombatTrait.CRITAVOID);
		return avoid;
	}
	
	@Override
	public boolean isUsingMagic() {
		Item i = getEquippedItem();
		return i instanceof Weapon && ((Weapon) i).isMagic();
	}

	@Override
	public void useWeapon(boolean hit) {
		HandheldWeapon w = getEquippedWeapon();
		if (w != null) {
			proficiency[w.getProficiencyIndex()]++;
			if (hit || w.usesDurabilityWithoutHitting()) {
				inventory[0][2]--; //Equipped item loses durability
				//TODO add breaking function somewhere else, so we can tell what weapon
				//killed a person even if it breaks. Also, remember to remove this part
				//in EquippedMonster
//				if (inventory[0][2] == 0) {
//					inventory[0] = null;
//					autoEquip();
//				}
			}
		}
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

	public void incrementSupportRelationship() {
		if (supportPartner != null && getGroup() == supportPartner.getGroup()) {
			alterSupportRelationship(1);
			supportPartner.alterSupportRelationship(1);
		}
	}

	@Override
	public boolean gainExperience(int exp) {
		//Gain experience only if you're not already max level
		if (level != Unit.MAX_LEVEL) {
			exp -= effectiveLevel();
			exp = Math.max(1, Math.min(Unit.EXPERIENCE_TOWARDS_LEVEL, exp)); //Max 100, Min 1
			experience += exp;
			//If you got enough experience for a level,
			if (experience >= Unit.EXPERIENCE_TOWARDS_LEVEL) {
				//Level up
				//We don't need a level-up animation, considering there are so many units
				//So just adjust the stats silently here
				levelUp();
				//Adjust experience appropriately
				if (level == MAX_LEVEL) {
					experience = 0;
				} else {
					experience -= EXPERIENCE_TOWARDS_LEVEL;
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Used by training facilities for leveling up units
	 */
	public void finishLevel() {
		if (level < Unit.MAX_LEVEL) {
			experience = 0;
			levelUp();
		}
	}
	
	public void fullHeal() {
		for (int q = 0; q < bodyPartsCurrentHP.length; q++) {
			bodyPartsCurrentHP[q] = bodyPartsMaximumHP[q];
		}
	}
	
	public int getAge() {
		return age;
	}
	
	public boolean isMortal() {
		return isMortal;
	}
	
	public void toggleMortality() {
		isMortal = !(isMortal);
	}

	/**
	 * Increment age if mortal and perform aging processes.
	 * @return true if the unit is still alive, false if they died
	 */
	public boolean incrementAge() {
		if (isMortal) {
			age++;
			if (RNGStuff.random0To99() < age - 40) {
				//Reduce growths of all body parts except eyes and mount
				bodyPartsMaximumHPGrowth[HEAD] = Math.max(0, bodyPartsMaximumHPGrowth[HEAD] - 5);
				bodyPartsMaximumHPGrowth[TORSO] = Math.max(0, bodyPartsMaximumHPGrowth[1] - 5);
				bodyPartsMaximumHPGrowth[RIGHT_ARM] = Math.max(0, bodyPartsMaximumHPGrowth[RIGHT_ARM] - 5);
				bodyPartsMaximumHPGrowth[LEFT_ARM] = Math.max(0, bodyPartsMaximumHPGrowth[LEFT_ARM] - 5);
				bodyPartsMaximumHPGrowth[RIGHT_LEG] = Math.max(0, bodyPartsMaximumHPGrowth[RIGHT_LEG] - 5);
				bodyPartsMaximumHPGrowth[LEFT_LEG] = Math.max(0, bodyPartsMaximumHPGrowth[LEFT_LEG] - 5);
				//Reduce growths of physical attributes
				skillGrowth = Math.max(0, skillGrowth - 5);
				reflexGrowth = Math.max(0, reflexGrowth - 5);
				awarenessGrowth = Math.max(0, awarenessGrowth - 5);
			}
			if (RNGStuff.random0To99() < (age - 50) / 2) {
				//TODO die
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void deathSequence() {
		affiliation.getArmy().remove(this);
		if (affiliation.getRuler() == this) {
			affiliation.setRuler(null);
		}
		group.remove(this);
		Human sp = supportPartner;
		supportPartner = null;
		if (sp != null) {
			sp.supportPartner = null;
		}
		if (sp != null) {
			int moraleLoss = Math.min(sp.getMorale(),
					sp.relationshipWithSupportPartner - (sp.getMilitarism() / 5));
			moraleLoss = Math.max(moraleLoss, 0);
			sp.morale -= moraleLoss;
		}
		home.mournSoldierDeath();
	}

	/**
	 * Determines if the unit can use classes with unicorns
	 * @return
	 */
	public boolean getGender() {
		return gender;
	}
	
	@Override
	public boolean canUseBallista() {
		return armStrength() > 10;
	}

	@Override
	public boolean canUseMagicTurrets() {
		return magic > 0;
	}
	
	@Override
	public boolean canUse(StationaryWeapon weapon) {
		if (weapon.isMagic()) {
			return canUseMagicTurrets();
		}
		return canUseBallista();
	}

	@Override
	public Item getEquippedItem() {
		return InventoryIndex.getElement(inventory[0]);
	}
	
	@Override
	public String getWeaponName() {
		HandheldWeapon w = getEquippedWeapon();
		if (w == null) {
			return "None";
		}
		return w.getName();
	}
	
	@Override
	public String getArmorName() {
		Armor a = getArmor();
		if (a == null) {
			return "None";
		}
		return a.getName();
	}
	
	@Override
	public int[][] getInventory() {
		return inventory;
	}
	
	@Override
	public Armor getArmor() {
		Item i = InventoryIndex.getElement(armor);
		if (i instanceof Armor) {
			return (Armor) i;
		}
		return null;
	}
	
	@Override
	public void destroyArmor() {
		armor = null;
	}

	@Override
	public HandheldWeapon getEquippedWeapon() {
		Item i = InventoryIndex.getElement(inventory[0]);
		if (i instanceof HandheldWeapon) {
			return (HandheldWeapon) i;
		}
		return null;
	}
	
	@Override
	public void autoEquip() {
		int idx = 0;
		int h = getEquipmentHeuristic(inventory[idx]);
		if (getEquipmentHeuristic(inventory[1]) > h) {
			idx = 1;
		}
		if (getEquipmentHeuristic(inventory[2]) > h) {
			idx = 2;
		}
		equip(idx);
	}
	
	@Override
	public int getEquipmentHeuristic(int[] item) {
		Item i = InventoryIndex.getElement(item);
		if (i instanceof HandheldWeapon) {
			int h = 0;
			HandheldWeapon w = (HandheldWeapon)i;
			int profBonus = proficiency[w.getProficiencyIndex()] - w.getProficiencyRequirement();
			h += Math.min(100, (profBonus * 10));
			h += w.getHit();
			h += w.getMight();
			if (w.isMagic()) {
				h += getMagic();
			} else {
				h += armStrength();
			}
			return h;
		} else if (i instanceof Armor) {
			//TODO
		} else if (i instanceof Staff) {
			//TODO
		}
		return Integer.MIN_VALUE;
	}
	
	@Override
	public void equip(int idx) {
		int[] temp = inventory[0];
		inventory[0] = inventory[idx];
		inventory[idx] = temp;
	}
	
	@Override
	public boolean receiveNewItem(int[] item) {
		for (int q = 0; q < inventory.length; q++) {
			if (inventory[q] == null) {
				inventory[q] = InventoryIndex.newInstanceOfItem(item);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean receiveNewArmor(int[] is) {
		if (armor == null) {
			armor = InventoryIndex.newInstanceOfItem(is);
			return true;
		}
		return false;
	}

	@Override
	public int proficiencyWith(int type) {
		return proficiency[type];
	}

	@Override
	public String getDisplayName() {
		return name + " of " + home.getName();
	}
	public int getNationalism() {
		return nationalism;
	}
	public int getMilitarism() {
		return militarism;
	}
	public int getAltruism() {
		return altruism;
	}
	public int getFamilism() {
		return familism;
	}
	public int getConfidence() {
		return confidence;
	}
	public int getTolerance() {
		return tolerance;
	}
	public String getInterest1() {
		return interests[0].getDisplayName();
	}
	public String getInterest2() {
		return interests[1].getDisplayName();
	}
	public String getInterest3() {
		return interests[2].getDisplayName();
	}
	public String getDisinterest1() {
		return disinterests[0].getDisplayName();
	}
	public String getDisinterest2() {
		return disinterests[1].getDisplayName();
	}
	public String getDisinterest3() {
		return disinterests[2].getDisplayName();
	}
	public Demeanor getDemeanor() {
		return demeanor;
	}
	public CombatTrait getValuedTrait() {
		return valuedTrait;
	}
	public String getDemeanorAsString() {
		return demeanor.getDisplayName();
	}
	public String getValuedTraitAsString() {
		return valuedTrait.getDisplayName();
	}
	public int[] getAppearance() {
		return appearance;
	}
	public Color getHairColor() {
		return RNGStuff.HAIR_COLORS_IN_USE.colorAtIndex(appearance[10]);
	}
	public Color getSkinColor() {
		return new Color((0.0 + appearance[11])/255,
				(0.0 + appearance[12])/255,
				(0.0 + appearance[13])/255,
				1);
	}
	public Color getEyeColor() {
		return RNGStuff.EYE_COLORS_IN_USE.colorAtIndex(appearance[14]);
	}
	public Human getSupportPartner() {
		return supportPartner;
	}
	public String getSupportPartnerName() {
		if (supportPartner == null) {
			return "None";
		}
		return supportPartner.getName();
	}
	public String getGenderAsString() {
		if (gender) {
			return "Female";
		}
		return "Male";
	}
	
	public int getMorale() {
		return morale;
	}
	
	public CityState getHome() {
		return home;
	}

	@Override
	public void defect(Nation n) {
		group.remove(this);
		group = null;
		affiliation = n;
		//You are as loyal to your new nation as you are to your family, which
		//encouraged you to rebel
		militarism = familism;
		if (home.getNation() != n) {
			home = n.getCapital();
		}
		//No need to add or remove yourself to/from an army. The Nation constructor does this
	}
	
	/**
	 * Meant for owners of buildings only, not for playable units
	 * @param n
	 */
	public void setAffiliation(Nation n) {
		this.affiliation = n;
	}
	
	public void declareLoyalty() {
		this.home = affiliation.getCapital();
	}
	
	@Override
	public Nation getAffiliation() {
		return affiliation;
	}
	
	public boolean retire() {
		if (!isMortal) {
			return false;
			//Immortal units will not be allowed to retire
		}
		if (home.getNation() != affiliation) {
			home = affiliation.getCapital();
		}
		int res = RNGStuff.nextInt(home.getResidentialAreas().size());
		int initRes = res;
		while (!(home.getResidentialAreas().get(res).addVeteran(this))) {
			res++;
			if (res == home.getResidentialAreas().size()) {
				res = 0;
			}
			if (res == initRes) {
				//All residential areas are full, so the unit cannot be retired
				return false;
			}
		}
		if (group != null) {
			group.remove(this);
			group = null;
		}
		affiliation.getArmy().remove(this);
		return true;
	}

	public static Human completelyRandomHuman(CityState home) {
		Random rng = RNGStuff.rng;
		int[] personalValues = new int[6];
		Interest[] interests = new Interest[3];
		Interest[] disinterests = new Interest[3];
		generatePersonality(personalValues, interests, disinterests, home.getValues());
		Demeanor[] types = Demeanor.values();
		Demeanor demeanor = types[rng.nextInt(types.length)];
		if (demeanor.getRarity() > 0) {
			demeanor = types[rng.nextInt(types.length)];
		}
		CombatTrait[] traits = CombatTrait.values();
		CombatTrait valued = traits[rng.nextInt(traits.length)];
		int[] appearance = generateAppearance();
		int[] maxHPs = generateMaxHPs();
		boolean gend = rng.nextBoolean();
		//Stats ranges based on Thracia 776
		int mag = rng.nextInt(12);
		int skl = rng.nextInt(13);
		int rfx = rng.nextInt(12);
		int awr = rng.nextInt(13);
		int res = rng.nextInt(9);
		int mov = rng.nextInt(3) + 5;
		int ldr = rng.nextInt(6);
		if (ldr > 3 && RNGStuff.nextBoolean()) {
			ldr = rng.nextInt(2); //Can't have too many good leaders
		}
		int[] maxHPsGrowths = generateMaxHPGrowths();
		int magGrowth = rng.nextInt(31); //Growth rate before class modifiers is between 0 and 60
		int sklGrowth = rng.nextInt(51);
		int rfxGrowth = rng.nextInt(51);
		int awrGrowth = rng.nextInt(51);
		int resGrowth = rng.nextInt(11);
		String genName = RNGStuff.randomName(home.getLanguage());
		int age = rng.nextInt(13) + 18;
		return new Human(genName, gend, maxHPs, mag, skl, rfx, awr, res, mov, ldr,
				maxHPsGrowths, magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth,
				age, personalValues, appearance, interests, disinterests, demeanor,
				valued, home);
	}
	
	protected static void generatePersonality(int[] hVals, Interest[] interests,
			Interest[] disinterests) {
		hVals[0] = RNGStuff.random0To100(); //Nationalism
		hVals[1] = RNGStuff.random0To100(); //Militarism
		hVals[2] = RNGStuff.random0To100(); //Altruism
		hVals[3] = RNGStuff.random0To100(); //Familism
		hVals[4] = RNGStuff.random0To100(); //Confidence
		hVals[5] = RNGStuff.random0To100(); //Tolerance
		Random rng = RNGStuff.rng;
		Interest[] choices = Interest.values();
		int i1 = rng.nextInt(choices.length);
		int i2 = rng.nextInt(choices.length);
		if (i1 == i2) {
			i2++;
			if (i2 == choices.length) {
				i2 = 0;
			}
		}
		int i3 = rng.nextInt(choices.length);
		while (i2 == i3 || i1 == i3) {
			i3++;
			if (i3 == choices.length) {
				i3 = 0;
			}
		}
		int d1 = rng.nextInt(choices.length);
		int d2 = rng.nextInt(choices.length);
		int d3 = rng.nextInt(choices.length);
		while (d1 == i1 || d1 == i2 || d1 == i3) {
			d1++;
			if (d1 == choices.length) {
				d1 = 0;
			}
		}
		while (d2 == i1 || d2 == i2 || d2 == i3 || d2 == d1) {
			d2++;
			if (d2 == choices.length) {
				d2 = 0;
			}
		}
		while (d3 == i1 || d3 == i2 || d3 == i3 || d3 == d1 || d3 == d2) {
			d3++;
			if (d3 == choices.length) {
				d3 = 0;
			}
		}
		interests[0] = choices[i1];
		interests[1] = choices[i2];
		interests[2] = choices[i3];
		disinterests[0] = choices[d1];
		disinterests[1] = choices[d2];
		disinterests[2] = choices[d3];
	}
	
	/**
	 * Set the values of the interests, disinterests, and personal values arrays (the latter
	 * influenced by the city-state values) and return the demeanor
	 * @param hVals
	 * @param interests
	 * @param disinterests
	 * @param csValues
	 * @return
	 */
	protected static void generatePersonality(int[] hVals, Interest[] interests, 
			Interest[] disinterests, int[] csValues) {
		//Generate personality as normal
		generatePersonality(hVals, interests, disinterests);
		//Average personal values with city-state's values
		hVals[0] = (hVals[0] + csValues[0]) / 2;
		hVals[1] = (hVals[1] + csValues[1]) / 2;
		hVals[2] = (hVals[2] + csValues[2]) / 2;
		hVals[3] = (hVals[3] + csValues[3]) / 2;
		hVals[4] = (hVals[4] + csValues[4]) / 2;
		hVals[5] = (hVals[5] + csValues[5]) / 2;
	}
	
	/**
	 * For humans, body part indexes are
	 * 0 for head
	 * 1 for torso
	 * 2 for arm1
	 * 3 for arm2
	 * 4 for leg1
	 * 5 for leg2
	 * 6 for eye1
	 * 7 for eye2
	 * 8 for mount
	 * @return
	 */
	protected static int[] generateMaxHPs() {
		int[] mhps = new int[9];
		Random rng = RNGStuff.rng;
		mhps[HEAD] = rng.nextInt(11) + 10; //Head HP is 10-20
		mhps[TORSO] = rng.nextInt(17) + 14; //Torso HP is 14-30
		mhps[RIGHT_ARM] = rng.nextInt(11) + 10; //Arms HPs are 10-20
		mhps[LEFT_ARM] = mhps[RIGHT_ARM];
		mhps[RIGHT_LEG] = rng.nextInt(16) + 10; //Legs HPs are 10-25
		mhps[LEFT_LEG] = mhps[RIGHT_LEG];
		mhps[RIGHT_EYE] = rng.nextInt(6) + 5; //Eyes HPs are 5-10
		mhps[LEFT_EYE] = mhps[RIGHT_EYE];
		mhps[MOUNT] = 0; //Mount's HP is always 0, since no one starts with a mount
		return mhps;
	}
	
	/**
	 * For humans, body part indexes are
	 * 0 for head
	 * 1 for torso
	 * 2 for arm1
	 * 3 for arm2
	 * 4 for leg1
	 * 5 for leg2
	 * 6 for eye1
	 * 7 for eye2
	 * 8 for mount
	 * @return
	 */
	protected static int[] generateMaxHPGrowths() {
		int[] growths = new int[9];
		Random rng = RNGStuff.rng;
		growths[HEAD] = rng.nextInt(51) + 20; //Head
		growths[TORSO] = rng.nextInt(61) + 30; //Torso
		growths[RIGHT_ARM] = rng.nextInt(71) + 10; //Arm1
		growths[LEFT_ARM] = growths[RIGHT_ARM];           //Arm2 = Arm1
		growths[RIGHT_LEG] = rng.nextInt(71) + 20; //Leg1
		growths[LEFT_LEG] = growths[RIGHT_LEG];           //Leg2 = Leg1
		growths[RIGHT_EYE] = 0; //Eyes always have a growth rate of 0
		growths[LEFT_EYE] = 0;
		growths[MOUNT] = 0; //Mount's growth rate will be determined if/when the unit gets a mount
		return growths;
	}
	
	protected static int[] generateAppearance() {
		int[] app = new int[15];
		//TODO make sure numbers are all right
		Random rng = RNGStuff.rng;
		app[0] = rng.nextInt(15); //Face shape
		app[1] = rng.nextInt(2); //Lips
		app[2] = rng.nextInt(15); //Nose shape
		app[3] = rng.nextInt(7); //Ear shape
		app[4] = rng.nextInt(15); //Eye shape
		app[5] = rng.nextInt(7); //Iris appearance (Unused)
		app[6] = rng.nextInt(3); //Eyebrows
		app[7] = rng.nextInt(15); //Hairstyle
		if (RNGStuff.nextBoolean()) {
			app[8] = rng.nextInt(12); //Mustache style
			app[9] = rng.nextInt(12); //Beard style
		} else {
			app[8] = 0; //No mustache
			app[9] = 0; //No beard
		}
		app[10] = RNGStuff.getRandomHairColor(); //Hair color
		Color skinColor = RNGStuff.SKIN_COLORS_IN_USE.colorAtIndex(RNGStuff.getRandomSkinColor()); //Skin color
		app[11] = (int)Math.round(skinColor.getRed() * 255);
		app[12] = (int)Math.round(skinColor.getGreen() * 255);
		app[13] = (int)Math.round(skinColor.getBlue() * 255);
		app[14] = RNGStuff.getRandomEyeColor(); //Eye color
		return app;
	}

	/**
	 * For testing purposes only
	 * @return
	 */
	public String showStats() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Name: %s, Gender: %s, Age: %d\n", getDisplayName(), getGenderAsString(), age));
		sb.append(String.format("Militarism: %d, Altruism: %d, Familism: %d, Nationalism: %d\n", getMilitarism(), getAltruism(), getFamilism(), getNationalism()));
		sb.append(String.format("Confidence: %d, Tolerance: %d, Interests: %s, %s, %s\n",
				getConfidence(), getTolerance(), getInterest1(), getInterest2(), getInterest3()));
		sb.append(String.format("Demeanor: %s, Valued Trait: %s, Disinterests: %s, %s, %s\n",
				getDemeanorAsString(), getValuedTraitAsString(), getDisinterest1(), getDisinterest2(), getDisinterest3()));
		int[] c = bodyPartsCurrentHP;
		int[] m = bodyPartsMaximumHP;
		int[] g = bodyPartsMaximumHPGrowth;
		sb.append(String.format("Head: %d/%d (%d%%), Torso: %d/%d (%d%%), Arm1: %d/%d (%d%%), Arm2: %d/%d (%d%%)\n"
				+ "Leg1: %d/%d (%d%%), Leg2: %d/%d (%d%%), Eye1: %d/%d (%d%%), Eye2: %d/%d (%d%%), Mount: %d/%d (%d%%)\n", 
				c[HEAD], m[HEAD], g[HEAD], c[TORSO], m[TORSO], g[TORSO], c[RIGHT_ARM], m[RIGHT_ARM], g[RIGHT_ARM], c[LEFT_ARM], m[LEFT_ARM], g[LEFT_ARM], c[RIGHT_LEG], m[RIGHT_LEG], g[RIGHT_LEG], c[LEFT_LEG], m[LEFT_LEG], g[LEFT_LEG],
				c[RIGHT_EYE], m[RIGHT_EYE], g[RIGHT_EYE], c[LEFT_EYE], m[LEFT_EYE], g[LEFT_EYE], c[MOUNT], m[MOUNT], g[MOUNT]));
		sb.append(String.format("Magic: %d (%d%%), Skill: %d (%d%%), Reflex: %d (%d%%), Awareness: %d (%d%%)\n"
				+ "Resistance: %d (%d%%), Movement: %d, Leadership: %d\n",
				magic, magicGrowth, skill, skillGrowth, reflex, reflexGrowth, awareness, awarenessGrowth,
				resistance, resistanceGrowth, getMovement(), leadership));
		sb.append(String.format("Class: %s, Level: %d, EXP: %d, Weapon: %s, Armor: %s\n",
				getUnitClassName(), getLevel(), getExperience(), getWeaponName(), getArmorName()));
		sb.append(String.format("Support Partner: %s (%d)\n", getSupportPartnerName(), relationshipWithSupportPartner));
		sb.append(String.format("ATK: %d, ACC: %d, AVO(Torso): %d, CRT: %d, CRTAVO(Torso): %d\n",
				attackStrength(), accuracy(), avoidance(1), criticalHitRate(), criticalHitAvoid()));
		
		return sb.toString();
	}

	@Override
	public String[] getBodyPartsNames() {
		return BODY_PARTS_STRINGS;
	}

}
