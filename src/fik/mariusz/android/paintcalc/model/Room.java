package fik.mariusz.android.paintcalc.model;

import fik.mariusz.android.paintcalc.utils.Utils;

public class Room {

	private int id;
	private double lenght;
	private double width;
	private double height;

	public Room() {
	}

	public Room(double lenght, double width, double height) {
		this.lenght = Utils.roundToTwoDecimalPoints(lenght);
		this.width = Utils.roundToTwoDecimalPoints(width);
		this.height = Utils.roundToTwoDecimalPoints(height);
	}

	public Room(int id, double lenght, double width, double height) {
		this.id = id;
		this.lenght = Utils.roundToTwoDecimalPoints(lenght);
		this.width = Utils.roundToTwoDecimalPoints(width);
		this.height = Utils.roundToTwoDecimalPoints(height);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLenght() {
		return lenght;
	}

	public void setLenght(double lenght) {
		this.lenght = lenght;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public String toString() {
		// get room sizes
		return lenght + " * " + width + " * " + height;
	}

	/** Return room's ceiling area */
	public Double ceilingArea() {
		return Utils.roundToTwoDecimalPoints(lenght * width);
	}

	/** Return room's walls area */
	public Double wallsArea() {
		return Utils.roundToTwoDecimalPoints((2 * lenght + 2 * width) * height);
	}

	/** Returns whole room area to paint */
	public Double totalArea() {
		return Utils.roundToTwoDecimalPoints(ceilingArea() + wallsArea());
	}

}
