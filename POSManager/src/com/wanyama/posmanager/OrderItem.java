package com.wanyama.posmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wanyama.configuration.SlaveMain;
import com.wanyama.helper.MasterDatabaseAdapter;

public class OrderItem extends Activity {

	private Button add;
	private Button delete;
	private Button menuReturn;
	private Button checkoutBtn;
	private Button setupScreenBtn;
	private TextView vCode;
	private TextView  vPrice;
	private TextView vQuantity;
	private int code;
	private int price;
	private int quantity;
	private MasterDatabaseAdapter db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.order_item);
	
	    Intent i = getIntent();
        // getting attached intent data
        String product_code = i.getStringExtra("code");
        
        // initiate database
        db = new MasterDatabaseAdapter(getApplicationContext());
        
        // initiate layout variables
        add = (Button) findViewById(R.id.addBtn);
        delete = (Button) findViewById(R.id.deleteBtn);
        menuReturn = (Button) findViewById(R.id.menuReturnBtn);
        checkoutBtn = (Button) findViewById(R.id.checkoutBtn);
        setupScreenBtn = (Button) findViewById(R.id.setupReturnBtn);
        
        // makes an order (adds to purchase table)
        add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        
        // deletes an order (deletes from purchase table)
        delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        
        // returns to product list page
        menuReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchProductMenu = new Intent(OrderItem.this,
						ProductMenu.class);
				startActivity(launchProductMenu);
				
			}
		});
        
        // navigate to checkout page
        checkoutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchCheckoutScreen = new Intent(OrderItem.this,
						Checkout.class);
				startActivity(launchCheckoutScreen);
				
			}
		});
        
        // navigate to setup page
        setupScreenBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchSetupScreen = new Intent(OrderItem.this,
						SlaveMain.class);
				startActivity(launchSetupScreen);
				
			}
		});
        
        
	}

}
