package util;

import java.util.Random;

import data_structures.List;
import javafx.scene.paint.Color;
import util.color_set.ColorSet;
import util.naming_conventions.BoundedNamingConvention;
import util.naming_conventions.NamingConvention;
import util.naming_conventions.PrimitiveNamingConvention;
import util.naming_conventions.ReverseSyllabicNamingConvention;
import util.naming_conventions.SyllabicNamingConvention;

public class RNGStuff {

	public static Random rng = new Random();
	
	public static final NamingConvention[] LANGUAGES = {new PrimitiveNamingConvention(), new BoundedNamingConvention(),
			new SyllabicNamingConvention(), new ReverseSyllabicNamingConvention()};
	public static NamingConvention[] LANGUAGES_IN_USE = {new PrimitiveNamingConvention(), new BoundedNamingConvention(),
			new SyllabicNamingConvention(), new ReverseSyllabicNamingConvention()};
	
	public static final ColorSet[] SKIN_COLORS = {
			new ColorSet("Realistic", new Color[] {
					new Color(243.0/255, 213.0/255, 208.0/255, 1),
					new Color(218.0/255, 185.0/255, 176.0/255, 1),
					new Color(233.0/255, 185.0/255, 149.0/255, 1),
					new Color(225.0/255, 158.0/255, 149.0/255, 1),
					new Color(242.0/255, 170.0/255, 146.0/255, 1),
					new Color(205.0/255, 161.0/255, 132.0/255, 1),
					new Color(147.0/255, 97.0/255, 74.0/255, 1),
					new Color(117.0/255, 57.0/255, 21.0/255, 1),
			}),
			new ColorSet("Ants", new Color[] {
					new Color(193.0/255, 44.0/255, 25.0/255, 1),
					new Color(59.0/255, 51.0/255, 63.0/255, 1),
					new Color(209.0/255, 143.0/255, 42.0/255, 1),
					new Color(122.0/255, 159.0/255, 84.0/255, 1),
					new Color(166.0/255, 94.0/255, 64.0/255, 1),
					new Color(33.0/255, 31.0/255, 30.0/255, 1),
					new Color(209.0/255, 108.0/255, 70.0/255, 1),
					new Color(206.0/255, 109.0/255, 2.0/255, 1),
			}),
			new ColorSet("Gems", new Color[] {
					new Color(239.0/255,175.0/255,214.0/255,1),
					new Color(15.0/255,196.0/255,244.0/255,1),
					new Color(243.0/255,238.0/255,97.0/255,1),
					new Color(245.0/255, 245.0/255, 245.0/255, 1),
					new Color(184.0/255,123.0/255,200.0/255, 1),
					new Color(172.0/255,73.0/255,91.0/255, 1),
					new Color(138.0/255,231.0/255,102.0/255, 1),
					new Color(140.0/255,163.0/255,201.0/255, 1),
			}),
			new ColorSet("Rainbow", new Color[] {
					new Color(247.0/255, 0, 0, 1),
					new Color(247.0/255, 160.0/255, 0, 1),
					new Color(247.0/255, 247.0/255, 0, 1),
					new Color(0, 124.0/255, 0, 1),
					new Color(0, 0, 247.0/255, 1),
					new Color(73.0/255, 0, 126.0/255, 1),
					new Color(231.0/255, 126.0/255, 231.0/255, 1),
					new Color(164.0/255, 164.0/255, 164.0/255, 1),
			})
	};
	public static final ColorSet[] HAIR_COLORS = {
			new ColorSet("Realistic", new Color[] {
					new Color(8.0/255,8.0/255,6.0/255, 1),
					new Color(107.0/255,78.0/255,64.0/255, 1),
					new Color(166.0/255,132.0/255,105.0/255, 1),
					new Color(164.0/255,108.0/255,71.0/255, 1),
					new Color(184.0/255,65.0/255,49.0/255, 1),
					new Color(254.0/255,246.0/255,225.0/255, 1),
					new Color(202.0/255,193.0/255,178.0/255, 1),
					new Color(202.0/255,164.0/255,120.0/255, 1)
			}),
			new ColorSet("Anime", new Color[] {
					new Color(48.0/255,104.0/255,216.0/255, 1),
					new Color(216.0/255,232.0/255,232.0/255, 1),
					new Color(200.0/255,24.0/255,16.0/255, 1),
					new Color(32.0/255,160.0/255,16.0/255, 1),
					new Color(248.0/255,192.0/255,24.0/255, 1),
					new Color(129.0/255,70.0/255,40.0/255, 1),
					new Color(248.0/255,96.0/255,144.0/255, 1),
					new Color(8.0/255,8.0/255,6.0/255, 1),
			}),
			new ColorSet("Grey-Scale", new Color[] {
					new Color(0, 0, 0, 1),
					new Color(36.0/255, 36.0/255, 36.0/255, 1),
					new Color(72.0/255, 72.0/255, 72.0/255, 1),
					new Color(108.0/255, 108.0/255, 108.0/255, 1),
					new Color(144.0/255, 144.0/255, 144.0/255, 1),
					new Color(180.0/255, 180.0/255, 180.0/255, 1),
					new Color(216.0/255, 216.0/255, 216.0/255, 1),
					new Color(1, 1, 1, 1),
			}),
			new ColorSet("Grass And Grapes", new Color[] {
					new Color(3.0/255,55.0/255,7.0/255, 1),
					new Color(196.0/255,173.0/255,120.0/255, 1),
					new Color(85.0/255,67.0/255,52.0/255, 1),
					new Color(87.0/255,20.0/255,72.0/255, 1),
					new Color(196.0/255,211.0/255,23.0/255, 1),
					new Color(152.0/255,39.0/255,39.0/255, 1),
					new Color(82.0/255,114.0/255,165.0/255, 1),
					new Color(97.0/255,153.0/255,55.0/255, 1),
			})
	};
	public static final ColorSet[] EYE_COLORS = {
			new ColorSet("Realistic", new Color[] {
					new Color(78.0/255,96.0/255,163.0/255, 1),
					new Color(176.0/255,185.0/255,217.0/255, 1),
					new Color(62.0/255,68.0/255,66.0/255, 1),
					new Color(102.0/255,114.0/255,78.0/255, 1),
					new Color(123.0/255,92.0/255,51.0/255, 1),
					new Color(104.0/255,23.0/255,17.0/255, 1),
					new Color(77.0/255,54.0/255,35.0/255, 1),
					new Color(159.0/255,174.0/255,112.0/255, 1),
			}),
			new ColorSet("Human Rare", new Color[] {
					new Color(221.0/255,179.0/255,50.0/255, 1),
					new Color(198.0/255,193.0/255,191.0/255, 1),
					new Color(11.0/255,11.0/255,20.0/255, 1),
					new Color(155.0/255,29.0/255,27.0/255, 1),
					new Color(114.0/255,79.0/255,124.0/255, 1),
					new Color(0,240.0/255,241.0/255, 1),
					new Color(234.0/255,2.0/255,245.0/255, 1),
					new Color(253.0/255,253.0/255,253.0/255, 1),
			}),
			new ColorSet("Grey-Scale", new Color[] {
					new Color(0, 0, 0, 1),
					new Color(36.0/255, 36.0/255, 36.0/255, 1),
					new Color(72.0/255, 72.0/255, 72.0/255, 1),
					new Color(108.0/255, 108.0/255, 108.0/255, 1),
					new Color(144.0/255, 144.0/255, 144.0/255, 1),
					new Color(180.0/255, 180.0/255, 180.0/255, 1),
					new Color(216.0/255, 216.0/255, 216.0/255, 1),
					new Color(1, 1, 1, 1),
			}),
			new ColorSet("Rainbow", new Color[] {
					new Color(247.0/255,0,0, 1),
					new Color(247.0/255,160.0/255,0, 1),
					new Color(247.0/255,247.0/255,0, 1),
					new Color(0,124.0/255,0, 1),
					new Color(0,0,247.0/255, 1),
					new Color(73.0/255,0,126.0/255, 1),
					new Color(231.0/255,126.0/255,231.0/255, 1),
					new Color(164.0/255,164.0/255,164.0/255, 1),
			})
	};
	
