package manager;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import battle.BattleGround;
import data_structures.List;
import inventory.Convoy;
import inventory.Herd;
import inventory.InventoryIndex;
import inventory.staff.OffensiveStaff;
import inventory.staff.SupportStaff;
import inventory.staff.TileStaff;
import inventory.weapon.HandheldWeapon;
import inventory.weapon.StationaryWeapon;
import location.BFTileOccupant;
import location.BattlegroundTile;
import report.StandardBattleReport;
import unit.Equippable;
import unit.Unit;
import unit.UnitClass;
import unit.human.Human;

public class BattleGroundAIManager {

	public static HashMap<BattlegroundTile, int[]> traversable;
	
	public static Decision bestOption;
	
	public static BattleGround battleground;
	
	public static void makeDecisions(BattleGround battle, List<Unit> moveable,
			List<Unit> enemies) {
		//TODO in whatever method calls this, put this in a while loop that keeps going
		//until moveable is empty
		bestOption = new Decision();
		battleground = battle;
		//Go through all possible options for all units and find the best one
		for (int q = 0; q < moveable.size(); q++) {
			Unit u = moveable.get(q);
			BattlegroundTile[][] map = battle.getMap();
			traversable = getTraversableTiles(map, u, battle.getCoordsOfUnit(u), enemies, battle);
			Set<Entry<BattlegroundTile, int[]>> destinationSet = traversable.entrySet();
			for (Entry<BattlegroundTile, int[]> entry : destinationSet) {
				//TODO equip and trade can be used with other options. Figure out
				//how to work them in
				int[] coords = entry.getValue();
				BattlegroundTile dest = entry.getKey();
				
				//Try to wait
				Decision d = new Decision(u, coords,
						ThingToDo.WAIT, null, null, 0, 0, null);
				if (d.heuristic > bestOption.heuristic) {
					bestOption = d;
				}
				
				//Try to use an item
				if (u instanceof Equippable) {
					int[][] inv = ((Equippable)u).getInventory();
					for (int a = 0; a < inv.length; a++) {
						if (inv[a] != null) {
							if (inv[a][0] == InventoryIndex.USABLE_ITEM) {
							checkDecision(new Decision(u, coords,
									ThingToDo.USE, null, null, a, 0, null));
							} else if (inv[a][0] == InventoryIndex.STATIONARY_STAFF) {
								checkDecision(new Decision(u, coords, ThingToDo.STATIONARY_STAFF,
										null, null, a, 0, null));
							}
						}
					}
				}
				
				if (dest.getInanimateObjectOccupant() instanceof StationaryWeapon
						&& u instanceof Equippable) {
					StationaryWeapon stWep = (StationaryWeapon)entry.getKey().getInanimateObjectOccupant();
					for (int en = 0; en < enemies.size(); en++) {
						Unit enem = enemies.get(en);
							int[] enemCoords = battle.getCoordsOfUnit(enemies.get(en));
							if (distance(coords, enemCoords)
									>= stWep.getBattlegroundMinRange()) {
								//Try attacking with the stationary weapon
								checkDecision(new Decision(u, coords, ThingToDo.ATTACK,
										stWep, enem, 0, 0, null));
							}
					}
				} else if (dest.getInanimateObjectOccupant() instanceof Convoy) {
					if (u instanceof Equippable) {
						//Try to steal from the convoy
						checkDecision(new Decision(u, coords, ThingToDo.STEAL,
								null, dest.getInanimateObjectOccupant(), 0, 0, null));
					}
					if (u.getUnitClass().getClassAbility() == UnitClass.ClassAbility.CONVOY) {
						//Try to pick up the convoy
						checkDecision(new Decision(u, coords, ThingToDo.RESCUE,
								null, dest.getInanimateObjectOccupant(), 0, 0, null));
					}
				} else if (dest.getInanimateObjectOccupant() instanceof Herd) {
					Herd h = (Herd)dest.getInanimateObjectOccupant();
					if (u.getUnitClass().getMount() != null
							&& u.getUnitClass().getMountType() == h.getAnimalType()
							&& u.canBeCarried()) { //If the herd has the unit's mount type and the unit's mount is dead
								//Try to get a new mount
								checkDecision(new Decision(u, coords, ThingToDo.STEAL,
										null, dest.getInanimateObjectOccupant(), 0, 0, null));
					}
					if (u.getUnitClass().getClassAbility() == UnitClass.ClassAbility.HERDING) {
								//Try to pick up the herd
								checkDecision(new Decision(u, coords, ThingToDo.RESCUE,
										null, dest.getInanimateObjectOccupant(), 0, 0, null));
					}
				}
			}
			
			HashMap<BattlegroundTile, List<BattlegroundTile>> interactable = getInteractableTiles(map, traversable, u);
			Set<Entry<BattlegroundTile, List<BattlegroundTile>>> targetsSet = interactable.entrySet();
			for (Entry<BattlegroundTile, List<BattlegroundTile>> entry : targetsSet) {
				BattlegroundTile target = entry.getKey();
				int[] targCoords = traversable.get(target);
				List<BattlegroundTile> from = entry.getValue();
				//Check every possible action and every possible place the action can be
				//performed from and rank them.
				//TODO equip, and trade can be used with other options. Figure out
				//how to work them in
				if (target.getUnit() == null) {
					if (u instanceof Equippable) {
						int[][] inv = ((Equippable)u).getInventory();
						for (int a = 0; a < inv.length; a++) {
							if (inv[a] != null && inv[a][0] == InventoryIndex.TILE_STAFF) {
								for (int fs = 0; fs < from.size(); fs++) {
									int[] fromCoords = traversable.get(from.get(fs));
									if (distance(targCoords, fromCoords) <=
											((TileStaff)InventoryIndex.getElement(inv[a])).maxRange()) {
										//Try a tile staff
										checkDecision(new Decision(u, fromCoords, ThingToDo.TILE_STAFF,
												null, null, a, 0, targCoords));
									}
								}
							}
						}
					}
					if (u.getPassenger() != null) {
						for (int a = 0; a < from.size(); a++) {
							int[] fromCoords = traversable.get(from.get(a));
							if (distance(targCoords, fromCoords) == 1) {
								//Try dropping off a passenger
								checkDecision(new Decision(u, fromCoords, ThingToDo.DROP,
										u.getPassenger(), null, 0, 0, targCoords));
							}
						}
					}
				} else {
					Unit other = target.getUnit();
					if (u.getAffiliation().isAlliedWith(other.getAffiliation())) {
						if (u instanceof Equippable) {
							int[][] inv = ((Equippable)u).getInventory();
							for (int a = 0; a < inv.length; a++) {
								if (inv[a] != null && inv[a][0] == InventoryIndex.SUPPORT_STAFF) {
									for (int fs = 0; fs < from.size(); fs++) {
										int[] fromCoords = traversable.get(from.get(fs));
										if (distance(targCoords, fromCoords) <=
												((SupportStaff)InventoryIndex.getElement(inv[a])).maxRange()) {
											//Try using support staff
											checkDecision(new Decision(u, fromCoords,
													ThingToDo.SUPPORT_STAFF, null, other, a, 0, null));
										}
									}
								}
							}
						}
						if (u.getAffiliation() == other.getAffiliation()) {
							for (int a = 0; a < from.size(); a++) {
								int[] fromCoords = traversable.get(from.get(a));
								if (distance(targCoords, fromCoords) == 1) {
									//Try rescuing an ally
									checkDecision(new Decision(u, fromCoords, ThingToDo.RESCUE,
											null, other, 0, 0, null));
								}
							}
						}
					} else if (u.getAffiliation().isAtWarWith(other.getAffiliation())) {
						if (u instanceof Equippable) {
							int[][] inv = ((Equippable)u).getInventory();
							for (int invIdx = 0; invIdx < inv.length; invIdx++) {
								if (inv[invIdx] != null && inv[invIdx][0] == InventoryIndex.HANDHELD_WEAPON) {
									for (int a = 0; a < from.size(); a++) {
										int[] fromCoords = traversable.get(from.get(a));
										if (distance(targCoords, fromCoords) <=
												((HandheldWeapon)InventoryIndex.getElement(inv[invIdx])).maxRange()) {
											//Try an attack with a handheld weapon
											checkDecision(new Decision(u, fromCoords,
													ThingToDo.ATTACK, null, other, invIdx, 0,
													null));
										}
									}
								} else if (inv[invIdx] != null && inv[invIdx][0] == InventoryIndex.OFFENSIVE_STAFF) {
									for (int a = 0; a < inv.length; a++) {
										if (inv[a][0] == InventoryIndex.OFFENSIVE_STAFF) {
											for (int fs = 0; fs < from.size(); fs++) {
												int[] fromCoords = traversable.get(from.get(fs));
												if (distance(targCoords, fromCoords) <=
														((OffensiveStaff)InventoryIndex.getElement(inv[a])).maxRange()) {
													//Try using offensive staff
													checkDecision(new Decision(u, fromCoords,
															ThingToDo.OFFENSE_STAFF, null, other, a, 0, null));
												}
											}
										}
									}
								} else {
									for (int a = 0; a < from.size(); a++) {
										int[] fromCoords = traversable.get(from.get(a));
										if (distance(targCoords, fromCoords) == 1) {
											//Try an attack with no weapon
											checkDecision(new Decision(u, fromCoords,
													ThingToDo.ATTACK, null, other, invIdx, 0,
													null));
										}
									}
								}
							}
						} else {
							int maxRange = u.getRanges()[1];
							for (int a = 0; a < from.size(); a++) {
								int[] fromCoords = traversable.get(from.get(a));
								int distance = distance(target, from.get(a));
								if (distance <= maxRange) {
									//Try a standard attack
									checkDecision(new Decision(u, fromCoords,
											ThingToDo.ATTACK, null, other, 0, 0, null));
								}
							}
						}
					}
				}
				if (target.getInanimateObjectOccupant() != null) {
					BFTileOccupant inan = target.getInanimateObjectOccupant();
					if (u instanceof Equippable) {
						int[][] inv = ((Equippable)u).getInventory();
						for (int a = 0; a < inv.length; a++) {
							if (inv[a] != null && inv[a][0] == InventoryIndex.HANDHELD_WEAPON) {
								for (int fs = 0; fs < from.size(); fs++) {
									int[] fromCoords = traversable.get(from.get(fs));
									if (distance(targCoords, fromCoords) <=
											((HandheldWeapon)InventoryIndex.getElement(inv[a])).maxRange()) {
										//Try attacking stationary weapon with weapon
										checkDecision(new Decision(u, fromCoords, ThingToDo.ATTACK,
												null, inan, a, 0, null));
									}
								}
							} else {
								for (int fs = 0; fs < from.size(); fs++) {
									int[] fromCoords = traversable.get(from.get(fs));
									if (distance(targCoords, fromCoords) == 1) {
										//Try attacking stationary weapon with no weapon
										checkDecision(new Decision(u, fromCoords, ThingToDo.ATTACK,
												null, inan, a, 0, null));
									}
								}
							}
						}
					} else {
						for (int fs = 0; fs < from.size(); fs++) {
							int[] fromCoords = traversable.get(from.get(fs));
							if (distance(targCoords, fromCoords) == 1) {
								//Try attacking standard attack against stationary weapon
								checkDecision(new Decision(u, fromCoords, ThingToDo.ATTACK,
										null, inan, 0, 0, null));
							}
						}
					}
				}
			}
		}
		//When done, remove fields to save space (we have them as static to reference them easier)
		commitToDecision(moveable);
		traversable = null;
		bestOption = null;
		battleground = null;
	}
	
