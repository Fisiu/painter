package fik.mariusz.android.paintcalc.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import fik.mariusz.android.paintcalc.utils.Utils;

@Table(name = "Rooms")
public class Room extends Model {

	@Column(name = "Length")
	private double length;

	@Column(name = "Width")
	private double width;

	@Column(name = "Height")
	private double height;

	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public Room() {
		super();
	}

	public Room(double length, double width, double height) {
		super();
		this.length = Utils.roundToTwoDecimalPoints(length);
		this.width = Utils.roundToTwoDecimalPoints(width);
		this.height = Utils.roundToTwoDecimalPoints(height);
	}

	/**
	 * Get last added room (with the highest Id)
	 * 
	 * @return Room
	 */
	public static Room getLastAdded() {
		return new Select().from(Room.class).orderBy("Id DESC").executeSingle();
	}

	/**
	 * Get list of all rooms
	 * 
	 * @return list of rooms
	 */
	public static List<Room> getAll() {
		return new Select().from(Room.class).execute();
	}

	/** Return room's ceiling area */
	public Double ceilingArea() {
		return Utils.roundToTwoDecimalPoints(length * width);
	}

	/** Return room's walls area */
	public Double wallsArea() {
		return Utils.roundToTwoDecimalPoints((2 * length + 2 * width) * height);
	}

	/** Returns whole room area to paint */
	public Double totalArea() {
		return Utils.roundToTwoDecimalPoints(ceilingArea() + wallsArea());
	}

}
