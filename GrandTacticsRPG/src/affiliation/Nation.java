package affiliation;

import java.util.HashMap;

import buildings.Building;
import buildings.goods_deliverer.Storehouse;
import buildings.goods_deliverer.TradeCenter;
import data_structures.List;
import history.HistoricalRecord;
import inventory.InventoryIndex;
import inventory.item.Armor;
import inventory.item.EdibleCrop;
import inventory.item.Item;
import inventory.item.Resource;
import inventory.item.UsableCrop;
import inventory.item.UsableItem;
import inventory.staff.Staff;
import inventory.weapon.Weapon;
import politics.DiplomaticRelation;
import politics.MajorEvent;
import politics.War;
import politics.War.WarCause;
import report.OldAgeDeathReport;
import report.Report;
import unit.Unit;
import unit.UnitClass.Mount;
import unit.UnitGroup;
import unit.human.Human;
import unit.monster.Monster;
import util.RNGStuff;

public class Nation {

	public static final String[] NATION_TYPES = {"Nation", "Kingdom", "Hegemony", "Empire"};

	public static final int MAX_ARMY_SIZE = 500;
	
	protected String name;
	protected List<CityState> cityStates;
	protected Human leader;
	protected int type;
	protected int nationalLanguage;
	protected HashMap<Nation, DiplomaticRelation> diplomaticRelations;
	protected List<Unit> army;
	protected List<UnitGroup> unitGroups;
	protected List<HistoricalRecord> history;
	
	/**
	 * Constructor for player nation
	 * @param name
	 * @param capitalName
	 * @param type
	 * @param language
	 */
	public Nation(String name, String capitalName, int type, int language) {
		this.name = name;
		this.cityStates = new List<>();
		//City-State automatically adds itself to the nation
		this.nationalLanguage = language;
		new CityState(capitalName, this);
		this.type = type;
		this.diplomaticRelations = new HashMap<>();
		this.diplomaticRelations.put(this, null);
		this.army = new List<>(50, MAX_ARMY_SIZE);
		this.unitGroups = new List<>();
		this.history = new List<>();
	}
	
	/**
	 * General constructor, used for AI nations
	 */
	public Nation() {
		this.nationalLanguage = RNGStuff.nextInt(RNGStuff.numberOfLanguages());
		this.name = RNGStuff.newLocationName(this.nationalLanguage);
		this.cityStates = new List<>();
		//City-State automatically adds itself to the nation
		new CityState(RNGStuff.newLocationName(this.nationalLanguage), this);
		this.leader = Human.completelyRandomHuman(cityStates.get(0));
		this.type = RNGStuff.nextInt(NATION_TYPES.length);
		this.diplomaticRelations = new HashMap<>();
		this.diplomaticRelations.put(this, null);
		this.army = new List<>(50, MAX_ARMY_SIZE);
		army.add(this.leader);
		this.unitGroups = new List<>();
		this.history = new List<>();
	}
	
	/**
	 * Constructor for seceeded nations
	 * @param cs
	 */
	protected Nation(CityState cs) {
		Nation formerNation = cs.getNation();
		cs.defect(this);
		this.cityStates = new List<>();
		cityStates.add(cs);
		formerNation.cityStates.remove(cs);
		this.nationalLanguage = cs.getLanguage();
		this.name = RNGStuff.newLocationName(this.nationalLanguage);
		this.type = RNGStuff.nextInt(NATION_TYPES.length);
		this.diplomaticRelations = new HashMap<>();
		this.diplomaticRelations.put(this, null);
		this.army = new List<>(50, MAX_ARMY_SIZE);
		this.unitGroups = new List<>();
		this.history = new List<>();
		formerNation.cityStates.remove(cs);
		Human formerLeader = formerNation.leader;
		//Start at 1, because the capital cannot rebel
		for (int q = 1; q < formerNation.cityStates.size(); q++) {
			CityState check = formerNation.cityStates.get(q);
			int diffsWithRebels = Math.abs(check.getNationalism() - cs.getNationalism())
					+ Math.abs(check.getMilitarism() - cs.getMilitarism())
					+ Math.abs(check.getAltruism() - cs.getAltruism())
					+ Math.abs(check.getFamilism() - cs.getFamilism())
					//More Tolerance, less likely to rebel
					+ check.getTolerance();
			int diffsWithNation = Math.abs(check.getNationalism() - formerLeader.getNationalism())
					+ Math.abs(check.getMilitarism() - formerLeader.getMilitarism())
					+ Math.abs(check.getAltruism() - formerLeader.getAltruism())
					+ Math.abs(check.getFamilism() - formerLeader.getFamilism())
					//More Confidence, more likely to rebel
					+ check.getConfidence();
			if (diffsWithNation > diffsWithRebels) {
				cityStates.add(check);
				formerNation.cityStates.remove(q);
				q--;
				check.defect(this);
			}
		}
		//First get the humans, so we know if monsters should rebel or not
		for (int q = 0; q < formerNation.army.size(); q++) {
			Unit u = formerNation.army.get(q);
			if (u instanceof Human) {
				Human h = (Human)u;
				if (this.cityStates.contains(h.getHome())
						&& h.getFamilism() + h.getConfidence() > h.getNationalism() + h.getTolerance()) {
					this.army.add(h);
					formerNation.army.remove(q);
					q--;
					h.defect(this);
				} else {
					h.declareLoyalty();
				}
			}
		}
		//Then remove monsters if their master rebelled
		for (int q = 0; q < formerNation.army.size(); q++) {
			Unit u = formerNation.army.get(q);
			if (u instanceof Monster) {
				Monster m = (Monster)u;
				if (m.getAffiliation() == this) {
					formerNation.army.remove(q);
					q--;
					m.defect(this);
				}
			}
		}
		Human lead = null;
		for (int q = 0; q < this.cityStates.size() && lead == null; q++) {
			lead = this.cityStates.get(q).getLeadingNoble();
		}
		if (lead == null) {
			for (int q = 0; q < this.cityStates.size() && lead == null; q++) {
				lead = this.cityStates.get(q).getLeadingVeteran();
			}
			if (lead == null) {
				lead = Human.completelyRandomHuman(cs);
			}
		}
		this.leader = lead;
		//TODO initialize relations with all nations that formerNation had relations with,
		//and with formerNation of course
		//TODO copy technology tree
		//TODO organize army into groups
		//TODO add secession to nation's history
	}
	
