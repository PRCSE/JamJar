package com.prcse.jamjar;

import java.util.ArrayList;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Billing;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Activity: Artist Detail. Displays detailed information about an artist. This includes
 * an image, bio, current tours and events.
 */
public class ActivityArtistDetail extends Activity implements OnClickListener, 
								OnItemClickListener, OnClosedListener, OnOpenedListener {

	// ======== CLASS VARIABLES ======================================================== //
	// Application state
	private JarLid appState;
	
	// Menu declarations
	private ActionBar actionBar;
	private RelativeLayout menu_profile_btn;
	private RelativeLayout menu_spotlight_btn;
	private RelativeLayout menu_search_btn;
	private RelativeLayout menu_artists_btn;
	private RelativeLayout menu_venues_btn;
	private RelativeLayout menu_tours_btn;
	
	// UI elements
	private TextView artistBio;
	private Spinner toursSpinner; // Spinner to show artist's tours
	private GridView eventGrid; // Grid to show custom adapter items.
	private TextView noEventsMessage; // displays string if no events.
	
	private Artist artist; // holds the artist to be focused.
	private EventGridAdapter eventGridAdapter; // holds a adapter of artist's events 
	private ArrayAdapter<String> toursArrayAdapter; // holds a adapter of artist's tours
	
	// ================================================================================= //
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail); 	// set layout
		appState = ((JarLid)this.getApplication());			// get snapshot of global state
		artist = (Artist) getIntent().getExtras().get("artist"); /* fetch selected artist *
		 														  * from intent extras to *
		 														  * focus.                */
		
		
		// Get UI elements from res.
		artistBio = (TextView) findViewById(R.id.artist_bio);
		toursSpinner = (Spinner) findViewById(R.id.tour_filter_spinner);
		eventGrid = (GridView) findViewById(R.id.event_tickets);
		noEventsMessage = (TextView) findViewById(R.id.event_unavalible_message);
		

		
		// --- ARTIST --- //
		// set pages artist title and description for focused artist.
		setTitle(artist.getName());
		artistBio.setText(artist.getBio());
		
		
		
		// --- TOURS --- //
		// Parse tour information for focused artist.
		toursArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		ArrayList<Tour> tours = (ArrayList<Tour>) artist.getTours();
		
		if (tours.size() > 0) // IF: the focused artist has tours...
		{
			setToursAdapterWith(tours);
			toursSpinner.setEnabled(true);
		}
		else // else if the artist has no tours, disable spinner
		{
			toursArrayAdapter.add("Currently No Scheduled Tours");
			toursSpinner.setEnabled(false);
		}
		
		toursSpinner.setAdapter(toursArrayAdapter); // set spinner adapter with values
		
		
		
		// --- EVENT --- //
		// Parse event information for focused artist.
		eventGridAdapter = new EventGridAdapter(this, artist);
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

        
        
        // --- LISTENERS --- //
        eventGrid.setOnItemClickListener(this);
		
        
        
        // --- SLIDING TRAY SET UP --- //
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

	private void setToursAdapterWith(ArrayList<Tour> passedTours) 
	{
		toursArrayAdapter.add("All Events");	// set, to be, default menu string.
		
		for (Tour t : passedTours) // FOR EACH: of the artists tours...
		{
			toursArrayAdapter.add(t.getName()); // ... Add tour name to adapter
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
	
	private void menuTraySetUp() 
	{
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        
        // get width of application on device
        Display display = getWindowManager().getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		int width = point.x;
        
		
		// get the instance of the menu tray and pass context and width...
        MenuTraySingleton.getInstance().menuTraySetUp(this, width);
    	MenuTraySingleton.getInstance().getMenu_tray().setOnOpenedListener(this);
        MenuTraySingleton.getInstance().getMenu_tray().setOnClosedListener(this);
		
        
        // Get menu elements (items)
		menu_profile_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.profile);
		menu_spotlight_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.spotlight);
		menu_search_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.search);
		menu_artists_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.artists);
		menu_venues_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.venues);
		menu_tours_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.tours);
		
		
		// set listeners for elements
		menu_profile_btn.setOnClickListener(this);
		menu_spotlight_btn.setOnClickListener(this);
		menu_search_btn.setOnClickListener(this);
		menu_artists_btn.setOnClickListener(this);
		menu_venues_btn.setOnClickListener(this);
		menu_tours_btn.setOnClickListener(this);
		
		
		// set background colour for of relevant location to purple to give orientation to user
		menu_artists_btn.setBackgroundColor(getResources().getColor(R.color.dark_purple));
		
		
		// IF: the user is logged in...
		if (appState.isLoggedIn())
		{
			// change the menu to display there name...
			TextView menu_profile_text = (TextView) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.profile_text);
			ImageView menu_profile_icon = (ImageView) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.profile_icon);
			
			// and, if available, their profile icon. 
			menu_profile_text.setText(appState.getUser().getCustomer().getFullName());
			if (appState.getUser().getCustomer().getThumb() != null)
			{
				menu_profile_icon = (ImageView) findViewById(R.id.profile_icon);
				menu_profile_icon.setImageBitmap(appState.getUser_image());
			}
		}
	}

	@Override
	public void onOpened() 
	{
		actionBar.setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public void onClosed() 
	{
		actionBar.setDisplayHomeAsUpEnabled(true);
	}	

	@Override
	public void onClick(View view) 
	{
		Intent intent = null;
    	
		// close or open menu - depending on current state
    	if(MenuTraySingleton.getInstance().getMenu_tray().isMenuShowing()){
    		MenuTraySingleton.getInstance().getMenu_tray().toggle();
    	}
    	
    	switch(view.getId())
    	{
    	case R.id.profile:
    		if (appState.isLoggedIn())
    		{
    			intent = new Intent(view.getContext(), ActivityProfile.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    			startActivity(intent);
    		}
    		else 
    		{
    			intent = new Intent(view.getContext(), ActivityLogin.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    			startActivity(intent);
    		}
    		break;
    		
    	case R.id.spotlight:
    		intent = new Intent(view.getContext(), ActivitySpotlight.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    		
    	case R.id.search:
    		intent = new Intent(view.getContext(), ActivitySearch.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    		startActivity(intent);
    		break;
    		
    	case R.id.artists:
    		intent = new Intent(view.getContext(), ActivityArtistsGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    		
    	case R.id.venues:
    		intent = new Intent(view.getContext(), ActivityVenuesGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    			
    	case R.id.tours:
    		intent = new Intent(view.getContext(), ActivityToursGrid.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
    		break;
    	}
		
	}
}
