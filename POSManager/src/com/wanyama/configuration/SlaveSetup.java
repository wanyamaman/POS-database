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

import com.wanyama.helper.MasterDatabaseAdapter;
import com.wanyama.model.Stall;
import com.wanyama.posmanager.HomeScreen;
import com.wanyama.posmanager.ProductMenu;
import com.wanyama.posmanager.R;

public class SlaveMain extends Activity {

	private Button homeBtn;
	private Button orderMenuBtn;
	private Button update;
	private TextView inputNumber;
	private TextView currentNumber;
	private int number;
	private MasterDatabaseAdapter dbAdapter;
		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.slave_main_menu);
	    dbAdapter = new MasterDatabaseAdapter(getApplicationContext());
	    
	    // initialize layout
	    homeBtn = (Button) findViewById(R.id.homeReturn);
	    orderMenuBtn = (Button) findViewById(R.id.menu);
	    update = (Button) findViewById(R.id.updateButton);
	    inputNumber = (TextView) findViewById(R.id.stall_id);
	    currentNumber = (TextView) findViewById(R.id.number);
	    
	    // update number in layout
	    updateNumber();
	    
	    // BUTTON LISTENERS
	    // returns to home screen
	    homeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Intent launchHomeScreen = new Intent(SlaveMain.this,
						HomeScreen.class);
				startActivity(launchHomeScreen);
				
			}
		});
	    
	    // navigates to product order menu
	    orderMenuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Intent launchProductMenu = new Intent(SlaveMain.this,
						ProductMenu.class);
				startActivity(launchProductMenu);
				
			}
		});
	    
	    // updates stall number
	    update.setOnClickListener(new OnClickListener() {
			 
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
							// clear sales from old stall (alternative: update table number in purchase list)
							dbAdapter.deleteAllPurchases(); 
							dbAdapter.createStall(new Stall(number, 0));
						}
					updateNumber();
				 }
				
			}
		});	    
	    
	}
	
	public void updateNumber(){
		// set number to default value: 0000
		if (dbAdapter.countStalls() <= 0){
			Toast.makeText(getApplicationContext(), "no stall record exists",
					Toast.LENGTH_LONG).show();
			currentNumber.setText(""+99);
		} else {
			// update display number from stall table entry			
			currentNumber.setText(""+dbAdapter.getAllStalls().get(0).getNumber());
		}
	}

}
