package com.wanyama.posmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wanyama.configuration.InsertProduct;
import com.wanyama.configuration.SlaveSetup;
import com.wanyama.database.DatabaseAdapter;
import com.wanyama.model.Product;

public class OrderItem extends Activity {

	// layout variables
	private Button add;
	private Button clear;
	private Button menuReturn;
	private Button checkoutBtn;
	private Button setupScreenBtn;
	private TextView vCode;
	private TextView  vPrice;
	private TextView vQuantity;
	// stall variables
	private int stall_number;
	// product variables
	private Product product;
	private String product_code;
	private int code;
	private int price;
	// purchase variables
	private int quantity;
	//database variables
	private DatabaseAdapter dbAdapter;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.order_item);
	    
	    Log.i("OrderItem", "beginning of onCreate");
	    
	    Intent i = getIntent();
        // getting attached intent data
        product_code = i.getStringExtra("code");
        
        Log.i("OrderItem", "fetched code from intent: "+product_code);
        
        // initialize database
        dbAdapter = new DatabaseAdapter(getApplicationContext());
        setupDatabaseValues();
        setupProductValues();
        orderCount();
        
        // initiate layout variables
        add = (Button) findViewById(R.id.addBtn);
        clear = (Button) findViewById(R.id.clearBtn);
        menuReturn = (Button) findViewById(R.id.menuReturnBtn);
        checkoutBtn = (Button) findViewById(R.id.checkoutBtn);
        setupScreenBtn = (Button) findViewById(R.id.setupReturnBtn);
        vCode = (TextView) findViewById(R.id.code);
        vPrice = (TextView) findViewById(R.id.price);
        vQuantity = (TextView) findViewById(R.id.quantity);
        
        Log.i("OrderItem", "Initialised layout");
        
        // update layout values
        updateDisplay();
        
        // makes an order (adds to purchase table)
        add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dbAdapter.placeOrder(stall_number, code);
				// count number of entries in table
				orderCount();
				// update count and display
				updateDisplay();
			}
		});
        
        // clears all orders
        clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.deleteOrderByCode(code);
				orderCount();
				updateDisplay();

				
			}
		});
        
        // returns to product list page
        menuReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Log.i("OrderItem", "Closed database 1");
				Intent launchProductMenu = new Intent(OrderItem.this,
						InventoryList.class);
				startActivity(launchProductMenu);
				
			}
		});
        
        // navigate to checkout page
        checkoutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Log.i("OrderItem", "Closed database 2");
				Intent launchCheckoutScreen = new Intent(OrderItem.this,
						Checkout.class);
				startActivity(launchCheckoutScreen);
				
			}
		});
        
        // navigate to setup page
        setupScreenBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dbAdapter.closeDB();
				Log.i("OrderItem", "Closed database 3");
				Intent launchSetupScreen = new Intent(OrderItem.this,
						SlaveSetup.class);
				startActivity(launchSetupScreen);
				
			}
		});
    
        
	}
	
	// updates price and quantity values on layout
	public void updateDisplay(){
		// set vCode value
		vCode.setText(String.valueOf(code)); 
		// set vPrice value
		vPrice.setText("R"+String.valueOf(price));
		// set vQuantity value
		vQuantity.setText(String.valueOf(quantity));
	}
	
	// counts the number of orders of this product
	public void orderCount(){
		// set quantity value
		quantity = dbAdapter.countProductOrders(code);
		
	}
	
	// setup stall and product data
	public void setupDatabaseValues(){
		
		if (dbAdapter.countStalls() <=0){
        	// force user to add stall number
        	Toast.makeText(getApplicationContext(), "Please insert a stall number!", Toast.LENGTH_LONG).show();
        	dbAdapter.closeDB();
        	Log.i("OrderItem", "Closed database 5");
        	Intent launchSetup = new Intent(OrderItem.this, SlaveSetup.class);
        	startActivity(launchSetup);
        }
        else if (dbAdapter.countProducts() <=0){
        	// force user to add products
        	Toast.makeText(getApplicationContext(), "Please add products to the database!", Toast.LENGTH_LONG).show();
        	dbAdapter.closeDB();
        	Log.i("OrderItem", "Closed database 6");
        	Intent launchAddProduct = new Intent(OrderItem.this, InsertProduct.class);
        	startActivity(launchAddProduct);
        } else {
        	// get stall identity information
        	stall_number = dbAdapter.getAllStalls().get(0).getNumber();
        	Log.i("OrderItem", "stall number: "+stall_number);
        	
        }
	}
	
	public void setupProductValues(){
		
		// set product code
		try {
		 code =	Integer.parseInt(product_code);
		} catch (NumberFormatException e) {
			Log.d("OrderItem", "unable to convert product code to integer value");
			e.printStackTrace();
			code = 911;
		}
	//	Toast.makeText(getApplicationContext(), String.valueOf(code), Toast.LENGTH_LONG).show();
		
		// set product price
		product = dbAdapter.getProductByCode(code);
		Log.v("OrderItem", product.toString());
		price = product.getPrice();
		
	}

}
