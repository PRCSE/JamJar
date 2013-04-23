package com.prcse.jamjar;

import java.util.Observable;
import java.util.Observer;

import com.prcse.datamodel.Customer;
import com.prcse.protocol.CustomerForm;
import com.prcse.protocol.CustomerInfo;
import com.prcse.protocol.Request;
import com.prcse.utils.ResponseHandler;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenedListener;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityRegister extends Activity implements OnClickListener, OnItemSelectedListener, OnClosedListener, OnOpenedListener {
	
	private ActionBar actionBar;
	
	private CustomerInfo customer;
	private CustomerForm enumsData;
	private JarLid appState;
	private boolean gettingEnums = true;
	
	private String passCheck = null;
	private String tempEmail = null;
	
	private EditText editTextEmail = null;
	private EditText editTextPassword = null;
	private EditText editTextPasswordConfirm = null;
	private Spinner spinnerTitle = null;
	private EditText editTextForename = null;
	private EditText editTextSurname = null;
	private EditText editTextTelephone = null;
	private EditText editTextMobile = null;
	private EditText editTextAddress1 = null;
	private EditText editTextAddress2 = null;
	private EditText editTextTown = null;
	private EditText editTextCounty = null;
	private EditText editTextPostCode = null;
	private Spinner spinnerCountry = null;
	private TextView viewTextError = null;
	private Button btnRegister = null;
	
	private ArrayAdapter<String> titleAdapter;
	private ArrayAdapter<String> countryAdapter;
	
	public CustomerInfo getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerInfo customer) {
		this.customer = customer;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		titleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		
		editTextEmail = (EditText)findViewById(R.id.txtEmailReg);
		editTextPassword = (EditText)findViewById(R.id.txtPasswordReg);
		editTextPasswordConfirm = (EditText)findViewById(R.id.txtPasswordRegConfirm);
		spinnerTitle = (Spinner)findViewById(R.id.spnTitle);
		editTextForename = (EditText)findViewById(R.id.txtForename);
		editTextSurname = (EditText)findViewById(R.id.txtSurname);
		editTextTelephone = (EditText)findViewById(R.id.txtTelephone);
		editTextMobile = (EditText)findViewById(R.id.txtMobile);
		editTextAddress1 = (EditText)findViewById(R.id.txtAddress1);
		editTextAddress2 = (EditText)findViewById(R.id.txtAddress2);
		editTextTown = (EditText)findViewById(R.id.txtTown);
		editTextCounty = (EditText)findViewById(R.id.txtCounty);
		editTextPostCode = (EditText)findViewById(R.id.txtPostCode);
		spinnerCountry = (Spinner)findViewById(R.id.spnCountry);
		
		btnRegister = (Button)findViewById(R.id.btnRegister);
		
		spinnerTitle.setEnabled(false);
		spinnerCountry.setEnabled(false);
		spinnerTitle.setOnItemSelectedListener(this);
		spinnerCountry.setOnItemSelectedListener(this);
		btnRegister.setOnClickListener(this);
		
		appState = ((JarLid)this.getApplication());
		
		appState.getConnection().addObserver(new Observer() {
			
			@Override
			public void update(Observable arg0, Object arg1) {
				if(arg1 == appState.IMAGES) {
					// do nothing
				}
				else if(arg1 instanceof CustomerForm && appState.getConnection().isConnected()) {
					if(((CustomerForm)arg1).getError() != null) {
						ActivityRegister.this.runOnUiThread(new Runnable() {
	
							@Override
							public void run() {
								btnRegister.setEnabled(true);
								viewTextError.setText(customer.getError());
							}
							
						});
					}
					else {
						if(gettingEnums == false) {
							ActivityRegister.this.getEnumsData(arg1);
						}
					}
				}
				else if(appState.getConnection().isConnected() == false) {
					//TODO setContentView(R.layout.network_error_alert);
					btnRegister.setEnabled(false);
				}
			}
			
		});
		
		if(appState.getConnection().isConnected()) {
			gettingEnums = true;
			getEnumsData(null);
		}
		else {
			//TODO setContentView(R.layout.network_error_alert);
		}
	}
	
	@Override
    public void onResume()
    {
    	super.onResume();
    	menuTraySetUp();
    }

	public void getEnumsData(Object request) {
		if(request == null) {
			enumsData = new CustomerForm();
			appState.getConnection().getCustomerFormData(enumsData, new ResponseHandler() {
	
				@Override
				public void handleResponse(Request response) {
					ActivityRegister.this.getEnumsData(response);
				}
				
			});
		}
		else if(request instanceof CustomerForm) {
			enumsData = (CustomerForm)request;
		}
		
		if(enumsData.getError() != null || enumsData.getCountries().size() < 1 || enumsData.getCountries().size() < 1) {
			Log.e("Connection Error", "Could not get form fill data.");
			//TODO setContentView(R.layout.network_error_alert);
		}
		else {
			titleAdapter.clear();
			
			// build adapter
			titleAdapter.add("Select Title");
			for (String s : enumsData.getTitles())
			{
				titleAdapter.add(s);
			}
	
			// set spinner adapter
			spinnerTitle.setAdapter(titleAdapter);
			spinnerTitle.setEnabled(true);
			
			titleAdapter.clear();
			
			// build adapter
			countryAdapter.add("Select Country");
			for (String s : enumsData.getCountries())
			{
				countryAdapter.add(s);
			}
			
			// set spinner adapter
			spinnerCountry.setAdapter(countryAdapter);
			spinnerCountry.setEnabled(true);
		}
		gettingEnums = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}
	
	 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
				case android.R.id.home:
					MenuTraySingleton.getInstance().getMenu_tray().toggle();
					break;
			}
			
			return true;
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
		
		//Change background colour of menu item representing the current activity
		menu_spotlight_btn.setBackgroundColor(Color.parseColor("#7f4993"));
		
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
	public void onClick(View view) {
		int id = view.getId();
		
		switch(id) {
		case R.id.btnRegister:
			//TODO add constraints check
			executeRegister();
			btnRegister.setEnabled(false);
			break;
		}
		
	}

	private void executeRegister() {
		customer = new CustomerInfo();
		customer.setCustomer(new Customer(editTextEmail.getText().toString(),
										editTextPassword.getText().toString(),
										spinnerTitle.getSelectedItem().toString(),
										editTextForename.getText().toString(),
										editTextSurname.getText().toString(),
										editTextTelephone.getText().toString(),
										editTextMobile.getText().toString(),
										editTextAddress1.getText().toString(),
										editTextAddress2.getText().toString(),
										editTextTown.getText().toString(),
										editTextCounty.getText().toString(),
										editTextPostCode.getText().toString(),
										spinnerCountry.getSelectedItem().toString(),
										null, //thumbnail URL
										null, //date created
										true)); //new account
		
		appState.getConnection().syncCustomer(customer, new ResponseHandler() {

			@Override
			public void handleResponse(Request response) {
				if(customer.getError() != null) {
					ActivityRegister.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							btnRegister.setEnabled(true);
							viewTextError.setText(customer.getError());
						}
						
					});
				}
				else {
					finish();
				}
			}
			
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> view, View subView, int position,
			long row) {
		// TODO do we need anything here?
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO nope, nada
		
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
