package fik.mariusz.android.paintcalc;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fik.mariusz.android.paintcalc.model.Room;
import fik.mariusz.android.paintcalc.utils.Utils;

public class MainActivity extends ActionBarActivity implements OnItemClickListener, OnItemLongClickListener {

	private static final int ADD_NEW_ROOM = 1;
	private static final int ACTION_SETTINGS = 11;

	private TextView mRooms;
	private TextView mTotal;
	private TextView mCost;

	private double l, w, h; // room dimensions

	private RoomAdapter roomAdapter; // adapter
	private List<Room> roomList; // list with rooms
	private ListView roomListView;

	// total area (from all rooms) to paint
	private double total = 0.0;

	double getTotal() {
		return total;
	}

	void setTotal(double total) {
		this.total = Utils.roundToTwoDecimalPoints(total);
	}

	// preferences
	private SharedPreferences sP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupActionBar();

		sP = PreferenceManager.getDefaultSharedPreferences(this);

		mRooms = (TextView) findViewById(R.id.rooms);
		mTotal = (TextView) findViewById(R.id.total);
		mCost = (TextView) findViewById(R.id.cost);

		roomAdapter = new RoomAdapter(this);

		roomList = new ArrayList<Room>();

		// fake data to test UI and calculations
		// populateWithFakeData(47);

		roomListView = (ListView) findViewById(R.id.room_list);
		roomListView.setAdapter(roomAdapter);
		roomListView.setOnItemClickListener(this);
		recalculate();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// we need recalculate after changing settings
		recalculate();
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
		case R.id.action_add:
			Intent intent = new Intent(this, AddRoom.class);
			startActivityForResult(intent, ADD_NEW_ROOM);
			return true;
		case R.id.action_remove_last:
			removeLastRoom();
			return true;
		case R.id.action_reset:
			removeAllRooms();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(this, "Room " + position + " clicked [" + id + "]", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		return false;
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

	private void setupActionBar() {
		getSupportActionBar().show();
	}

	/** Add room to the list */
	private void addRoom(Room room) {
		roomList.add(room);
		setTotal(getTotal() + room.totalArea());
		// Log.d("Total", "walls: " + room.wallsArea() + " ceiling: " +
		// room.ceilingArea() + " | TOTAL: " + getTotal());
		recalculate();
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
			recalculate();
		}
	}

	/** Clear the list with all rooms */
	private void removeAllRooms() {
		if (!roomList.isEmpty()) {
			roomList.clear();
			setTotal(0.0);
			recalculate();
		}
	}

	/** recalculate and update UI */
	private void recalculate() {
		mRooms.setText("" + roomList.size());
		mTotal.setText("" + getTotal());
		final String cost = getRoomCost(sP.getString(SettingsActivity.KEY_PREF_PRICE, "0.00"), getTotal());
		mCost.setText(cost);

		// update listview items
		roomAdapter.updateRooms(roomList);
	}

	private String getRoomCost(String price, double meters) {
		BigDecimal c = new BigDecimal(price);
		BigDecimal m = c.multiply(new BigDecimal(meters));
		NumberFormat n = NumberFormat.getCurrencyInstance();
		double money = m.doubleValue();
		return n.format(money);
	}

	/**
	 * Populate list with fake data. It allows to test UI layout and
	 * calculations.
	 * 
	 * @param roomsCount
	 *            Rooms count to be added to listview.
	 */
	@SuppressWarnings("unused")
	private void populateWithFakeData(int roomsCount) {
		Random random = new Random(47);
		for (int i = 0; i < roomsCount; i++) {
			double l = random.nextDouble() * 10;
			double w = random.nextDouble() * 10;
			double h = random.nextDouble() * 10;
			if (l > 0 && w > 0 && h > 0) {
				Room room = new Room(l, w, h);
				addRoom(room);
			}
		}
	}

	class RoomAdapter extends BaseAdapter {

		private List<Room> rooms = Collections.emptyList();
		private final Context context;

		public RoomAdapter(Context context) {
			this.context = context;
		}

		public void updateRooms(List<Room> rooms) {
			this.rooms = rooms;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return rooms.size();
		}

		@Override
		public Room getItem(int position) {
			return rooms.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO customize list item
			View rootView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
			TextView text = (TextView) rootView.findViewById(android.R.id.text1);

			text.setText(getItem(position).toString());
			return rootView;
		}

	}
}
