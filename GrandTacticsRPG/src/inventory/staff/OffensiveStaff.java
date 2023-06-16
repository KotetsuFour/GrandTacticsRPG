package inventory.staff;

import inventory.InventoryIndex;
import unit.Equippable;
import unit.Unit;

public class OffensiveStaff extends Staff {

	private int statusEffect;
	private int power;
	
	public OffensiveStaff(String name, int initialUses, int approximateWorth, int weight,
			int range, int effect, int power, int[][] recipe) {
		super(name, initialUses, approximateWorth, weight, range, recipe);
		// TODO Auto-generated constructor stub
		if (effect == Staff.BERSERK || effect == Staff.POISON || effect == Staff.SLEEP
				|| effect == Staff.INJURY) {
			this.statusEffect = effect;
		} else {
			throw new IllegalArgumentException("Invalid status effect type");
		}
		this.power = power;
	}

	public boolean effect(Unit user, Unit target, int bodyPart) {
		//TODO
		//If status effect is injury (that is, if this is a disintegration staff)
		if (false/*TODO preconditions fail*/) {
			return false;
		}
		if (statusEffect == Staff.INJURY) {
			if (target instanceof Equippable && target.defense(false, bodyPart) > 0) {
				Equippable e = (Equippable) target;
				e.destroyArmor();
			} else {
				//Crit the unit such that their current HP is reduced to 1
				//Disintegration staff OP
				target.takeCriticalDamage(false, bodyPart, target.getCurrentHPOfBodyPart(bodyPart) - 1);
			}
		} else {
			target.getStatusEffects()[statusEffect] += power;
		}
		return true;
	}

	@Override
	public int getGeneralItemId() {
		return InventoryIndex.OFFENSIVE_STAFF;
	}
}
