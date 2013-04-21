package com.prcse.jamjar;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.prcse.protocol.CustomerInfo;
import com.prcse.protocol.Request;
import com.prcse.utils.PrcseConnection;
import com.prcse.utils.ResponseHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityLogin extends Activity implements OnClickListener {

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
			setErrorState(null);
			executeLogin();
			break;
		case R.id.register:
			Intent intent = new Intent(this, ActivityRegister.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
    		overridePendingTransition(0,0);
    		startActivity(intent);
			break;
		}
	}

	private void executeLogin() {
		customer = new CustomerInfo(editTextEmail.getText().toString(),
									editTextPassword.getText().toString());
		
		
		
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
}
