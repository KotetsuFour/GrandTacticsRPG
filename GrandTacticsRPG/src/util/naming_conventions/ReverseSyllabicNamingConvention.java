package util.naming_conventions;

import util.RNGStuff;

public class ReverseSyllabicNamingConvention implements NamingConvention {

	@Override
	public String getName() {
		int numSyls = RNGStuff.nextInt(4) + 1;
		StringBuilder name = new StringBuilder();
		if (RNGStuff.nextInt(15) == 0) {
			name.append(reverseSyllabicSyllable(true));
			if (numSyls > 1) {
				numSyls--;
			}
		}
		for (int q = 0; q < numSyls; q++) {
			name.append(reverseSyllabicSyllable(false));
		}
		name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
		return name.toString();
	}
	private String reverseSyllabicSyllable(boolean mustBeConsonant) {
		String[] cs = {"q", "w", "r", "t", "y", "p", "s", "d", "f", "g", "h", "j",
				"k", "l", "z", "x", "c", "v", "b", "n", "m", "ph", "ch", "sh", "th"};
		if (mustBeConsonant) {
			return cs[RNGStuff.nextInt(cs.length)];
		}
		String[] vs = {"e", "u", "i", "o", "a", "y", "aa", "ee"};
		return vs[RNGStuff.nextInt(vs.length)] + cs[RNGStuff.nextInt(cs.length)];
	}
	@Override
	public String titleOfNamingConvention() {
		return "Reverse Syllabic";
	}

}
