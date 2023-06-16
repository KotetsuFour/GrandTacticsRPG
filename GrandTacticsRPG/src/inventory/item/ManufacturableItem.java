package inventory.item;

public abstract class ManufacturableItem extends Item {

	protected int[][] recipe;
	
	public ManufacturableItem(String name, int initialUses, int approximateWorth, int weight, int[][] recipe) {
		super(name, initialUses, approximateWorth, weight);
		this.recipe = recipe;
	}

	
	public int[][] getRecipe() {
		return recipe;
	}
}
