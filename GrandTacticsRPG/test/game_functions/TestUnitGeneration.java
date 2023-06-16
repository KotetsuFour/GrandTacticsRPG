package game_functions;

import static org.junit.Assert.*;

import org.junit.Test;

import affiliation.CityState;
import affiliation.Nation;
import buildings.defendable.TrainingFacility;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.weapon.Weapon;
import location.WorldMapTile;
import location.WorldMapTile.WorldMapTileType;
import manager.DialogueManager;
import manager.GeneralGameplayManager;
import reference.ArtificialHumanIndex;
import reference.ArtificialHumanTemplate;
import reference.UnitClassIndex;
import unit.Unit;
import unit.UnitClass;
import unit.UnitGroup;
import unit.human.Clone;
import unit.human.Human;

public class TestUnitGeneration {

	@Test
	public void testGenericRecruitedHuman() {
		
		//Create two city-states with different characteristics
		int[] vals1 = new int[11]; //All 0s
		CityState cs1 = new CityState(new Nation(), vals1);
		int[] vals2 = {50, 50, 50, 50, 50, 50, 0, 0, 0, 0, 0};
		CityState cs2 = new CityState(new Nation(), vals2);
		List<Human> army = new List<>(300, 300);
		
		//Fill out army with half members from each city-state
		for (int q = 0; q < 150; q++) {
			army.add(cs1.recruitUnit());
		}
		for (int q = 0; q < 150; q++) {
			army.add(cs2.recruitUnit());
		}
		
		//Make sure that politically related parts of personality do not stray
		//super significantly from the unit's home's general opinions
		for (int q = 0; q < army.size(); q++) {
			Human s = army.get(q);
			CityState h = s.getHome();
			int lim = 50;
			if (s.getNationalism() > lim + h.getNationalism() || s.getNationalism() < h.getNationalism() - lim
					|| s.getAltruism() > lim + h.getAltruism() || s.getAltruism() < h.getAltruism() - lim
					|| s.getMilitarism() > lim + h.getMilitarism() || s.getMilitarism() < h.getMilitarism() - lim
					|| s.getFamilism() > lim + h.getFamilism() || s.getFamilism() < h.getFamilism() - lim) {
				fail(String.format("Unit\nN: %d, A: %d, M: %d, F: %d\nCS\nN: %d, A: %d, M: %d, F: %d\n",
						s.getNationalism(), s.getAltruism(), s.getMilitarism(), s.getFamilism(),
						h.getNationalism(), h.getAltruism(), h.getMilitarism(), h.getFamilism()));
			}
		}
		
		//Print all units just to see how unique they are
//		for (int q = 0; q < army.size(); q++) {
//			System.out.println(army.get(q).showStats());
//			System.out.println();
//		}
	}
	
	@Test
	public void testUnitCapacity() {
//		long originalMemory = Runtime.getRuntime().freeMemory();
//		int testSize = Nation.MAX_ARMY_SIZE * 10;
//		int numNations = 10;
//		int perNation = testSize / numNations;
//		List<Unit> list = new List<>(testSize, testSize);
//		for (int q = 0; q < numNations; q++) {
//			Nation n = new Nation();
//			CityState cs = new CityState(n);
//			for (int w = 0; w < perNation; w++) {
//				list.add(cs.recruitUnit());
//			}
//		}
//		assertEquals(testSize, list.size());
//		long finalMemory = Runtime.getRuntime().freeMemory();
//		long memoryTaken = originalMemory - finalMemory;
//		double percentage = (0.0 + memoryTaken) / originalMemory * 100;
//		System.out.println(String.format("%d - %d = %d (%f%%)", originalMemory, finalMemory,
//				memoryTaken, percentage));
	}
	
	@Test
	public void testUnitTraining() {
		
//		//Create all of the standard classes for testing
//		GeneralGameplayManager.indexesInitialization();
//		
//		int[] vals2 = {50, 50, 50, 50, 50, 50, 0, 0, 0, 0, 0};
//		CityState cs2 = new CityState(new Nation(), vals2);
//		List<Human> trainees = new List<>(UnitGroup.CAPACITY, UnitGroup.CAPACITY);
//		for (int q = 0; q < trainees.limit(); q++) {
//			trainees.add(cs2.recruitUnit());
//		}
//
//		System.out.println("Before assigning classes and partners\n");
//		for (int q = 0; q < trainees.size(); q++) {
//			System.out.println(trainees.get(q).showStats());
//			System.out.println();
//		}
//		
//		TrainingFacility tf = new TrainingFacility("Test Academy", 1, 1, 0, 0);
//		for (int q = 0; q < trainees.size(); q++) {
//			tf.addUnit(trainees.get(q));
//		}
//		tf.addMount(UnitClass.Mount.HORSE, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.PEGASUS, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.ALICORN, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.GRIFFIN, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.UNICORN, UnitGroup.CAPACITY);
//		tf.addMount(UnitClass.Mount.WYVERN, UnitGroup.CAPACITY);
//		int[] swords = {InventoryIndex.HANDHELD_WEAPON, 0, 20};
//		int[] lances = {InventoryIndex.HANDHELD_WEAPON, 1, 20};
//		int[] axes = {InventoryIndex.HANDHELD_WEAPON, 2, 20};
//		int[] bows = {InventoryIndex.HANDHELD_WEAPON, 3, 20};
//		int[] knives = {InventoryIndex.HANDHELD_WEAPON, 4, 20};
////		int[] ballistae = {InventoryIndex.WEAPON, 0, 20};
//		int[] anima = {InventoryIndex.HANDHELD_WEAPON, 5, 20};
//		int[] light = {InventoryIndex.HANDHELD_WEAPON, 6, 20};
//		int[] dark = {InventoryIndex.HANDHELD_WEAPON, 7, 20};
//		tf.receiveGoods(swords);
//		tf.receiveGoods(lances);
//		tf.receiveGoods(axes);
//		tf.receiveGoods(bows);
//		tf.receiveGoods(knives);
//		tf.receiveGoods(anima);
//		tf.receiveGoods(light);
//		tf.receiveGoods(dark);
//
//		tf.autoAssignClasses();
//		tf.autoAssignSupportPartners();
//		tf.autoAssignWeapons();
//		
//		System.out.println("After assigning classes and partners and before training\n");
//		for (int q = 0; q < trainees.size(); q++) {
//			System.out.println(trainees.get(q).showStats());
//			System.out.println();
//		}
//		
//		tf.completeMonthlyAction();
//		
//		System.out.println("After training\n");
//		for (int q = 0; q < trainees.size(); q++) {
//			System.out.println(trainees.get(q).showStats());
////			System.out.println(DialogueManager.tellSupportRelationship(trainees.get(q), trainees.get(q).getSupportPartner()));
//			System.out.println();
//		}
		
	}
	
