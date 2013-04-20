package com.prcse.jamjar;

import java.util.ArrayList;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Tour;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityArtistDetail extends Activity implements OnItemClickListener {

	private GridView eventGrid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail);
		
		
		Artist artist = (Artist) getIntent().getExtras().get("artist");
		ArrayList<Tour> tours = (ArrayList<Tour>) artist.getTours();
		TextView artistBio = (TextView) findViewById(R.id.artist_bio);
		Spinner toursSpinner = (Spinner) findViewById(R.id.tour_filter_spinner);
		ArrayAdapter<String> toursArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		TextView noEventsMessage = (TextView) findViewById(R.id.event_unavalible_message);
		
		setTitle(artist.getName());
		artistBio.setText(artist.getBio());
		
		if (tours.size() > 0)
		{
			toursArrayAdapter.add("All Events");
			for (Tour t : tours)
			{
				toursArrayAdapter.add(t.getName());
			}
			toursSpinner.setEnabled(true);
		}
		else
		{
			toursArrayAdapter.add("Currently No Scheduled Tours");
			toursSpinner.setEnabled(false);
		}
		
		toursSpinner.setAdapter(toursArrayAdapter);
		
		EventGridAdapter eventGridAdapter = new EventGridAdapter(this, artist);
		eventGrid = (GridView) findViewById(R.id.event_tickets);
        eventGrid.setAdapter(eventGridAdapter);
        eventGridAdapter.notifyDataSetChanged();
        
        if (eventGridAdapter.getCount() == 0)
        {
        	noEventsMessage.setVisibility(View.VISIBLE);
        }
        else
        {
        	noEventsMessage.setVisibility(View.GONE);
        }
        
        eventGrid.setOnItemClickListener(this);
		
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artist_view, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		switch(parent.getId())
		{
		case R.id.tour_filter_spinner:
			// TODO: filter based on selected tour.
			break;
		case R.id.event_tickets:
			viewEvent(position);
			break;
		}
		
	}

	private void viewEvent(int position) {
		
		Toast.makeText(ActivityArtistDetail.this, "" + position, Toast.LENGTH_SHORT).show();
		//Intent intent = new Intent(v.getContext(), ActivityBooking.class);
		//intent.putExtra("artist", ActivityArtistDetail.this.artist);
		
	}

}
