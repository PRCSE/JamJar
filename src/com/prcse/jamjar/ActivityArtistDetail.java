package com.prcse.jamjar;

import java.util.ArrayList;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Tour;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityArtistDetail extends Activity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail);
		
		
		Artist artist = (Artist) getIntent().getExtras().get("artist");
		//ArrayList<Tour> tours = (ArrayList<Tour>) artist.getTours();
		TextView artistBio = (TextView) findViewById(R.id.artist_bio);
//		Spinner toursSpinner = (Spinner) findViewById(R.id.tour_filter_spinner);
//		ArrayAdapter<String> toursArrayAdapter = new ArrayAdapter<String>(this, 0);
//		
//		
//		setTitle(artist.getName());
//		artistBio.setText(artist.getBio());
//		toursArrayAdapter.addAll(tours.toString());
//		toursSpinner.setAdapter(toursArrayAdapter);
//		toursSpinner.setOnItemClickListener(this);
		
		
		
		
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
