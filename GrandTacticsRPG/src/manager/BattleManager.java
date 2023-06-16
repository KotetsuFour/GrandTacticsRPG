package manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import affiliation.Nation;
import battle.BattleGround;
import data_structures.List;
import history.HistoricalRecord;
import inventory.weapon.StationaryWeapon;
import location.BattlegroundTile;
import location.WorldMapTile;
import report.ShipBattleReport;
import report.StandardBattleReport;
import ship.Ship;
import unit.Equippable;
import unit.Unit;
import unit.human.Human;
import util.RNGStuff;

public class BattleManager {

	public static final int DOUBLE_ATTACK_CONSTANT = 4;
	
	/**
	 * Returns an array with the indexes...
	 * [0] = Current attacker torso HP
	 * [1] = Attacker might
	 * [2] = Attacker accuracy
	 * [3] = Attacker crit rate
	 * [4] = 2 if attacker can double attack, or 1 otherwise
	 * [5] = Defender current HP
	 * If the defender can retaliate, then the following indexes will be filled in.
	 * Otherwise, index [9] will be set to -1.
	 * [6] = Defender might
	 * [7] = Defender accuracy
	 * [8] = Defender crit rate
	 * [9] = 2 if defender can double attack, or 1 otherwise
	 * @param atk
	 * @param dfd
	 * @param bodyPart
	 * @param atkTile
	 * @param dfdTile
	 * @return
	 */
	public static int[] normalBattleForecast(Unit atk, Unit dfd, int bodyPart,
			BattlegroundTile atkTile, BattlegroundTile dfdTile, boolean canCounter) {
		int[] ret = new int[10];
		ret[0] = atk.getCurrentHPOfBodyPart(1);
		//Assume the attacker has the defender in range
		ret[1] = atk.attackStrength() - dfd.defense(atk.isUsingMagic(), bodyPart);
		ret[2] = atk.accuracy() - dfd.avoidance(bodyPart);
		if (!(dfd.canFly())) {
			ret[2] -= dfdTile.avoidanceBonus();
		}
		ret[2] = Math.max(0, ret[2]);
		ret[3] = Math.max(0, atk.criticalHitRate() - dfd.criticalHitAvoid());
		if (atk.attackSpeed() - dfd.attackSpeed() >= DOUBLE_ATTACK_CONSTANT) {
			ret[4] = 2;
		} else {
			ret[4] = 1;
		}
		//Counter attack
		ret[5] = dfd.getCurrentHPOfBodyPart(bodyPart); //Always counter attack torso
		if (canCounter) {
			ret[6] = Math.max(0, dfd.attackStrength() - atk.defense(atk.isUsingMagic(), 1));
			ret[7] = dfd.accuracy() - atk.avoidance(1);
			if (!(atk.canFly())) {
				ret[7] -= atkTile.avoidanceBonus();
			}
			ret[7] = Math.max(0, ret[7]);
			ret[8] = Math.max(0, dfd.criticalHitRate() - atk.criticalHitAvoid());
			if (dfd.attackSpeed() - atk.attackSpeed() >= DOUBLE_ATTACK_CONSTANT) {
				ret[9] = 2;
			} else {
				ret[9] = 1;
			}
		} else {
			ret[6] = -1;
			ret[7] = -1;
			ret[8] = -1;
			ret[9] = -1;
		}
		
		return ret;
	}
	
	/**
	 * This method is used to perform parts of a combat sequence one at a time.
	 * The name does NOT mean that it is only for combat where the defender cannot retaliate
	 * @return 0 if attack missed, 1 if hit, 2 if crit
	 */
	private static int oneSidedStandardAttack(Unit atk, Unit dfd, int bodyPart,
			BattlegroundTile dfdTile) {
		//Store power in a variable so we only calculate it once
		//we still only calculate it once without doing this (if statements), but whatever
		int power = atk.attackStrength();
		int hitChance = atk.accuracy() - dfd.avoidance(bodyPart);
		if (!(dfd.canFly())) {
			hitChance -= dfdTile.avoidanceBonus();
		}
		int crit = atk.criticalHitRate() - dfd.criticalHitAvoid();
		int num = RNGStuff.random0To99();
		if (hitChance > num) {
			if (atk instanceof Equippable) {
				((Equippable)atk).useWeapon(true);
			}
			if (crit > num) {
				dfd.takeCriticalDamage(atk.isUsingMagic(), bodyPart, power);
				return 2;
			} else {
				dfd.takeDamage(atk.isUsingMagic(), bodyPart, power);
				return 1;
			}
		} else {
			if (atk instanceof Equippable) {
				((Equippable)atk).useWeapon(false); //Long-range weapons still lose durability even if they miss
			}
		}
		return 0;
	}
	
