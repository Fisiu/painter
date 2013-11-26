package fik.mariusz.android.paintcalc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private static final int ADD_NEW_ROOM = 1;
	private static final int ACTION_SETTINGS = 11;

	private TextView mRooms;
	private TextView mTotal;
	private TextView mCost;
	private TextView mCurrency;

	private Button mButtonAddRoom;
	private Button mButtonReset;
	private Button mButtonRemoveLast;

	private double l, w, h;

	private ListView roomListView;
	ArrayAdapter<Room> aa;
	private List<Room> roomList;
	private double total = 0.0;

	private SharedPreferences sP;

	double getTotal() {
		return total;
	}

	void setTotal(double total) {
		this.total = total;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupActionBar();

		sP = PreferenceManager.getDefaultSharedPreferences(this);

		mButtonAddRoom = (Button) findViewById(R.id.button_add_room);
		mButtonAddRoom.setOnClickListener(this);
		mButtonReset = (Button) findViewById(R.id.button_reset);
		mButtonReset.setOnClickListener(this);
		mButtonRemoveLast = (Button) findViewById(R.id.button_remove_last);
		mButtonRemoveLast.setOnClickListener(this);

		mRooms = (TextView) findViewById(R.id.rooms);
		mTotal = (TextView) findViewById(R.id.total);
		mCost = (TextView) findViewById(R.id.cost);
		mCurrency = (TextView) findViewById(R.id.currency);

		roomList = new ArrayList<Room>();
		roomListView = (ListView) findViewById(R.id.room_list);
		
		aa = new ArrayAdapter<Room>(this, android.R.layout.simple_list_item_1, roomList);
		updateUI();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// we need recalculate after changing settings
		updateUI();
	}

	private void setupActionBar() {
		getSupportActionBar().show();
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
			startActivityForResult(settings, ACTION_SETTINGS);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_add_room:
			Intent intent = new Intent(this, AddRoom.class);
			startActivityForResult(intent, ADD_NEW_ROOM);
			break;
		case R.id.button_reset:
			removeAllRooms();
			break;
		case R.id.button_remove_last:
			removeLastRoom();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_NEW_ROOM:
			if (resultCode == Activity.RESULT_OK) {
				// Add room
				l = data.getDoubleExtra(AddRoom.LENGHT_VALUE, 0.0);
				w = data.getDoubleExtra(AddRoom.WIDTH_VALUE, 0.0);
				h = data.getDoubleExtra(AddRoom.HEIGHT_VALUE, 0.0);
				addRoom(new Room(l, w, h));
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** Add room to the list */
	private void addRoom(Room room) {
		roomList.add(room);
		setTotal(getTotal() + room.totalArea());
		updateUI();
	}

	private void removeLastRoom() {
		if (!roomList.isEmpty()) {
			// we need last room to get area size
			Room last = roomList.get(roomList.size() - 1);
			// remove room from list
			roomList.remove(last);
			// substract last room area from total area
			setTotal(getTotal() - last.totalArea());
			// update UI
			updateUI();
		}
	}

	/** Clear the list with all rooms */
	private void removeAllRooms() {
		if (!roomList.isEmpty()) {
			roomList.clear();
			setTotal(0.0);
			updateUI();
		}
	}

	/** update UI labels */
	private void updateUI() {
		mRooms.setText("" + roomList.size());
		mTotal.setText("" + getTotal());
		BigDecimal c = new BigDecimal(sP.getString(SettingsActivity.KEY_PREF_PRICE, "0.00"));
		mCost.setText("" + c.multiply(new BigDecimal(getTotal())));
		mCurrency.setText("" + sP.getString(SettingsActivity.KEY_PREF_CURRENCY, "PLN"));
		
		// rebind listview items
		roomListView.setAdapter(aa);
	}
}
