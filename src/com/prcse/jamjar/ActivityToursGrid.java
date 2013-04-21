package com.prcse.jamjar;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class ActivityToursGrid extends Activity implements OnClickListener, OnClosedListener, OnOpenedListener {
	
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tours_grid);
		setTitle(R.string.title_activity_tours);
		
		menuTraySetUp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tour_grid, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
    	
    	switch (item.getItemId()) {
			case android.R.id.home:
				intent = new Intent(this, ActivitySpotlight.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		    	overridePendingTransition(0,0);
		    	startActivity(intent);
				break;
		}
		
		return true;
	}
	
	private void menuTraySetUp() {
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Display display = getWindowManager().getDefaultDisplay();
		Point point = new Point();
		
		display.getSize(point);
		int width = point.x;
        
        MenuTraySingleton.getInstance().menuTraySetUp(this, width);
    	
    	MenuTraySingleton.getInstance().getMenu_tray().setOnOpenedListener(this);
        MenuTraySingleton.getInstance().getMenu_tray().setOnClosedListener(this);
		
		RelativeLayout menu_profile_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.profile);
		RelativeLayout menu_spotlight_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.spotlight);
		RelativeLayout menu_search_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.search);
		RelativeLayout menu_artists_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.artists);
		RelativeLayout menu_venues_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.venues);
		RelativeLayout menu_tours_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.tours);
		
		menu_profile_btn.setOnClickListener(this);
		menu_spotlight_btn.setOnClickListener(this);
		menu_search_btn.setOnClickListener(this);
		menu_artists_btn.setOnClickListener(this);
		menu_venues_btn.setOnClickListener(this);
		menu_tours_btn.setOnClickListener(this);
		
		menu_tours_btn.setBackgroundColor(Color.parseColor("#7f4993"));
	}
	
    @Override
    public void onClick(View v)
    {
    	Intent intent = null;
    	
    	if(MenuTraySingleton.getInstance().getMenu_tray().isMenuShowing()){
    		MenuTraySingleton.getInstance().getMenu_tray().toggle();
    	}
    	
    	switch(v.getId()){
    	
    	case R.id.profile:
    		intent = new Intent(v.getContext(), ActivityProfile.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    		
    	case R.id.spotlight:
    		intent = new Intent(v.getContext(), ActivitySpotlight.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    		
    	case R.id.search:
    		intent = new Intent(v.getContext(), ActivitySearch.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
    		startActivity(intent);
    		break;
    		
    	case R.id.artists:
    		intent = new Intent(v.getContext(), ActivityArtistsGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    		
    	case R.id.venues:
    		intent = new Intent(v.getContext(), ActivityVenuesGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    			
    	case R.id.tours:
    		intent = new Intent(v.getContext(), ActivityToursGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    	}
    }

	@Override
	public void onOpened() {
		// TODO Auto-generated method stub
		actionBar.setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public void onClosed() {
		// TODO Auto-generated method stub
		actionBar.setDisplayHomeAsUpEnabled(true);
	}	
	
}
