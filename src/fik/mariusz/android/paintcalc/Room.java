package fik.mariusz.android.paintcalc;

public class Room {

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

	public Room(double w, double l, double h) {
		this.w = w;
		this.l = l;
		this.h = h;
	}

	Double ceilingArea() {
		return getW() * getL();
	}

	Double wallsArea() {
		return (2 * getW() + 2 * getL()) * getH();
	}

	Double totalArea() {
		return ceilingArea() + wallsArea();
	}
}
