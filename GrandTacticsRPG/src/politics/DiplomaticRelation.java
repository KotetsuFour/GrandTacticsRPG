package politics;

import affiliation.Nation;
import data_structures.List;

public class DiplomaticRelation {

	private Nation nation1;
	private Nation nation2;
	private int relationshipStrength;
	private int wars;
	private int sports;
	private int festivals;
	private List<int[]> tradeDeals;
	private MajorEvent currentEvent;
	
	public DiplomaticRelation(Nation nation1, Nation nation2) {
		this.nation1 = nation1;
		this.nation2 = nation2;
		this.tradeDeals = new List<>();
	}
	
	public MajorEvent getCurrentEvent() {
		return currentEvent;
	}

	public boolean isAlliance() {
		return currentEvent instanceof WarAlliance;
	}
	
	public Nation getNation1() {
		return nation1;
	}
	
	public Nation getNation2() {
		return nation2;
	}
	
	public int getRelationshipStrength() {
		return relationshipStrength;
	}
	
	public int getWarsCount() {
		return wars;
	}
	
	public int getSportsCount() {
		return sports;
	}
	
	public int getFestivalsCount() {
		return festivals;
	}
	
	public List<int[]> getTradeDeals() {
		return tradeDeals;
	}
	
	public String getRelationshipStrengthDisplay() {
		StringBuilder sb = new StringBuilder();
		if (relationshipStrength >= 90) {
			sb.append("Faithful Alliance");
		} else if (relationshipStrength >= 70) {
			sb.append("Very Strong");
		} else if (relationshipStrength >= 50) {
			sb.append("Strong");
		} else if (relationshipStrength >= 30) {
			sb.append("Good");
		} else if (relationshipStrength >= 10) {
			sb.append("Decent");
		} else if (relationshipStrength >= 0) {
			sb.append("Somewhat Positive");
		} else if (relationshipStrength >= -10) {
			sb.append("Somewhat Negative");
		} else if (relationshipStrength >= -30) {
			sb.append("Poor");
		} else if (relationshipStrength >= -50) {
			sb.append("Bad");
		} else if (relationshipStrength >= -70) {
			sb.append("Contemptuous");
		} else if (relationshipStrength >= -90) {
			sb.append("Very Contemptuous");
		} else {
			sb.append("Deep Mutual Hatred");
		}
		sb.append(" (" + relationshipStrength + ")");
		
		return sb.toString();
	}

	public void cancelTradeDeal(int idx) {
		tradeDeals.remove(idx);
		//TODO maybe affect relationship
	}

	public void startWar(War war) {
		tradeDeals = new List<>();
		//TODO probably go through trade centers and stop shipping

		this.currentEvent = war;
		//TODO affect relationship
		
		this.wars++;
	}
}
