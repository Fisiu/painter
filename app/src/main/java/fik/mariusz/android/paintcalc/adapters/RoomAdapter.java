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
		ViewHolder viewHolder;
		if (view == null) {
			view = layoutInflater.inflate(R.layout.room_list_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.roomNo = (TextView) view.findViewById(R.id.room_list_item_no);
			viewHolder.wallSize = (TextView) view.findViewById(R.id.room_list_item_walls_value);
			viewHolder.ceilingSize = (TextView) view.findViewById(R.id.room_list_item_ceiling_value);
			viewHolder.roomDimensions = (TextView) view.findViewById(R.id.room_list_item_dimensions);
			viewHolder.roomWallsCost = (TextView) view.findViewById(R.id.room_list_item_walls_cost);
			viewHolder.roomTotalCost = (TextView) view.findViewById(R.id.room_list_item_total_cost);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		final Room room = getItem(position);
		final String price = sp.getString(Constants.KEY_PREF_PRICE, "0.00");

		viewHolder.roomNo.setText(position + 1 + "");
		viewHolder.wallSize.setText(room.wallsArea().toString());
		viewHolder.ceilingSize.setText(room.ceilingArea().toString());
		viewHolder.roomDimensions.setText("[" + room.getLenght() + " x " + room.getWidth() + " x " + room.getHeight()
				+ "]");
		viewHolder.roomWallsCost.setText(Utils.getRoomCost(price, room.wallsArea()));
		viewHolder.roomTotalCost.setText(Utils.getRoomCost(price, room.totalArea()));

		return view;
	}

	public void updateRooms(List<Room> roomList) {
		this.roomList = roomList;
		notifyDataSetChanged();
	}

	static class ViewHolder {
		TextView roomNo;
		TextView wallSize;
		TextView ceilingSize;
		TextView roomDimensions;
		TextView roomWallsCost;
		TextView roomTotalCost;
	}
}
