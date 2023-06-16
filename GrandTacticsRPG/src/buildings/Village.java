package buildings;

import affiliation.CityState;
import affiliation.Nation;
import buildings.defendable.TrainingFacility;
import buildings.goods_deliverer.Storehouse;
import data_structures.List;
import inventory.InventoryIndex;
import unit.human.Human;
import util.RNGStuff;

public class Village extends Building {
	
	protected double populationInThousands;
	protected List<Human> veteranResidents;
	protected CityState cityState;

	//TODO decide actual values
	public static final int[] materialsNeededForConstruction = {};
	public static final int MAX_INTEGRITY = 10;
	public static final int DURABILITY = 10;
	public static final int RESISTANCE = 10;
	
	public Village(String name, CityState cityState) {
		super(name, MAX_INTEGRITY, DURABILITY, RESISTANCE, Human.completelyRandomHuman(cityState));
		veteranResidents = new List<Human>(5, 5);
		//Between 1 and 10 thousand residents will arrive
		this.populationInThousands = RNGStuff.nextInt(9) + 1;
		this.cityState = cityState;
	}
	
	public Village(CityState cityState) {
		this(RNGStuff.randomName(cityState.getLanguage()), cityState);
	}
	
	@Override
	public String getType() {
		return Building.VILLAGE;
	}
	
	public int getPopulation() {
		return (int)Math.round(populationInThousands);
	}
	
	public CityState getCityState() {
		return cityState;
	}
	
	public List<TrainingFacility> recruitDestinations() {
		List<TrainingFacility> ret = new List<>();
		for (int q = 0; q < cityState.getOtherBuildings().size(); q++) {
			if (cityState.getOtherBuildings().get(q) instanceof TrainingFacility) {
				ret.add((TrainingFacility)cityState.getOtherBuildings().get(q));
			}
		}
		return ret;
	}

	@Override
	public void completeDailyAction() {
		int feed = 0;
		int build = 0;
		int bonusFeed = 0;
		for (int q = 0; q < materials.size(); q++) {
			int[] itemArray = materials.get(q);
			if (itemArray[0] == InventoryIndex.USABLECROP) {
				//TODO get enough wood to fix damage
			} else if (itemArray[0] == InventoryIndex.EDIBLECROP) {
				int needed = Math.min(itemArray[2], (int)Math.round(populationInThousands * 200) - feed);
				itemArray[2] -= needed;
				feed += needed;
				bonusFeed += itemArray[2];
			}
		}
		
		//TODO only affect population if there's a major food shortage
		
//		double percentFed = (feed + 0.0) / (populationInThousands * 200);
//		populationInThousands *= percentFed;
//		
//		//TODO possibly rebalance
//		double percentGrowth = Math.min((0.0 + bonusFeed) / (populationInThousands * 200), 0.01);
//		populationInThousands *= (1 + percentGrowth);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	public void rebuild(int[] materials) {
		//TODO
	}
	
	public Human recruitUnit() {
		return cityState.recruitUnit();
	}

	@Override
	public void completeMonthlyAction() {
		int feed = 0;
		int build = 0;
		int bonusFeed = 0;
		for (int q = 0; q < materials.size(); q++) {
			int[] itemArray = materials.get(q);
			if (itemArray[0] == InventoryIndex.USABLECROP) {
				//TODO get enough wood to fix damage
			} else if (itemArray[0] == InventoryIndex.EDIBLECROP) {
				int needed = Math.min(itemArray[2], (int)Math.round(populationInThousands * 6000) - feed);
				itemArray[2] -= needed;
				feed += needed;
				bonusFeed += itemArray[2];
			}
		}
		double percentFed = Math.max(0.8, (feed + 0.0) / (populationInThousands * 6000));
		populationInThousands *= percentFed;
		
		//TODO possibly rebalance
		double percentGrowth = Math.min((0.0 + bonusFeed) / (populationInThousands * 6000), 0.01);
		populationInThousands *= (1 + percentGrowth);
	}

	@Override
	public boolean canReceiveGoods(int[] goods) {
		int type = goods[0];
		return type == InventoryIndex.EDIBLECROP
				|| type == InventoryIndex.USABLECROP;
	}

	@Override
	public void defect(Nation n) {
		this.owner.setAffiliation(n);
		for (int q = 0; q < veteranResidents.size(); q++) {
			Human h = veteranResidents.get(q);
			h.defect(n);
			if (h.getAffiliation() != null) {
				//If the unit failed to defect, they will try to flee from this city-state
				if (!(h.retire())) {
					//If they can't flee, they are forced to defect anyway
					h.setAffiliation(n);
				}
			}
		}
	}

	public List<Human> getVeterans() {
		return veteranResidents;
	}

	public boolean addVeteran(Human h) {
		return veteranResidents.add(h);
	}

	@Override
	public void restockInventory() {
		List<Building> b = getCityState().getOtherBuildings();
		//TODO stop  traversing if the need for food is met
		for (int q = 0; q < b.size(); q++) {
			if (b.get(q) instanceof Storehouse) {
				Storehouse s = (Storehouse)b.get(q);
				List<int[]> store = s.getMaterials();
				//TODO get as much food and stuff as you need from the storehouse
			}
		}
	}

}
