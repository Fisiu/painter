package fik.mariusz.android.paintcalc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import fik.mariusz.android.paintcalc.R;
import fik.mariusz.android.paintcalc.utils.Utils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class RoomFragment extends Fragment implements OnClickListener {

	private Button mButtonAccept;
	private EditText mLenght, mWidth, mHeight;

	private OnNewRoomRequestedListener mListener;

	public RoomFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_room, container, false);

		getActivity().setTitle(R.string.room_dimension);

		mLenght = (EditText) view.findViewById(R.id.input_l);
		mWidth = (EditText) view.findViewById(R.id.input_w);
		mHeight = (EditText) view.findViewById(R.id.input_h);
		mButtonAccept = (Button) view.findViewById(R.id.button_add);

		mButtonAccept.setOnClickListener(this);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnNewRoomRequestedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnNewRoomRequestedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_add:
			double l = getDoubleValue(mLenght);
			double w = getDoubleValue(mWidth);
			double h = getDoubleValue(mHeight);
			// Check if dimenstion is > 0
			if (l > 0 && w > 0 && h > 0) {
				mListener.onDimensionsProvided(l, w, h);
				getFragmentManager().popBackStack();
			} else {
				Toast.makeText(getActivity(), R.string.toast_missing_dimension, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	private double getDoubleValue(EditText editText) {
		double value = 0.0;
		try {
			value = Double.parseDouble(editText.getText().toString());
		} catch (NumberFormatException e) {
			// getText() returned null
			Log.e("getDoubleValue", "Input was null");
		}
		return Utils.roundToTwoDecimalPoints(value);
	}

	// Container Activity must implement this interface
	public interface OnNewRoomRequestedListener {
		public void onDimensionsProvided(double l, double w, double h);
	}
}
