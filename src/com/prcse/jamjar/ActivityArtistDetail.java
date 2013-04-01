package com.prcse.jamjar;

import com.prcse.datamodel.Artist;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ActivityArtistDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail);
		
		Artist artist = (Artist) getIntent().getExtras().get("artist");
		TextView artistBio = (TextView) findViewById(R.id.artist_bio);
		
		setTitle(artist.getName());
		artistBio.setText(artist.getBio());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artist_view, menu);
		return true;
	}

}