	private static void commitToDecision(List<Unit> moveable) {
		// TODO Auto-generated method stub
		moveable.remove(bestOption.actor);
		ThingToDo todo = bestOption.todo;
		battleground.moveUnit(bestOption.actor, bestOption.destination,
				battleground.getMap());
		if (todo == ThingToDo.ATTACK) {
			if (bestOption.thingToDoItWith == null) {
				//TODO heuristic for attack with handheld weapon
				//remember to consider counter attack power and orders
				if (bestOption.thingToDoItTo instanceof Unit) {
					Unit u = (Unit)bestOption.thingToDoItTo;
					StandardBattleReport ret = BattleManager.performBattleSequence(
							bestOption.actor, u, bestOption.targetInventoryIndex,
							battleground.getTileAtCoords(bestOption.destination),
							battleground.getTileAtCoords(battleground.getCoordsOfUnit(u)),
							battleground, (Boolean)bestOption.miscDetail);
				}
			} else {
				//TODO heuristic for attack with stationary weapon
				//remember to consider orders
			}
		} else if (todo == ThingToDo.DROP) {
			
		} else if (todo == ThingToDo.OFFENSE_STAFF) {
			
		} else if (todo == ThingToDo.RESCUE) {
			
		} else if (todo == ThingToDo.STATIONARY_STAFF) {
			
		} else if (todo == ThingToDo.STEAL) {
			
		} else if (todo == ThingToDo.SUPPORT_STAFF) {
			
		} else if (todo == ThingToDo.TILE_STAFF) {
			
		} else if (todo == ThingToDo.USE) {
			
		} else if (todo == ThingToDo.WAIT) {
			//Nothing
		}

	}

