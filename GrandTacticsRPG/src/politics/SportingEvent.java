package politics;

import affiliation.Nation;
import buildings.Coliseum;
import data_structures.List;
import unit.Unit;

public class SportingEvent extends MajorEvent {

	private Nation nation1;
	private Nation nation2;
	private boolean bracket;
	/**
	 * Round types are as follows:
	 * 0 = friendly battle
	 * 1 = battle to the death
	 * 2 = strength test
	 * 3 = accuracy test
	 * 4 = avoidance test
	 */
	private int[] rounds;
	private int currentRound;
	private List<Unit> participants;
	private Coliseum location;
	
	public SportingEvent(Nation nation1, Nation nation2, Coliseum location, long startDate) {
		super(startDate);
		this.nation1 = nation1;
		this.nation2 = nation2;
		this.location = location;
		//TODO set name
	}
	public void setBracket(int[] rounds, List<Unit> participants) {
		if (participants.size() != Math.pow(rounds.length, 2)) {
			throw new IllegalArgumentException();
		}
		this.bracket = true;
		this.rounds = rounds;
		this.participants = participants;
	}
	public void setNormalRounds(int[] rounds, List<Unit> participants) {
		this.rounds = rounds;
		this.participants = participants;
	}
}
