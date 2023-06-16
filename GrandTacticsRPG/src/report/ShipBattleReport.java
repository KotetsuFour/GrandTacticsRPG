package report;

import unit.human.Human;

public class ShipBattleReport extends Report {

	private int[] details;
	private int atkLevelsGained;
	private boolean dfdGainedLevel;
	private String initialDeathNotification;
	private String shipDeathNotification;
	private Human deadSpeaker;
	private Human deadSpeakerSupportPartner;
	private String deathQuote;
	private String despairQuote;
	private boolean shouldEndBattle;

	public ShipBattleReport(int[] details) {
		this.details = details;
	}

	public void setAtkGainedLevel(boolean atkGainedLevel) {
		if (atkGainedLevel) {
			atkLevelsGained++;
		}
	}

	public void setDfdGainedLevel(boolean dfdGainedLevel) {
		this.dfdGainedLevel = dfdGainedLevel;
	}

	public void setInitialDeathNotification(String notification) {
		this.initialDeathNotification = notification;
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

	public boolean shouldEndBattle() {
		return shouldEndBattle;
	}

	public void setShouldEndBattle(boolean shouldEndBattle) {
		this.shouldEndBattle = shouldEndBattle;
	}

	public void setShipDeathNotification(String notification) {
		// TODO Auto-generated method stub
		
	}


}
