package com.prcse.jamjar;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.prcse.protocol.CustomerInfo;
import com.prcse.protocol.Request;
import com.prcse.utils.PrcseConnection;
import com.prcse.utils.ResponseHandler;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;

import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityLogin extends Activity implements OnClickListener, OnClosedListener, OnOpenedListener {

	private ActionBar actionBar;
	private CustomerInfo customer;
	private EditText editTextEmail = null;
	private EditText editTextPassword = null;
	private TextView viewTextError = null;
	private Button btnLogin = null;
	private TextView textRegister = null;;
	private JarLid appState;
	private String errorState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		appState = ((JarLid)this.getApplication());
		
		editTextEmail = (EditText)findViewById(R.id.email);
		editTextPassword = (EditText)findViewById(R.id.password);
		viewTextError = (TextView)findViewById(R.id.errorText);
		btnLogin = (Button)findViewById(R.id.login);
		textRegister = (TextView)findViewById(R.id.register);
		
		btnLogin.setOnClickListener(this);
		textRegister.setOnClickListener(this);
		
		menuTraySetUp();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
    	
    	switch (item.getItemId()) {
			case android.R.id.home:
				MenuTraySingleton.getInstance().getMenu_tray().toggle();
				break;
		}
		
		return true;
	}

	@Override
	public void onClick(View view) {
		
		Intent intent = null;
    	
		//Toggles sliding menu if open
    	if(MenuTraySingleton.getInstance().getMenu_tray().isMenuShowing()){
    		MenuTraySingleton.getInstance().getMenu_tray().toggle();
    	}
    	
		int id = view.getId();
		
		//Implementation of OnClickListener. Starts an appropriate intent, using flags to prevent duplicate activities
    	//being created and wasting memory.
		switch(id) {
		case R.id.login:
			setErrorState(null);
			executeLogin();
			break;
		case R.id.register:
			intent = new Intent(this, ActivityRegister.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
    		overridePendingTransition(0,0);
    		startActivity(intent);
			break;
		case R.id.profile:
    		if (appState.isLoggedIn())
    		{
    			intent = new Intent(view.getContext(), ActivityProfile.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
    			startActivity(intent);
    		}
    		else 
    		{
    			intent = new Intent(view.getContext(), ActivityLogin.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
    			startActivity(intent);
    		}
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
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
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

	private void executeLogin() {
		//Creates a customer info object using the email and password
		customer = new CustomerInfo(editTextEmail.getText().toString(),
									editTextPassword.getText().toString());
		
		
		//Connects to the database using the login protocol to check credentials
		appState.getConnection().login(customer, new ResponseHandler() {

			@Override
			public void handleResponse(final Request response) {
				if(response.getError() != null) {
					ActivityLogin.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							setErrorState(response.getError());
						}
						
					});
				}
				else {
					appState.setUser((CustomerInfo)response);
					finish();
				}
			}
			
		});
	}
	
	public String getErrorState() {
		return errorState;
	}

	public void setErrorState(String errorState) {
		if(errorState != null) {
			this.errorState = errorState;
			btnLogin.setEnabled(true);
			viewTextError.setVisibility(View.VISIBLE);
			viewTextError.setText(customer.getError());
		}
		else {
			this.errorState = "";
			btnLogin.setEnabled(false);
			viewTextError.setText("");
		}
	}
	
	public CustomerInfo getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerInfo customer) {
		this.customer = customer;
	}
	
	private void menuTraySetUp() {
		
		//Get variables needed to calculate width of display. Also some nifty stuff with the action bar button.
		actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        Display display = getWindowManager().getDefaultDisplay();
		Point point = new Point();
		
		display.getSize(point);
		int width = point.x;
        
		//Call instance of singleton and its setup method
        MenuTraySingleton.getInstance().menuTraySetUp(this, width);
    	
        //Attach listeners to the sliding menu
    	MenuTraySingleton.getInstance().getMenu_tray().setOnOpenedListener(this);
        MenuTraySingleton.getInstance().getMenu_tray().setOnClosedListener(this);
		
		RelativeLayout menu_profile_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.profile);
		RelativeLayout menu_spotlight_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.spotlight);
		RelativeLayout menu_search_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.search);
		RelativeLayout menu_artists_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.artists);
		RelativeLayout menu_venues_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.venues);
		RelativeLayout menu_tours_btn = (RelativeLayout) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.tours);
		
		//Set listeners to clickable things
		menu_profile_btn.setOnClickListener(this);
		menu_spotlight_btn.setOnClickListener(this);
		menu_search_btn.setOnClickListener(this);
		menu_artists_btn.setOnClickListener(this);
		menu_venues_btn.setOnClickListener(this);
		menu_tours_btn.setOnClickListener(this);
		
		//Change background colour of menu item representing profile activity to make it stand out
		menu_profile_btn.setBackgroundColor(Color.parseColor("#7f4993"));
		
		//Check to see if user is logged in and if so places profile picture in the sliding menu next to the profile selection
		if (appState.isLoggedIn())
		{
			TextView menu_profile_text = (TextView) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.profile_text);
			ImageView menu_profile_icon = (ImageView) MenuTraySingleton.getInstance().getMenu_tray().findViewById(R.id.profile_icon);
			
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
		//Makes little arrow next to action bar icon disappear. Mainly because it drove me insane that it didn't do that. - Vlad.
		actionBar.setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public void onClosed() 
	{
		//Makes little arrow next to action bar icon appear when menu is closed. Mainly because it drove me insane that it didn't do that. - Vlad
		actionBar.setDisplayHomeAsUpEnabled(true);
	}	
}
