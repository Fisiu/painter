package fik.mariusz.android.paintcalc;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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

	static final String KEY_PREF_PRICE = "pref_price";
	static final String KEY_PREF_CURRENCY = "pref_currency";

	private SharedPreferences sharedPreferences;
	private Preference mPrice;
	private Preference mCurrency;

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
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		if (key.equals(KEY_PREF_PRICE)) {
			String output = moneyFormater(sp.getString(key, "0.00"));
			// save rounded value and set summary with current value
			sharedPreferences.edit().putString(KEY_PREF_PRICE, output).commit();
			mPrice.setSummary(getString(R.string.settings_price_summary) + ": " + output);
		} else if (key.equals(KEY_PREF_CURRENCY)) {
			mCurrency.setSummary(getString(R.string.settings_currency_summary) + ": " + sp.getString(key, ""));
		}
	}

	private String moneyFormater(String input) {
		// make sure entered value has max 2 digits after .
		NumberFormat formatter = new DecimalFormat("#0.00");
		String output = formatter.format(Double.parseDouble(input));
		// change , into .
		output = output.replace(',', '.');
		return output;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void findAllPreferences() {
		mPrice = findPreference(KEY_PREF_PRICE);
		mCurrency = findPreference(KEY_PREF_CURRENCY);
	}

	private void setSummary() {
		mPrice.setSummary(getString(R.string.settings_price_summary) + ": "
				+ sharedPreferences.getString(KEY_PREF_PRICE, "0"));
		mCurrency.setSummary(getString(R.string.settings_currency_summary) + ": "
				+ sharedPreferences.getString(KEY_PREF_CURRENCY, ""));
	}
}
