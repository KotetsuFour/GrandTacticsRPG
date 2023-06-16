package inventory.weapon;

import inventory.InventoryIndex;
import location.BFTileOccupant;

public class StationaryWeapon extends Weapon implements BFTileOccupant {

	protected int battlegroundMapLocationX;
	protected int battlegroundMapLocationY;
	protected int reloadStage;
	protected int hp;
	protected int defense;
	protected int resistance;
	protected int battlegroundMinRange;
	//TODO initialize hp
	
	public StationaryWeapon(String name, int proficiencyRequirement, int minRange, int maxRange,
			int might, int hit, int crit,
			boolean isMagic, int proficiencyIndex, int[][] recipe, int initialUses, int approximateWorth,
			int weight, int defense, int resistance, int battlegroundMinRange) {
		super(name, proficiencyRequirement, minRange, maxRange, might, hit, crit, isMagic,
				proficiencyIndex, recipe, initialUses, approximateWorth, weight);
		this.defense = defense;
		this.resistance = resistance;
		this.battlegroundMinRange = battlegroundMinRange;
	}
	
	public StationaryWeapon clone() {
		return new StationaryWeapon(name, proficiencyRequirement, minRange, maxRange,
				might, hit, crit, isMagic, proficiencyIndex, recipe, initialUses, approximateWorth,
				weight, defense, resistance, battlegroundMinRange);
	}
	
	public void use() {
		reloadStage = 2;
		initialUses--;
	}
	public void reload() {
		if (initialUses == 0) {
			throw new IllegalArgumentException("This weapon has no more ammunition to load.");
		}
		reloadStage--;
	}
	public boolean ready() {
		return reloadStage == 0 && initialUses > 0;
	}

	public int getDefense() {
		return defense;
	}

	public int getResistance() {
		return resistance;
	}
	
	public int getBattlegroundMinRange() {
		return battlegroundMinRange;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.STATIONARY_WEAPON;
	}
}
