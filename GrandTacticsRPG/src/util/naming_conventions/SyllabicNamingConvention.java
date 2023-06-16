package util.naming_conventions;

import util.RNGStuff;

public class SyllabicNamingConvention implements NamingConvention {

	@Override
	public String getName() {
		int numSyls = RNGStuff.nextInt(4) + 1;
		StringBuilder name = new StringBuilder();
		if (RNGStuff.nextInt(15) == 0) {
			name.append(syllabicSyllable(true));
			if (numSyls > 1) {
				numSyls--;
			}
		}
		for (int q = 0; q < numSyls; q++) {
			name.append(syllabicSyllable(false));
		}
		name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
		return name.toString();
	}
	private String syllabicSyllable(boolean mustBeVowel) {
		String[] vs = {"e", "u", "i", "o", "a", "y", "aa", "ee"};
		if (mustBeVowel) {
			return vs[RNGStuff.nextInt(vs.length)];
		}
		String[] cs = {"q", "w", "r", "t", "y", "p", "s", "d", "f", "g", "h", "j",
				"k", "l", "z", "x", "c", "v", "b", "n", "m", "ph", "ch", "sh", "th"};
		return cs[RNGStuff.nextInt(cs.length)] + vs[RNGStuff.nextInt(vs.length)];
	}
	@Override
	public String titleOfNamingConvention() {
		return "Syllabic";
	}

}
