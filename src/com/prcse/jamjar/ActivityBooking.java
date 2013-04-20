package com.prcse.jamjar;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Event;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ActivityBooking extends Activity {

	private Artist artist;
	private Event event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking);
		setTitle("Event Booking");
		
		artist = (Artist) getIntent().getExtras().get("artist");
		event = (Event) getIntent().getExtras().get("event");
		
		TextView artistName = (TextView) findViewById(R.id.artist_name);
		TextView tourName = (TextView) findViewById(R.id.tour_name);
		TextView venueName = (TextView) findViewById(R.id.venue_name);
		TextView cityName = (TextView) findViewById(R.id.city_name);
		TextView dateString = (TextView) findViewById(R.id.date_string);
		
		artistName.setText(artist.getName());
		//tourName.setText
		venueName.setText(event.getSeatingPlan().getVenue().getName());
		cityName.setText(event.getSeatingPlan().getVenue().getPostcode());
		dateString.setText(event.getDateToString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_booking, menu);
		return true;
	}

}
