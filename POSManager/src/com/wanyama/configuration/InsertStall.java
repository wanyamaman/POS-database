package com.wanyama.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wanyama.helper.MasterDatabaseAdapter;
import com.wanyama.model.Stall;
import com.wanyama.posmanager.HomeScreen;
import com.wanyama.posmanager.R;


public class InsertStall extends Activity {
	
	private MasterDatabaseAdapter dbAdapter;
	private Context ctx;
	private long stallCount;
	private TextView number;
	private TextView count;
	private Button submit;
	private Button reset;
	private Button menuReturn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.add_stall); //connection to layout
	    // set up variables
	    ctx = this;
	    dbAdapter = new MasterDatabaseAdapter(ctx);
	    
	    // set up view objects
	    number = (TextView) findViewById(R.id.numberInput);
	    count = (TextView) findViewById(R.id.count);
	    menuReturn = (Button) findViewById(R.id.menu);
	    submit = (Button) findViewById(R.id.submitButton);
	    reset = (Button) findViewById(R.id.reset);
	    
	    // read number of fields from database
	    updateCount();
	    
	    // adds a stall into the database
	    submit.setOnClickListener(new OnClickListener() {
			
	    	
			@Override
			public void onClick(View v) {
				// check for valid stall number
				if (TextUtils.isEmpty(number.getText().toString())){
					Toast.makeText(ctx, "please enter a valid stall number", Toast.LENGTH_LONG).show();
				}
				else {
					// insert into database
					dbAdapter.createStall(new Stall(Integer.parseInt(number.getText().toString()), 0));
					// update count
					updateCount();
					Toast.makeText(ctx, "number of stalls: "+stallCount, Toast.LENGTH_LONG).show();
				}			
			}
		});
	    
	    // return to the main menu
	    menuReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchMasterMain = new Intent(InsertStall.this, MasterMain.class);
				startActivity(launchMasterMain);
			}
		});
	    
	    // delete all entered stalls
	    reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// clear all entries in stall table
				dbAdapter.deleteAllStalls();
				updateCount();
				
			}
		});
	    
	  
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		dbAdapter.closeDB();
		
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		dbAdapter.closeDB();
	}
	
    public long updateCount(){
    	stallCount = dbAdapter.countStalls();
    	count.setText(""+dbAdapter.countStalls());
    	return stallCount;
    }
    
}
