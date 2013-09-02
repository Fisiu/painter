package fik.mariusz.android.paintcalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddRoom extends Activity implements OnClickListener {

	static final String WIDTH_VALUE = "width";
	static final String LENGHT_VALUE = "lenght";
	static final String HEIGHT_VALUE = "height";
	private Button mButtonAccept;
	private EditText mWidth, mLenght, mHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_room);

		mButtonAccept = (Button) findViewById(R.id.button_add);
		mButtonAccept.setOnClickListener(this);

		mWidth = (EditText) findViewById(R.id.input_w);
		mLenght = (EditText) findViewById(R.id.input_l);
		mHeight = (EditText) findViewById(R.id.input_h);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_add:
			Intent result = new Intent();
			result.putExtra(WIDTH_VALUE, getDoubleValue(mWidth));
			result.putExtra(LENGHT_VALUE, getDoubleValue(mLenght));
			result.putExtra(HEIGHT_VALUE, getDoubleValue(mHeight));
			setResult(Activity.RESULT_OK, result);
			finish();
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