package fik.mariusz.android.paintcalc.adapters;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fik.mariusz.android.paintcalc.R;
import fik.mariusz.android.paintcalc.model.Room;
import fik.mariusz.android.paintcalc.utils.Constants;
import fik.mariusz.android.paintcalc.utils.Utils;

public class RoomAdapter extends BaseAdapter {

	private List<Room> roomList = Collections.emptyList();
	private final LayoutInflater layoutInflater;

	private SharedPreferences sp;

	public RoomAdapter(Context context, List<Room> roomList, SharedPreferences sP) {
		this.roomList = roomList;
		this.layoutInflater = LayoutInflater.from(context);
		this.sp = sP;
	}

	@Override
	public int getCount() {
		return roomList.size();
	}

	@Override
	public Room getItem(int position) {
		return roomList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO customize list item
		View rootView = layoutInflater.inflate(R.layout.room_list_item, parent, false);
		TextView roomNo = (TextView) rootView.findViewById(R.id.room_list_item_no);
		TextView wallSize = (TextView) rootView.findViewById(R.id.room_list_item_walls_value);
		TextView ceilingSize = (TextView) rootView.findViewById(R.id.room_list_item_ceiling_value);
		TextView roomDimensions = (TextView) rootView.findViewById(R.id.room_list_item_dimensions);
		TextView roomCost = (TextView) rootView.findViewById(R.id.room_list_item_cost);

		final Room room = getItem(position);
		final String price = sp.getString(Constants.KEY_PREF_PRICE, "0.00");

		roomNo.setText(position + 1 + "");
		wallSize.setText(room.wallsArea().toString());
		ceilingSize.setText(room.ceilingArea().toString());
		roomDimensions.setText("[" + room.getLenght() + " x " + room.getWidth() + " x " + room.getHeight() + "]");
		roomCost.setText(Utils.getRoomCost(price, room.totalArea()));

		return rootView;
	}

	public void updateRooms(List<Room> roomList) {
		this.roomList = roomList;
		notifyDataSetChanged();
	}
}
