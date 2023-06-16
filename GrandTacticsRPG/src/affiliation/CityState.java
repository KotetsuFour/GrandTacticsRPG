package affiliation;

import buildings.Building;
import buildings.Village;
import buildings.defendable.Castle;
import buildings.goods_deliverer.Storehouse;
import data_structures.List;
import inventory.InventoryIndex;
import report.OldAgeDeathReport;
import report.Report;
import unit.human.Human;
import util.RNGStuff;

public class CityState {

	protected String name;
	protected List<Village> residentialAreas;
	protected List<Castle> nobleResidences;
	//Storehouses are what provide resources for villages
	protected List<Building> otherBuildings;
	protected Nation nation;
	protected int[] values;
	protected int language;
	protected int size;
	public static final int MAX_SIZE = 16;
	public static final int NATIONALISM = 0;
	public static final int MILITARISM = 1;
	public static final int ALTRUISM = 2;
	public static final int FAMILISM = 3;
	public static final int CONFIDENCE = 4;
	public static final int TOLERANCE = 5;
	
	public CityState(String name, Nation nation) {
		this(nation);
		this.name = name;
	}
	
	public CityState(Nation nation) {
		this.residentialAreas = new List<>(8, 8);
		this.nobleResidences = new List<>(2, 2);
		//Storehouses are what provide resources for villages
		this.otherBuildings = new List<>(6, 6);
		this.nation = nation;
		nation.getCityStates().add(this);
		this.language = nation.getNationalLanguage();
		this.name = RNGStuff.newLocationName(language);
		this.values = new int[6];
		for (int q = 0; q < values.length; q++) {
			this.values[q] = RNGStuff.random0To100();
		}
	}
	
	public CityState(int language, Nation nation) {
		this(nation);
		this.language = language;
		this.name = RNGStuff.newLocationName(language);
	}
	
	/**
	 * Maybe only used for testing
	 * @param nation
	 * @param values
	 */
	public CityState(Nation nation, int[] values) {
		this(nation);
		this.values = values;
		this.language = RNGStuff.nextInt(RNGStuff.numberOfLanguages());
		this.name = RNGStuff.newLocationName(language);
	}
	
	public int getPopulation() {
		int pop = 0;
		for (int q = 0; q < residentialAreas.size(); q++) {
			pop += residentialAreas.get(q).getPopulation();
		}
		return pop;
	}
	
	public int getLanguage() {
		return language;
	}
	
	public List<Village> getResidentialAreas() {
		return residentialAreas;
	}
	
	public List<Castle> getNobleResidences() {
		return nobleResidences;
	}
	
	public List<Building> getOtherBuildings() {
		return otherBuildings;
	}
	
	public int getNationApproval() {
		return getNationalism() + getTolerance();
	}
	
	public Human getLeadingNoble() {
		for (int q = 0; q < nobleResidences.size(); q++) {
			Human h = nobleResidences.get(q).getOwner();
			if (h != null) {
				return h;
			}
		}
		return null;
	}
	
	public Human getLeadingVeteran() {
		Human ret = null;
		for (int q = 0; q < residentialAreas.size(); q++) {
			List<Human> vets = residentialAreas.get(q).getVeterans();
			for (int w = 0; w < vets.size(); w++) {
				Human check = vets.get(w);
				if (ret == null
						|| (getMilitarism() > 50 && ret.getBattles() < check.getBattles()) 
						|| ret.getLeadership() < check.getLeadership()) {
					ret = check;
				}
			}
		}
		return ret;
	}
	
	public int getNumBuildings() {
		return residentialAreas.size() + nobleResidences.size() + otherBuildings.size();
	}

	public int[] getValues() {
		return values;
	}

	public Nation getNation() {
		return nation;
	}

	public Human recruitUnit() {
		Human ret = Human.completelyRandomHuman(this);
		int wars = nation.numCurrentWars();
		for (int q = 0; q < wars; q++) {
			ret.incrementWars();
		}
		int detriment = getMilitarism() + getTolerance();
		if (detriment < 0) { //Assume Tolerance is always > 0
			values[NATIONALISM] = Math.max(-100, getNationalism() + (getMilitarism() / 10));
		}
		return ret;
	}
	
	public boolean wantsSecession() {
		Human lead = getLeadingNoble();
		//Veteran opinions don't have an affect
		if (lead == null) {
			return getNationApproval() < 0;
		}
		Human rule = nation.getRuler();
		int ret = getNationApproval();
		int diffs = Math.abs(rule.getNationalism() - lead.getNationalism())
				+ Math.abs(rule.getAltruism() - lead.getAltruism())
				+ Math.abs(rule.getFamilism() - lead.getFamilism())
				+ Math.abs(rule.getMilitarism() - lead.getMilitarism())
				+ lead.getConfidence() - lead.getTolerance();
		return ret - diffs < 0;
	}
	
	public void defect(Nation n) {
		nation = n;
		values[NATIONALISM] = values[FAMILISM];
		for (int q = 0; q < residentialAreas.size(); q++) {
			Village b = residentialAreas.get(q);
			b.defect(n);
		}
		for (int q = 0; q < nobleResidences.size(); q++) {
			Castle b = nobleResidences.get(q);
			b.defect(n);
		}
		for (int q = 0; q < otherBuildings.size(); q++) {
			Building b = otherBuildings.get(q);
			b.defect(n);
		}
	}
	
	public int getNationalism() {
		return values[0];
	}
	public int getMilitarism() {
		return values[1];
	}
	public int getAltruism() {
		return values[2];
	}
	public int getFamilism() {
		return values[3];
	}
	public int getConfidence() {
		return values[4];
	}
	public int getTolerance() {
		return values[5];
	}