	@Test
	public void testCloning() {
		
		GeneralGameplayManager.indexesInitialization();
		
		Nation n = new Nation();
		int[] vals = {50, 50, 50, 50, 50, 50, 0, 0, 0, 0, 0};
		CityState cs = new CityState(n, vals);
		Human template = cs.recruitUnit();
		Human cloner = cs.recruitUnit();
		TrainingFacility tf = new TrainingFacility("Test Academy", null, new WorldMapTile(WorldMapTileType.PLAIN, 0, 0));
		
		//Train and graduate the cloner, then work up their dark magic
		tf.addUnit(cloner);
		int[] dark = {InventoryIndex.HANDHELD_WEAPON, 7, 20};
		tf.receiveGoods(dark);
		tf.assignClass(0, UnitClassIndex.getClassByName(UnitClassIndex.HUMAN, "Dark Mage"));
		tf.autoAssignAllWeapons();
		tf.completeMonthlyAction();
		tf.graduateUnitsForTesting();
		while (cloner.proficiencyWith(Weapon.DARK) < 300) {
			cloner.useWeapon(true);
		}
		
		//Show the stats of the cloner and pre-training template
		System.out.println("Cloner:\n" + cloner.showStats());
		System.out.println("Base template:\n" + template.showStats());

		tf.addUnit(template);
		tf.addMount(UnitClass.Mount.HORSE, UnitGroup.CAPACITY);
		tf.addMount(UnitClass.Mount.PEGASUS, UnitGroup.CAPACITY);
		tf.addMount(UnitClass.Mount.ALICORN, UnitGroup.CAPACITY);
		tf.addMount(UnitClass.Mount.GRIFFIN, UnitGroup.CAPACITY);
		tf.addMount(UnitClass.Mount.UNICORN, UnitGroup.CAPACITY);
		tf.addMount(UnitClass.Mount.WYVERN, UnitGroup.CAPACITY);
		int[] swords = {InventoryIndex.HANDHELD_WEAPON, 0, 20};
		int[] lances = {InventoryIndex.HANDHELD_WEAPON, 1, 20};
		int[] axes = {InventoryIndex.HANDHELD_WEAPON, 2, 20};
		int[] bows = {InventoryIndex.HANDHELD_WEAPON, 3, 20};
		int[] knives = {InventoryIndex.HANDHELD_WEAPON, 4, 20};
//		int[] ballistae = {InventoryIndex.WEAPON, 0, 20};
		int[] anima = {InventoryIndex.HANDHELD_WEAPON, 5, 20};
		int[] light = {InventoryIndex.HANDHELD_WEAPON, 6, 20};
		tf.receiveGoods(swords);
		tf.receiveGoods(lances);
		tf.receiveGoods(axes);
		tf.receiveGoods(bows);
		tf.receiveGoods(knives);
		tf.receiveGoods(anima);
		tf.receiveGoods(light);
		
		tf.autoAssignClasses();
		tf.autoAssignAllWeapons();
		tf.completeMonthlyAction();
		tf.graduateUnitsForTesting();
		System.out.println("Template after training:\n" + template.showStats());
		
		//Add the template to be clonable
		ArtificialHumanTemplate temp = ArtificialHumanIndex.addCloneTemplate(template);
		
		//Make a group of clones
		List<Human> clones = new List<>(UnitGroup.CAPACITY, UnitGroup.CAPACITY);
		System.out.println("Clones before assigning classes:\n");
		for (int q = 0; q < clones.limit(); q++) {
			Clone current = Clone.cloneOfXProducedByY(temp, cloner, cs);
			System.out.println(current.showStats());
			clones.add(current);
			tf.addUnit(current);
		}
		
		//Assign classes to the clones
		tf.autoAssignClasses();
		tf.autoAssignAllWeapons();
		System.out.println("Clones after assigning classes:\n");
		for (int q = 0; q < clones.limit(); q++) {
			System.out.println(clones.get(q).showStats());
		}
		//Train the clones
		tf.completeMonthlyAction();
		System.out.println("Clones after training:\n");
		for (int q = 0; q < clones.limit(); q++) {
			System.out.println(clones.get(q).showStats());
		}
	}
	
}