	/**
	 * Complete a battle sequence, then return a report of it so the front-end can
	 * make an animation
	 * We perform the battle before showing the animation so that the player cannot stop
	 * the game in the middle of it to prevent an unfavorable outcome
	 * @param atk
	 * @param dfd
	 * @param bodyPart
	 * @param atkTile
	 * @param dfdTile
	 * @return a report with the following format
	 * [0] = 0 if attacker missed, 1 if attacker hit, 2 if attacker crit
	 * [1] = defender's HP before first attack
	 * [2] = defender's HP after first attack
	 * [3] = -1 if defender could not counter attack, 0 if defender missed, 
	 *       1 if defender hit, 2 if defender crit
	 * [4] = attacker's HP before counter attack
	 * [5] = attacker's HP after counter attack
	 * [6] = 0 if there is no double attack, 1 if attacker will double, 2 if defender will double
	 * [7] = 0 if doubler missed, 1 if doubler hit, 2 if doubler crit
	 * [8] = slower's HP before double
	 * [9] = slower's HP after double
	 */
	private static int[] standardBattleSequence(Unit atk, Unit dfd, int bodyPart,
			BattlegroundTile atkTile, BattlegroundTile dfdTile, boolean canCounter) {
		int[] ret = new int[10];
		ret[1] = dfd.getCurrentHPOfBodyPart(bodyPart);
		ret[0] = oneSidedStandardAttack(atk, dfd, bodyPart, dfdTile);
		ret[2] = dfd.getCurrentHPOfBodyPart(bodyPart);
		if (dfd.isAlive() && canCounter) {
			ret[4] = atk.getCurrentHPOfBodyPart(1); //Attack torso
			ret[3] = oneSidedStandardAttack(dfd, atk, 1, atkTile);
			ret[5] = atk.getCurrentHPOfBodyPart(1);
		} else {
			ret[3] = -1;
			return ret;
		}
		if (atk.isAlive() && dfd.isAlive()) {
			int dub = atk.attackSpeed() - dfd.attackSpeed();
			if (dub >= DOUBLE_ATTACK_CONSTANT) {
				ret[6] = 1;
				ret[8] = dfd.getCurrentHPOfBodyPart(bodyPart);
				ret[7] = oneSidedStandardAttack(atk, dfd, bodyPart, dfdTile);
				ret[9] = dfd.getCurrentHPOfBodyPart(bodyPart);
				return ret;
			} else if (ret[3] != -1 && dub <= -(DOUBLE_ATTACK_CONSTANT)) {
				ret[6] = 2;
				ret[8] = atk.getCurrentHPOfBodyPart(1);
				ret[7] = oneSidedStandardAttack(dfd, atk, 1, atkTile);
				ret[9] = atk.getCurrentHPOfBodyPart(1);
				return ret;
			}
		}
		ret[6] = 0;
		return ret;
	}
	
	public static StandardBattleReport performBattleSequence(Unit atk, Unit dfd, int bodyPart,
			BattlegroundTile atkTile, BattlegroundTile dfdTile, BattleGround battle, boolean canCounter) {
		int[] details = standardBattleSequence(atk, dfd, bodyPart, atkTile, dfdTile, canCounter);
		StandardBattleReport ret = new StandardBattleReport(details);
		if (!atk.isAlive()) {
			registerStandardBattleDeath(atk, dfd, battle, null, atkTile, 1, ret);
			ret.setDfdGainedLevel(dfd.gainExperience(atk.effectiveLevel() + 50));
		} else if (!dfd.isAlive()) {
			registerStandardBattleDeath(dfd, atk, battle, null, dfdTile, bodyPart, ret);
			ret.setAtkGainedLevel(atk.gainExperience(dfd.effectiveLevel() + 50));
		} else {
			if (dfd.getCurrentHPOfBodyPart(bodyPart) <= 0) {
				ret.setAtkGainedLevel(atk.gainExperience(dfd.effectiveLevel() + 30));
			} else {
				ret.setAtkGainedLevel(atk.gainExperience(dfd.effectiveLevel() + 10));
			}
			ret.setDfdGainedLevel(dfd.gainExperience(atk.effectiveLevel() + 10));
		}
		return ret;
	}
	
