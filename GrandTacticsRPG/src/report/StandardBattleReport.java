package report;

import unit.human.Human;

public class StandardBattleReport extends Report {

	private int[] details;
	private String notification;
	private Human deadSpeaker;
	private Human deadSpeakerSupportPartner;
	private String deathQuote;
	private String despairQuote;
	private boolean shouldEndBattle;
	private boolean atkGainedLevel;
	private boolean dfdGainedLevel;
	
	public StandardBattleReport(int[] details) {
		this.details = details;
	}

	public boolean shouldEndBattle() {
		return shouldEndBattle;
	}

	public void setShouldEndBattle(boolean shouldEndBattle) {
		this.shouldEndBattle = shouldEndBattle;
	}

	public int[] getDetails() {
		return details;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public String getDeathQuote() {
		return deathQuote;
	}

	public void setDeathQuote(String deathQuote) {
		this.deathQuote = deathQuote;
	}

	public String getDespairQuote() {
		return despairQuote;
	}

	public void setDespairQuote(String despairQuote) {
		this.despairQuote = despairQuote;
	}

	public boolean isAtkGainedLevel() {
		return atkGainedLevel;
	}

	public void setAtkGainedLevel(boolean atkGainedLevel) {
		this.atkGainedLevel = atkGainedLevel;
	}

	public boolean isDfdGainedLevel() {
		return dfdGainedLevel;
	}

	public void setDfdGainedLevel(boolean dfdGainedLevel) {
		this.dfdGainedLevel = dfdGainedLevel;
	}

	public Human getDeadSpeaker() {
		return deadSpeaker;
	}

	public void setDeadSpeaker(Human deadSpeaker) {
		this.deadSpeaker = deadSpeaker;
	}

	public Human getDeadSpeakerSupportPartner() {
		return deadSpeakerSupportPartner;
	}

	public void setDeadSpeakerSupportPartner(Human deadSpeakerSupportPartner) {
		this.deadSpeakerSupportPartner = deadSpeakerSupportPartner;
	}
}
