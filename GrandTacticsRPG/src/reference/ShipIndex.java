package reference;

import data_structures.List;
import ship.Ship;

public class ShipIndex {

	private static List<Ship> ships;
	
	public Ship getShip(int idx) {
		try {
			return ships.get(idx);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public static void initialize() {
		ships = new List<>();
		addDefaultShips();
	}
	
	public static void addShip(Ship s) {
		s.setBluePrint(ships.size());
		ships.add(s);
	}
	
	public static void addDefaultShips() {
		//TODO make actual default ships
	}
}
