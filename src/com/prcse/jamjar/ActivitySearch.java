package com.prcse.jamjar;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class ActivitySearch extends Activity implements OnClickListener {

	private ActionBar actionBar;
	private SlidingMenu menu_tray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setTitle(R.string.title_activity_search);
		
		menuTraySetUp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search, menu);
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
		
		RelativeLayout menu_profile_btn = (RelativeLayout) menu_tray.findViewById(R.id.profile);
		RelativeLayout menu_spotlight_btn = (RelativeLayout) menu_tray.findViewById(R.id.spotlight);
		RelativeLayout menu_search_btn = (RelativeLayout) menu_tray.findViewById(R.id.search);
		RelativeLayout menu_artists_btn = (RelativeLayout) menu_tray.findViewById(R.id.artists);
		RelativeLayout menu_venues_btn = (RelativeLayout) menu_tray.findViewById(R.id.venues);
		RelativeLayout menu_tours_btn = (RelativeLayout) menu_tray.findViewById(R.id.tours);
		menu_profile_btn.setOnClickListener(this);
		menu_spotlight_btn.setOnClickListener(this);
		menu_search_btn.setOnClickListener(this);
		menu_artists_btn.setOnClickListener(this);
		menu_venues_btn.setOnClickListener(this);
		menu_tours_btn.setOnClickListener(this);
		
		menu_search_btn.setBackgroundColor(Color.parseColor("#7f4993"));
	}

    @Override
    public void onClick(View v)
    {
    	Intent intent = null;
    	
    	if(menu_tray.isMenuShowing()){
    		menu_tray.toggle();
    	}
    	
    	switch(v.getId()){
    	
    	case R.id.profile:
    		intent = new Intent(v.getContext(), ActivityProfile.class);
			startActivity(intent);
    		break;
    		
    	case R.id.spotlight:
    		intent = new Intent(v.getContext(), ActivitySpotlight.class);
			startActivity(intent);
    		break;
    		
    	case R.id.search:
    		intent = new Intent(v.getContext(), ActivitySearch.class);
    		startActivity(intent);
    		break;
    		
    	case R.id.artists:
    		intent = new Intent(v.getContext(), ActivityArtistsGrid.class);
			startActivity(intent);
    		break;
    		
    	case R.id.venues:
    		intent = new Intent(v.getContext(), ActivityVenuesGrid.class);
			startActivity(intent);
    		break;
    			
    	case R.id.tours:
    		intent = new Intent(v.getContext(), ActivityToursGrid.class);
			startActivity(intent);
    		break;
    	}
    }
}