	public static ColorSet SKIN_COLORS_IN_USE = new ColorSet("Skin Colors", null);
	public static ColorSet HAIR_COLORS_IN_USE = new ColorSet("Hair Colors", null);
	public static ColorSet EYE_COLORS_IN_USE = new ColorSet("Eye Colors", null);
	
	/**
	 * Used for percentage chance to perform an action or increment a stat
	 * If whatever value > this return value, perform the action
	 * @return random value
	 */
	public static int random0To99() {
		return rng.nextInt(100);
	}
	
	/**
	 * Used for generating stats ranging from 0 to 100
	 * @return random value
	 */
	public static int random0To100() {
		return rng.nextInt(101);
	}
	
	public static int nextInt(int range) {
		return rng.nextInt(range);
	}
	
	public static boolean nextBoolean() {
		return rng.nextBoolean();
	}
	
	/**
	 * Gives a randomly generated name for a character
	 * @param style represents the kind of name generation, meant to individualize different cultures
	 * @return randomly generated name
	 */
	public static String randomName(int style) {
		return LANGUAGES_IN_USE[style].getName();
	}
	
	public static int numberOfLanguages() {
		return LANGUAGES_IN_USE.length;
	}
	
	public static String newLocationName(int language) {
		//TODO maybe change this to differentiate names of people and nations
		return randomName(language);
	}
	
	public static void useLanguage(List<Integer> langs) {
		LANGUAGES_IN_USE = new NamingConvention[langs.size()];
		for (int q = 0; q < LANGUAGES_IN_USE.length; q++) {
			LANGUAGES_IN_USE[q] = LANGUAGES[langs.get(q)];
		}
		//TODO save this in database
	}
	
	public static void useColors(List<Integer> hair, List<Integer> skin, List<Integer> eye) {
		for (int q = 0; q < hair.size(); q++) {
			HAIR_COLORS_IN_USE.addColors(HAIR_COLORS[hair.get(q)]);
		}
		for (int q = 0; q < skin.size(); q++) {
			SKIN_COLORS_IN_USE.addColors(SKIN_COLORS[skin.get(q)]);
		}
		for (int q = 0; q < eye.size(); q++) {
			EYE_COLORS_IN_USE.addColors(EYE_COLORS[eye.get(q)]);
		}
		//TODO save in database
	}
	
	public static int getRandomHairColor() {
		return nextInt(HAIR_COLORS_IN_USE.size());
	}
	public static int getRandomSkinColor() {
		return nextInt(SKIN_COLORS_IN_USE.size());
	}
	public static int getRandomEyeColor() {
		return nextInt(EYE_COLORS_IN_USE.size());
	}
}