	private static void registerStandardBattleDeath(Unit victim, Unit killer,
			BattleGround battle, StationaryWeapon weapon, BattlegroundTile location,
			int bodyPart, StandardBattleReport report) {
		killer.incrementKills();
		victim.wasKilledBy(killer);
		//Add death quotes first, because the death sequence removes the unit from
		//their unitGroup and support partner
		if (victim.getAffiliation() == GeneralGameplayManager.getPlayerNation()
				|| victim.isImportant()) {
			String notification =
					DialogueManager.standardKillNotification(victim, killer, weapon, battle);
			report.setNotification(notification);
			if (victim.isImportant()) {
				victim.getAffiliation().addHistoricalRecord(
						HistoricalRecord.standardBattleDeath(notification));
			}
		}
		if (victim instanceof Human) {
			Human h = (Human) victim;
			report.setDeadSpeaker(h);
			report.setDeathQuote(DialogueManager.getDeathQuote(h, killer));
			report.setDeadSpeakerSupportPartner(h.getSupportPartner());
			report.setDespairQuote(DialogueManager.getSupportPartnerDespairQuote(h, killer));
		}
		victim.deathSequence();
		battle.removeOccupant(victim);
		report.setShouldEndBattle(!(battle.isActive()));
	}
	
	public static int[] stationaryAttacksInfantryBattleForecast(Unit atk, Unit dfd, int bodyPart,
			StationaryWeapon weapon, BattlegroundTile dfdTile) {
		int[] ret = new int[5];
		ret[0] = atk.getCurrentHPOfBodyPart(1);
		ret[1] = weapon.getMight() - dfd.defense(weapon.isMagic(), bodyPart);
		ret[2] = atk.getBaseAccuracy() + weapon.getHit() - dfd.avoidance(bodyPart);
		if (!(dfd.canFly())) {
			ret[2] -= dfdTile.avoidanceBonus();
		}
		ret[3] = atk.getBaseCrit() + weapon.getCrit() - dfd.criticalHitAvoid();
		//Can never double attack with a stationary weapon, as they need to be reloaded
		ret[4] = dfd.getCurrentHPOfBodyPart(bodyPart);
		return ret;
	}
	
