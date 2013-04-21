package com.prcse.jamjar;

import java.util.ArrayList;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Event;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityBooking extends Activity implements OnClosedListener, OnOpenedListener, OnClickListener {
	
	private RelativeLayout menu_profile_btn;
	private RelativeLayout menu_spotlight_btn;
	private RelativeLayout menu_search_btn;
	private RelativeLayout menu_artists_btn;
	private RelativeLayout menu_venues_btn;
	private RelativeLayout menu_tours_btn;
	
	private ActionBar actionBar;
	private SlidingMenu menu_tray;
	private GridView artistGrid;
	private ArtistGridAdapter artistAdapter;
	private ArrayList<Artist> artists;
	private JarLid appState;
	private Artist artist;
	private Event event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking);
		setTitle("Event Booking");
		
		appState = ((JarLid)this.getApplication());
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
		
		menuTraySetUp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_booking, menu);
		return true;
	}

	private void menuTraySetUp() {
		actionBar = getActionBar();
		Display display = getWindowManager().getDefaultDisplay();
		Point point = new Point();

		actionBar.setDisplayHomeAsUpEnabled(true);
		display.getSize(point);
		int width = point.x;

		menu_tray = new SlidingMenu(this);
		menu_tray.setMode(SlidingMenu.LEFT);
		menu_tray.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu_tray.setBehindOffset(width / 2);
		menu_tray.setFadeDegree(0.35f);
		menu_tray.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu_tray.setMenu(R.layout.menu_tray);
		
		menu_tray.setOnClosedListener(this);
		menu_tray.setOnOpenedListener(this);
		
		menu_profile_btn = (RelativeLayout) menu_tray.findViewById(R.id.profile);
		menu_spotlight_btn = (RelativeLayout) menu_tray.findViewById(R.id.spotlight);
		menu_search_btn = (RelativeLayout) menu_tray.findViewById(R.id.search);
		menu_artists_btn = (RelativeLayout) menu_tray.findViewById(R.id.artists);
		menu_venues_btn = (RelativeLayout) menu_tray.findViewById(R.id.venues);
		menu_tours_btn = (RelativeLayout) menu_tray.findViewById(R.id.tours);
		
		menu_profile_btn.setOnClickListener(this);
		menu_spotlight_btn.setOnClickListener(this);
		menu_search_btn.setOnClickListener(this);
		menu_artists_btn.setOnClickListener(this);
		menu_venues_btn.setOnClickListener(this);
		menu_tours_btn.setOnClickListener(this);
		
		menu_artists_btn.setBackgroundColor(Color.parseColor("#7f4993"));
	}

	@Override
	public void onClick(View view) 
	{
		Intent intent = null;
    	
    	if(menu_tray.isMenuShowing()){
    		menu_tray.toggle();
    	}
    	
    	switch(view.getId()){
    	
    	case R.id.profile:
    		intent = new Intent(view.getContext(), ActivityProfile.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    		
    	case R.id.spotlight:
    		intent = new Intent(view.getContext(), ActivitySpotlight.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    		
    	case R.id.search:
    		intent = new Intent(view.getContext(), ActivitySearch.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
    		startActivity(intent);
    		break;
    		
    	case R.id.artists:
    		intent = new Intent(view.getContext(), ActivityArtistsGrid.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    		
    	case R.id.venues:
    		intent = new Intent(view.getContext(), ActivityVenuesGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    			
    	case R.id.tours:
    		intent = new Intent(view.getContext(), ActivityToursGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    	}
	}
	
	@Override
	public void onClosed() {
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onOpened() {
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
}
