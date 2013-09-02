package fik.mariusz.android.paintcalc;

public class Room {

	// Fields to store room dimensions
	private double w, l, h;

	double getW() {
		return w;
	}

	void setW(double w) {
		this.w = w;
	}

	double getL() {
		return l;
	}

	void setL(double l) {
		this.l = l;
	}

	double getH() {
		return h;
	}

	void setH(double h) {
		this.h = h;
	}

	/**
	 * Creates room with specified sizes.
	 * 
	 * @param w
	 *            Room width
	 * @param l
	 *            Room length
	 * @param h
	 *            Room height
	 */
	public Room(double w, double l, double h) {
		this.w = w;
		this.l = l;
		this.h = h;
	}

	/** Return room's ceiling area */
	Double ceilingArea() {
		return getW() * getL();
	}

	/** Return room's walls area */
	Double wallsArea() {
		return (2 * getW() + 2 * getL()) * getH();
	}

	/** Returns whole room area to paint */
	Double totalArea() {
		return ceilingArea() + wallsArea();
	}
}
