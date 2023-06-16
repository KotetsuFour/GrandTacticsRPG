package unit.human;

public enum Demeanor {
	
	SERIOUS("Serious", 0, 4, 0,
			"Yes, I agree.",
			"I can see that.",
			"Hmm... I disagree."),
	RELAXED("Relaxed", 0, 0, 3,
			"Yeah, dude...",
			"Oh, cool...",
			"Eh, I dunno..."),
	DETERMINED("Determined", 0, 3, 1,
			"For sure!",
			"Ah, I see.",
			"I don't know about that."),
	ENTHUSIASTIC("Enthusiastic", 0, 1, 1,
			"",
			"",
			""),
	NERVOUS("Nervous", 0, 2, 1,
			"Oh, yeah. Heh.",
			"Ah.",
			"Oh..."),
	FRIENDLY("Friendly", 0, 1, 3,
			"Totally, dude!",
			"Huh, interesting way to think about it!",
			"Eh, we'll have to agree to disagree."),
	POLITE("Polite", 0, 0, 1,
			"",
			"",
			""),
	CURIOUS("Curious", 0, 4, 3,
			"Yes, I think so too!",
			"That's an interesting way to think about it.",
			"I'm having trouble understanding..."),
	DISMISSIVE("Dismissive", 0, 0, 0,
			"",
			"",
			""),
	CHARISMATIC("Charismatic", 0, 4, 1,
			"",
			"",
			""),
	ASSERTIVE("Assertive", 0, 3, 3,
			"",
			"",
			""),
	REFLECTIVE("Reflective", 0, 1, 0,
			"",
			"",
			""),
	ABSENT("Absent", 0, 1, 3,
			"Heh.",
			"Sure.",
			"Meh."),
	CREEPY("Creepy", 1, 3, 1,
			"You get me...",
			"Hehehe.",
			"..."),
	SNOBBISH("Snobbish", 1, 4, 2,
			"That's what I keep telling people!",
			"Yeah, I guess.",
			"Incorrect. Try again."),
	INTIMIDATING("Intimidating", 1, 3, 2,
			"Indeed.",
			"Hmm...",
			"You... wanna run that by me again?")
	;
	
	public static final int BROW_BLANK = 0;
	public static final int BROW_HAPPY = 1;
	public static final int BROW_AFRAID = 2;
	public static final int BROW_ANGRY = 3;
	public static final int BROW_CONFUSED = 4;
	
	public static final int MOUTH_BLANK = 0;
	public static final int MOUTH_HAPPY = 1;
	public static final int MOUTH_UPSET = 2;
	public static final int MOUTH_SLANT = 3;
	

	
	private String displayName;
	private int rarity;
	private int browOrientation;
	private int mouthOrientation;
	
	private String agree;
	private String indifferent;
	private String disagree;
	
	//The following are portions of quotes, meant for constructing unique pieces of dialogue
	
	private String de; //Expression disappointment that loving support partner was killer
	private String ie; //Expression of irony that support partner was killer
	private String ve; //Expression of contempt towards support parter who was killer
	
	private String re; //Expression of regret for killing support partner
	private String se; //Expression of mild shame for killing support partner
	private String ge; //Expression of gladness after killing support partner
	
	private String dp; //Dying words to dead parent(s)
	private String ap; //Dying words to alive parent(s)
	
	private String fw;
	private String nw;
	private String hw;
	
	private String rr; //Dying words to support partner in romantic relationship
	private String sr; //Dying words to support partner in strong relationship
	private String gr; //Dying words to support partner in good relationship
	
	private Demeanor(String displayName, int rarity,
			int browOrientation, int mouthOrientation,
			String agree, String indifferent, String disagree) {
		this.displayName = displayName;
		this.rarity = rarity;
		this.browOrientation = browOrientation;
		this.mouthOrientation = mouthOrientation;
		this.agree = agree;
		this.indifferent = indifferent;
		this.disagree = disagree;
	}
	public String getDisplayName() {
		return displayName;
	}
	public int getRarity() {
		return rarity;
	}
	public int getBrowOrientation() {
		return browOrientation;
	}
	public int getMouthOrientation() {
		return mouthOrientation;
	}
	
	public String agreementQuote() {
		return agree;
	}
	public String indifferenceQuote() {
		return indifferent;
	}
	public String disagreementQuote() {
		return disagree;
	}
	
	public String disappointedExpression() {
		return de;
	}
	public String ironicExpression() {
		return ie;
	}
	public String vengefulExpression() {
		return ve;
	}
	
	public String regretfulExpression() {
		return re;
	}
	public String shamefulExpression() {
		return se;
	}
	public String gladnessExpression() {
		return ge;
	}
	
	public String aliveParent() {
		return ap;
	}
	public String deadParent() {
		return dp;
	}
	
	public String romanticRelationship() {
		return rr;
	}
	public String strongRelationship() {
		return sr;
	}
	public String goodRelationship() {
		return gr;
	}
	
}

