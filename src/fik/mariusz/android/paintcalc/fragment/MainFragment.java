package fik.mariusz.android.paintcalc.fragment;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fik.mariusz.android.paintcalc.R;
import fik.mariusz.android.paintcalc.adapters.RoomAdapter;
import fik.mariusz.android.paintcalc.model.Room;
import fik.mariusz.android.paintcalc.sqlite.DatabaseHelper;
import fik.mariusz.android.paintcalc.utils.Constants;
import fik.mariusz.android.paintcalc.utils.Utils;

public class MainFragment extends Fragment implements OnItemClickListener {

	private static final String TAG = "MainFragment";

	private TextView mRooms;
	private TextView mTotal;
	private TextView mCost;

	private RoomAdapter roomAdapter; // adapter
	private ListView roomListView;

	// total area (from all rooms) to paint
	private double total = 0.0;

	// preferences
	private SharedPreferences sP;

	// database handler
	private DatabaseHelper databaseHandler;

	double getTotal() {
		return total;
	}

	void setTotal(double total) {
		this.total = Utils.roundToTwoDecimalPoints(total);
	}

	public MainFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		sP = PreferenceManager.getDefaultSharedPreferences(getActivity());
		databaseHandler = DatabaseHelper.getInstance(getActivity());

		// fake data to test UI and calculations
		// populateWithFakeData(47);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		getActivity().setTitle(R.string.app_name);

		mRooms = (TextView) view.findViewById(R.id.rooms);
		mTotal = (TextView) view.findViewById(R.id.total);
		mCost = (TextView) view.findViewById(R.id.cost);
		roomListView = (ListView) view.findViewById(R.id.room_list);

		View headerView = inflater.inflate(R.layout.room_list_header, null);
		roomListView.addHeaderView(headerView);

		roomAdapter = new RoomAdapter(getActivity(), databaseHandler.getAllRooms());
		roomListView.setAdapter(roomAdapter);
		roomListView.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		// recalculate before making fragment visible
		recalculate();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_fragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_remove_last:
			removeLastRoom();
			return true;
		case R.id.action_reset:
			removeAllRooms();
			return true;
		case R.id.action_add:
			Fragment roomFragment = new RoomFragment();
			getFragmentManager().saveFragmentInstanceState(this);
			getFragmentManager().beginTransaction().addToBackStack(Constants.BACK_STACK_ROOM)
					.replace(R.id.fragment_container, roomFragment).commit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Show detailed fragment for selected room.
		Toast.makeText(getActivity(), "Room " + position + " clicked [" + id + "]", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Add room to the list.<br>
	 * <b>Used only to populate fake data!</b>
	 */
	private void addRoom(Room room) {
		databaseHandler.addRoom(room);

		// update UI after addding one room
		setTotal(getTotal() + room.totalArea());
		Log.d(TAG, "walls: " + room.wallsArea() + " ceiling: " + room.ceilingArea() + " | TOTAL: " + getTotal());
	}

	/**
	 * Deletes recently added room from list and database.
	 */
	private void removeLastRoom() {
		// FIXME: We could get latest room before deleting, to substract values
		// from current total.
		if (databaseHandler.getRoomsCount() > 0) {
			databaseHandler.deleteLatestRoom();
		}
		// update UI
		recalculate();
	}

	/** Clear the list with all rooms */
	private void removeAllRooms() {
		// TODO: Add confirmation dialog?
		if (databaseHandler.getRoomsCount() > 0) {
			new RemoveRoomsDialogFragment().show(getFragmentManager(), "RemoveRoomsDialogFragment");
			// databaseHandler.deleteAllRooms();
		}
	}

	/** Recalculate and update UI */
	public void recalculate() {
		//
		final List<Room> roomList = databaseHandler.getAllRooms();
		setTotal(0);
		for (Room room : roomList) {
			setTotal(getTotal() + room.totalArea());
		}

		mRooms.setText("" + roomList.size());
		mTotal.setText("" + getTotal());

		final String cost = Utils.getRoomCost(sP.getString(Constants.KEY_PREF_PRICE, "0.00"), getTotal());
		mCost.setText(cost);

		// update listview items
		roomAdapter.updateRooms(roomList);
	}

	/**
	 * Populate list with fake data. It allows to test UI layout and calculations.
	 * 
	 * @param roomsCount
	 *            Rooms count to be added to listview.
	 */
	@SuppressWarnings("unused")
	private void populateWithFakeData(int roomsCount) {
		Random random = new SecureRandom();
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
}
