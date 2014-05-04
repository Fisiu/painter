package fik.mariusz.android.paintcalc;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class Painter extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// init ActiveAndroid
		ActiveAndroid.initialize(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		// dispose ActiveAndroid
		ActiveAndroid.dispose();
	}
}
