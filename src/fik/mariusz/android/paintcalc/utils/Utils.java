package fik.mariusz.android.paintcalc.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Utils {

	/**
	 * It rounds (half-up) double value to 2 decimal places.
	 * 
	 * @param doubleValue
	 *            Double value which should be rounded.
	 * @return Double value with 2 decimal places.
	 */
	public static double roundToTwoDecimalPoints(double doubleValue) {
		BigDecimal bd = new BigDecimal(doubleValue);
		BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return rounded.doubleValue();
	}

	/**
	 * calculate cost of painting room.
	 * 
	 * @param price
	 *            Price for 1m² taken from settings.
	 * @param meters
	 *            Total size of room, including wall and ceiling.
	 * @return Product of <b>meteres</b> and <b>price</b>, rounded up to 2 decimal places.
	 */
	public static String getRoomCost(String price, double meters) {
		BigDecimal c = new BigDecimal(price);
		BigDecimal m = c.multiply(new BigDecimal(meters));
		NumberFormat n = NumberFormat.getCurrencyInstance();
		double money = m.doubleValue();
		return n.format(money);
	}
}
