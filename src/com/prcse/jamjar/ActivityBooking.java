package com.prcse.jamjar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import com.prcse.datamodel.Artist;
import com.prcse.datamodel.Billing;
import com.prcse.datamodel.Booking;
import com.prcse.datamodel.Event;
import com.prcse.datamodel.SeatingArea;
import com.prcse.protocol.AvailableSeats;
import com.prcse.protocol.BaseRequest;
import com.prcse.protocol.CustomerBooking;
import com.prcse.protocol.Request;
import com.prcse.utils.ResponseHandler;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityBooking extends Activity implements OnClosedListener, OnOpenedListener, OnClickListener {
	
	private static final int ADD_SEATS = 0;
	private static final int CANCEL = 1;
	
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
	private TextView seatsRemaining;
	private ArrayList<Artist> artists;
	private Button bookBtn;
	private JarLid appState;
	private Artist artist;
	private Event event;
	private AvailableSeats seats;
	
	private Booking booking;
	private ArrayList<Long> selectedSeats;
	
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
		seatsRemaining = (TextView) findViewById(R.id.number_of_tickets);
		Button seatPicker = (Button) findViewById(R.id.seat_picker_button);
		bookBtn = (Button) findViewById(R.id.book_button);
		
		artistName.setText(artist.getName());
		tourName.setText(event.getTourName());
		venueName.setText(event.getSeatingPlan().getVenue().getName());
		cityName.setText(event.getSeatingPlan().getVenue().getPostcode());
		dateString.setText(event.getDateToString());
		
		
		appState.getConnection().addObserver(new Observer(){

			@Override
			public void update(Observable observable, final Object data) {
				
				if (data instanceof CustomerBooking)
				{
					if (event == ((CustomerBooking)data).getBooking().getEvent())
					{
						getAvaliableSeats();
						
						if (((CustomerBooking) data).getClientId() == appState.getConnection().getClientId())
						{
							ActivityBooking.this.runOnUiThread(new Runnable() {

								@Override
								public void run() 
								{
									if (((CustomerBooking) data).getBooking().getCancelled() != null)
									{
										if (((CustomerBooking) data).getBooking().getSeats().size() == 0)
										{
											bookBtn.setEnabled(true);
											bookBtn.setBackgroundColor(getResources().getColor(R.color.text_grey));
										}
										else
										{
											bookBtn.setEnabled(true);
											Toast toast = Toast.makeText(ActivityBooking.this, "Cancellation failed, try again later", Toast.LENGTH_LONG);
											toast.show();
										}
									}
									else
									{
										if (((CustomerBooking) data).getError() != null)
										{
											bookBtn.setEnabled(true);
											bookBtn.setBackgroundColor(getResources().getColor(R.color.dark_purple));
											bookBtn.setText("Booked! (tap again to cancel)");
											appState.getBookings().put(event.getId(), ((CustomerBooking)data));
										}
										else
										{
											bookBtn.setEnabled(true);
											bookBtn.setBackgroundColor(getResources().getColor(R.color.text_grey));
											Toast toast = Toast.makeText(ActivityBooking.this, "Booking failed, try again later", Toast.LENGTH_LONG);
											toast.show();
										}
									}
								}
								
							});
							
						}
					}
				}
			}
			
		});
		
    	getAvaliableSeats();
    	
		
    	booking = new Booking(event);
    	
    	
		seatPicker.setOnClickListener(this);
		bookBtn.setOnClickListener(this);
		
		menuTraySetUp();
	}

	private void getAvaliableSeats() {
		appState.getConnection().getEventAvailability(new AvailableSeats(this.event), new ResponseHandler() {
			
			@Override
			public void handleResponse(final Request response) {
				
				if(response.getError() == null) {
					ActivityBooking.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							seats = ((AvailableSeats) response);
							seatsRemaining.setText(seats.getTotal() + " TICKETS REMAINING");
						}
						
					});
				}
				else {
					Log.e("CONNECTION_ERROR", "failed to get avalible event seats.");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_booking, menu);
		return true;
	}

	//TODO implement new menu thingy
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
    	
		//Toggles sliding menu if open
    	if(menu_tray.isMenuShowing()){
    		menu_tray.toggle();
    	}
    	
    	//Implementation of OnClickListener. Starts an appropriate intent, using flags to prevent duplicate activities
    	//being created and wasting memory.
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
    	case R.id.seat_picker_button:
    		intent = new Intent(ActivityBooking.this, ActivitySeatPicker.class);
    		intent.putExtra("seats", seats);
    		startActivityForResult(intent, RESULT_CANCELED);
    		break;
    	case R.id.book_button:
    		tryBook();
    		break;
    	}
	}


	private void tryBook() {
		
		
		
		boolean shouldBook = true;
		
		for (Long key : appState.getBookings().keySet())
		{
			if (key == event.getId())
			{
				shouldBook = false;
				break;
			}
		}
		
		if (shouldBook == true)
		{
			if (appState.getUser().getCustomer() != null)
			{
				booking = new Booking(event);
				
				if (selectedSeats != null && selectedSeats.size() > 0)
				{
					CustomerBooking bookingProto = new CustomerBooking(booking, appState.getUser().getClientId(), selectedSeats);
					appState.getConnection().createBooking(bookingProto, new ResponseHandler(){

						@Override
						public void handleResponse(Request response) {
							
							getAvaliableSeats();

						}
						
						
					});
					bookBtn.setEnabled(false);
				}
				else 
				{
					Toast toast = Toast.makeText(this, "Please select your seats", Toast.LENGTH_LONG);
					toast.show();
				}
			}
			else
			{
				Toast toast = Toast.makeText(this, "Please login to make a booking", Toast.LENGTH_LONG);
				toast.show();
			}
		}
		else
		{
			CustomerBooking cancelation = appState.getBookings().get(event.getId());
			cancelation.getBooking().cancel();
			appState.getConnection().cancelBooking(cancelation, new ResponseHandler(){

				@Override
				public void handleResponse(Request response) {
					
					getAvaliableSeats();
					
				}
				
			});
		}
		
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == ADD_SEATS)
		{
			ArrayList<SeatingArea> returnedSeats = (ArrayList<SeatingArea>) getIntent().getExtras().get("seats");
			
			for (SeatingArea sa : returnedSeats)
			{
				selectedSeats.add(sa.getId());
			}
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
