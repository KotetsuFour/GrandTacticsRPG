package history;

import manager.GeneralGameplayManager;

public class HistoricalRecord {

	private long date;
	private String description;
	
	public static HistoricalRecord standardBattleDeath(String notification) {
		return new HistoricalRecord(notification);
		//TODO this method probably isn't necessary. You can just instantiate the
		//HistoricalRecord directly. I was thinking the process would be more complicated
		//than it actually needs to be
	}
	public HistoricalRecord(String notification) {
		this.description = notification;
		this.date = GeneralGameplayManager.getDaysSinceGameStart();
	}
	private String getDateAsString() {
		long dayOfGame = date;
		long year = dayOfGame / 360;
		dayOfGame %= 360;
		long month = (dayOfGame / 12) + 1;
		dayOfGame %= 12;
		long dayOfYear = dayOfGame + 1;
		//TODO make cooler names for the months and the calendar system (___ moon, 2nd year of the ___ calendar)
		return String.format("Day %d of Month %d, in Year %d", dayOfYear, month, year);
	}
}
