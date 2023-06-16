package util.naming_conventions;

import util.RNGStuff;

public class BoundedNamingConvention implements NamingConvention {

	@Override
	public String getName() {
		int syls = RNGStuff.nextInt(3);
		StringBuilder name = new StringBuilder();
		if (RNGStuff.nextInt(15) != 0) {
			name.append(boundedBasicNameStartingConsonant());
			name.append(boundedBasicNameMiddlingVowel());
		} else {
			name.append(boundedBasicNameStartingVowel());
		}
		if (syls > 0) {
			if (syls == 2) { //If there is a middle part
				if (RNGStuff.nextBoolean()) {
					name.append(boundedBasicNameMiddlingConsonant());
				}
				name.append(boundedBasicNameMiddlingConsonant());
				name.append(boundedBasicNameMiddlingVowel());
			}
			//Ending syllable
			if (RNGStuff.nextBoolean()) {
				name.append(boundedBasicNameMiddlingConsonant());
			}
			name.append(boundedBasicNameMiddlingConsonant());
			if (RNGStuff.nextBoolean()) {
				name.append(boundedBasicNameMiddlingVowel());
				name.append(boundedBasicNameEndingConsonant());
			} else {
				name.append(boundedBasicNameEndingVowel());
			}
		}
		name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
		return name.toString();
	}
	private String boundedBasicNameStartingConsonant() {
		String[] cs = {"b", "c", "ch", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p",
				"ph", "q", "r", "s", "sh", "t", "th", "v", "w", "wh", "x", "y", "z"};
		return cs[RNGStuff.nextInt(cs.length)];
	}
	private String boundedBasicNameMiddlingConsonant() {
		String[] cs = {"b", "c", "ch", "d", "f", "g", "h", "j", "k", "l", "ll", "m", "n", "p",
				"ph", "q", "r", "rr", "s", "sh", "t", "th", "v", "w", "wh", "x", "y", "z"};
		return cs[RNGStuff.nextInt(cs.length)];
	}
	private String boundedBasicNameEndingConsonant() {
		String[] cs = {"b", "c", "ch", "d", "f", "g", "h", "j", "k", "l", "ll", "m", "n", "p",
				"ph", "q", "r", "rr", "s", "sh", "t", "th", "v", "w", "x", "y", "z"};
		return cs[RNGStuff.nextInt(cs.length)];
	}
	private String boundedBasicNameStartingVowel() {
		String[] vs = {"a", "e", "i", "o", "u", "ai", "ao", "au", "ea", "ee", "ei",
				"io", "oa", "ou"};
		return vs[RNGStuff.nextInt(vs.length)];
	}
	private String boundedBasicNameMiddlingVowel() {
		String[] vs = {"a", "e", "i", "o", "u", "ai", "ao", "au", "ea", "ee", "ei",
				"oa", "oi", "oo", "ou"};
		return vs[RNGStuff.nextInt(vs.length)];
	}
	private String boundedBasicNameEndingVowel() {
		String[] vs = {"a", "e", "i", "o", "u", "ai", "ao", "au", "ea", "ee", "ei", "ia", "ie",
				"io", "oa", "oi", "oo", "ou", "ua", "ue", "ui", "uo"};
		return vs[RNGStuff.nextInt(vs.length)];
	}
	@Override
	public String titleOfNamingConvention() {
		return "Standard Bounded";
	}

}
