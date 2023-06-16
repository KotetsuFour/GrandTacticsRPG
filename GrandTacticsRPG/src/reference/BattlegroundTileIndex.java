package reference;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import location.BattlegroundTile;
import location.BattlegroundTile.BattlegroundTileType;
import location.WorldMapTile;
import location.WorldMapTile.WorldMapTileType;
import ship.Ship;

public class BattlegroundTileIndex {

	private static HashMap<WorldMapTileType, char[][]> maps;
	private static HashMap<Character, BattlegroundTileType> tileMap;
	public static final int TILE_DIMENSION = 20;

	public static void initialize() {
		maps = new HashMap<>();
		tileMap = new HashMap<>();
		tileMap.put('C', BattlegroundTileType.CAVE);
		tileMap.put('c', BattlegroundTileType.CHEST);
		tileMap.put('d', BattlegroundTileType.DECK);
		tileMap.put('D', BattlegroundTileType.DEEP_WATER);
		tileMap.put('o', BattlegroundTileType.DOCK);
		tileMap.put('f', BattlegroundTileType.FLOOR);
		tileMap.put('g', BattlegroundTileType.GATE);
		tileMap.put('G', BattlegroundTileType.GRASS);
		tileMap.put('H', BattlegroundTileType.HOUSE);
		tileMap.put('h', BattlegroundTileType.HOUSE_DOOR);
		tileMap.put('m', BattlegroundTileType.MAGMA);
		tileMap.put('M', BattlegroundTileType.MOUNTAIN);
		tileMap.put('P', BattlegroundTileType.PEAK);
		tileMap.put('p', BattlegroundTileType.PILLAR);
		tileMap.put('R', BattlegroundTileType.ROAD);
		tileMap.put('r', BattlegroundTileType.RUBBLE);
		tileMap.put('S', BattlegroundTileType.SAND);
		tileMap.put('s', BattlegroundTileType.SHALLOW_WATER);
		tileMap.put('N', BattlegroundTileType.SNOW);
		tileMap.put('I', BattlegroundTileType.THICKET);
		tileMap.put('t', BattlegroundTileType.THRONE);
		tileMap.put('T', BattlegroundTileType.TREE);
		tileMap.put('a', BattlegroundTileType.WALL);
		tileMap.put('r', BattlegroundTileType.WARP_TILE);
		tileMap.put('w', BattlegroundTileType.WASTELAND);
		tileMap.put('W', BattlegroundTileType.WETLAND);
		
		initializeBattlegroundMaps(tileMap);
	}
	
	private static void initializeBattlegroundMaps(HashMap<Character, BattlegroundTileType> tileMap) {
		//TODO fix this map and make the others
		char[][] plain = {
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
			{'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G'},
		};
		maps.put(WorldMapTileType.PLAIN, plain);
		
		
		//TODO When you're finished adding all the tiles, use the commented code below to make
		//sure they're all the right size
		Set<Entry<WorldMapTileType, char[][]>> vals = maps.entrySet();
		for (Entry<WorldMapTileType, char[][]> m : vals) {
			char[][] c = m.getValue();
			if (c.length != 20 || c[0].length != 20) {
				throw new IllegalArgumentException("The map for " + m.getKey().getName() +
						" is " + c.length + " by " + c[0].length);
			}
		}
	}

	public static BattlegroundTile[][] mapToUse(WorldMapTile wmTile) {
		//Get a copy of the map of tile types (NOT the original)
		char[][] m = maps.get(wmTile.getType()).clone();
		if (wmTile.getGroupPresent() instanceof Ship) {
			//TODO Replace the appropriate tiles with DECK (based on ship size and orientation)
		} else if (wmTile.getBuilding() != null) {
			//TODO Replace the appropriate tiles with those corresponding to the building
		}
		BattlegroundTile[][] ret = new BattlegroundTile[m.length][m[0].length];
		for (int q = 0; q < m.length; q++) {
			for (int w = 0; w < m[0].length; w++) {
				ret[q][w] = new BattlegroundTile(tileMap.get(m[q][w]));
			}
		}
		return ret;
	}
}