	public String getName() {
		return name;
	}

	/**
	 * Lose militarism due to a soldier from here dying in battle
	 */
	public void mournSoldierDeath() {
		values[MILITARISM] = Math.max(-100, values[MILITARISM] - 1);
	}

	public void addBuilding(Building b) {
		if (b instanceof Village) {
			residentialAreas.add((Village)b);
		} else if (b instanceof Castle) {
			nobleResidences.add((Castle)b);
		} else {
			otherBuildings.add(b);
		}
	}
	
	public void incrementSize() {
		if (size == MAX_SIZE) {
			throw new IllegalArgumentException("Cannot expand this city-state further");
		}
		size++;
	}
	
	public void decrementSize() {
		if (size == 0) {
			throw new IllegalArgumentException("Cannot lose more territory");
		}
		size--;
	}
	
	public int size() {
		return size;
	}
	
	public boolean canExpand() {
		return size < MAX_SIZE;
	}

	public List<Report> passMonth(boolean yearEnded) {
		List<Report> ret = new List<>();
		for (int q = 0; q < residentialAreas.size(); q++) {
			Village b = residentialAreas.get(q);
			b.completeMonthlyAction();
		}
		for (int q = 0; q < nobleResidences.size(); q++) {
			Castle b = nobleResidences.get(q);
			b.completeMonthlyAction();
		}
		for (int q = 0; q < otherBuildings.size(); q++) {
			Building b = otherBuildings.get(q);
			b.completeMonthlyAction();
		}
		if (yearEnded) {
			for (int q = 0; q < residentialAreas.size(); q++) {
				Village b = residentialAreas.get(q);
				if (!(b.getOwner().incrementAge())) {
					if (b.getOwner().isImportant()) {
						ret.add(new OldAgeDeathReport(b.getOwner()));
					}
					b.removeOwner(this);
				}
				for (int w = 0; w < b.getVeterans().size(); w++) {
					Human vet = b.getVeterans().get(w);
					if (!(vet.incrementAge())) {
						if (vet.isImportant()) {
							ret.add(new OldAgeDeathReport(vet));
						}
						b.getVeterans().remove(w);
						w--;
					}
				}
			}
			for (int q = 0; q < nobleResidences.size(); q++) {
				Castle b = nobleResidences.get(q);
				b.completeMonthlyAction();
				if (!(b.getOwner().incrementAge())) {
					if (b.getOwner().isImportant()) {
						ret.add(new OldAgeDeathReport(b.getOwner()));
					}
					b.removeOwner(this);
				}
			}
			for (int q = 0; q < otherBuildings.size(); q++) {
				Building b = otherBuildings.get(q);
				b.completeMonthlyAction();
				if (!(b.getOwner().incrementAge())) {
					if (b.getOwner().isImportant()) {
						ret.add(new OldAgeDeathReport(b.getOwner()));
					}
					b.removeOwner(this);
				}
			}
		}
		return ret;
	}
	public List<Report> passDay(boolean yearEnded) {
		List<Report> ret = new List<>();
		for (int q = 0; q < residentialAreas.size(); q++) {
			Village b = residentialAreas.get(q);
			b.completeDailyAction();
		}
		for (int q = 0; q < nobleResidences.size(); q++) {
			Castle b = nobleResidences.get(q);
			b.completeDailyAction();
		}
		for (int q = 0; q < otherBuildings.size(); q++) {
			Building b = otherBuildings.get(q);
			b.completeDailyAction();
		}
		if (yearEnded) {
			for (int q = 0; q < residentialAreas.size(); q++) {
				Village b = residentialAreas.get(q);
				if (!(b.getOwner().incrementAge())) {
					if (b.getOwner().isImportant()) {
						ret.add(new OldAgeDeathReport(b.getOwner()));
					}
					b.removeOwner(this);
				}
				for (int w = 0; w < b.getVeterans().size(); w++) {
					Human vet = b.getVeterans().get(w);
					if (!(vet.incrementAge())) {
						if (vet.isImportant()) {
							ret.add(new OldAgeDeathReport(vet));
						}
						b.getVeterans().remove(w);
						w--;
					}
				}
			}
			for (int q = 0; q < nobleResidences.size(); q++) {
				Castle b = nobleResidences.get(q);
				b.completeMonthlyAction();
				if (!(b.getOwner().incrementAge())) {
					if (b.getOwner().isImportant()) {
						ret.add(new OldAgeDeathReport(b.getOwner()));
					}
					b.removeOwner(this);
				}
			}
			for (int q = 0; q < otherBuildings.size(); q++) {
				Building b = otherBuildings.get(q);
				b.completeMonthlyAction();
				if (!(b.getOwner().incrementAge())) {
					if (b.getOwner().isImportant()) {
						ret.add(new OldAgeDeathReport(b.getOwner()));
					}
					b.removeOwner(this);
				}
			}
		}
		return ret;
	}
	
	public List<int[]> getAllItemsInStorgage() {
		List<int[]> ret = new List<>();
		
		for (int q = 0; q < otherBuildings.size(); q++) {
			if (otherBuildings.get(q) instanceof Storehouse) {
				List<int[]> mat = ((Storehouse)otherBuildings.get(q)).getMaterials();
				for (int w = 0; w < mat.size(); w++) {
					InventoryIndex.moveItemToInventory(ret, mat.get(w).clone());
				}
			}
		}
		
		return ret;
	}
}
