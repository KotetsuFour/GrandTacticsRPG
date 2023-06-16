package util.naming_conventions;

import util.RNGStuff;

public class PrimitiveNamingConvention implements NamingConvention {

	@Override
	public String getName() {
		int firstSyls = RNGStuff.nextInt(3) + 1;
		StringBuilder name = new StringBuilder();
		boolean addedConsonant = true;
		for (int q = 0; q < firstSyls; q++) {
			String c = basicNameConsonant(addedConsonant);
			if (c.contentEquals("")) {
				addedConsonant = false;
			} else {
				addedConsonant = true;
			}
			name.append(c);
			name.append(basicNameVowel());
			c = basicNameConsonant(addedConsonant);
			if (c.contentEquals("")) {
				addedConsonant = false;
			} else {
				addedConsonant = true;
			}
			name.append(c);
		}
		name.setCharAt(0, Character.toUpperCase(name.charAt(0)));
		return name.toString();
	}
	private String basicNameConsonant(boolean allowedToSkip) {
		if (!allowedToSkip || RNGStuff.nextBoolean()) {
			String[] cs = {"b", "c", "ch", "d", "f", "g", "h", "j", "k", "l", "ll", "m", "n", "p",
					"q", "r", "rr", "s", "sh", "t", "th", "v", "w", "wh", "x", "y", "z"};
			return cs[RNGStuff.nextInt(cs.length)];
		}
		return "";
	}
	
	private String basicNameVowel() {
		String[] vs = {"a", "e", "i", "o", "u", "ai", "ao", "au", "ea", "ee", "ei", "ia", "ie",
				"io", "oa", "oi", "oo", "ou", "ua", "ue", "ui", "uo"};
		return vs[RNGStuff.nextInt(vs.length)];
	}
	@Override
	public String titleOfNamingConvention() {
		return "Standard Unbounded";
	}

}
