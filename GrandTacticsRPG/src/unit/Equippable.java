package unit;

import inventory.item.Armor;
import inventory.item.Item;
import inventory.weapon.HandheldWeapon;
import inventory.weapon.StationaryWeapon;

public interface Equippable {
	
	public int[][] getInventory();

	public void useWeapon(boolean hit);
	
	public boolean canUseBallista();
	
	public boolean canUseMagicTurrets();

	public boolean canUse(StationaryWeapon weapon);
	
	public Armor getArmor();
	
	public void destroyArmor();
	
	public String getArmorName();
	
	public Item getEquippedItem();
	
	public String getWeaponName();
	
	public HandheldWeapon getEquippedWeapon();
	
	public void autoEquip();
	
	public int getEquipmentHeuristic(int[] item);

	public void equip(int idx);
	
	public boolean receiveNewArmor(int[] armor);
	
	public boolean receiveNewItem(int[] item);
	
	public int armStrength();

	public int proficiencyWith(int type);

}
