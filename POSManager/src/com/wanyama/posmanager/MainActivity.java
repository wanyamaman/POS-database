package com.wanyama.posmanager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.wanyama.helper.SingleHelper;
import com.wanyama.model.Product;

public class MainActivity extends Activity {

	SingleHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postransaction);
		
		db = new SingleHelper(getApplicationContext());
		
		db.clearDatabase(db.getWritableDatabase());

		Product eggs = new Product(55,55);
		Product fish = new Product(10, 40);
		long fish_id = db.createProduct(fish);
		long eggs_idd = db.createProduct(eggs);
		Log.d("Product", "" +eggs_idd);
		Log.d("Product", "" +fish_id);
		
		//creating products
/*		Product eggs = new Product(1, 6);
		Product fish = new Product(10, 40);
		Product oil = new Product(100, 20);
		Product cheese = new Product(8, 13);
		
		// insert products into database
		long eggs_id = db.createProduct(eggs);
		long fish_id = db.createProduct(fish);
		long oil_id = db.createProduct(oil);
		long cheese_id = db.createProduct(cheese);
		long minus = db.createProduct(eggs);
		
		Log.d("Product", "" +eggs_id);
		Log.d("Product", "" +fish_id);
		Log.d("Product", "" +oil_id);
		Log.d("Product", "" +cheese_id);
		Log.d("Product", "" +minus);
		Log.d("Product count", "Number in Stock: "+ db.getAllProducts().size());
		*/
		
		Log.d("Product count", "Number in Stock: "+ db.getAllProducts().size());
		db.closeDB();
	}
}