	public CityState getCapital() {
		return cityStates.get(0);
	}
	
	public List<CityState> getCityStates() {
		return cityStates;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFullName() {
		return "The " + NATION_TYPES[type] + " of " + name;
	}
	
	public int getNationalLanguage() {
		return nationalLanguage;
	}

	public int getPopulation() {
		int pop = 0;
		for (int q = 0; q < cityStates.size(); q++) {
			pop += cityStates.get(q).getPopulation();
		}
		return pop;
	}
	
	public List<Unit> getArmy() {
		return army;
	}
	
	public Human getRuler() {
		return leader;
	}
	public void setRuler(Human ruler) {
		this.leader = ruler;
	}
	
	public List<UnitGroup> getUnitGroups() {
		return unitGroups;
	}
	
	/**
	 * Gives the military strength of the nation
	 * @return array with the following indexes:
	 * [0] = physical strength
	 * [1] = magical strength
	 * [2] = accuracy
	 * [3] = avoidance (Basic. that is, torso for units)
	 * [4] = crit rate
	 * [5] = crit avoidance (Basic. that is, torso for units)
	 * [6] = average attack speed
	 * [7] = defense (Basic. that is, torso for units)
	 * [8] = resistance
	 * [9] = head HP
	 * [10] = torso HP
	 */
	public int[] getPower() {
		int[] ret = new int[11];
		for (int q = 0; q < army.size(); q++) {
			int[] current = army.get(q).getPower();
			for (int w = 0; w < current.length; w++) {
				ret[w] += current[w];
			}
		}
		ret[6] /= Math.max(1, army.size()); //Make sure attack speed is the average
		
		//TODO add power of assigned things (ships, and defendable buildings)
		return ret;
	}

	public void addHistoricalRecord(HistoricalRecord record) {
		history.add(record);
	}
	
	public boolean isAtWarWith(Nation n) {
		if (n == null) {
			return true;
		}
		DiplomaticRelation r = diplomaticRelations.get(n);
		if (r == null) {
			return false;
		}
		return r.getCurrentEvent() instanceof War;
	}

	public boolean isAlliedWith(Nation n) {
		if (n == this) {
			return true;
		}
		DiplomaticRelation r = diplomaticRelations.get(n);
		if (r == null) {
			return false;
		}
		return r.isAlliance();
	}

	public DiplomaticRelation relationshipWith(Nation n) {
		DiplomaticRelation dr = diplomaticRelations.get(n);
		if (dr == null) {
			dr = new DiplomaticRelation(this, n);
			diplomaticRelations.put(n, dr);
			n.diplomaticRelations.put(this, dr);
		}
		return dr;
	}
	
	public War getCurrentWarWith(Nation n) {
		if (n == this) {
			//TODO
		}
		MajorEvent me = diplomaticRelations.get(n).getCurrentEvent();
		if (me instanceof War) {
			return (War)me;
		}
		return null;
	}
	
	public Nation seceededNation(CityState rebels) {
		Nation ret = new Nation(rebels);
		//TODO anything else?
		return ret;
	}

	public List<Report> passMonth(boolean yearEnded) {
		List<Report> ret = new List<>();
		for (int q = 0; q < cityStates.size(); q++) {
			ret.addAll(cityStates.get(q).passMonth(yearEnded));
		}
		if (yearEnded) {
			for (int q = 0; q < army.size(); q++) {
				if (army.get(q) instanceof Human && !((Human)army.get(q)).incrementAge()) {
					ret.add(new OldAgeDeathReport((Human) army.get(q)));
				}
			}
		}
		return ret;
	}

	public List<Report> passDay(boolean yearEnded) {
		List<Report> ret = new List<>();
		for (int q = 0; q < cityStates.size(); q++) {
			ret.addAll(cityStates.get(q).passDay(yearEnded));
		}
		if (yearEnded) {
			for (int q = 0; q < army.size(); q++) {
				if (army.get(q) instanceof Human && !((Human)army.get(q)).incrementAge()) {
					ret.add(new OldAgeDeathReport((Human) army.get(q)));
				}
			}
		}
		return ret;
	}
	
	public List<int[]> getAllItemsInStorage() {
		List<int[]> ret = new List<>();
		for (int q = 0; q < cityStates.size(); q++) {
			List<int[]> inv = cityStates.get(q).getAllItemsInStorgage();
			for (int w = 0; w < inv.size(); w++) {
				InventoryIndex.moveItemToInventory(ret, inv.get(w));
			}
		}
		
		return ret;
	}
	
	public int[] getAllMountsForTraining() {
		//TODO give all mounts in training facilities
		return null;
	}
	
	public boolean evaluateTradeDeal(int[] desired, int[] offered) {
		List<int[]> allItems = getAllItemsInStorage();
		int[] allMounts = getAllMountsForTraining();
		int nationalismAssets = 0;
		int familismAssets = 0;
		int altruismAssets = 0;
		int militarismAssets = 0;
		for (int q = 0; q < allItems.size(); q++) {
			Item item = InventoryIndex.getElement(allItems.get(q));
			int worth = item.getApproximateWorth() * allItems.get(q)[2];
			if (item instanceof Weapon || item instanceof Armor) {
				militarismAssets += worth;
				nationalismAssets += worth / 2;
			} else if (item instanceof EdibleCrop) {
				familismAssets+= worth;
				altruismAssets += worth / 2;
			} else if (item instanceof Staff || item instanceof UsableItem) {
				altruismAssets += worth;
				familismAssets += worth / 2;
			} else if (item instanceof UsableCrop) {
				nationalismAssets += worth;
				familismAssets += worth;
			} else if (item instanceof Resource) {
				nationalismAssets += worth;
			}
		}
		for (int q = 0; q < allMounts.length; q++) {
			militarismAssets += (Mount.values()[q].getWorth() * allMounts[q]);
		}
		
		//TODO evaluate trade deals
		
		//TODO based on items held, mounts held, and trade deals established,
		//evaluate the nation's current standing in all pursuits.
		//TODO Evaluate worth of trade
		//TODO Decide if the trade is worth it
		
		return false;
	}
	
	public List<Storehouse> getAllStorehousesInNation(Storehouse sender) {
		List<Storehouse> ret = new List<>();
		for (int q = 0; q < cityStates.size(); q++) {
			List<Building> b = cityStates.get(q).getOtherBuildings();
			for (int w = 0; w < b.size(); w++) {
				Building build = b.get(w);
				if (build instanceof Storehouse && build != sender) {
					ret.add((Storehouse)build);
				}
			}
		}
		return ret;
	}

	public List<TradeCenter> getAllTradeCentersInNation() {
		List<TradeCenter> ret = new List<>();
		for (int q = 0; q < cityStates.size(); q++) {
			List<Building> b = cityStates.get(q).getOtherBuildings();
			for (int w = 0; w < b.size(); w++) {
				Building build = b.get(w);
				if (build instanceof TradeCenter) {
					ret.add((TradeCenter)build);
				}
			}
		}
		return ret;
	}

	public void declareWar(Nation n, WarCause choice, long date) {
		War war = new War(this, n, choice, date);
		DiplomaticRelation dr = diplomaticRelations.get(n);
		dr.startWar(war);
		HistoricalRecord rec = new HistoricalRecord(war.getName() + " Begins. Cause: " + war.getCause().getDisplayName());
		addHistoricalRecord(rec);
		n.addHistoricalRecord(rec);
		//TODO if this is AI nation, notify AI to reorganize military
		//TODO allow respondent to ask allies for help
		
		for (int q = 0; q < army.size(); q++) {
			army.get(q).incrementWars();
		}
		for (int q = 0; q < n.army.size(); q++) {
			n.army.get(q).incrementWars();
		}
	}

	public int numCurrentWars() {
		int ret = 0;
		for (DiplomaticRelation dr : diplomaticRelations.values()) {
			if (dr.getCurrentEvent() instanceof War) {
				ret++;
			}
		}
		return ret;
	}
}
