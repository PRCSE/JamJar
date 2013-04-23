package com.prcse.jamjar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import com.prcse.protocol.AvailableSeats;
import com.prcse.protocol.CustomerBooking;
import com.prcse.protocol.CustomerInfo;
import com.prcse.protocol.Request;
import com.prcse.utils.ResponseHandler;
import com.prcse.datamodel.Event;
import com.prcse.datamodel.SeatingArea;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class ActivitySeatPicker extends Activity implements OnClickListener {

	private static final int ADD_SEATS = 0;
	private static final int CANCEL = 1;
	
	private JarLid appState;
	private AvailableSeats seats;
	private HashMap<String, SeatingArea> seatHash;
	private ArrayList<SeatingArea> selectedSeats;
	private Button confirmBtn;
	private Button cancelBtn;
	private View standingArea;
	private View row_1_seat_a;
	private View row_1_seat_b;
	private View row_1_seat_c;
	private View row_1_seat_d;
	private View row_1_seat_e;
	private View row_1_seat_f;
	private View row_1_seat_g;
	private View row_1_seat_h;
	private View row_1_seat_i;
	private View row_1_seat_j;
	private View row_2_seat_a;
	private View row_2_seat_b;
	private View row_2_seat_c;
	private View row_2_seat_d;
	private View row_2_seat_e;
	private View row_2_seat_f;
	private View row_2_seat_g;
	private View row_2_seat_h;
	private View row_2_seat_i;
	private View row_2_seat_j;
	private View row_3_seat_a;
	private View row_3_seat_b;
	private View row_3_seat_c;
	private View row_3_seat_d;
	private View row_3_seat_e;
	private View row_3_seat_f;
	private View row_3_seat_g;
	private View row_3_seat_h;
	private View row_3_seat_i;
	private View row_3_seat_j;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seat_picker);
		setTitle(R.string.title_activity_activity_seat_picker);
		appState = ((JarLid)this.getApplication());
		
		findSeatViews();
		confirmBtn = (Button) findViewById(R.id.choose_seating_plan_button);
		cancelBtn = (Button) findViewById(R.id.cancel_seating_plan_button);
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		
		seats = (AvailableSeats) getIntent().getExtras().get("seats");
		
		HashMap<String, SeatingArea> seatHash = new HashMap<String, SeatingArea>();
		ArrayList<SeatingArea> selectedSeats = new ArrayList<SeatingArea>();
		
		appState.getConnection().addObserver(new Observer(){

			@Override
			public void update(Observable observable, Object data) {
				
				if (data instanceof CustomerBooking)
				{
					if (((CustomerBooking)data).getBooking().getEvent() == seats.getEvent())
					{
						appState.getConnection().getEventAvailability(seats, new ResponseHandler(){

							@Override
							public void handleResponse(final Request response) {
								
								if(response.getError() != null) {
									ActivitySeatPicker.this.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											seats = (AvailableSeats)response;
											createSeatingPlan();
										}
										
									});
								}
								else {
									Log.e("CONNECTION ERROR", "couldn't respond to callback");
								}
							}
							
						});
					}
				}
			}
			
		});
		
		createSeatingPlan();
	}

	private void findSeatViews() {
		
		standingArea = (View) findViewById(R.id.standingArea);
		row_1_seat_a = (View) findViewById(R.id.row_1_seat_a);
		row_1_seat_b = (View) findViewById(R.id.row_1_seat_b);
		row_1_seat_c = (View) findViewById(R.id.row_1_seat_c);
		row_1_seat_d = (View) findViewById(R.id.row_1_seat_d);
		row_1_seat_e = (View) findViewById(R.id.row_1_seat_e);
		row_1_seat_f = (View) findViewById(R.id.row_1_seat_f);
		row_1_seat_g = (View) findViewById(R.id.row_1_seat_g);
		row_1_seat_h = (View) findViewById(R.id.row_1_seat_h);
		row_1_seat_i = (View) findViewById(R.id.row_1_seat_i);
		row_1_seat_j = (View) findViewById(R.id.row_1_seat_j);
		row_2_seat_a = (View) findViewById(R.id.row_2_seat_a);
		row_2_seat_b = (View) findViewById(R.id.row_2_seat_b);
		row_2_seat_c = (View) findViewById(R.id.row_2_seat_c);
		row_2_seat_d = (View) findViewById(R.id.row_2_seat_d);
		row_2_seat_e = (View) findViewById(R.id.row_2_seat_e);
		row_2_seat_f = (View) findViewById(R.id.row_2_seat_f);
		row_2_seat_g = (View) findViewById(R.id.row_2_seat_g);
		row_2_seat_h = (View) findViewById(R.id.row_2_seat_h);
		row_2_seat_i = (View) findViewById(R.id.row_2_seat_i);
		row_2_seat_j = (View) findViewById(R.id.row_2_seat_j);
		row_3_seat_a = (View) findViewById(R.id.row_3_seat_a);
		row_3_seat_b = (View) findViewById(R.id.row_3_seat_b);
		row_3_seat_c = (View) findViewById(R.id.row_3_seat_c);
		row_3_seat_d = (View) findViewById(R.id.row_3_seat_d);
		row_3_seat_e = (View) findViewById(R.id.row_3_seat_e);
		row_3_seat_f = (View) findViewById(R.id.row_3_seat_f);
		row_3_seat_g = (View) findViewById(R.id.row_3_seat_g);
		row_3_seat_h = (View) findViewById(R.id.row_3_seat_h);
		row_3_seat_i = (View) findViewById(R.id.row_3_seat_i);
		row_3_seat_j = (View) findViewById(R.id.row_3_seat_j);

		standingArea.setOnClickListener(this);
		row_1_seat_a.setOnClickListener(this);
		row_1_seat_b.setOnClickListener(this);
		row_1_seat_c.setOnClickListener(this);
		row_1_seat_d.setOnClickListener(this);
		row_1_seat_e.setOnClickListener(this);
		row_1_seat_f.setOnClickListener(this);
		row_1_seat_g.setOnClickListener(this);
		row_1_seat_h.setOnClickListener(this);
		row_1_seat_i.setOnClickListener(this);
		row_1_seat_j.setOnClickListener(this);
		row_2_seat_a.setOnClickListener(this);
		row_2_seat_b.setOnClickListener(this);
		row_2_seat_c.setOnClickListener(this);
		row_2_seat_d.setOnClickListener(this);
		row_2_seat_e.setOnClickListener(this);
		row_2_seat_f.setOnClickListener(this);
		row_2_seat_g.setOnClickListener(this);
		row_2_seat_h.setOnClickListener(this);
		row_2_seat_i.setOnClickListener(this);
		row_2_seat_j.setOnClickListener(this);
		row_3_seat_a.setOnClickListener(this);
		row_3_seat_b.setOnClickListener(this);
		row_3_seat_c.setOnClickListener(this);
		row_3_seat_d.setOnClickListener(this);
		row_3_seat_e.setOnClickListener(this);
		row_3_seat_f.setOnClickListener(this);
		row_3_seat_g.setOnClickListener(this);
		row_3_seat_h.setOnClickListener(this);
		row_3_seat_i.setOnClickListener(this);
		row_3_seat_j.setOnClickListener(this);
		
	}

	private void createSeatingPlan() {

		seatHash = new HashMap<String, SeatingArea>();
		
		for (SeatingArea sa : seats.getAvailableSeats())
		{
			if (sa.getParent() == 0)
			{
				seatHash.put("standingArea", sa);
				fillSeat(sa);
			}
			else if (sa.getParent() == 3)
			{
				seatHash.put("row_1_"+sa.getNameAsId(), sa);
				fillSeat(sa);
			}
			else if (sa.getParent() == 4)
			{
				seatHash.put("row_2_"+sa.getNameAsId(), sa);
				fillSeat(sa);
			}
			else
			{
				seatHash.put("row_3_"+sa.getNameAsId(), sa);
				fillSeat(sa);
			}
		}
	}

	private void fillSeat(SeatingArea sa) {
		if (sa.getCapacity() == 0)
		{
			standingArea.setBackgroundColor(getResources().getColor(R.color.black));
			standingArea.setClickable(false);
		}
		else
		{
			standingArea.setBackgroundColor(getResources().getColor(R.color.text_grey));
			standingArea.setClickable(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seat_picker, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		
		Intent intent;
		
		switch(view.getId())
		{
		case R.id.standingArea:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_a:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_b:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_c:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_d:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_e:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_f:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_g:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_h:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_i:
			toggleSeatSelect(view);
			break;
		case R.id.row_1_seat_j:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_a:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_b:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_c:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_d:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_e:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_f:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_g:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_h:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_i:
			toggleSeatSelect(view);
			break;
		case R.id.row_2_seat_j:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_a:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_b:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_c:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_d:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_e:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_f:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_g:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_h:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_i:
			toggleSeatSelect(view);
			break;
		case R.id.row_3_seat_j:
			toggleSeatSelect(view);
			break; 
		case R.id.choose_seating_plan_button:
			intent = new Intent(this, ActivityBooking.class);
			intent.putExtra("seats", selectedSeats);
			setResult(ADD_SEATS, intent);
			startActivity(intent);
			break;
		case R.id.cancel_seating_plan_button:
			intent = new Intent(this, ActivityBooking.class);
			setResult(CANCEL, intent);
			startActivity(intent);
			break;
		}
		
	}


	private void toggleSeatSelect(View view) {
		
//		if (view.getAlpha())
//		{
//			deselect(view);
//		}
//		else
//		{
//			select(view);
//		}
		
	}

	private void select(View view) {
		
		SeatingArea seat = seatHash.get(view.getId());
		
		selectedSeats.add(seat);
		
	}

	private void deselect(View view) {
		
		SeatingArea seat = seatHash.get(view.getId());
		
		Iterator<SeatingArea> i = selectedSeats.iterator();
		
		SeatingArea tempSeat;
		while (i.hasNext())
		{
			tempSeat = (SeatingArea) i.next();
			
			if (seat == tempSeat)
			{
				i.remove();
			}
		}
	}

}
