package manager;

import java.util.HashMap;

import affiliation.CityState;
import affiliation.Nation;
import battle.BattleGround;
import buildings.Building;
import buildings.Village;
import buildings.defendable.Castle;
import buildings.defendable.Defendable;
import buildings.defendable.TrainingFacility;
import buildings.goods_deliverer.Farm;
import buildings.goods_deliverer.MiningFacility;
import buildings.goods_deliverer.Storehouse;
import data_structures.LinkedQueue;
import data_structures.List;
import inventory.InventoryIndex;
import inventory.weapon.HandheldWeapon;
import inventory.weapon.StationaryWeapon;
import location.BattlegroundTile;
import location.WorldMap;
import location.WorldMapTile;
import reference.ArtificialHumanIndex;
import reference.BattlegroundTileIndex;
import reference.ShipIndex;
import reference.UnitClassIndex;
import ship.Ship;
import unit.Equippable;
import unit.Unit;
import unit.UnitGroup;
import unit.human.Demeanor;
import unit.human.Human;
import unit.human.Human.CombatTrait;
import unit.human.Human.Interest;
import util.RNGStuff;

public class GeneralGameplayManager {

	private static WorldMap worldMap;
	private static Nation playerNation;
	private static Human player;
	private static long daysSinceGameStart;
	private static boolean aging;
	private static int actionsLeft;
	private static boolean eventTime;
	
	private static List<Nation> npcNations;
	
	public static final String[] MONTH_NAMES = {"Space Moon", "Light Moon", "Earthen Moon",
			"Time's Moon", "Reality Moon", "Dark Moon",
			"Fortune Moon", "War Moon", "Peace Moon",
			"Bonding Moon", "Life Moon", "Death Moon"};
	public static final int DAYS_IN_MONTH = 30;
	public static final int DAYS_IN_YEAR = DAYS_IN_MONTH * MONTH_NAMES.length;
	public static final int ACTIONS_PER_DAY = 5;
	public static final int INITIAL_NPC_NATION_COUNT = 4;
	
	/**
	 * Tells if the game is being played in event time
	 * @return true if the player's nation is in the middle of a major event, in which case,
	 * processes should move day-by-day instead of month-by-month
	 */
	public static boolean eventTime() {
		return eventTime;
	}
	
	public static void switchToEventTime() {
		if (!eventTime) {
			eventTime = true;
			if (actionsLeft < ACTIONS_PER_DAY * DAYS_IN_MONTH) {
				//TODO determine day based on actions used
			} else {
				actionsLeft = 0;
			}
		}
	}
	
	public static long getDaysSinceGameStart() {
		return daysSinceGameStart;
	}

	public static String getTimeAsString() {
		long time = daysSinceGameStart;
		long year = time / DAYS_IN_YEAR;
		time %= DAYS_IN_YEAR;
		int month = (int)time / DAYS_IN_MONTH;
		time %= DAYS_IN_MONTH;
		int day = (int)time + 1;
		return String.format("Day %d of %s, Year %d", day, MONTH_NAMES[month], year);
	}
	
	public static int getActionsLeft() {
		return actionsLeft;
	}
	public static void resetMonthActions() {
		actionsLeft = DAYS_IN_MONTH * ACTIONS_PER_DAY;
	}
	public static void resetDayActions() {
		actionsLeft += ACTIONS_PER_DAY;
	}
	
	public static void endPlayerTurn() {
		//TODO un-exhaust all units
		if (eventTime()) {
			resetDayActions();
			daysSinceGameStart++;
			getPlayerNation().passDay(aging && daysSinceGameStart % DAYS_IN_YEAR == 0);
		} else {
			resetMonthActions();
			daysSinceGameStart += DAYS_IN_MONTH;
			getPlayerNation().passMonth(aging && daysSinceGameStart % DAYS_IN_YEAR == 0);
			//TODO everyone else does their turn, then it's the player's turn again
		}
	}

	public static Nation getPlayerNation() {
		return playerNation;
	}
	
	public static void indexesInitialization() {
		InventoryIndex.initialize();
		UnitClassIndex.initialize();
		ShipIndex.initialize();
		BattlegroundTileIndex.initialize();
		ArtificialHumanIndex.initialize();
	}

