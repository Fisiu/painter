package fik.mariusz.android.paintcalc.fragment;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fik.mariusz.android.paintcalc.R;
import fik.mariusz.android.paintcalc.model.Room;
import fik.mariusz.android.paintcalc.utils.Constants;
import fik.mariusz.android.paintcalc.utils.Utils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MainFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class MainFragment extends Fragment implements OnItemClickListener {

	private TextView mRooms;
	private TextView mTotal;
	private TextView mCost;

	private RoomAdapter roomAdapter; // adapter
	private List<Room> roomList; // list with rooms
	private ListView roomListView;

	// total area (from all rooms) to paint
	private double total = 0.0;

	// preferences
	private SharedPreferences sP;

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
		roomList = new ArrayList<Room>();

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

		roomAdapter = new RoomAdapter(getActivity());

		roomListView.setAdapter(roomAdapter);
		roomListView.setOnItemClickListener(this);
		// recalculate();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		// we need recalculate after changing settings
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
		Toast.makeText(getActivity(), "Room " + position + " clicked [" + id + "]", Toast.LENGTH_SHORT).show();
	}

	/** Add room to the list */
	public void addRoom(Room room) {
		roomList.add(room);
		setTotal(getTotal() + room.totalArea());
		// Log.d("Total", "walls: " + room.wallsArea() + " ceiling: " +
		// room.ceilingArea() + " | TOTAL: " + getTotal());
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
		final String cost = getRoomCost(sP.getString(Constants.KEY_PREF_PRICE, "0.00"), getTotal());
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
