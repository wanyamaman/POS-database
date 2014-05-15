package com.wanyama.configuration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wanyama.database.DatabaseAdapter;
import com.wanyama.model.Product;
import com.wanyama.posmanager.R;

public class EditProduct extends Activity {

	// layout variables
	private Button updateCodeBtn;
	private Button updatePriceBtn;
	private Button productListBtn;
	private Button settingsBtn;
	private TextView vCode;
	private TextView vPrice;
	private TextView codeInput;
	private TextView priceInput;
	// product variables
	private String originalCode;
	private String originalPrice;
	private int code;
	private int price;
	private Product product;
	// database variables
	private DatabaseAdapter dbAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.edit_product);
	    
	    // Initialize layout variables
	    updateCodeBtn = (Button) findViewById(R.id.updateCode);
	    updatePriceBtn = (Button) findViewById(R.id.updatePrice);
	    productListBtn = (Button) findViewById(R.id.productList);
	    settingsBtn = (Button) findViewById(R.id.settings);
	    vCode = (TextView) findViewById(R.id.code);
	    vPrice = (TextView) findViewById(R.id.price);
	    codeInput = (TextView) findViewById(R.id.inputCode);
	    priceInput = (TextView) findViewById(R.id.inputPrice);
	    
	    // fetch calling product code and price
	    Intent i = getIntent();
	    originalCode = i.getStringExtra("code");
	    originalPrice = i.getStringExtra("price");
	    
	    // Initiate database and fetch product info
	    dbAdapter = new DatabaseAdapter(getApplicationContext());
	    // convert passed intent values to display
	    setupProductValues();
	    updateDisplay();
	    
	    // BUTTON RESPONSES
	    
	    // validate and update product code
	    updateCodeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
	    
	    // validate and update product price
	    updatePriceBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
	    
	    // returns to list of products
	    productListBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Intent launchProductList = new Intent(EditProduct.this, Settings.class);
				startActivity(launchProductList);				
			}
		});
	    
	    // returns to settings menu
	    settingsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Intent launchSettings = new Intent(EditProduct.this, Settings.class);
				startActivity(launchSettings);				
			}
		});
	
	 
	}
	
	// converts string of product values to integers
	public void setupProductValues(){
		
		try {
			code = Integer.parseInt(originalCode);
			price = Integer.parseInt(originalPrice);
		} catch (NumberFormatException e) {
			Log.d("EditProduct", "unable to convert product code/price to integer");
			e.printStackTrace();
			price = 911;
			code = 911;
		}
	}
	
	// updates display values
	public void updateDisplay(){
		// set vCode value
		vCode.setText(String.valueOf(code));
		// set vPrice value
		vPrice.setText(String.valueOf(price));
	}

}
