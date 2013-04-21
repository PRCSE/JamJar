package com.prcse.jamjar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ActivitySeatPicker extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seat_picker);
		setTitle("Seat Picker");
		
		createSeatingPlan();
	}

	private void createSeatingPlan() {
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seat_picker, menu);
		return true;
	}

}