	/**
	 * Calculates a stationary weapon's attack against infantry
	 * @return 0 if missed, 1 if hit, 2 if crit
	 */
	private static int stationaryAttackAgainstInfantry(Unit atk, Unit dfd, int bodyPart,
			StationaryWeapon weapon, BattlegroundTile dfdTile) {
		int hitChance = atk.getBaseAccuracy() + weapon.getHit() - dfd.avoidance(bodyPart);
		if (!(dfd.canFly())) {
			hitChance -= dfdTile.avoidanceBonus();
		}
		int crit = atk.getBaseCrit() + weapon.getCrit() - dfd.criticalHitAvoid();
		weapon.use(); //Use durability if ut hits or misses
		int num = RNGStuff.random0To99();
		if (hitChance > num) {
			if (crit > num) {
				dfd.takeCriticalDamage(atk.isUsingMagic(), bodyPart, weapon.getMight());
				return 2;
			} else {
				dfd.takeDamage(atk.isUsingMagic(), bodyPart, weapon.getMight());
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * Complete a battle sequence, then return a report of it so the front-end can
	 * make an animation
	 * We perform the battle before showing the animation so that the player cannot stop
	 * the game in the middle of it to prevent an unfavorable outcome
	 * @param atk
	 * @param dfd
	 * @param bodyPart
	 * @param weapon
	 * @param dfdTile
	 * @return a report with the following format
	 * [0] = 0 if attacker missed, 1 if attacker hit, 2 if attacker crit
	 * [1] = defender's HP before attack
	 * [2] = defender's HP after attack
	 * [3] = -2 to show that the attack type is uncounterable
	 */
	private static int[] stationaryAttacksInfantrySequence(Unit atk, Unit dfd, int bodyPart,
			StationaryWeapon weapon, BattlegroundTile dfdTile) {
		int[] ret = new int[3];
		ret[1] = dfd.getCurrentHPOfBodyPart(bodyPart);
		ret[0] = stationaryAttackAgainstInfantry(atk, dfd, bodyPart, weapon, dfdTile);
		ret[2] = dfd.getCurrentHPOfBodyPart(bodyPart);
		ret[3] = -2;
		return ret;
	}
	
	public static StandardBattleReport performStationaryAgainstInfantrySequence(
			Unit atk, Unit dfd, int bodyPart,
			StationaryWeapon weapon, BattlegroundTile dfdTile, BattleGround battle) {
		int[] details = stationaryAttacksInfantrySequence(atk, dfd, bodyPart, weapon, dfdTile);
		StandardBattleReport ret = new StandardBattleReport(details);
		if (!dfd.isAlive()) {
			registerStandardBattleDeath(dfd, atk, battle, weapon, dfdTile, bodyPart, ret);
			ret.setAtkGainedLevel(atk.gainExperience(dfd.effectiveLevel() + 30));
		} else {
			ret.setAtkGainedLevel(atk.gainExperience(dfd.effectiveLevel() + 10));
			ret.setDfdGainedLevel(dfd.gainExperience(atk.effectiveLevel() + 10));
		}
		return ret;

	}
		
	public static int[] infantryAttacksStationaryBattleForecast() {
		//TODO
		return null;
	}
	
	public static int[] stationaryAttacksShipBattleForecast(Unit atk, Unit dfd, int shipPart,
			StationaryWeapon weapon, Ship dfdShip) {
		int[] ret = new int[7];
		ret[0] = atk.getCurrentHPOfBodyPart(1);
		ret[1] = weapon.getMight() - dfdShip.getDefense(weapon.isMagic());
		int baseAcc = atk.getBaseAccuracy() + weapon.getHit();
		//Calculate avoidance for ship part and unit
		//Ship part's avoidance is 0 for everything except hull and helm
		//Hull and helm have more avoidance when there are more ship parts
		ret[2] = baseAcc - dfdShip.avoidance(shipPart);
		ret[3] = baseAcc - dfd.avoidance(1);
		//Critical hits only affect the unit manning the ship part
		ret[4] = atk.getBaseCrit() + weapon.getCrit() - dfd.criticalHitAvoid();
		//Can never double attack with a stationary weapon, as they need to be reloaded
		ret[5] = dfdShip.getShipPartHP(shipPart);
		ret[6] = dfd.getCurrentHPOfBodyPart(1);
		return ret;
	}
	
	private static int stationaryAttackAgainstShip(Unit atk, Unit dfd, int shipPart,
			StationaryWeapon weapon, Ship dfdShip) {
		int baseAcc = atk.getBaseAccuracy() + weapon.getHit();
		int accAgainstShip = baseAcc - dfdShip.avoidance(shipPart);
		int num = RNGStuff.random0To99();
		weapon.use();
		if (num < accAgainstShip) {
			dfdShip.takeDamage(weapon.isMagic(), shipPart, weapon.getMight());
			if (dfd != null) {
				int accAgainstUnit = baseAcc - dfd.avoidance(1);
				if (num < accAgainstUnit) {
					int critRate = atk.getBaseCrit() + weapon.getCrit() - dfd.criticalHitAvoid();
					if (num < critRate) {
						dfd.takeCriticalDamage(weapon.isMagic(), 1, weapon.getMight());
						return 2;
					}
					dfd.takeDamage(weapon.isMagic(), 1, weapon.getMight());
				}
			}
			return 1;
		}
		return 0;
	}
	
	/**
	 * Complete a battle sequence, then return a report of it so the front-end can
	 * make an animation
	 * We perform the battle before showing the animation so that the player cannot stop
	 * the game in the middle of it to prevent an unfavorable outcome
	 * @param atk
	 * @param dfd
	 * @param bodyPart
	 * @param weapon
	 * @param dfdTile
	 * @return a report with the following format
	 * [0] = 0 if attacker missed, 1 if attacker hit, 2 if attacker crit
	 * [1] = ship part's HP before attack
	 * [2] = defender unit's HP before attack
	 * [3] = ship part's HP after attack
	 * [4] = defender's HP after attack
	 * [5] = -2 to show that the attack type is uncounterable
	 */
	private static int[] stationaryAttacksShipSequence(Unit atk, Unit dfd, int shipPart,
			StationaryWeapon weapon, Ship dfdShip) {
		int[] ret = new int[6];
		ret[1] = dfdShip.getShipPartHP(shipPart);
		ret[2] = dfd.getCurrentHPOfBodyPart(1);
		ret[0] = stationaryAttackAgainstShip(atk, dfd, shipPart, weapon, dfdShip);
		ret[3] = dfdShip.getShipPartHP(shipPart);
		ret[2] = dfd.getCurrentHPOfBodyPart(1);
		ret[5] = -2;
		return ret;
	}
	
	public static ShipBattleReport performStationaryAgainstShip(Unit atk, Unit dfd, int shipPart,
			StationaryWeapon weapon, Ship dfdShip, WorldMapTile worldLocation,
			BattlegroundTile battleLocation, BattleGround battle) {
		//TODO
		int[] details = stationaryAttacksShipSequence(atk, dfd, shipPart, weapon, dfdShip);
		ShipBattleReport ret = new ShipBattleReport(details);
		if (!dfd.isAlive()) {
			registerShipBattleDeath(dfd, atk, battle, weapon, dfdShip, worldLocation,
					battleLocation, shipPart, ret);
			ret.setAtkGainedLevel(atk.gainExperience(dfd.effectiveLevel() + 30));
		} else {
			ret.setAtkGainedLevel(atk.gainExperience(dfd.effectiveLevel() + 10));
			ret.setDfdGainedLevel(dfd.gainExperience(atk.effectiveLevel() + 10));
		}
		if (!(dfdShip.partIsIntact(shipPart))) {
			if (dfdShip.isDestroyed()) {
				registerShipDestruction(atk, battle, weapon, dfdShip, shipPart,
						worldLocation, ret);
				ret.setAtkGainedLevel(atk.gainExperience(100));
			} else {
				ret.setAtkGainedLevel(atk.gainExperience(30));
			}
		}
		return ret;
	}

	private static void registerShipBattleDeath(Unit victim, Unit killer, BattleGround battle,
			StationaryWeapon weapon, Ship dfdShip, WorldMapTile worldLocation,
			BattlegroundTile battleLocation, int shipPart, ShipBattleReport report) {
		killer.incrementKills();
		victim.wasKilledBy(killer);
		if (battle != null) {
		//TODO put the new occupant in the battleground, because the tile is scrapped and
		//reloaded every time the battleground is accessed
		}
		if (battleLocation != null) {
			battleLocation.placeOccupant(victim.getPassenger());
		}
		//Add death quotes first, because the death sequence removes the unit from
		//their unitGroup and support partner
		if (victim.getAffiliation() == GeneralGameplayManager.getPlayerNation()
				|| victim.isImportant()) {
			String notification =
					DialogueManager.shipKillNotification(victim, killer, weapon, battle, dfdShip,
							worldLocation);
			report.setInitialDeathNotification(notification);
			if (victim.isImportant()) {
				victim.getAffiliation().addHistoricalRecord(
						HistoricalRecord.standardBattleDeath(notification));
			}
		}
		if (victim instanceof Human) {
			Human h = (Human) victim;
			report.setDeadSpeaker(h);
			report.setDeathQuote(DialogueManager.getDeathQuote(h, killer));
			report.setDeadSpeakerSupportPartner(h.getSupportPartner());
			report.setDespairQuote(DialogueManager.getSupportPartnerDespairQuote(h, killer));
		}
		victim.deathSequence();
		battle.removeOccupant(victim);
		report.setShouldEndBattle(!(battle.isActive()));
	}

	private static void registerShipDestruction(Unit killer,
			BattleGround battle, StationaryWeapon weapon,
			Ship dfdShip, int shipPart, WorldMapTile worldLocation, ShipBattleReport report) {
		List<Unit> drowned;
		if (dfdShip.getBattle() == null) {
			drowned = dfdShip.getPassengersAboard();
		} else {
			drowned = dfdShip.getAllPassengers();
			//TODO reload battleground without ship
		}
		killer.incrementKills(drowned.size());
		Map<Nation, Nation> nationsInvolved = new HashMap<>();
		for (int q = 0; q < drowned.size(); q++) {
			Unit u = drowned.get(q);
			u.wasKilledBy(killer);
			//TODO remove from BattleGround tile
//			battle.removeOccupant(u);
			u.kill(); //So that when the ship leaves the battleground, dead units are not returned to the barracks
			u.deathSequence();
			if (nationsInvolved.get(u.getAffiliation()) == null) {
				nationsInvolved.put(u.getAffiliation(), u.getAffiliation());
			}
		}
		//Add death notifications first, because the death sequence removes the unit from
		//their unitGroup and support partner
		if (dfdShip.hasPlayerNationUnit() || dfdShip.isImportant()) {
			String notification = DialogueManager.shipDestructionNotification(killer, weapon,
					battle, dfdShip, drowned, worldLocation);
			report.setShipDeathNotification(notification);
			if (dfdShip.isImportant()) {
				Set<Nation> nations = nationsInvolved.keySet();
				for (Nation n : nations) {
					n.addHistoricalRecord(
						HistoricalRecord.standardBattleDeath(notification));
				}
			}
		}
		dfdShip.destructionSequence();
		report.setShouldEndBattle(!(battle.isActive()));
	}
	
}
