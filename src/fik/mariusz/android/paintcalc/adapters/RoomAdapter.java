package fik.mariusz.android.paintcalc.adapters;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fik.mariusz.android.paintcalc.model.Room;

public class RoomAdapter extends BaseAdapter {

	private List<Room> roomList = Collections.emptyList();
	private final Context context;

	public RoomAdapter(Context context, List<Room> roomList) {
		this.context = context;
		this.roomList = roomList;
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
		View rootView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
		TextView text = (TextView) rootView.findViewById(android.R.id.text1);

		text.setText(getItem(position).toString());
		return rootView;
	}

	public void updateRooms(List<Room> roomList) {
		this.roomList = roomList;
		notifyDataSetChanged();
	}
}
