package politics;

import affiliation.Nation;
import data_structures.List;
import unit.Unit;
import unit.human.Clone;
import unit.monster.Monster;

public class War extends MajorEvent {

	private Nation initiator;
	private Nation responder;
	private WarCause warCause;
	private int battles;
	private int initiatorRecruitDeaths;
	private int responderRecruitDeaths;
	private int initiatorMonsterDeaths;
	private int responderMonsterDeaths;
	private int initiatorCloneDeaths;
	private int responderCloneDeaths;
	private int initiatorCivilianDeaths;
	private int responderCivilianDeaths;
	
	public War(Nation initiator, Nation responder, WarCause cause, long date) {
		super(date);
		this.initiator = initiator;
		this.responder = responder;
		this.warCause = cause;
		this.name = initiator.getName() + "-" + responder.getName() + " War";
	}
	
	public void incrementKills(Unit unit) {
		if (unit.getAffiliation() == initiator) {
			if (unit instanceof Monster) {
				this.initiatorMonsterDeaths++;
			} else if (unit instanceof Clone) {
				this.initiatorCloneDeaths++;
			} else {
				this.initiatorRecruitDeaths++;
			}
		} else if (unit.getAffiliation() == responder) {
			if (unit instanceof Monster) {
				this.responderMonsterDeaths++;
			} else if (unit instanceof Clone) {
				this.responderCloneDeaths++;
			} else {
				this.responderRecruitDeaths++;
			}
		} else {
			throw new IllegalArgumentException("The unit given is not a part of this war");
		}
	}
	
	public void registerCivilianDeaths(Nation n, int num) {
		if (n == initiator) {
			initiatorCivilianDeaths += num;
		} else if (n == responder) {
			responderCivilianDeaths += num;
		} else {
			throw new IllegalArgumentException("The nation given is not a part of this war");
		}
	}
	
	public void incrementBattles() {
		battles++;
	}

	public WarCause getCause() {
		return warCause;
	}
	
	public void rename(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Nation getInitiator() {
		return this.initiator;
	}

	public Nation getResponder() {
		return this.responder;
	}
	
	public int getInitiatorRecruitDeaths() {
		return this.initiatorRecruitDeaths;
	}

	public int getInitiatorCivilianDeaths() {
		return this.initiatorCivilianDeaths;
	}

	public int getInitiatorCloneDeaths() {
		return this.initiatorCloneDeaths;
	}

	public int getInitiatorMonsterDeaths() {
		return this.initiatorMonsterDeaths;
	}

	public int getResponderRecruitDeaths() {
		return this.responderRecruitDeaths;
	}

	public int getResponderCivilianDeaths() {
		return this.responderCivilianDeaths;
	}

	public int getResponderCloneDeaths() {
		return this.responderCloneDeaths;
	}

	public int getResponderMonsterDeaths() {
		return this.responderMonsterDeaths;
	}

	public enum WarCause {
		CANCEL("Cancel", true),
		TRADE_REFUSAL("Target refused peaceful diplomacy", true),
		WEALTH_ENVY("To steal wealth from target", true),
		EXPANSION_BLOCKING("Target is blocking expansion", true),
		ALLIANCE_LOYALTY("Joining war on behalf of an ally", false),
		SIMPLE_CONQUEST("Pursuing goal of conquest", true),
		SIMPLE_HATRED("Desire to destroy the target", true),
		;
		
		private String displayName;
		private boolean initiatorOption;
		
		WarCause(String displayName, boolean initiatorOption) {
			this.displayName = displayName;
			this.initiatorOption = initiatorOption;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
		
		public boolean isInitiatorOption() {
			return this.initiatorOption;
		}
		
		@Override
		public String toString() {
			return getDisplayName();
		}
		
		public static WarCause[] getAllInitiatorOptions() {
			List<WarCause> list = new List<WarCause>(values().length);
			for (int q = 0; q < values().length; q++) {
				if (values()[q].isInitiatorOption()) {
					list.add(values()[q]);
				}
			}
			WarCause[] ret = new WarCause[list.size()];
			for (int q = 0; q < list.size(); q++) {
				ret[q] = list.get(q);
			}
			return ret;
		}
	}

}
