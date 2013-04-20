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
		
		
		setTitle(artist.getName());
		artistBio.setText(artist.getBio());
		
		toursArrayAdapter.add("All Events");
		for (Tour t : tours)
		{
			toursArrayAdapter.add(t.getName());
		}
		toursSpinner.setAdapter(toursArrayAdapter);
		
		EventGridAdapter eventGridAdapter = new EventGridAdapter(this, artist);
		eventGrid = (GridView) findViewById(R.id.event_tickets);
        eventGrid.setAdapter(eventGridAdapter);
        eventGridAdapter.notifyDataSetChanged();
        String test = "test";
        //eventGridAdapter.setArtist(artist);
//        eventGrid.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//				Toast.makeText(ActivityArtistDetail.this, "" + position, Toast.LENGTH_SHORT).show();
//				//Intent intent = new Intent(v.getContext(), ActivityArtistDetail.class);
//				//intent.putExtra("artist", artists.get(position));
//				//startActivity(intent);
//			}
//        });
		
		
		
		
		
		
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
		}
		
	}

}