	public static Human getPlayer() {
		return player;
	}
	
	public static void addBuildingToCityAndMap(Building b, CityState cs, WorldMapTile tile) {
		tile.setBuilding(b);
		cs.addBuilding(b);
	}
	
	public static void claimTileForNation(CityState cs, WorldMapTile tile) {
		tile.setOwner(cs);
		cs.incrementSize();
	}

	public static void initializePlayerNation(String name, int type, String capitalName,
			int language) {
		//TODO procedural generation
		worldMap = new WorldMap();
		eventTime = false;
		playerNation = new Nation(name, capitalName, type, language);
		resetMonthActions();
	}
	
	public static void initializeGeneralWorld() {
		npcNations = new List<>(9, 9);
		
		for(int q = 0; q < INITIAL_NPC_NATION_COUNT; q++) {
			Nation n = new Nation();
			CityState cs = n.getCapital();
			Human r = n.getRuler();
			npcNations.add(n);
			
			//TODO change the city and building placement to be systematic, based on terrain
			//generation
			claimTileForNation(cs, worldMap.at((q + 1) * 5, 0));
			Castle castle = new Castle(r, worldMap.at((q + 1) * 5, 0));
			new UnitGroup(r);
			castle.assignGroup(r.getGroup());
			addBuildingToCityAndMap(castle, cs, worldMap.at((q + 1) * 5, 0));
			claimTileForNation(cs, worldMap.at((q + 1) * 5, 1));
			addBuildingToCityAndMap(new Village(cs), cs, worldMap.at((q + 1) * 5, 1));
			claimTileForNation(cs, worldMap.at((q + 1) * 5, 2));
			addBuildingToCityAndMap(new Village(cs), cs, worldMap.at((q + 1) * 5, 2));
			claimTileForNation(cs, worldMap.at((q + 1) * 5, 3));
			addBuildingToCityAndMap(new Village(cs), cs, worldMap.at((q + 1) * 5, 3));
			claimTileForNation(cs, worldMap.at(((q + 1) * 5) + 1, 0));
			addBuildingToCityAndMap(new TrainingFacility(RNGStuff.newLocationName(n.getNationalLanguage()),
					Human.completelyRandomHuman(cs), worldMap.at((q * 5) + 1, 0)),
					cs, worldMap.at(((q + 1) * 5) + 1, 0));
			claimTileForNation(cs, worldMap.at(((q + 1) * 5) + 1, 1));
			addBuildingToCityAndMap(new Farm(RNGStuff.newLocationName(n.getNationalLanguage()),
					Human.completelyRandomHuman(cs), worldMap.at(((q + 1) * 5) + 1, 1)),
					cs, worldMap.at(((q + 1) * 5) + 1, 1));
			claimTileForNation(cs, worldMap.at(((q + 1) * 5) + 1, 2));
			addBuildingToCityAndMap(new Storehouse(RNGStuff.newLocationName(n.getNationalLanguage()),
					Human.completelyRandomHuman(cs), worldMap.at(((q + 1) * 5) + 1, 2)),
					cs, worldMap.at(((q + 1) * 5) + 1, 2));
			claimTileForNation(cs, worldMap.at(((q + 1) * 5) + 1, 3));
			addBuildingToCityAndMap(new MiningFacility(RNGStuff.newLocationName(n.getNationalLanguage()),
					Human.completelyRandomHuman(cs), worldMap.at(((q + 1) * 5) + 1, 3)),
					cs, worldMap.at(((q + 1) * 5) + 1, 3));
		}
		
	}
	
