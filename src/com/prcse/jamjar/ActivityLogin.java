package com.prcse.jamjar;

import java.util.ArrayList;

import com.prcse.protocol.CustomerInfo;
import com.prcse.utils.PrcseConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityLogin extends Activity implements OnClickListener {

	private CustomerInfo customer;
	private PrcseConnection connection;
	private String host = "77.99.8.110";
	private int port = 1234;
	private EditText editTextEmail = null;
	private EditText editTextPassword = null;
	private TextView viewTextError = null;
	private Button btnLogin = null;
	
	public CustomerInfo getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerInfo customer) {
		this.customer = customer;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		editTextEmail = (EditText)findViewById(R.id.email);
		editTextPassword = (EditText)findViewById(R.id.password);
		viewTextError = (TextView)findViewById(R.id.errorText);
		btnLogin = (Button)findViewById(R.id.login);
		
		btnLogin.setOnClickListener(this);
		
		connection = new PrcseConnection(host, port);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		
		switch(id) {
		case R.id.login:
			btnLogin.setEnabled(false);
			executeLogin();
			break;
		}
		
		// TODO if the customer wants to create a new account
		// open new activity on click of 'create new account'
		// start new activity with customer form
		// construct customer info object with no parameters
		// set the customer to one you have created from the form
		// execute async connector task and login task in the same manner (no need to change the code for them)
		// while the customerinfo object had null for its customer or error loop
		// if error occurs re enable submit button and print error
		// else take to login page.
	}

	private void executeLogin() {
		customer = new CustomerInfo(editTextEmail.getText().toString(),
									editTextPassword.getText().toString());
				
		new Connector().execute(connection);
		
		while(customer.getCustomer() == null || customer.getError() == null) {
			if(customer.getError() != null) {
				btnLogin.setEnabled(true);
				viewTextError.setText(customer.getError());
			}
			else {
				// TODO close login activity and open customer profile page (passing customer info object)
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return connection.isConnected();
		}
		
		protected void onPostExecute(Boolean result) {
    		if(result == true) {
    			new RunLogin().execute(connection);
    		}
    	}
    }
	
	private class RunLogin extends AsyncTask<PrcseConnection, Integer, CustomerInfo> {
    	@Override
		protected CustomerInfo doInBackground(PrcseConnection... params) {
			PrcseConnection connection = params[0];
			CustomerInfo customer = ActivityLogin.this.getCustomer();
			
			try {
				connection.login(customer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return customer;
		}
    	
    	protected void onPostExecute(CustomerInfo result) {
    		ActivityLogin.this.setCustomer(result);
    	}
    }
}
