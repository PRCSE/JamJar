package com.prcse.jamjar;

import java.util.ArrayList;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Event;
import com.prcse.datamodel.Tour;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityArtistDetail extends Activity implements OnClickListener, OnItemClickListener, OnClosedListener, OnOpenedListener {

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
	private GridView eventGrid;
	private Artist artist;
	private EventGridAdapter eventGridAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail);
		
		artist = (Artist) getIntent().getExtras().get("artist");
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
		
		eventGridAdapter = new EventGridAdapter(this, artist);
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
		
        menuTraySetUp();

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
			viewEvent(view, position);
			break;
		}
	}

	private void viewEvent(View view, int position) 
	{
		Toast.makeText(ActivityArtistDetail.this, "" + position, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(view.getContext(), ActivityBooking.class);
		intent.putExtra("artist", ActivityArtistDetail.this.artist);
		intent.putExtra("event", ActivityArtistDetail.this.eventGridAdapter.getItem(position));
		startActivity(intent);
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
	public void onOpened() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