	public static void initializePlayer(String pName, boolean pGender, int pFace, int pNose, int pLips, int pEar,
			int pEye, int pIris, int pBrow, int pHair, int pStache, int pBeard, int pInterest1, int pInterest2,
			int pInterest3, int pInterest4, int pInterest5, int pInterest6, int pTrait, int pDemeanor, int pHpBoon,
			int pHpBane, int pAttributeBoon, int pAttributeBane, int pHairColor,
			double skinRed, double skinGreen, double skinBlue,
			int pEyeColor) {
		Interest[] interestsList = Interest.values();
		int[] bodyPartsMaximumHP = {15/**Head*/, 23/**Torso*/, 15, 15/**Arms*/,
				17, 17/**Legs*/, 7, 7/**Eyes*/, 0/**Mount*/};
		int[] bodyPartsMaximumHPGrowth = {35/**Head*/, 45/**Torso*/, 40, 40/**Arms*/,
				45, 45/**Legs*/, 0, 0/**Eyes*/, 0/**Mount*/};
		if (pHpBoon == 0) {
			bodyPartsMaximumHP[Human.HEAD] += 5;
			bodyPartsMaximumHPGrowth[Human.HEAD] *= 1.5;
		} else if (pHpBoon == 1) {
			bodyPartsMaximumHP[Human.TORSO] += 5;
			bodyPartsMaximumHPGrowth[Human.TORSO] *= 1.5;
		} else if (pHpBoon == 2) {
			bodyPartsMaximumHP[Human.RIGHT_ARM] += 5;
			bodyPartsMaximumHPGrowth[Human.RIGHT_ARM] *= 1.5;
			bodyPartsMaximumHP[Human.LEFT_ARM] = bodyPartsMaximumHP[Human.RIGHT_ARM];
			bodyPartsMaximumHPGrowth[Human.LEFT_ARM]  = bodyPartsMaximumHPGrowth[Human.RIGHT_ARM];
		} else if (pHpBoon == 3) {
			bodyPartsMaximumHP[Human.RIGHT_LEG] += 5;
			bodyPartsMaximumHPGrowth[Human.RIGHT_LEG] *= 1.5;
			bodyPartsMaximumHP[Human.LEFT_LEG] = bodyPartsMaximumHP[Human.RIGHT_LEG];
			bodyPartsMaximumHPGrowth[Human.LEFT_LEG]  = bodyPartsMaximumHPGrowth[Human.RIGHT_LEG];
		}
		if (pHpBane == 0) {
			bodyPartsMaximumHP[Human.HEAD] -= 5;
			bodyPartsMaximumHPGrowth[Human.HEAD] /= 2;
		} else if (pHpBane == 1) {
			bodyPartsMaximumHP[Human.TORSO] -= 5;
			bodyPartsMaximumHPGrowth[Human.TORSO] /= 2;
		} else if (pHpBane == 2) {
			bodyPartsMaximumHP[Human.RIGHT_ARM] -= 5;
			bodyPartsMaximumHPGrowth[Human.RIGHT_ARM] /= 2;
			bodyPartsMaximumHP[Human.LEFT_ARM] = bodyPartsMaximumHP[Human.RIGHT_ARM];
			bodyPartsMaximumHPGrowth[Human.LEFT_ARM]  = bodyPartsMaximumHPGrowth[Human.RIGHT_ARM];
		} else if (pHpBane == 3) {
			bodyPartsMaximumHP[Human.RIGHT_LEG] -= 5;
			bodyPartsMaximumHPGrowth[Human.RIGHT_LEG] /= 2;
			bodyPartsMaximumHP[Human.LEFT_LEG] = bodyPartsMaximumHP[Human.RIGHT_LEG];
			bodyPartsMaximumHPGrowth[Human.LEFT_LEG]  = bodyPartsMaximumHPGrowth[Human.RIGHT_LEG];
		}
		int[] attributes = {5/**Magic*/, 6/**Skill*/, 5/**Reflex*/, 6/**Awareness*/,
				4/**Resistance*/};
		int[] attributeGrowths = {15/**Magic*/, 25/**Skill*/, 25/**Reflex*/, 25/**Awareness*/,
				5/**Resistance*/};
		attributes[pAttributeBoon] *= 2;
		attributeGrowths[pAttributeBoon] *= 2;
		attributes[pAttributeBane] /= 2;
		attributeGrowths[pAttributeBane] /= 2;
		int ldr = 5;
		int mov = 6;
		int age = 25;
		int[] values = {50, 50, 50, 50, 50, 50};
		int red = (int)Math.round(skinRed * 255);
		int green = (int)Math.round(skinGreen * 255);
		int blue = (int)Math.round(skinBlue * 255);
		int[] appearance = {pFace, pLips, pNose, pEar, pEye, pIris, pBrow, pHair, pStache,
				pBeard, pHairColor, red, green, blue, pEyeColor};
		Interest[] interests = {interestsList[pInterest1], interestsList[pInterest2],
				interestsList[pInterest3]};
		Interest[] disinterests = {interestsList[pInterest4], interestsList[pInterest5],
				interestsList[pInterest6]};
		
		player = new Human(pName, pGender, bodyPartsMaximumHP, attributes[0], attributes[1],
				attributes[2], attributes[3], attributes[4], mov, ldr, bodyPartsMaximumHPGrowth,
				attributeGrowths[0], attributeGrowths[1], attributeGrowths[2],
				attributeGrowths[3], attributeGrowths[4], age, values, appearance, interests,
				disinterests, Demeanor.values()[pDemeanor], CombatTrait.values()[pTrait],
				playerNation.getCapital());
		player.toggleImportance();
		player.toggleMortality();
		playerNation.setRuler(player);
		playerNation.getArmy().add(player);
		new UnitGroup(player); //The constructor for UnitGroup automatically adds itself the members' nation
		//TODO figure out actual coordinates
		claimTileForNation(playerNation.getCapital(), worldMap.at(0, 0));
		Castle playerCastle = new Castle(player, worldMap.at(0, 0));
		playerCastle.assignGroup(player.getGroup());
		addBuildingToCityAndMap(playerCastle, playerNation.getCapital(), worldMap.at(0, 0));
		claimTileForNation(playerNation.getCapital(), worldMap.at(0, 1));
		addBuildingToCityAndMap(new Village(playerNation.getCapital()), playerNation.getCapital(), worldMap.at(0, 1));
		claimTileForNation(playerNation.getCapital(), worldMap.at(0, 2));
		addBuildingToCityAndMap(new Village(playerNation.getCapital()), playerNation.getCapital(), worldMap.at(0, 2));
		claimTileForNation(playerNation.getCapital(), worldMap.at(0, 3));
		addBuildingToCityAndMap(new Village(playerNation.getCapital()), playerNation.getCapital(), worldMap.at(0, 3));
		claimTileForNation(playerNation.getCapital(), worldMap.at(1, 0));
		addBuildingToCityAndMap(new TrainingFacility(RNGStuff.newLocationName(playerNation.getNationalLanguage()),
				Human.completelyRandomHuman(playerNation.getCapital()), worldMap.at(1, 0)),
				playerNation.getCapital(), worldMap.at(1, 0));
		claimTileForNation(playerNation.getCapital(), worldMap.at(1, 1));
		addBuildingToCityAndMap(new Farm(RNGStuff.newLocationName(playerNation.getNationalLanguage()),
				Human.completelyRandomHuman(playerNation.getCapital()), worldMap.at(1, 1)),
				playerNation.getCapital(), worldMap.at(1, 1));
		claimTileForNation(playerNation.getCapital(), worldMap.at(1, 2));
		addBuildingToCityAndMap(new Storehouse(RNGStuff.newLocationName(playerNation.getNationalLanguage()),
				Human.completelyRandomHuman(playerNation.getCapital()), worldMap.at(1, 2)),
				playerNation.getCapital(), worldMap.at(1, 2));
		claimTileForNation(playerNation.getCapital(), worldMap.at(1, 3));
		addBuildingToCityAndMap(new MiningFacility(RNGStuff.newLocationName(playerNation.getNationalLanguage()),
				Human.completelyRandomHuman(playerNation.getCapital()), worldMap.at(1, 3)),
				playerNation.getCapital(), worldMap.at(1, 3));
	}
	
