package reference;

import data_structures.List;
import unit.human.Human;

public class ArtificialHumanIndex {

	private static List<List<ArtificialHumanTemplate>> index;
	
	public static final int CLONE = 0;
	public static final int TITAN = 1;
	
	public static void initialize() {
		index = new List<>();
		for (int q = 0; q < 2; q++) { //One for clones, one for titans, which will be added later
			index.add(new List<ArtificialHumanTemplate>());
		}
		addDefaultTitans();
	}
	
	private static void addTemplate(int type, ArtificialHumanTemplate t) {
		t.setSpecificTemplateIndex(index.get(type).size());
		index.get(type).add(t);
	}
	
	public static ArtificialHumanTemplate addCloneTemplate(Human h) {
		ArtificialHumanTemplate ret = new ArtificialHumanTemplate(h);
		addTemplate(CLONE, ret);
		return ret;
	}
	
	public static void addDefaultTitans() {
		//TODO add this in a later update
	}
}