	private static HashMap<BattlegroundTile, int[]> getTraversableTiles(BattlegroundTile[][] map,
			Unit u, int[] startCoords, List<Unit> enemies, BattleGround battle) {
		//Return a map where the key is a tile that the unit is able to move to
		//and the value is an array containing:
		//[0] = x coordinate of the tile that is the key
		//[1] = y coordinate of the tile that is the key
		//[2] = danger of the tile that is the key, based on enemies' ability to attack there
		//[3] = magic danger of the tile
		
		List<int[]> searchList = new List<>();
		HashMap<BattlegroundTile, int[]> discovered = new HashMap<>();
		int move = u.getMovement();
		boolean fly = u.canFly();//This value is manually calculated, so do it here so it's only calculated once
		int[] startSearchVal = {startCoords[0], startCoords[1], move};
		searchList.add(startSearchVal);
		//Include a fourth value so we can use this map as the return value later (after
		//altering the third and fourth values)
		int[] startVal = {startCoords[0], startCoords[1], Integer.MIN_VALUE, 0};
		discovered.put(map[startCoords[0]][startCoords[1]], startVal);
		while (!(searchList.isEmpty())) {
			int[] current = searchList.remove(searchList.size() - 1);
			int x = current[0];
			int y = current[1];
			BattlegroundTile currentTile = map[x][y];
			int[] coordsAndMaxMoveLeft = discovered.get(currentTile);
			if ((coordsAndMaxMoveLeft == null || coordsAndMaxMoveLeft[2] < current[2])
					&& currentTile.getUnit() == null) {
				discovered.put(currentTile, current);
				if (fly) {
					if (x > 0) {
						BattlegroundTile check = map[x - 1][y];
						int resultingMove = current[2] - check.moveCostInAir();
						if (resultingMove >= 0) {
							//Include a fourth value so we can use this map as the return value later (after
							//altering the third and fourth values)
							int[] val = {x - 1, y, resultingMove, 0};
							searchList.add(val);
						}
					}
					if (x < map.length - 1) {
						BattlegroundTile check = map[x + 1][y];
						int resultingMove = current[2] - check.moveCostInAir();
						if (resultingMove >= 0) {
							//Include a fourth value so we can use this map as the return value later (after
							//altering the third and fourth values)
							int[] val = {x + 1, y, resultingMove, 0};
							searchList.add(val);
						}
					}
					if (y > 0) {
						BattlegroundTile check = map[x][y - 1];
						int resultingMove = current[2] - check.moveCostInAir();
						if (resultingMove >= 0) {
							//Include a fourth value so we can use this map as the return value later (after
							//altering the third and fourth values)
							int[] val = {x, y - 1, resultingMove, 0};
							searchList.add(val);
						}
					}
					if (y < map[0].length - 1) {
						BattlegroundTile check = map[x][y + 1];
						int resultingMove = current[2] - check.moveCostInAir();
						if (resultingMove >= 0) {
							//Include a fourth value so we can use this map as the return value later (after
							//altering the third and fourth values)
							int[] val = {x, y + 1, resultingMove, 0};
							searchList.add(val);
						}
					}
				} else {
					if (x > 0) {
						BattlegroundTile check = map[x - 1][y];
						int resultingMove = current[2] - check.moveCostOnFoot();
						if (resultingMove >= 0) {
							//Include a fourth value so we can use this map as the return value later (after
							//altering the third and fourth values)
							int[] val = {x - 1, y, resultingMove, 0};
							searchList.add(val);
						}
					}
					if (x < map.length - 1) {
						BattlegroundTile check = map[x + 1][y];
						int resultingMove = current[2] - check.moveCostOnFoot();
						if (resultingMove >= 0) {
							//Include a fourth value so we can use this map as the return value later (after
							//altering the third and fourth values)
							int[] val = {x + 1, y, resultingMove, 0};
							searchList.add(val);
						}
					}
					if (y > 0) {
						BattlegroundTile check = map[x][y - 1];
						int resultingMove = current[2] - check.moveCostOnFoot();
						if (resultingMove >= 0) {
							//Include a fourth value so we can use this map as the return value later (after
							//altering the third and fourth values)
							int[] val = {x, y - 1, resultingMove, 0};
							searchList.add(val);
						}
					}
					if (y < map[0].length - 1) {
						BattlegroundTile check = map[x][y + 1];
						int resultingMove = current[2] - check.moveCostOnFoot();
						if (resultingMove >= 0) {
							//Include a fourth value so we can use this map as the return value later (after
							//altering the third and fourth values)
							int[] val = {x, y + 1, resultingMove, 0};
							searchList.add(val);
						}
					}
				}
			}
		}
		Set<Entry<BattlegroundTile, int[]>> entries = discovered.entrySet();
		for (Entry<BattlegroundTile, int[]> entry : entries) {
			BattlegroundTile tile = entry.getKey();
			int[] info = entry.getValue();
			info[2] = 0;
			info[3] = 0;
			for (int q = 0; q < enemies.size(); q++) {
				Unit enem = enemies.get(q);
				int[] enemCoords = battle.getCoordsOfUnit(enem);
				if (enem.canFly()) {
					int range = enem.getRanges()[1];
					if (enem.getMovement() + range/*TODO minus average cost for flying, plus attack range*/
							>= distance(enemCoords, info)) {
						if (enem.isUsingMagic()) {
							info[3] += enem.attackStrength();
						} else {
							info[2] += enem.attackStrength();
						}
					}
				} else {
					int range = enem.getRanges()[1];
					if (enem.getMovement() + range/*TODO minus average cost on foot, plus attack range*/
							>= distance(enemCoords, info)) {
						if (enem.isUsingMagic()) {
							info[3] += enem.attackStrength();
						} else {
							info[2] += enem.attackStrength();
						}
					}
				}
			}
		}
		return discovered;
	}
	
