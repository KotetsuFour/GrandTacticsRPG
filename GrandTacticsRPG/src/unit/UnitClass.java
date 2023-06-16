package unit;

import location.WorldMapTile.WorldMapTileType;
import unit.human.Human;
import util.RNGStuff;

public class UnitClass {

	private String name;
	private Mount mount;
	private int unitType;
	private int[] growthModifiers;
	private int[] proficiencyModifiers;
	private int[] statModifiers; //Null if 1st tier
	private int tier;
	private UnitClass previous; //Null if 1st tier
	private UnitClass promotion; //Null if 4th tier or there just isn't a promotion
	private ClassAbility ability;
	private int strengthHeuristicMultiplier;
	private int magicHeuristicMultiplier;
	private int generalInternalHeuristic;
	
	
	public UnitClass(String name, Mount mount, int unitType, int[] growthModifiers,
			int[] proficiencyModifiers, int[] statModifiers, UnitClass previous,
			ClassAbility ability) {
		this.name = name;
		this.mount = mount;
		this.unitType = unitType;
		//Growth modifiers should always be 0 for tiers above 1
		this.growthModifiers = growthModifiers;
		this.proficiencyModifiers = proficiencyModifiers;
		this.previous = previous;
		if (previous != null) {
			if (previous.getTier() == 4) {
				throw new IllegalArgumentException("Cannot create a promotion for a 4th tier class");
			}
			previous.promotion = this;
			this.tier = previous.tier + 1;
			this.statModifiers = statModifiers;
		} else {
			this.tier = 1;
		}
		this.ability = ability;
		
		//Calculate heuristics
		//Intended to favor classes with growths corresponding to their proficiencies
		int strNeed = 0;
		int magNeed = 0;
		//For each physical weapon type
		for (int q = 0; q < 6; q++) {
			strNeed += proficiencyModifiers[q];
		}
		//For each magic weapon type
		for (int q = 6; q < proficiencyModifiers.length - 1; q++) {
			magNeed += proficiencyModifiers[q];
		}
		magNeed += proficiencyModifiers[9] / 2; //Half for staff
		if (strNeed > magNeed) {
			strengthHeuristicMultiplier = 2;
			magicHeuristicMultiplier = 0;
		} else if (magNeed > strNeed) {
			strengthHeuristicMultiplier = 0;
			magicHeuristicMultiplier = 2;
		} else { //They're equal
			strengthHeuristicMultiplier = 1;
			magicHeuristicMultiplier = 1;
		}
		
		//Heuristic used for balancing class selection
		generalInternalHeuristic = 0;
		//Make sure proficiencies are concentrated
		for (int q = 0; q < proficiencyModifiers.length; q++) {
			generalInternalHeuristic -= Math.min(10, proficiencyModifiers[q]);
		}
		//Make sure growths are well balanced
		for (int q = 0; q < growthModifiers.length; q++) {
			generalInternalHeuristic += Math.min(10, growthModifiers[q]);
		}
		if (ability != null) {
			generalInternalHeuristic -= 10;
		}
	}

	public String getName() {
		return name;
	}
	
	public int getMountMovement() {
		if (mount == null) {
			throw new IllegalArgumentException("Tried to access mount movement when there was no mount");
		}
		return mount.getMovement();
	}
	
	public boolean mountCanFly() {
		if (mount == null) {
			return false;
		}
		return mount.canFly();
	}
	
	public Mount getMount() {
		return mount;
	}
	
	public int getMountType() {
		if (mount == null) {
			return -1;
		}
		return mount.getId();
	}
	
	public int initializeMountGrowth() {
		if (mount == null) {
			return 0;
		}
		return mount.getMinGrowth() + RNGStuff.nextInt(mount.getGrowthVariance());
	}
	
	public int initializeMountHealth() {
		if (mount == null) {
			return 0;
		}
		return mount.getMinInitialHealth() + RNGStuff.nextInt(mount.getHealthVariance());
	}

	public int getTier() {
		return tier;
	}
	
	public int getClassType() {
		return unitType;
	}
	
	public ClassAbility getClassAbility() {
		return ability;
	}
	
	public UnitClass getPromotion() {
		return promotion;
	}
	
	public int[] getGrowthModifiers() {
		return growthModifiers;
	}
	
	public int[] getClassTreeGrowthModifiers() {
		if (previous != null) {
			return previous.getClassTreeGrowthModifiers();
		}
		return getGrowthModifiers();
	}

	public int[] getProficiencyModifiers() {
		return proficiencyModifiers;
	}
	
	public int[] getStatModifiers() {
		return statModifiers;
	}

	public int magicHeuristicMultiplier() {
		return magicHeuristicMultiplier;
	}
	
