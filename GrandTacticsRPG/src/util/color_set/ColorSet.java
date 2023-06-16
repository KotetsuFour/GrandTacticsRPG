package util.color_set;


import data_structures.List;
import javafx.scene.paint.Color;

public class ColorSet {

	private String title;
	private List<Color> colors;
	
	public ColorSet(String title, Color[] colors) {
		this.title = title;
		this.colors = new List<>();
		if (colors != null) {
			for (int q = 0; q < colors.length; q++) {
				this.colors.add(colors[q]);
			}
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public List<Color> getColors() {
		return colors;
	}
	
	public int size() {
		return colors.size();
	}
	
	public Color colorAtIndex(int idx) {
		return colors.get(idx);
	}
	
	public void addColor(Color c) {
		colors.add(c);
	}
	
	public void addColors(Color[] c) {
		if (c == null) {
			throw new IllegalArgumentException("Cannot add a null array of colors");
		}
		for (int q = 0; q < c.length; q++) {
			colors.add(c[q]);
		}
	}
	public void addColors(List<Color> c) {
		if (c == null) {
			throw new IllegalArgumentException("Cannot add a null list of colors");
		}
		colors.addAll(c);
	}
	public void addColors(ColorSet c) {
		addColors(c.getColors());
	}
}
