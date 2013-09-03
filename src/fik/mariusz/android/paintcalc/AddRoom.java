package fik.mariusz.android.paintcalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRoom extends Activity implements OnClickListener {

	static final String WIDTH_VALUE = "width";
	static final String LENGHT_VALUE = "lenght";
	static final String HEIGHT_VALUE = "height";
	private Button mButtonAccept;
	private EditText mLenght, mWidth, mHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_room);

		mButtonAccept = (Button) findViewById(R.id.button_add);
		mButtonAccept.setOnClickListener(this);

		mLenght = (EditText) findViewById(R.id.input_l);
		mWidth = (EditText) findViewById(R.id.input_w);
		mHeight = (EditText) findViewById(R.id.input_h);

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
				Intent result = new Intent();
				result.putExtra(LENGHT_VALUE, l);
				result.putExtra(WIDTH_VALUE, w);
				result.putExtra(HEIGHT_VALUE, h);
				setResult(Activity.RESULT_OK, result);
				finish();
			} else {
				Toast.makeText(this, R.string.toast_missing_dimension, Toast.LENGTH_SHORT).show();
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
		return value;
	}
}
