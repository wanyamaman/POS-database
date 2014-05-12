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
import com.wanyama.posmanager.HomeScreen;
import com.wanyama.posmanager.InventoryList;
import com.wanyama.posmanager.R;

public class SlaveSetup extends Activity {

	private Button homeBtn;
	private Button orderMenuBtn;
	private Button updateNumber;
	private Button updateServerInfo;
	private TextView inputNumber;
	private TextView currentNumber;
	private TextView inputIP;
	private TextView inputPort;
	public static String serverIP = "192.168.43.110";
	public static int serverPort = 5500;
	private int number;
	private DatabaseAdapter dbAdapter;
		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.slave_setup);
	    dbAdapter = new DatabaseAdapter(getApplicationContext());
	    
	    // initialize layout
	    homeBtn = (Button) findViewById(R.id.homeBtn);
	    orderMenuBtn = (Button) findViewById(R.id.menu);
	    updateNumber = (Button) findViewById(R.id.updateButton);
	    updateServerInfo = (Button) findViewById(R.id.updateNetInfoBtn);
	    inputNumber = (TextView) findViewById(R.id.stall_id);
	    currentNumber = (TextView) findViewById(R.id.number);
	    inputIP = (TextView) findViewById(R.id.ip);
	    inputPort = (TextView) findViewById(R.id.port);
	    
	    // updateNumber number in layout
	    updateNumber();
	    
	    // BUTTON LISTENERS
	    // returns to home screen
	    homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Intent launchHomeScreen = new Intent(SlaveSetup.this,
						HomeScreen.class);
				startActivity(launchHomeScreen);
				
			}
		});
	    
	    // navigates to product order menu
	    orderMenuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Intent launchProductMenu = new Intent(SlaveSetup.this,
						InventoryList.class);
				startActivity(launchProductMenu);
				
			}
		});
	    
	    // updates stall number
	    updateNumber.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				
				// validate entry
				if (TextUtils.isEmpty(inputNumber.getText().toString())){ // empty input
					Toast.makeText(getApplicationContext(), "please enter a valid stall number",
							Toast.LENGTH_LONG).show();
				} 
				else 
					{
					 updateNumber();
					// convert input stall number to integer
					try {
						number = Integer.parseInt(inputNumber.getText().toString());
					} catch (NumberFormatException e) {
						Toast.makeText(getApplicationContext(), "there was an error convering 0000 to integer",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
						
					}
					
					// insert new stall identity into empty table
					if (dbAdapter.countStalls() == 0){
						dbAdapter.createStall(new Stall(number, 0));
					} else // replace old identity with new one
						{
							dbAdapter.deleteAllStalls();
							// clear sales from old stall (alternative: updateNumber table number in purchase list)
							dbAdapter.deleteAllOrders(); 
							dbAdapter.createStall(new Stall(number, 0));
						}
					updateNumber();
				 }
				
			}
		});	   
	    
	    updateServerInfo.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			// verify non blank fields
				// validate entry
				if (TextUtils.isEmpty(inputIP.getText().toString()) || TextUtils.isEmpty(inputPort.getText().toString())){ // empty input
					Toast.makeText(getApplicationContext(), "please enter an IP address AND a port number",
							Toast.LENGTH_LONG).show();
					Log.w("SlaveSetup", "blank ip or port number");
				}
				else{
					serverIP = inputIP.getText().toString();
					try {
						serverPort = Integer.parseInt(inputPort.getText().toString());
					} catch (NumberFormatException e) {
						Log.w("SlaveSetup", "failed to convert port number to integer");
						e.printStackTrace();
					}
				}
				
			}
		});
	    
	}
	
	public void updateNumber(){
		// set number to default value: 0000
		if (dbAdapter.countStalls() <= 0){
			Toast.makeText(getApplicationContext(), "no stall record exists",
					Toast.LENGTH_LONG).show();
			currentNumber.setText(""+0000);
		} else {
			// updateNumber display number from stall table entry			
			currentNumber.setText(""+dbAdapter.getAllStalls().get(0).getNumber());
		}
	}

}
