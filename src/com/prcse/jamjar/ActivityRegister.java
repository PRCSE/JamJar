package com.prcse.jamjar;

import java.util.Observable;
import java.util.Observer;

import com.prcse.datamodel.Customer;
import com.prcse.datamodel.Tour;
import com.prcse.protocol.CustomerForm;
import com.prcse.protocol.CustomerInfo;
import com.prcse.protocol.Request;
import com.prcse.utils.PrcseConnection;
import com.prcse.utils.ResponseHandler;

import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityRegister extends Activity implements OnClickListener, OnItemSelectedListener, OnKeyListener {
	
	private CustomerInfo customer;
	private CustomerForm enumsData;
	private JarLid appState;
	
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
	
	private ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	private ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	
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
		
		appState = ((JarLid)this.getApplication());
				
		appState.getConnection().addObserver(new Observer() {
			
			@Override
			public void update(Observable arg0, Object arg1) {
				if(appState.isLoggedIn()) {
					finish();
				}
			}
			
		});
		
		appState.getConnection().getCustomerFormData(enumsData, new ResponseHandler() {

			@Override
			public void handleResponse(Request response) {
				if(customer.getError() != null) {
					Log.e("Connection Error", "Could not get form fill data.");
					finish();
					// TODO send to can't connect page
				}
				else {
					// if result is not 0
					if (enumsData.getTitles().size() > 0)
					{
						// build adapter
						titleAdapter.add("Select Title");
						for (String s : enumsData.getTitles())
						{
							titleAdapter.add(s);
						}
						spinnerTitle.setEnabled(true);
					}
					else
					{
						// set no data
						titleAdapter.add("No Data");
						// disable spinner
						spinnerTitle.setEnabled(false);
					}
					// set spinner adapter
					spinnerTitle.setAdapter(titleAdapter);
					
					// if result is not 0
					if (enumsData.getCountries().size() > 0)
					{
						// build adapter
						countryAdapter.add("Select Country");
						for (String s : enumsData.getCountries())
						{
							countryAdapter.add(s);
						}
						spinnerCountry.setEnabled(true);
					}
					else
					{
						// set no data
						countryAdapter.add("No Data");
						// disable spinner
						spinnerCountry.setEnabled(false);
					}
					// set spinner adapter
					spinnerCountry.setAdapter(countryAdapter);
				}
			}
			
		});
		
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
		
		editTextEmail.setOnKeyListener(this);
		editTextPasswordConfirm.setOnKeyListener(this);
		spinnerTitle.setOnItemSelectedListener(this);
		spinnerCountry.setOnItemSelectedListener(this);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		
		switch(id) {
		case R.id.login:
			//TODO add constraints check
			btnRegister.setEnabled(false);
			executeRegister();
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
										null, //thumbnail URL
										editTextPostCode.getText().toString(),
										spinnerCountry.getSelectedItem().toString(),
										null, //date created
										true)); //new account
		
		appState.getConnection().syncCustomer(customer, new ResponseHandler() {

			@Override
			public void handleResponse(Request response) {
				if(customer.getError() != null) {
					btnRegister.setEnabled(true);
					viewTextError.setText(customer.getError());
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		int id = view.getId();
		
		switch(id) {
		case R.id.txtPasswordRegConfirm:
			passCheck = editTextPasswordConfirm.getText().toString();
			break;
			
		case R.id.txtEmailReg:
			tempEmail = editTextEmail.getText().toString();
			Log.i("Register", "email is: " + tempEmail);
			break;
			
		default:
			break;
				
		}
		
		return false;
	}

}
