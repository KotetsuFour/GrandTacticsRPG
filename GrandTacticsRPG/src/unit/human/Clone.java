package unit.human;

import java.util.Random;

import affiliation.CityState;
import inventory.weapon.Weapon;
import reference.ArtificialHumanTemplate;
import unit.Equippable;
import util.RNGStuff;

public class Clone extends Human {
	
	public static final int IDEAL_CLONING_PROFICIENCY = 300;

	private Clone(String name, boolean gender, int[] maxHPs, int magic, int skill, int reflex, int awareness, int resistance,
			int movement, int leadership, int[] maxHPGrowths, int magGrowth, int sklGrowth, int rfxGrowth,
			int awrGrowth, int resGrowth, int age, int[] personality, int[] appearance, Interest[] interests,
			Interest[] disinterests, Demeanor demeanor, CombatTrait valuedTrait, CityState home) {
		super(name, gender, maxHPs, magic, skill, reflex, awareness, resistance, movement, leadership, maxHPGrowths,
				magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth, age, personality, appearance,
				interests, disinterests, demeanor, valuedTrait, home);
	}
	
	/**
	 * Creates and returns a new human clone
	 * @param x is the human reference (a reference made from the human, not the actual human)
	 * @param y is the unit that is performing the cloning process
	 * @param loc is the city-state where the cloning is taking place
	 * @return
	 */
	public static Clone cloneOfXProducedByY(ArtificialHumanTemplate x, Equippable y, CityState loc) {
		Random rng = RNGStuff.rng;
		float percentageProficiency =
				(float)((0.0 + y.proficiencyWith(Weapon.DARK)) / (0.0 + IDEAL_CLONING_PROFICIENCY));
		percentageProficiency = (float)Math.min(1.0, (double)percentageProficiency);
		boolean fluke = (rng.nextInt(1000) == 0);
		int[] personalValues = new int[6];
		Interest[] interests = new Interest[3];
		Interest[] disinterests = new Interest[3];
		generatePersonality(personalValues, interests, disinterests, loc.getValues());
		Demeanor[] types = Demeanor.values();
		Demeanor demeanor = types[rng.nextInt(types.length)];
		if (demeanor.getRarity() > 0) {
			demeanor = types[rng.nextInt(types.length)];
		}
		CombatTrait[] traits = CombatTrait.values();
		CombatTrait valued = traits[rng.nextInt(traits.length)];
		int[] appearance = cloneAppearance(x.getAppearance(), percentageProficiency, fluke);
		int[] maxHPs = cloneMaxHPs(x.getBodyPartsMaximumHP(), percentageProficiency, fluke);
		int[] maxHPsGrowths = cloneMaxHPGrowths(x.getBodyPartsMaximumHPGrowth(), percentageProficiency, fluke);
		boolean gend = x.getGender();
		
		//Clone stats and growths imperfectly
		int mag = x.getMagic();
		int skl = x.getSkill();
		int rfx = x.getReflex();
		int awr = x.getAwareness();
		int res = x.getResistance();
		int magGrowth = x.getMagicGrowth();
		int sklGrowth = x.getSkillGrowth();
		int rfxGrowth = x.getReflexGrowth();
		int awrGrowth = x.getAwarenessGrowth();
		int resGrowth = x.getResistanceGrowth();
		
		int potMag = Math.round(mag * percentageProficiency);
		int potSkl = Math.round(skl * percentageProficiency);
		int potRfx = Math.round(rfx * percentageProficiency);
		int potAwr = Math.round(awr * percentageProficiency);
		int potRes = Math.round(res * percentageProficiency);
		int potMagGrowth = Math.round(magGrowth * percentageProficiency);
		int potSklGrowth = Math.round(sklGrowth * percentageProficiency);
		int potRfxGrowth = Math.round(rfxGrowth * percentageProficiency);
		int potAwrGrowth = Math.round(awrGrowth * percentageProficiency);
		int potResGrowth = Math.round(resGrowth * percentageProficiency);
		if (fluke) {
			//If there is a fluke, lack of proficiency means better clones and better
			//proficiency means worse clones
			if (percentageProficiency > 1.0) {
				mag = potMag + rng.nextInt(mag - potMag);
				skl = potSkl + rng.nextInt(skl - potSkl);
				rfx = potRfx + rng.nextInt(rfx - potRfx);
				awr = potAwr + rng.nextInt(awr - potAwr);
				res = potRes + rng.nextInt(res - potRes);
				magGrowth = potMagGrowth + rng.nextInt(magGrowth - potMagGrowth);
				sklGrowth = potSklGrowth + rng.nextInt(sklGrowth - potSklGrowth);
				rfxGrowth = potRfxGrowth + rng.nextInt(rfxGrowth - potRfxGrowth);
				awrGrowth = potAwrGrowth + rng.nextInt(awrGrowth - potAwrGrowth);
				resGrowth = potResGrowth + rng.nextInt(resGrowth - potResGrowth);
			} else if (percentageProficiency < 1.0) {
				mag += rng.nextInt(potMag - mag);
				skl += rng.nextInt(potSkl - skl);
				rfx += rng.nextInt(potRfx - rfx);
				awr += rng.nextInt(potAwr - awr);
				res += rng.nextInt(potRes - res);
				magGrowth += rng.nextInt(potMagGrowth - magGrowth);
				sklGrowth += rng.nextInt(potSklGrowth - sklGrowth);
				rfxGrowth += rng.nextInt(potRfxGrowth - rfxGrowth);
				awrGrowth += rng.nextInt(potAwrGrowth - awrGrowth);
				resGrowth += rng.nextInt(potResGrowth - resGrowth);
			}
		} else {
			//Otherwise, lack of proficiency means worse clones and worse proficiency means
			//better clones
			if (percentageProficiency > 1.0) {
				mag += rng.nextInt(potMag - mag);
				skl += rng.nextInt(potSkl - skl);
				rfx += rng.nextInt(potRfx - rfx);
				awr += rng.nextInt(potAwr - awr);
				res += rng.nextInt(potRes - res);
				magGrowth += rng.nextInt(potMagGrowth - magGrowth);
				sklGrowth += rng.nextInt(potSklGrowth - sklGrowth);
				rfxGrowth += rng.nextInt(potRfxGrowth - rfxGrowth);
				awrGrowth += rng.nextInt(potAwrGrowth - awrGrowth);
				resGrowth += rng.nextInt(potResGrowth - resGrowth);
			} else if (percentageProficiency < 1.0) {
				mag = potMag + rng.nextInt(mag - potMag);
				skl = potSkl + rng.nextInt(skl - potSkl);
				rfx = potRfx + rng.nextInt(rfx - potRfx);
				awr = potAwr + rng.nextInt(awr - potAwr);
				res = potRes + rng.nextInt(res - potRes);
				magGrowth = potMagGrowth + rng.nextInt(magGrowth - potMagGrowth);
				sklGrowth = potSklGrowth + rng.nextInt(sklGrowth - potSklGrowth);
				rfxGrowth = potRfxGrowth + rng.nextInt(rfxGrowth - potRfxGrowth);
				awrGrowth = potAwrGrowth + rng.nextInt(awrGrowth - potAwrGrowth);
				resGrowth = potResGrowth + rng.nextInt(resGrowth - potResGrowth);
			}
		}
		
		int mov = x.getMovement();
		int ldr = rng.nextInt(6);
		if (ldr > 3 && RNGStuff.nextBoolean()) {
			ldr = rng.nextInt(2); //Can't have too many good leaders
		}
		String genName = x.getSpecificTemplateIndex() + "-" + x.getAndIncrementNumCopiesMade();
		int age = 18;

		return new Clone(genName, gend, maxHPs, mag, skl, rfx, awr, res, mov, ldr,
				maxHPsGrowths, magGrowth, sklGrowth, rfxGrowth, awrGrowth, resGrowth,
				age, personalValues, appearance, interests, disinterests, demeanor,
				valued, loc);
	}
	
	private static int[] cloneAppearance(int[] ref, float percentageProficiency, boolean fluke) {
		//TODO
		return null;
	}
	
	private static int[] cloneMaxHPs(int[] maxHPs, float percentageProficiency,
			boolean fluke) {
		int[] ret = new int[maxHPs.length];
		if (fluke) {
			for (int q = 0; q < ret.length; q++) {
				ret[q] = Math.round(maxHPs[q] / percentageProficiency);
			}
		} else {
			for (int q = 0; q < ret.length; q++) {
				ret[q] = Math.round(maxHPs[q] * percentageProficiency);
			}
		}
		return ret;
	}

	private static int[] cloneMaxHPGrowths(int[] hpGrowths, float percentageProficiency,
			boolean fluke) {
		int[] ret = new int[hpGrowths.length];
		if (fluke) {
			for (int q = 0; q < ret.length; q++) {
				ret[q] = Math.round(hpGrowths[q] / percentageProficiency);
			}
		} else {
			for (int q = 0; q < ret.length; q++) {
				ret[q] = Math.round(hpGrowths[q] * percentageProficiency);
			}
		}
		return ret;
	}

}
