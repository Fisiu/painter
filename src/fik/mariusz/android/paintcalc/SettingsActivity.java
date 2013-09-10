package fik.mariusz.android.paintcalc;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private final int sdkVersion = Build.VERSION.SDK_INT;

	private static final String KEY_PREF_PRICE = "pref_price";

	private SharedPreferences sharedPreferences;
	private Preference mPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		if (sdkVersion >= Build.VERSION_CODES.HONEYCOMB) {
			setupActionBar();
		}

		sharedPreferences = getPreferenceScreen().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);

		findAllPreferences();
		setSummary();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		if (key.equals(KEY_PREF_PRICE)) {
			mPrice.setSummary(getString(R.string.settings_price_summary) + ": " + sp.getString(key, "0"));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void findAllPreferences() {
		mPrice = findPreference(KEY_PREF_PRICE);
	}

	private void setSummary() {
		mPrice.setSummary(getString(R.string.settings_price_summary) + ": "
				+ sharedPreferences.getString(KEY_PREF_PRICE, "0"));
	}
}