	private static HashMap<BattlegroundTile, List<BattlegroundTile>> getInteractableTiles(
			BattlegroundTile[][] map, HashMap<BattlegroundTile, int[]> traversable, Unit u) {
		HashMap<BattlegroundTile, List<BattlegroundTile>> ret = new HashMap<>();
		Set<Entry<BattlegroundTile, int[]>> travSet = traversable.entrySet();
		for (Entry<BattlegroundTile, int[]> entry : travSet) {
			BattlegroundTile tile = entry.getKey();
			int[] coords = entry.getValue();
			if (ret.get(tile) == null) {
				ret.put(tile, new List<BattlegroundTile>());
			}
			//We don't need this line (I think), because we use the map of traversable tiles
			//for figuring out WAIT and USE options
//			ret.get(tile).add(tile);
			
			//TODO maybe change return value to be a map that gives a list of lists, where
			//each list gives tiles that are in range for a different action?
			int maxRange = Math.max(u.getRanges()[1], u.getRanges()[3]);
			//Precalculate values so we only have to do so once
			int lowerBoundForQ = Math.max(0, coords[0] - maxRange);
			int upperBoundForQ = Math.min(map.length, coords[0] + maxRange + 1);
			int lowerBoundForW = Math.max(0, coords[1] - maxRange);
			int upperBoundForW = Math.min(map[0].length, coords[1] + maxRange + 1);
			for (int q = lowerBoundForQ; q < upperBoundForQ; q++) {
				for (int w = lowerBoundForW; w < upperBoundForW; w++) {
					BattlegroundTile dest = map[q][w];
					if (ret.get(dest) == null) {
						ret.put(dest, new List<BattlegroundTile>());
					}
					ret.get(dest).add(tile);
				}
			}
		}
		return ret;
	}
	
