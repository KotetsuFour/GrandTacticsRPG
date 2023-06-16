package inventory.weapon;

import inventory.item.ManufacturableItem;

public abstract class Weapon extends ManufacturableItem {

	public static final int SWORD = 0;
	public static final int LANCE = 1;
	public static final int AXE = 2;
	public static final int BOW = 3;
	public static final int KNIFE = 4;
	public static final int BALLISTA = 5;
	public static final int ANIMA = 6;
	public static final int LIGHT = 7;
	public static final int DARK = 8;
	//Even though staves aren't weapons
	public static final int STAFF = 9;
	
	protected int proficiencyRequirement;
	protected int proficiencyIndex;
	protected int minRange;
	protected int maxRange;
	protected int might;
	protected int hit;
	protected int crit;
	protected boolean isMagic;
	
	public Weapon(String name, int proficiencyRequirement, int minRange, int maxRange,
			int might, int hit, int crit,
			boolean isMagic, int proficiencyIndex, int[][] recipe, int initialUses,
			int approximateWorth, int weight) {
		super(name, initialUses, approximateWorth, weight, recipe);
		this.proficiencyRequirement = proficiencyRequirement;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.might = might;
		this.hit = hit;
		this.crit = crit;
		this.isMagic = isMagic;
		this.proficiencyIndex = proficiencyIndex;
		this.recipe = recipe;
	}

	public boolean isMagic() {
		return isMagic;
	}
	
	public int getMight() {
		return might;
	}

	public int getHit() {
		return hit;
	}
	
	public int getProficiencyRequirement() {
		return proficiencyRequirement;
	}
	
	public int getProficiencyIndex() {
		return proficiencyIndex;
	}

	public int getCrit() {
		return crit;
	}
	
	public String getProficiencyTypeAsString() {
		if (proficiencyIndex == SWORD) {
			return "Sword";
		}
		if (proficiencyIndex == LANCE) {
			return "Spear";
		}
		if (proficiencyIndex == AXE) {
			return "Axe";
		}
		if (proficiencyIndex == BOW) {
			return "Bow";
		}
		if (proficiencyIndex == KNIFE) {
			return "Knife";
		}
		if (proficiencyIndex == BALLISTA) {
			return "Ballista";
		}
		if (proficiencyIndex == ANIMA) {
			return "Earth";
		}
		if (proficiencyIndex == LIGHT) {
			return "Light";
		}
		if (proficiencyIndex == DARK) {
			return "Dark";
		}
		if (proficiencyIndex == STAFF) {
			return "Staff";
		}
		return null;
	}
	
	public boolean usesDurabilityWithoutHitting() {
		return maxRange > 1;
	}
	public int maxRange() {
		return maxRange;
	}
	public int minRange() {
		return minRange;
	}
	
	@Override
	public String[] getInformationDisplayArray(int[] itemArray) {
		return new String[] {
				String.format("Durability: %d/%d", itemArray[2], initialUses),
				String.format("Prof.: %s %d", getProficiencyTypeAsString(), proficiencyRequirement),
				String.format("Range: %d-%d", minRange, maxRange),
				String.format("Weight: %d", weight),
				String.format("Power: %d", might),
				String.format("Accuracy: %d", hit),
				String.format("Critical Rate: %d", crit),
				String.format("Magic: %b", isMagic)
		};
	}
}