	public static WorldMap getWorldMap() {
		return worldMap;
	}

	public static void setAging(boolean shouldAge) {
		//TODO set this value in database
		aging = shouldAge;
	}

	public static void addPlayerCityState(WorldMapTile tile) {
		addPlayerCityState(RNGStuff.newLocationName(playerNation.getNationalLanguage()), tile);
	}

	public static void addPlayerCityState(String name, WorldMapTile tile) {
		//City-state automatically adds itself to the nation's city list
		CityState cs = new CityState(name, getPlayerNation());
		claimTileForNation(cs, tile);
		addBuildingToCityAndMap(new Village(cs), cs, tile);
	}
	
	public static HashMap<WorldMapTile, Integer> getTraversableWorldMapTilesPeaceTime(UnitGroup group,
			int x, int y) {
		HashMap<WorldMapTile, Integer> traversable = new HashMap<>(800);
		LinkedQueue<int[]> searchList = new LinkedQueue<>(); //[0] = x, [1] = y, [2] = remainingMovement
		searchList.add(new int[] {x, y, group.getMovement()});
		while (!(searchList.isEmpty())) {
			int[] from = searchList.pop();
			WorldMapTile fromTile = worldMap.at(from[0], from[1]);
			traversable.put(fromTile, from[2]);
			if (from[2] == 0) {
				continue;
			}
			if (from[0] > 0) {
				int checkX = from[0] - 1;
				int checkY = from[1];
				WorldMapTile check = worldMap.at(from[0] - 1, from[1]);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null || check.getAffiliation() == group.getAffiliation())
						&& check.getBattle() == null) {
					if (check.getOwner() == null || check.getOwner() != group.getLocation().getOwner()) {
						searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
					} else {
						searchList.add(new int[] {checkX, checkY, Math.max(1, from[2] - check.getMoveCost(group))});
					}
				}
			}
			if (from[0] < WorldMap.SQRT_OF_MAP_SIZE - 1) {
				int checkX = from[0] + 1;
				int checkY = from[1];
				WorldMapTile check = worldMap.at(from[0] + 1, from[1]);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null || check.getAffiliation() == group.getAffiliation())
						&& check.getBattle() == null) {
					if (check.getOwner() == null || check.getOwner() != group.getLocation().getOwner()) {
						searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
					} else {
						searchList.add(new int[] {checkX, checkY, Math.max(1, from[2] - check.getMoveCost(group))});
					}
				}
			}
			if (from[1] > 0) {
				int checkX = from[0];
				int checkY = from[1] - 1;
				WorldMapTile check = worldMap.at(from[0], from[1] - 1);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null || check.getAffiliation() == group.getAffiliation())
						&& check.getBattle() == null) {
					if (check.getOwner() == null || check.getOwner() != group.getLocation().getOwner()) {
						searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
					} else {
						searchList.add(new int[] {checkX, checkY, Math.max(1, from[2] - check.getMoveCost(group))});
					}
				}
			}
			if (from[1] < WorldMap.SQRT_OF_MAP_SIZE - 1) {
				int checkX = from[0];
				int checkY = from[1] + 1;
				WorldMapTile check = worldMap.at(from[0], from[1] + 1);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null || check.getAffiliation() == group.getAffiliation())
						&& check.getBattle() == null) {
					if (check.getOwner() == null || check.getOwner() != group.getLocation().getOwner()) {
						searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
					} else {
						searchList.add(new int[] {checkX, checkY, Math.max(1, from[2] - check.getMoveCost(group))});
					}
				}
			}
		}
		return traversable;
	}

	public static HashMap<WorldMapTile, Integer> getTraversableWorldMapTilesEventTime(UnitGroup group,
			int x, int y) {
		HashMap<WorldMapTile, Integer> traversable = new HashMap<>(800);
		LinkedQueue<int[]> searchList = new LinkedQueue<>(); //[0] = x, [1] = y, [2] = remainingMovement
		searchList.add(new int[] {x, y, group.getMovement()});
		while (!(searchList.isEmpty())) {
			int[] from = searchList.pop();
			WorldMapTile fromTile = worldMap.at(from[0], from[1]);
			traversable.put(fromTile, from[2]);
			if (from[2] == 0) {
				continue;
			}
			//TODO Maybe change to allow traversal of allied defended buildings
			if (from[0] > 0) {
				int checkX = from[0] - 1;
				int checkY = from[1];
				WorldMapTile check = worldMap.at(from[0] - 1, from[1]);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null
						|| check.getOwner().getNation() == group.getAffiliation()
						|| check.getOwner().getNation().isAlliedWith(check.getOwner().getNation())
						|| group.getAffiliation().isAtWarWith(check.getOwner().getNation()))
						&& (!(check.getBuilding() instanceof Defendable)
						|| check.getAffiliation() == group.getAffiliation()
						|| ((Defendable)check.getBuilding()).getAssignedGroup() == null)
						&& check.getBattle() == null) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
			if (from[0] < WorldMap.SQRT_OF_MAP_SIZE - 1) {
				int checkX = from[0] + 1;
				int checkY = from[1];
				WorldMapTile check = worldMap.at(from[0] + 1, from[1]);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null
						|| check.getOwner().getNation() == group.getAffiliation()
						|| check.getOwner().getNation().isAlliedWith(check.getOwner().getNation())
						|| group.getAffiliation().isAtWarWith(check.getOwner().getNation()))
						&& (!(check.getBuilding() instanceof Defendable)
						|| check.getAffiliation() == group.getAffiliation()
						|| ((Defendable)check.getBuilding()).getAssignedGroup() == null)
						&& check.getBattle() == null) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
			if (from[1] > 0) {
				int checkX = from[0];
				int checkY = from[1] - 1;
				WorldMapTile check = worldMap.at(from[0], from[1] - 1);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null
						|| check.getOwner().getNation() == group.getAffiliation()
						|| check.getOwner().getNation().isAlliedWith(check.getOwner().getNation())
						|| group.getAffiliation().isAtWarWith(check.getOwner().getNation()))
						&& (!(check.getBuilding() instanceof Defendable)
						|| check.getAffiliation() == group.getAffiliation()
						|| ((Defendable)check.getBuilding()).getAssignedGroup() == null)
						&& check.getBattle() == null) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
			if (from[1] < WorldMap.SQRT_OF_MAP_SIZE - 1) {
				int checkX = from[0];
				int checkY = from[1] + 1;
				WorldMapTile check = worldMap.at(from[0], from[1] + 1);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null
						|| check.getOwner().getNation() == group.getAffiliation()
						|| check.getOwner().getNation().isAlliedWith(check.getOwner().getNation())
						|| group.getAffiliation().isAtWarWith(check.getOwner().getNation()))
						&& (!(check.getBuilding() instanceof Defendable)
						|| check.getAffiliation() == group.getAffiliation()
						|| ((Defendable)check.getBuilding()).getAssignedGroup() == null)
						&& check.getBattle() == null) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
		}
		return traversable;
	}
	
	public static HashMap<WorldMapTile, Integer> getTraversableTilesForShipPeaceTime(Ship group,
			int x, int y) {
		HashMap<WorldMapTile, Integer> traversable = new HashMap<>(200);
		LinkedQueue<int[]> searchList = new LinkedQueue<>(); //[0] = x, [1] = y, [2] = remainingMovement
		searchList.add(new int[] {x, y, group.getMovement()});
		while (!(searchList.isEmpty())) {
			int[] from = searchList.pop();
			WorldMapTile fromTile = worldMap.at(from[0], from[1]);
			traversable.put(fromTile, from[2]);
			if (from[2] == 0) {
				continue;
			}
			if (from[0] > 0) {
				int checkX = from[0] - 1;
				int checkY = from[1];
				WorldMapTile check = worldMap.at(from[0] - 1, from[1]);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& check.getBattle() == null) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
			if (from[0] < WorldMap.SQRT_OF_MAP_SIZE - 1) {
				int checkX = from[0] + 1;
				int checkY = from[1];
				WorldMapTile check = worldMap.at(from[0] + 1, from[1]);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()) {
					if ((traversable.get(check) == null || traversable.get(check) < from[2])
							&& from[2] - check.getMoveCost(group) >= 0
							&& check.isVacant()
							&& check.getBattle() == null) {
						searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
					}
				}
			}
			if (from[1] > 0) {
				int checkX = from[0];
				int checkY = from[1] - 1;
				WorldMapTile check = worldMap.at(from[0], from[1] - 1);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()) {
					if ((traversable.get(check) == null || traversable.get(check) < from[2])
							&& from[2] - check.getMoveCost(group) >= 0
							&& check.isVacant()
							&& check.getBattle() == null) {
						searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
					}
				}
			}
			if (from[1] < WorldMap.SQRT_OF_MAP_SIZE - 1) {
				int checkX = from[0];
				int checkY = from[1] + 1;
				WorldMapTile check = worldMap.at(from[0], from[1] + 1);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()) {
					if ((traversable.get(check) == null || traversable.get(check) < from[2])
							&& from[2] - check.getMoveCost(group) >= 0
							&& check.isVacant()
							&& check.getBattle() == null) {
						searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
					}
				}
			}
		}
		return traversable;
	}
	
	public static HashMap<WorldMapTile, Integer> getTraversableTilesForShipEventTime(Ship group,
			int x, int y) {
		HashMap<WorldMapTile, Integer> traversable = new HashMap<>(200);
		LinkedQueue<int[]> searchList = new LinkedQueue<>(); //[0] = x, [1] = y, [2] = remainingMovement
		searchList.add(new int[] {x, y, group.getMovement()});
		while (!(searchList.isEmpty())) {
			int[] from = searchList.pop();
			WorldMapTile fromTile = worldMap.at(from[0], from[1]);
			traversable.put(fromTile, from[2]);
			if (from[2] == 0) {
				continue;
			}
			if (from[0] > 0) {
				int checkX = from[0] - 1;
				int checkY = from[1];
				WorldMapTile check = worldMap.at(from[0] - 1, from[1]);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null
						|| check.getOwner().getNation() == group.getAffiliation()
						|| check.getOwner().getNation().isAlliedWith(check.getOwner().getNation())
						|| group.getAffiliation().isAtWarWith(check.getOwner().getNation()))) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
			if (from[0] < WorldMap.SQRT_OF_MAP_SIZE - 1) {
				int checkX = from[0] + 1;
				int checkY = from[1];
				WorldMapTile check = worldMap.at(from[0] + 1, from[1]);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null
						|| check.getOwner().getNation() == group.getAffiliation()
						|| check.getOwner().getNation().isAlliedWith(check.getOwner().getNation())
						|| group.getAffiliation().isAtWarWith(check.getOwner().getNation()))) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
			if (from[1] > 0) {
				int checkX = from[0];
				int checkY = from[1] - 1;
				WorldMapTile check = worldMap.at(from[0], from[1] - 1);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null
						|| check.getOwner().getNation() == group.getAffiliation()
						|| check.getOwner().getNation().isAlliedWith(check.getOwner().getNation())
						|| group.getAffiliation().isAtWarWith(check.getOwner().getNation()))) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
			if (from[1] < WorldMap.SQRT_OF_MAP_SIZE - 1) {
				int checkX = from[0];
				int checkY = from[1] + 1;
				WorldMapTile check = worldMap.at(from[0], from[1] + 1);
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(group) >= 0
						&& check.isVacant()
						&& (check.getOwner() == null
						|| check.getOwner().getNation() == group.getAffiliation()
						|| check.getOwner().getNation().isAlliedWith(check.getOwner().getNation())
						|| group.getAffiliation().isAtWarWith(check.getOwner().getNation()))) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(group)});
				}
			}
		}
		return traversable;
	}

	public static HashMap<BattlegroundTile, Integer> getTraversableBattlegroundTiles(BattleGround battleground, Unit u, int x, int y) {
		HashMap<BattlegroundTile, Integer> traversable = new HashMap<>(800);
		LinkedQueue<int[]> searchList = new LinkedQueue<>(); //[0] = x, [1] = y, [2] = remainingMovement
		searchList.add(new int[] {x, y, u.getMovement()});
		int[] dimensions = battleground.getDimensions();
		while (!(searchList.isEmpty())) {
			int[] from = searchList.pop();
			BattlegroundTile fromTile = battleground.getMap()[from[0]][from[1]];
			traversable.put(fromTile, from[2]);
			if (from[2] == 0) {
				continue;
			}
			if (from[0] > 0) {
				int checkX = from[0] - 1;
				int checkY = from[1];
				BattlegroundTile check = battleground.getMap()[checkX][checkY];
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(u) >= 0
						&& check.isVacant()) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(u)});
				}
			}
			if (from[0] < dimensions[0] - 1) {
				int checkX = from[0] + 1;
				int checkY = from[1];
				BattlegroundTile check = battleground.getMap()[checkX][checkY];
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(u) >= 0
						&& check.isVacant()) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(u)});
				}
			}
			if (from[1] > 0) {
				int checkX = from[0];
				int checkY = from[1] - 1;
				BattlegroundTile check = battleground.getMap()[checkX][checkY];
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(u) >= 0
						&& check.isVacant()) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(u)});
				}
			}
			if (from[1] < dimensions[1] - 1) {
				int checkX = from[0];
				int checkY = from[1] + 1;
				BattlegroundTile check = battleground.getMap()[checkX][checkY];
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - check.getMoveCost(u) >= 0
						&& check.isVacant()) {
					searchList.add(new int[] {checkX, checkY, from[2] - check.getMoveCost(u)});
				}
			}
		}
		return traversable;
	}
	
	public static HashMap<BattlegroundTile, Integer> getAttackableBattlegroundTilesFromDestination(
			BattleGround battleground, Unit u, int x, int y, StationaryWeapon w) {
		int minRange = 1;
		int maxRange = 1;
		if (u instanceof Equippable) {
			if (w == null) {
				int[] used = ((Equippable)u).getInventory()[u.getRanges()[0]];
				if (used != null && used[0] == InventoryIndex.HANDHELD_WEAPON) {
					maxRange = ((HandheldWeapon)InventoryIndex.getElement(used)).maxRange();
				}
			} else {
				minRange = w.minRange();
				maxRange = w.maxRange();
			}
		}
		HashMap<BattlegroundTile, Integer> traversable = new HashMap<>(800);
		HashMap<BattlegroundTile, Integer> attackable = new HashMap<>(800); //Gives distance from attacker for each target
		LinkedQueue<int[]> searchList = new LinkedQueue<>(); //[0] = x, [1] = y, [2] = remainingMovement
		searchList.add(new int[] {x, y, maxRange});
		int[] dimensions = battleground.getDimensions();
		while (!(searchList.isEmpty())) {
			int[] from = searchList.pop();
			BattlegroundTile fromTile = battleground.getMap()[from[0]][from[1]];
			traversable.put(fromTile, from[2]);
			int distance = maxRange - from[2];
			if (distance >= minRange
					&& fromTile.getUnit() != null && fromTile.getUnit().isEnemyOf(u)) {
				attackable.put(fromTile, distance);
			}
			if (from[2] == 0) {
				continue;
			}
			if (from[0] > 0) {
				int checkX = from[0] - 1;
				int checkY = from[1];
				BattlegroundTile check = battleground.getMap()[checkX][checkY];
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - 1 >= 0) {
					searchList.add(new int[] {checkX, checkY, from[2] - 1});
				}
			}
			if (from[0] < dimensions[0] - 1) {
				int checkX = from[0] + 1;
				int checkY = from[1];
				BattlegroundTile check = battleground.getMap()[checkX][checkY];
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - 1 >= 0) {
					searchList.add(new int[] {checkX, checkY, from[2] - 1});
				}
			}
			if (from[1] > 0) {
				int checkX = from[0];
				int checkY = from[1] - 1;
				BattlegroundTile check = battleground.getMap()[checkX][checkY];
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - 1 >= 0) {
					searchList.add(new int[] {checkX, checkY, from[2] - 1});
				}
			}
			if (from[1] < dimensions[1] - 1) {
				int checkX = from[0];
				int checkY = from[1] + 1;
				BattlegroundTile check = battleground.getMap()[checkX][checkY];
				if ((traversable.get(check) == null || traversable.get(check) < from[2])
						&& from[2] - 1 >= 0) {
					searchList.add(new int[] {checkX, checkY, from[2] - 1});
				}
			}
		}
		return attackable;
	}
}
