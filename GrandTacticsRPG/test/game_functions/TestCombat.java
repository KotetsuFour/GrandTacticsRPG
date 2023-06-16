package game_functions;

import static org.junit.Assert.*;

import org.junit.Test;

import affiliation.CityState;
import affiliation.Nation;
import battle.BattleGround;
import buildings.defendable.TrainingFacility;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.item.Item;
import location.BattlegroundTile;
import location.WorldMapTile;
import location.WorldMapTile.WorldMapTileType;
import manager.BattleManager;
import manager.GeneralGameplayManager;
import report.StandardBattleReport;
import unit.Unit;
import unit.UnitClass;
import unit.UnitGroup;
import unit.human.Human;
import util.RNGStuff;

public class TestCombat {

	@Test
	public void testBasicCombat() {
		//Create all of the standard classes for testing
		GeneralGameplayManager.indexesInitialization();
		List<Integer> colors = new List<>();
		colors.add(0);
		RNGStuff.useColors(colors, colors, colors);
		
		int[] vals1 = {50, 50, 50, 50, 50, 50, 0, 0, 0, 0, 0};
		CityState cs1 = new CityState(new Nation(), vals1);
		List<Human> trainees = new List<>(UnitGroup.CAPACITY, UnitGroup.CAPACITY);
		for (int q = 0; q < trainees.limit(); q++) {
			trainees.add(cs1.recruitUnit());
		}

		TrainingFacility tf = new TrainingFacility("Test Academy", null, new WorldMapTile(WorldMapTileType.PLAIN, 0, 0));
		for (int q = 0; q < trainees.size(); q++) {
			tf.addUnit(trainees.get(q));
		}
//		tf.addMount(UnitClass.Mount.HORSE, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.PEGASUS, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.ALICORN, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.GRIFFIN, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.UNICORN, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.WYVERN, UnitGroup.CAPACITY);
		int[] swords = {InventoryIndex.HANDHELD_WEAPON, 0, 20};
		int[] lances = {InventoryIndex.HANDHELD_WEAPON, 1, 20};
		int[] axes = {InventoryIndex.HANDHELD_WEAPON, 2, 20};
		int[] bows = {InventoryIndex.HANDHELD_WEAPON, 3, 20};
		int[] knives = {InventoryIndex.HANDHELD_WEAPON, 4, 20};
//		int[] ballistae = {InventoryIndex.WEAPON, 5, 20};
		int[] anima = {InventoryIndex.HANDHELD_WEAPON, 5, 20};
		int[] light = {InventoryIndex.HANDHELD_WEAPON, 6, 20};
		int[] dark = {InventoryIndex.HANDHELD_WEAPON, 7, 20};
		int[] armors = {InventoryIndex.ARMOR, 0, 20};
		tf.receiveGoods(swords);
		tf.receiveGoods(lances);
		tf.receiveGoods(axes);
		tf.receiveGoods(bows);
		tf.receiveGoods(knives);
		tf.receiveGoods(anima);
		tf.receiveGoods(light);
		tf.receiveGoods(dark);
		tf.receiveGoods(armors);

		tf.autoAssignClasses();
		tf.autoAssignSupportPartners();
		tf.autoAssignAllWeapons();
		tf.autoAssignAllArmors();
		tf.completeMonthlyAction();
		
		//Graduate the group, then make another one
		UnitGroup g1 = tf.graduateUnitsForTesting();
		
		int[] vals2 = {50, 50, 50, 50, 50, 50, 0, 0, 0, 0, 0};
		CityState cs2 = new CityState(new Nation(), vals2);
		List<Human> trainees2 = new List<>(UnitGroup.CAPACITY, UnitGroup.CAPACITY);
		for (int q = 0; q < trainees2.limit(); q++) {
			trainees2.add(cs2.recruitUnit());
		}

		TrainingFacility tf2 = new TrainingFacility("Test Academy 2", null, new WorldMapTile(WorldMapTileType.PLAIN, 0, 0));
		for (int q = 0; q < trainees2.size(); q++) {
			tf2.addUnit(trainees2.get(q));
		}
		tf2.addMount(UnitClass.Mount.HORSE, UnitGroup.CAPACITY);
		tf2.addMount(UnitClass.Mount.PEGASUS, UnitGroup.CAPACITY);
		tf2.addMount(UnitClass.Mount.ALICORN, UnitGroup.CAPACITY);
		tf2.addMount(UnitClass.Mount.GRIFFIN, UnitGroup.CAPACITY);
		tf2.addMount(UnitClass.Mount.UNICORN, UnitGroup.CAPACITY);
		tf2.addMount(UnitClass.Mount.WYVERN, UnitGroup.CAPACITY);
		tf2.receiveGoods(swords);
		tf2.receiveGoods(lances);
		tf2.receiveGoods(axes);
		tf2.receiveGoods(bows);
		tf2.receiveGoods(knives);
		tf2.receiveGoods(anima);
		tf2.receiveGoods(light);
		tf2.receiveGoods(dark);
		tf2.receiveGoods(armors);
		
		tf2.autoAssignClasses();
		tf2.autoAssignSupportPartners();
		tf2.autoAssignAllWeapons();
		tf2.autoAssignAllArmors();
		tf2.completeMonthlyAction();
		
		UnitGroup g2 = tf2.graduateUnitsForTesting();
		
		
		System.out.println(g1.getGroupAsString());
		System.out.println(g2.getGroupAsString());
		
		Human u1 = (Human)g1.getMembers().get(g1.getMembers().size() - 1);
		Human u2 = (Human)g2.getMembers().get(g2.getMembers().size() - 1);
		
		System.out.println(u1.getDisplayName() + " vs. " + u2.getDisplayName() + "\n");
		System.out.println(u1.showStats());
		System.out.println(u2.showStats());
		//TODO eventually, I will have to give more attention to the initialization of the
		//tiles and battleground, but that can come later
		BattlegroundTile testTile = new BattlegroundTile(BattlegroundTile.BattlegroundTileType.GRASS);
		BattleGround battle = null;
		
		int[] cast = BattleManager.normalBattleForecast(u1, u2, 1, testTile, testTile, true);
		StringBuilder forecast = new StringBuilder();
		forecast.append(String.format("%s: HP: %d/%d, MT: %d, Hit: %d, Crt: %d Double: %b\n",
				u1.getName(), cast[0], u1.getMaximumHPOfBodyPart(1), cast[1], cast[2], cast[3],
				cast[4] == 2));
		forecast.append(String.format("%s: HP: %d/%d, MT: %d, Hit: %d, Crt: %d Double: %b\n",
				u2.getName(), cast[5], u2.getMaximumHPOfBodyPart(1), cast[6], cast[7], cast[8],
				cast[9] == 2));

		System.out.println(forecast.toString());
		
		StandardBattleReport report = BattleManager.performBattleSequence(u1, u2, 1, testTile, testTile, battle, true);
		int[] seq = report.getDetails();
		StringBuilder sb = new StringBuilder(u1.getName());
		if (seq[0] == 0) {
			sb.append(" missed ");
		} else if (seq[0] == 1) {
			sb.append(" hit ");
		} else if (seq[0] == 2) {
			sb.append(" crit ");
		}
		sb.append(u2.getName() + ", reducing their torso HP from ");
		sb.append(seq[1] + " to " + seq[2] + ".\n");
		sb.append(u2.getName());
		if (seq[3] == -1) {
			sb.append(" could not counter attack.\n");
		} else {
			if (seq[3] == 0) {
				sb.append(" missed ");
			} else if (seq[3] == 1) {
				sb.append(" hit ");
			} else if (seq[3] == 2) {
				sb.append(" crit ");
			}
			sb.append(u1.getName() + ", reducing their torso HP from ");
			sb.append(seq[4] + " to " + seq[5] + ".\n");
			if (seq[6] == 0) {
				sb.append("There was no double attack");
			} else {
				Unit d = null;
				Unit f = null;
				if (seq[6] == 1) {
					sb.append(u1.getName() + " double attacks.\n");
					d = u1;
					f = u2;
				} else if (seq[6] == 2) {
					sb.append(u2.getName() + " double attacks.\n");
					d = u2;
					f = u1;
				}
				sb.append(d.getName());
				if (seq[7] == 0) {
					sb.append(" missed ");
				} else if (seq[7] == 1) {
					sb.append(" hit ");
				} else if (seq[7] == 2) {
					sb.append(" crit ");
				}
				sb.append(f.getName() + ", reducing their HP from ");
				sb.append(seq[8] + " to " + seq[9] + ".\n");
			}
		}
		System.out.println(sb.toString());
	}

}
