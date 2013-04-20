package com.prcse.jamjar;

import com.prcse.datamodel.Customer;
import com.prcse.protocol.CustomerForm;
import com.prcse.protocol.CustomerInfo;
import com.prcse.utils.PrcseConnection;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ActivityRegister extends Activity implements OnClickListener, OnItemSelectedListener, OnKeyListener {
	
	private CustomerInfo customer;
	private PrcseConnection connection;
	private String host = "77.99.8.110";
	private int port = 1234;
	
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
	
	//View registerView;
	
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
		
		connection = new PrcseConnection(host, port);
		
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

		
		//connection = new PrcseConnection(host, port);
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
		Intent intent;
		
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
		
		new Connector().execute(connection);
		
		while(customer.getCustomer() == null || customer.getError() == null) {
			if(customer.getError() != null) {
				btnRegister.setEnabled(true);
				viewTextError.setText(customer.getError());
			}
			else {
				// TODO copy to login activity
				intent = new Intent(this, ActivityProfile.class);
				intent.putExtra("customerObj", customer);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		}
	}
	
	private class Connector extends AsyncTask<PrcseConnection, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(PrcseConnection... params) {
			PrcseConnection connection = params[0];
			try {
				connection.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return connection.isConnected();
		}
		
		protected void onPostExecute(Boolean result) {
    		if(result == true) {
    			new RunRegister().execute(connection);
    		}
    	}
    }
	
	private class RunRegister extends AsyncTask<PrcseConnection, Integer, CustomerInfo> {
    	@Override
		protected CustomerInfo doInBackground(PrcseConnection... params) {
			PrcseConnection connection = params[0];
			CustomerInfo customer = ActivityRegister.this.getCustomer();
			
			try {
				connection.syncCustomer(customer);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return customer;
		}
    	
    	protected void onPostExecute(CustomerInfo result) {
    		ActivityRegister.this.setCustomer(result);
    	}
    }
	
//	private class getEnums extends AsyncTask<PrcseConnection, Integer, CustomerForm> {
//    	@Override
//		protected CustomerForm doInBackground(PrcseConnection... params) {
//			PrcseConnection connection = params[0];
//			CustomerInfo customer = ActivityRegister.this.getCustomer();
//			
//			try {
//				connection.syncCustomer(customer);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return customer;
//		}
//    	
//    	protected void onPostExecute(CustomerInfo result) {
//    		ActivityRegister.this.setCustomer(result);
//    	}
//    }

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
