package fik.mariusz.android.paintcalc.model;

import fik.mariusz.android.paintcalc.utils.Utils;

public class Room {

	// Fields to store room dimensions
	private double l, w, h;

	/**
	 * Creates room with specified sizes.
	 * 
	 * @param l
	 *            Room length
	 * @param w
	 *            Room width
	 * @param h
	 *            Room height
	 */
	public Room(double l, double w, double h) {
		this.l = Utils.roundToTwoDecimalPoints(l);
		this.w = Utils.roundToTwoDecimalPoints(w);
		this.h = Utils.roundToTwoDecimalPoints(h);
	}

	/** Return room's ceiling area */
	public Double ceilingArea() {
		return Utils.roundToTwoDecimalPoints(l * w);
	}

	/** Return room's walls area */
	public Double wallsArea() {
		return Utils.roundToTwoDecimalPoints((2 * l + 2 * w) * h);
	}

	/** Returns whole room area to paint */
	public Double totalArea() {
		return Utils.roundToTwoDecimalPoints(ceilingArea() + wallsArea());
	}

	@Override
	public String toString() {
		// get room sizes
		return l + " * " + w + " * " + h;
	}
}
