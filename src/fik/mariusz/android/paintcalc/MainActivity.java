package fik.mariusz.android.paintcalc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final int ADD_NEW_ROOM = 1;

	private TextView mRooms;
	private TextView mTotal;

	private Button mButtonAddRoom;
	private Button mButtonReset;

	private double w, l, h;

	private int room = 0;
	private double total = 0.0;

	public int getRoom() {
		return room;
	}

	public void setRoom(int room) {
		this.room = room;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mButtonAddRoom = (Button) findViewById(R.id.button_add_room);
		mButtonAddRoom.setOnClickListener(this);
		mButtonReset = (Button) findViewById(R.id.button_reset);
		mButtonReset.setOnClickListener(this);

		mRooms = (TextView) findViewById(R.id.rooms);
		mTotal = (TextView) findViewById(R.id.total);
		resetItems();
	}

	private void resetItems() {
		if (mRooms != null && mTotal != null) {
			mRooms.setText("" + getRoom());
			mTotal.setText("" + getTotal());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_add_room:
			Intent intent = new Intent(this, AddRoom.class);
			startActivityForResult(intent, ADD_NEW_ROOM);
			break;
		case R.id.button_reset:
			// TODO add reset code
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
				w = data.getDoubleExtra(AddRoom.WIDTH_VALUE, 0.0);
				l = data.getDoubleExtra(AddRoom.LENGHT_VALUE, 0.0);
				h = data.getDoubleExtra(AddRoom.HEIGHT_VALUE, 0.0);
				Toast.makeText(this, "W=" + w + " L=" + l + " H=" + h, Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}