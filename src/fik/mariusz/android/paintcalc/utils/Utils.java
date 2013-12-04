package fik.mariusz.android.paintcalc.utils;

import java.math.BigDecimal;

public class Utils {

	public static double roundToTwoDecimalPoints(double doubleValue) {
		BigDecimal bd = new BigDecimal(doubleValue);
		BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return rounded.doubleValue();
	}
}
