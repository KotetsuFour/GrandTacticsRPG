package politics;

import data_structures.List;
import history.HistoricalRecord;

public abstract class MajorEvent {

	protected long startDate;
	protected List<HistoricalRecord> relevantOccurences;
	protected String name;
	
	public MajorEvent(long startDate) {
		this.startDate = startDate;
		//TODO relevant occurrences is probably not needed. We'll probably only store
		//those in each nation's history list
		relevantOccurences = new List<>();
	}
	
	public String getName() {
		return name;
	}
	
}
