package com.wanyama.configuration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wanyama.database.DatabaseAdapter;
import com.wanyama.model.Stall;
import com.wanyama.posmanager.R;

public class EditStall extends Activity {

	// layout variables
	private Button updateBtn;
	private Button stallListBtn;
	private Button settingsBtn;
	private TextView vNumber;
	private TextView vBalance;
	private TextView inputNumber;
	// stall variables
	private String originalNumber;
	private int number;
	private int balance;
	private Stall stall;
	// database variables
	private DatabaseAdapter dbAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.edit_stall);
	    
	    updateBtn = (Button) findViewById(R.id.update);
	    stallListBtn = (Button) findViewById(R.id.stallList);
	    settingsBtn = (Button) findViewById(R.id.settings);
	    vNumber = (TextView) findViewById(R.id.number);
	    vBalance = (TextView) findViewById(R.id.balance);
	    inputNumber = (TextView) findViewById(R.id.numberInput);
	    
	    // fetch calling stall number
	    Intent i = getIntent();
	    originalNumber = i.getStringExtra("number");
	    
	    // initiate database and fetch stall info
	    dbAdapter = new DatabaseAdapter(getApplicationContext());
		// convert passed intent values to display	
		setupStallValues();
	    updateDisplay();
	    
	    // validates new stall number before updating old
	    updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// check for blank fields
				if (TextUtils.isEmpty(inputNumber.getText().toString())) {
					Toast.makeText(getApplicationContext(), "please enter a valid stall number",
							Toast.LENGTH_LONG).show();
				} else // update database entry
				{
					// fetch old stall object
					stall = dbAdapter.getStallByNumber(number);
					// change number to new value
					stall.setNumber(Integer.parseInt(inputNumber.getText().toString()));
					
					// insert updated stall info into database
					dbAdapter.updateStall(stall);
					number = stall.getNumber();
					balance = stall.getBalance();
					updateDisplay();
				}
				

				// update display
				updateDisplay();
			}
		});
	    
	    // returns to list of stalls
	    stallListBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Intent launchStallList = new Intent(EditStall.this, ViewStalls.class);
				startActivity(launchStallList);
				
			}
		});
	    
	    // returns to settings menu
	    settingsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Intent launchSettings = new Intent(EditStall.this, Settings.class);
				startActivity(launchSettings);
				
			}
		});
	   
	}
	
	// converts string of stall number to integer
	public void setupStallValues(){
		try {
			number = Integer.parseInt(originalNumber);
		}catch (NumberFormatException e){
			Log.d("EditStall", "unable to convert stall number to integer value");
			e.printStackTrace();
			number = 911;
		}
	}
	
	// updates display values
	public void updateDisplay(){
		// set vNumber value
		vNumber.setText(String.valueOf(number));
		// set vBalance value
		vBalance.setText(String.valueOf(balance));
		
	}

}