	public static int distance(BattlegroundTile t1, BattlegroundTile t2) {
		int[] c1 = traversable.get(t1);
		int[] c2 = traversable.get(t2);
		return distance(c1, c2);
	}
	
	public static int distance(int[] c1, int[] c2) {
		return Math.abs(c1[0] - c2[0]) + Math.abs(c1[1] - c2[1]);
	}
	
	public static void checkDecision(Decision d) {
		if (d.heuristic > bestOption.heuristic) {
			bestOption = d;
		}
	}
	
	private static class Decision {
		public Unit actor;
		public int[] destination;
		public ThingToDo todo;
		public BFTileOccupant thingToDoItWith;
		public BFTileOccupant thingToDoItTo;
		public int actorInventoryIndex;
		public int targetInventoryIndex;
		public int heuristic;
		public Object miscDetail;
		
		public Decision() {
			this.heuristic = Integer.MIN_VALUE;
		}

		//TODO make more constructors so there aren't as many uselessly called instructions
		public Decision(Unit actor, int[] destination, ThingToDo todo, BFTileOccupant thingToDoItWith,
				BFTileOccupant thingToDoItTo, int actorInventoryIndex, int targetInventoryIndex,
				int[] tileToActOn) {
			super();
			this.actor = actor;
			this.destination = destination;
			this.todo = todo;
			this.thingToDoItWith = thingToDoItWith;
			this.thingToDoItTo = thingToDoItTo;
			this.actorInventoryIndex = actorInventoryIndex;
			this.targetInventoryIndex = targetInventoryIndex;
			int[] orders = actor.getGroup().getBattleGroundObjective();
			//TODO calculate heuristic
			this.heuristic =
					(actor.defense(false, 1) - destination[2]) + (actor.defense(true, 1) - destination[3]);
			//TODO consider HP and orders along with danger
			if (todo == ThingToDo.ATTACK) {
				if (thingToDoItWith == null) {
					//TODO heuristic for attack with handheld weapon
					//remember to consider counter attack power and orders
					if (thingToDoItTo instanceof Unit) {
						int toAdd = Integer.MIN_VALUE;
						Unit u = (Unit)thingToDoItTo;
						for (int q = 0; q < u.getBodyPartsCurrentHP().length; q++) {
							if (u.getCurrentHPOfBodyPart(q) <= 0) {
								continue;
							}
							int counterRange = 1;
							if (u instanceof Equippable) {
								Equippable eq = (Equippable)u;
								if (eq.getEquippedWeapon() != null) {
									counterRange = eq.getEquippedWeapon().maxRange();
								}
							}
							int[] targetCoords = battleground.getCoordsOfUnit(u);
							miscDetail = counterRange >= distance(destination, targetCoords);
							int[] fc = BattleManager.normalBattleForecast(actor, u,
									q, battleground.getTileAtCoords(destination),
									battleground.getTileAtCoords(targetCoords),
									(Boolean)miscDetail);
							int check = Math.round((fc[2] / (float)100.0) * fc[1])
									+ Math.round((fc[3] / (float)100.0) * fc[1]);
							if (fc[2] < 50 || q >= Unit.TORSO || check < u.getCurrentHPOfBodyPart(q)) {
								//If original attack is not likely to be lethal, consider the double
								//and counter attacks
								check *= fc[4];
								if (fc[5] > -1) {
									int negCheck = Math.round((fc[7] / (float)100.0) * fc[6])
											+ Math.round((fc[8] / (float)100.0) * fc[6]);
									negCheck *= fc[9];
									if (negCheck >= actor.getCurrentHPOfBodyPart(Unit.TORSO)) {
										//If the counter attack(s) will kill the attacker, this
										//isn't really worth it
										negCheck *= 2;
									}
									check -= negCheck;
								}
							}
							if (check > toAdd) {
								toAdd = check;
								targetInventoryIndex = q;
							}
						}
						//TODO check orders
						this.heuristic += toAdd;
					} else {
						//TODO heuristic for attacking non-living things
					}
				} else {
					//TODO heuristic for attack with stationary weapon
					//remember to consider orders
				}
			} else if (todo == ThingToDo.DROP) {
				if (thingToDoItWith instanceof Unit) {
					//TODO drop a unit. Consider orders
					Unit u = (Unit)actor.getPassenger();
					heuristic +=
							(u.defense(false, 1) - destination[2]) + (u.defense(true, 1) - destination[3]);
					heuristic += (u.getMovement() * 5);
				} else {
					//TODO drop a non-living passenger
				}
			} else if (todo == ThingToDo.OFFENSE_STAFF) {
				//TODO
			} else if (todo == ThingToDo.RESCUE) {
				//TODO
			} else if (todo == ThingToDo.STATIONARY_STAFF) {
				//TODO
			} else if (todo == ThingToDo.STEAL) {
				//TODO
			} else if (todo == ThingToDo.SUPPORT_STAFF) {
				//TODO
			} else if (todo == ThingToDo.TILE_STAFF) {
				//TODO
			} else if (todo == ThingToDo.USE) {
				//TODO
			} else if (todo == ThingToDo.WAIT) {
				//Nothing. If you can be productive, be productive
			}
			
		}
		
	}
	
	private enum ThingToDo {
		ATTACK,
		WAIT,
		USE,
		STEAL,
		RESCUE,
		DROP,
		SUPPORT_STAFF,
		OFFENSE_STAFF,
		TILE_STAFF,
		STATIONARY_STAFF
	}
}
