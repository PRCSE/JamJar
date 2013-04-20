package com.prcse.jamjar;

import java.util.ArrayList;


import com.prcse.datamodel.Artist;
import com.slidingmenu.lib.SlidingMenu;

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
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ActivityArtistsGrid extends Activity implements OnClickListener, OnTouchListener, OnItemClickListener{

	RelativeLayout menu_profile_btn;
	RelativeLayout menu_spotlight_btn;
	RelativeLayout menu_search_btn;
	RelativeLayout menu_artists_btn;
	RelativeLayout menu_venues_btn;
	RelativeLayout menu_tours_btn;
	
	private ActionBar actionBar;
	private SlidingMenu menu_tray;
	private GridView artistGrid;
	private ArtistGridAdapter artistAdapter;
	private ArrayList<Artist> artists;
	private JarLid appState;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists_grid);
        setTitle(R.string.title_activity_artists);
        
        // sets up sliding menu tray
        menuTraySetUp();

        // gets grid, set values to custom adapter then sets adapter to grid.
        // Finally, sets listener for artist select.
        artistGrid = (GridView) findViewById(R.id.artists_grid);
        artistAdapter = new ArtistGridAdapter(this, appState.getImage_base());
        artistGrid.setAdapter(artistAdapter);
        artistGrid.setOnItemClickListener(this);
        
        appState = ((JarLid)this.getApplication());
        artists = appState.getArtists();

        artistGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(ActivityArtistsGrid.this, "" + position, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(v.getContext(), ActivityArtistDetail.class);
				intent.putExtra("artist", artists.get(position));
				startActivity(intent);
			}
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.artists, menu);
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
    	
    	switch (item.getItemId()) {
			case android.R.id.home:
				intent = new Intent(this, ActivitySpotlight.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		overridePendingTransition(0,0);
	    		startActivity(intent);
				break;
		}
		
		return true;
	}
    
	public ArrayList<Artist> getArtists() {
		return artists;
	}

	public void setArtists(ArrayList<Artist> artists) {
		this.artists = artists;
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
		
		menu_profile_btn.setBackgroundColor(Color.parseColor("#7f4993"));
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
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
    		break;
    		
    	case R.id.spotlight:
    		intent = new Intent(view.getContext(), ActivitySpotlight.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
    		break;
    		
    	case R.id.search:
    		intent = new Intent(view.getContext(), ActivitySearch.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(intent);
    		break;
    		
    	case R.id.artists:
    		intent = new Intent(view.getContext(), ActivityArtistsGrid.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
    		break;
    		
    	case R.id.venues:
    		intent = new Intent(view.getContext(), ActivityVenuesGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
    		break;
    			
    	case R.id.tours:
    		intent = new Intent(view.getContext(), ActivityToursGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
    		break;
    	}
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		switch(view.getId())
		{
    	
    	case R.id.profile:
    		menu_profile_btn.setBackgroundColor(Color.parseColor("#7f4993"));
    		break;
    		
    	case R.id.spotlight:
    		menu_spotlight_btn.setBackgroundColor(Color.parseColor("#7f4993"));
    		break;
    		
    	case R.id.search:
    		menu_search_btn.setBackgroundColor(Color.parseColor("#7f4993"));
    		break;
    		
    	case R.id.artists:
    		menu_artists_btn.setBackgroundColor(Color.parseColor("#7f4993"));
    		break;
    		
    	case R.id.venues:
    		menu_venues_btn.setBackgroundColor(Color.parseColor("#7f4993"));
    		break;
    			
    	case R.id.tours:
    		menu_tours_btn.setBackgroundColor(Color.parseColor("#7f4993"));
    		break;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		switch(parent.getId())
		{
		case R.id.artist_venue_grid:
			Intent intent = new Intent(view.getContext(), ActivityArtistDetail.class);
			intent.putExtra("artist", artists.get(position));
			startActivity(intent);
			break;
		}
	}
}
