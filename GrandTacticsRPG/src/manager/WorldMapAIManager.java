package manager;

import data_structures.List;
import location.WorldMap;
import unit.UnitGroup;

public class WorldMapAIManager {

	public static void sortDecisions(WorldMap map, List<UnitGroup> moveable, List<UnitGroup> enemies) {
		//TODO
	}
	
	
	public enum ActionType {
		ENGAGE,
		CAMP,
		IMPRISON,
		OPTIMIZE,
		JOIN,
		SPLIT,
		DESTROY,
		SEIZE,
		ENTER,
		WARP
		;
	}
}
