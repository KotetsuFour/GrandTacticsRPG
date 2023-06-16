package inventory.weapon;

import inventory.InventoryIndex;

public class HandheldWeapon extends Weapon {

	public HandheldWeapon(String name, int proficiencyRequirement, int maxRange, int might, int hit, int crit,
			boolean isMagic, int proficiencyIndex, int[][] recipe, int initialUses, int approximateWorth,
			int weight) {
		super(name, proficiencyRequirement, 1, maxRange, might, hit, crit, isMagic,
				proficiencyIndex, recipe, initialUses, approximateWorth, weight);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.HANDHELD_WEAPON;
	}

}
