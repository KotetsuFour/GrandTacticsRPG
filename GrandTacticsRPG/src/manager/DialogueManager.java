package manager;

import battle.BattleGround;
import data_structures.List;
import inventory.weapon.StationaryWeapon;
import location.WorldMapTile;
import ship.Ship;
import unit.Unit;
import unit.human.Demeanor;
import unit.human.Human;
import unit.human.Offspring;

public class DialogueManager {
	
	public static final int STRONG = 70;
	public static final int GOOD = 30;
	public static final int CASUAL = 0;
	public static final int BAD = -40;
	
	public static String getDeathQuote(Human victim, Unit killer) {
		if (victim.getCurrentHPOfBodyPart(0) > 0
				&& victim.getCurrentHPOfBodyPart(1) > -10) {
			
			if (victim.getSupportPartner() == killer) {
				
				Human partner = victim.getSupportPartner();
				Demeanor d = victim.getDemeanor();
				int relationship = victim.getRelationshipWithSupportPartner();
				
				if (victim.marriedToSupportPartner()) { //Romantic
					return String.format("%s, my darling... %s", 
							partner.getName(),
							d.disappointedExpression());
				} else if (relationship > STRONG) { //Strong
					return String.format("%s, my dear friend... %s", 
							partner.getName(),
							d.disappointedExpression());
				} else if (relationship > GOOD) { //Good
					return String.format("%s ...my friend... %s", 
							partner.getName(),
							d.ironicExpression());
				} else if (relationship > CASUAL) { //Casual
					return String.format("%s... you... %s", 
							partner.getName(),
							d.ironicExpression());
				} else if (relationship > BAD) { //Bad
					return String.format("%s... you git... %s", 
							partner.getName(),
							d.vengefulExpression());
				} else { //Horrible
					return String.format("%s... you rat!... %s", 
							partner.getName(),
							d.vengefulExpression());
				}
			} else {
				
				Demeanor d = victim.getDemeanor();
				
				if (victim.getMilitarism() >= 70
						&& victim.getMilitarism() >= victim.getAltruism()
						&& victim.getMilitarism() >= victim.getFamilism()
						&& victim.getMilitarism() >= victim.getNationalism()) {
					if (victim.getConfidence() >= 80) {
						return String.format("%s will remember me....", victim.getAffiliation().getName());
					} else if (victim.getConfidence() >= 60) {
						return "Such a... nice place... to die....";
					} else if (victim.getConfidence() >= 40) {
						return "You were... a worthy opponent....";
					} else if (victim.getConfidence() >= 20) {
						return "Was I... wrong...?";
					} else {
						return String.format("No... I want to go... back to %s....", victim.getHome().getName());
					}
				} else if (victim.getAltruism() >= 70
						&& victim.getAltruism() >= victim.getFamilism()
						&& victim.getAltruism() >= victim.getNationalism()) {
					//TODO
				} else if (victim.getFamilism() >= 70
						&& victim.getFamilism() >= victim.getNationalism()) {
					if (victim instanceof Offspring) {
						Offspring off = (Offspring)victim;
						if (off.getMother().isAlive() && off.getFather().isAlive()) {
							return String.format("Mother, Father, %s", d.aliveParent());
						} else if (off.getMother().isAlive()) {
							int relationship = off.getRelationshipWithMother();
							if (relationship > CASUAL) {
								return String.format("Mother, %s", d.aliveParent());
							} else {
								return String.format("Father, %s", d.deadParent());
							}
						} else if (off.getFather().isAlive()) {
							int relationship = off.getRelationshipWithFather();
							if (relationship > CASUAL) {
								return String.format("Father, %s", d.aliveParent());
							} else {
								return String.format("Mother, %s", d.deadParent());
							}
						} else {
							return String.format("Mother, Father, %s", d.deadParent());
						}
					} else if (victim.getSupportPartner() != null && victim.getRelationshipWithSupportPartner() > GOOD) {
						Human partner = victim.getSupportPartner();
						if (victim.mayReproduce()) {
							return String.format("%s %s", partner.getName(), d.romanticRelationship());
						} else if (victim.getRelationshipWithSupportPartner() > STRONG) {
							return String.format("%s %s", partner.getName(), d.strongRelationship());
						} else {
							return String.format("%s %s", partner.getName(), d.goodRelationship());
						}
					} else {
						if (victim.getConfidence() >= 80) {
							return String.format("You'll never lay a hand on %s...", victim.getHome().getName());
						} else if (victim.getConfidence() >= 60) {
							return String.format("The people of %s... will stop you...", victim.getHome().getName());
						} else if (victim.getConfidence() >= 40) {
							return String.format("I hope... %s... is safe...", victim.getHome().getName());
						} else if (victim.getConfidence() >= 20) {
							return String.format("You can't... %s... be safe...", victim.getHome().getName());
						} else {
							return String.format("Please... spare... %s...", victim.getHome().getName());
						}
					}
				} else if (victim.getNationalism() >= 70) {
					if (victim.getConfidence() >= 80) {
						return String.format("For the glory of %s...", victim.getAffiliation().getName());
					} else if (victim.getConfidence() >= 60) {
						return String.format("Long live %s...", victim.getAffiliation().getName());
					} else if (victim.getConfidence() >= 40) {
						return String.format("You won't defeat %s...", victim.getAffiliation().getName());
					} else if (victim.getConfidence() >= 20) {
						return String.format("No.... %s mustn't lose....", victim.getAffiliation().getName());
					} else {
						return String.format("%s... I've failed you....", victim.getAffiliation().getName());
					}
				} else {
					if (victim.getConfidence() >= 80) {
						//TODO
					} else if (victim.getConfidence() >= 60) {
						//TODO
					} else if (victim.getConfidence() >= 40) {
						//TODO
					} else if (victim.getConfidence() >= 20) {
						//TODO
					} else {
						//TODO
					}
				}
				//TODO death quote based on personality
			}
		}
		//TODO
		return null;
	}

