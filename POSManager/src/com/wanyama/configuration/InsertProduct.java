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
import com.wanyama.model.Product;
import com.wanyama.posmanager.R;

public class InsertProduct extends Activity {

	private MasterDatabaseAdapter dbAdapter;
	private Context ctx;
	private long productCount;
	private TextView code;
	private TextView price;
	private TextView count;
	private Button menuReturn;
	private Button submit;
	private Button reset;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);

		// Initialize variables
		ctx = this;
		dbAdapter = new MasterDatabaseAdapter(ctx);

		// assign layout objects to views
		code = (TextView) findViewById(R.id.code);
		price = (TextView) findViewById(R.id.price);
		count = (TextView) findViewById(R.id.count);
		submit = (Button) findViewById(R.id.submit);
		menuReturn = (Button) findViewById(R.id.menu);
		reset = (Button) findViewById(R.id.reset);

		// adds a product into the database
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// check for valid product code
				if (TextUtils.isEmpty(code.getText().toString())) {
					Toast.makeText(ctx, "please enter a product code",
							Toast.LENGTH_LONG).show();
				} else // check: validate product price 
					if(TextUtils.isEmpty(price.getText().toString())) {
					Toast.makeText(ctx, "please enter a product price",
							Toast.LENGTH_LONG).show();
				}				
				else {
					// valid: insert into database
					dbAdapter.createProduct(new Product(Integer.parseInt(code
							.getText().toString()), Integer.parseInt(price
									.getText().toString())));
					// update count
					updateCount();
					Toast.makeText(ctx, "number of products: " + productCount,
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// return to the main menu
		menuReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent launchMasterMain = new Intent(InsertProduct.this,
						MasterMain.class);
				startActivity(launchMasterMain);
			}
		});

		// delete all entered products
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// clear all entries in stall table
				dbAdapter.deleteAllProducts();
				updateCount();

			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		dbAdapter.closeDB();

	}

	@Override
	protected void onStop() {
		super.onStop();
		dbAdapter.closeDB();
	}

	public long updateCount() {
		productCount = dbAdapter.countProducts();
		count.setText("" + dbAdapter.countProducts());
		return productCount;
	}

}
