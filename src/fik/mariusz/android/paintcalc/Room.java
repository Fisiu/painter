package fik.mariusz.android.paintcalc;

public class Room {

	// Fields to store room dimensions
	private double l, w, h;

	double getL() {
		return l;
	}

	void setL(double l) {
		this.l = l;
	}

	double getW() {
		return w;
	}

	void setW(double w) {
		this.w = w;
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
	 * @param l
	 *            Room length
	 * @param w
	 *            Room width
	 * @param h
	 *            Room height
	 */
	public Room(double l, double w, double h) {
		this.l = l;
		this.w = w;
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

	@Override
	public String toString() {
		// get room sizes
		return this.getL() + " * " + this.getW() + " * " + this.getH();
	}

}
