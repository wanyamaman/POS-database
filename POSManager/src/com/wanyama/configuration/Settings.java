package com.wanyama.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wanyama.posmanager.HomeScreen;
import com.wanyama.posmanager.R;

public class Settings extends Activity {

	private Context ctx;
	private Button addStall;
	private Button editStall;
	private Button addProduct;
	private Button editProduct;
	private Button homeScreen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.master_menu);
	    ctx = this;
	    
	    addStall = (Button) findViewById(R.id.addStallBtn);
	    editStall = (Button) findViewById(R.id.editStallBtn);
	    addProduct = (Button) findViewById(R.id.addProductBtn);
	    editProduct = (Button) findViewById(R.id.editProductBtn);
	    homeScreen = (Button) findViewById(R.id.menuReturn);
	    
	    // navigate to the add a stall properties screen
	    addStall.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchAddStallActivity = new Intent(Settings.this, InsertStall.class);
				startActivity(launchAddStallActivity);
				
			}
		});
	    
	    // navigate to the edit stall properties screen
	    editStall.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchEditStall = new Intent(Settings.this, ViewStalls.class);
				startActivity(launchEditStall);
				
			}
		});
	    
	    // navigate to the add a product screen
	    addProduct.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchAddProduct= new Intent(Settings.this, InsertProduct.class);
				startActivity(launchAddProduct);
				
			}
		});
	
	    // navigate to the edit product screen
	    editProduct.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchEditProduct = new Intent(Settings.this, ViewProducts.class);
				startActivity(launchEditProduct);
				
			}
		});
	    
	    // navigate back to the homescreen
	    homeScreen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchHomeScreen = new Intent(Settings.this, HomeScreen.class);
				startActivity(launchHomeScreen);
				
			}
		});
	}

}