	public static String getSupportPartnerDespairQuote(Human victim, Unit killer) {
		if (victim.getSupportPartner() != null
				&& victim.getGroup() == victim.getSupportPartner().getGroup()) {
			
			Human partner = victim.getSupportPartner();
			Demeanor d = victim.getDemeanor();
			int relationship = victim.getRelationshipWithSupportPartner();
			
			if (partner == killer) {
				if (victim.marriedToSupportPartner()) { //Romantic
					return String.format("%s, my dear. I'll always love you.", 
							d.regretfulExpression());
				} else if (relationship > STRONG) { //Strong
					return String.format("%s, old friend. I will miss you.", 
							d.regretfulExpression());
				} else if (relationship > GOOD) { //Good
					return String.format("%s, my friend.", 
							d.shamefulExpression());
				} else if (relationship > CASUAL) { //Casual
					return String.format("%s.", 
							d.shamefulExpression());
				} else if (relationship > BAD) { //Bad
					return String.format("%s, %s!", 
							d.gladnessExpression(),
							partner.getName());
				} else { //Horrible
					return String.format("%s, you scum!", 
							d.gladnessExpression());
				}
			} else {
				//TODO death quote based on personality and relationship
			}
		}
		return null;
	}

	public static String standardKillNotification(Unit victim, Unit killer,
			StationaryWeapon weapon, BattleGround battle) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Tells the strength of the relationship between two (non-player) support partners
	 * @param a
	 * @param b
	 * @return
	 */
	public static String tellSupportRelationship(Human a, Human b) {
		return evaluateOneSidedRelationship(a, b, true)
				+ evaluateOneSidedRelationship(b, a, true);
	}
	public static String tellRelationshipWithPlayer(Human h) {
		Human p = GeneralGameplayManager.getPlayer();
		if (h.getSupportPartner() == p) {
			return evaluateOneSidedRelationship(h, p, true);
		}
		return evaluateOneSidedRelationship(h, p, false);
	}
	private static String evaluateOneSidedRelationship(Human a, Human b, boolean bIsPartner) {
		StringBuilder sb = new StringBuilder();
		int ar;
		if (bIsPartner) {
			ar = a.getRelationshipWithSupportPartner();
		} else {
			//b is Player
			ar = a.getRelationshipWithPlayer();
		}
		sb.append(a.getName());
		if (ar >= 90) {
			sb.append(" feels a very deep connection to ");
		} else if (ar >= 70) {
			sb.append(" is very fond of ");
		} else if (ar >= 50) {
			sb.append(" greatly admires ");
		} else if (ar >= 30) {
			sb.append(" is glad to be partnered with ");
		} else if (ar >= 10) {
			sb.append(" thinks well of ");
		} else if (ar >= 0) {
			sb.append(" feels alright about ");
		} else if (ar >= -10) {
			sb.append(" feels unsure about ");
		} else if (ar >= -30) {
			sb.append(" thinks poorly of ");
		} else if (ar >= -50) {
			sb.append(" wants to be separated from ");
		} else if (ar >= -70) {
			sb.append(" is disturbed by ");
		} else if (ar >= -90) {
			sb.append(" holds contempt for ");
		} else {
			sb.append(" deeply despises ");
		}
		sb.append(b.getName() + ".\n");
		return sb.toString();
	}

	public static String shipKillNotification(Unit victim, Unit killer, StationaryWeapon weapon, BattleGround battle,
			Ship dfdShip, WorldMapTile worldLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String shipDestructionNotification(Unit killer, StationaryWeapon weapon,
			BattleGround battle,
			Ship victimShip, List<Unit> victims, WorldMapTile worldLocation) {
		StringBuilder sb = new StringBuilder(
				String.format("The ship \"%s\" was sunk by %s, using %s.\n"
						+ "The following combatants drowned as the ship sank:\n",
						victimShip.getName(),
						killer.getDisplayName(),
						weapon.getName()));
		for (int q = 0; q < victims.size(); q++) {
			sb.append(victims.get(q).getDisplayName());
			sb.append("\n");
		}
		//TODO maybe add battle description of some kind (ship was sunk during the Battle of/at ___)
		return sb.toString();
	}
}