	public int strengthHeuristicMultiplier() {
		return strengthHeuristicMultiplier;
	}
	
	public int generalInternalHeuristic() {
		return generalInternalHeuristic;
	}
	
	public double getMountEvasionBonus() {
		if (mount == null) {
			return 0;
		}
		return mount.getEvasion();
	}
	
	public boolean canTrainUnitWithMaterials(Human h, int[] mounts) {
		//If there are no mounts, there are no constraints
		if (mount == null) {
			return true;
		}
		//If the mount isn't there, return false
		if (mounts[mount.getId()] == 0) {
			return false;
		}
		//Only female units can ride unicorns
		if (mount == Mount.UNICORN && !(h.getGender())) {
			return false;
		}
		return true;
	}

	public enum Mount {
		//TODO add descriptions
		HORSE(0, 9, 5,
				new WorldMapTileType[] {WorldMapTileType.PLAIN},
				0.8, 30, 31, 20, 61, false, "Horse",
				"Common beasts of burden. They are relatively simple to raise and tame."),
		UNICORN(1, 9, 10,
				new WorldMapTileType[] {WorldMapTileType.DENSE_FOREST},
					0.2, 20, 41, 0, 51, false, "Unicorn",
				"Horses with horns that grant a strong magical affinity. They can only be tamed by women."),
		GRIFFIN(2, 8, 10,
				new WorldMapTileType[] {WorldMapTileType.FOREST, WorldMapTileType.MOUNTAIN},
				0.7, 30, 31, 30, 51, true, "Griffin",
				"Products of the hybridization of lions and eagles. They are agreeable, yet ferocious."),
		WYVERN(3, 10, 10,
				new WorldMapTileType[] {WorldMapTileType.DESERT, WorldMapTileType.MOUNTAIN, WorldMapTileType.SNOWY_MOUNTAIN},
				0.5, 45, 51, 0, 31, true, "Wyvern",
				"Often considered a lesser species of dragon. They are easy to raise and difficult to tame."),
		PEGASUS(4, 8, 15,
				new WorldMapTileType[] {WorldMapTileType.MOUNTAIN, WorldMapTileType.SNOWY_MOUNTAIN},
				0.4, 20, 41, 40, 61, true, "Pegasus",
				"Horses with large, bird-like wings. Their wings keep them balanced as they magically gallop on thin air."),
		ALICORN(5, 10, 20,
				new WorldMapTileType[] {WorldMapTileType.SNOWY_MOUNTAIN},
				0, 30, 41, 30, 61, true, "Alicorn",
				"Hybrids of unicorns and pegasi. They combine the inherent strengths of both species.");
		
		private int id;
		private int movement;
		private int evasion;
		private double rearingSimplicity;
		private WorldMapTileType[] favoredClimates;
		private int minInitHealth;
		private int healthVariance;
		private int minGrowth;
		private int growthVariance;
		private boolean canFly;
		private String displayName;
		private String description;
		private Mount(int id, int movement, int evasion, WorldMapTileType[] favoredClimates,
				double rearingSimplicity, int minInitHealth, int healthVariance,
				int minGrowth, int growthVariance, boolean canFly, String displayName,
				String description) {
			this.id = id;
			this.movement = movement;
			this.evasion = evasion;
			this.favoredClimates = favoredClimates;
			this.rearingSimplicity = rearingSimplicity;
			this.minInitHealth = minInitHealth;
			this.healthVariance = healthVariance;
			this.minGrowth = minGrowth;
			this.growthVariance = growthVariance;
			this.canFly = canFly;
			this.displayName = displayName;
			this.description = description;
		}
		
		public int getId() {
			return id;
		}
		
		public int getMovement() {
			return movement;
		}
		
		public int getEvasion() {
			return evasion;
		}
		
		public float getRearingFactor(WorldMapTileType type) {
			for (int q = 0; q < favoredClimates.length; q++) {
				if (favoredClimates[q] == type) {
					return 1;
				}
			}
			return (float)(rearingSimplicity * type.getProliferability());
		}
		
		public WorldMapTileType[] favoredClimate() {
			return favoredClimates;
		}
		
		public int getMinInitialHealth() {
			return minInitHealth;
		}
		
		public int getHealthVariance() {
			return healthVariance;
		}
		
		public int getMinGrowth() {
			return minGrowth;
		}
		
		public int getGrowthVariance() {
			return growthVariance;
		}
		public boolean canFly() {
			return canFly;
		}

		public String getDisplayName() {
			return displayName;
		}
		
		public String getDescription() {
			return description;
		}

		public int getWorth() {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	public enum ClassAbility {
		CONVOY,
		SAILING,
		HERDING
	}
	

}
