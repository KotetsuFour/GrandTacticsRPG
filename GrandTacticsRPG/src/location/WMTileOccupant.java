package location;

import affiliation.Nation;
import battle.BattleGround;
import unit.Unit;

public interface WMTileOccupant {

	public BattleGround getBattle();
	
	public Nation getAffiliation();
	
	public WorldMapTile getLocation();
	
	public int getMovement();
	
	public Unit getLeader();

}
