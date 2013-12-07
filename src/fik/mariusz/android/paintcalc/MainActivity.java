package fik.mariusz.android.paintcalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fik.mariusz.android.paintcalc.fragment.MainFragment;
import fik.mariusz.android.paintcalc.fragment.RemoveRoomsDialogFragment;
import fik.mariusz.android.paintcalc.fragment.RoomFragment;
import fik.mariusz.android.paintcalc.model.Room;
import fik.mariusz.android.paintcalc.sqlite.DatabaseHelper;
import fik.mariusz.android.paintcalc.utils.Constants;

public class MainActivity extends ActionBarActivity implements RoomFragment.OnNewRoomRequestedListener, RemoveRoomsDialogFragment.RemoveRoomsDialogListener {

	private static final String TAG = "MainActivity";

	public DatabaseHelper databaseHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupActionBar();

		databaseHandler = DatabaseHelper.getInstance(this);

		Fragment firstFragment = new MainFragment();
		firstFragment.setArguments(getIntent().getExtras());

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		databaseHandler.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return true;
		case R.id.action_settings:
			Intent settings = new Intent(this, SettingsActivity.class);
			startActivityForResult(settings, Constants.ACTION_SETTINGS);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDimensionsProvided(double l, double w, double h) {
		// lets create new room with those values
		final Room newRoom = new Room(l, w, h);
		Log.d(TAG, "Adding new room... " + newRoom);
		databaseHandler.addRoom(newRoom);
	}

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        databaseHandler.deleteAllRooms();
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        mainFragment.recalculate();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // TODO: Special handling when user aborted action?
    }

	private void setupActionBar() {
		getSupportActionBar().show();
	}
}
