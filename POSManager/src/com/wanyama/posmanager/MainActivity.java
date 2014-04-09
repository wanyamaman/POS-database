package com.wanyama.posmanager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.wanyama.helper.MasterDatabaseAdapter;
import com.wanyama.model.Product;
import com.wanyama.model.Stall;

public class MainActivity extends Activity {

	MasterDatabaseAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postransaction);
		
		db = new MasterDatabaseAdapter(getApplicationContext());
		
		db.dropDatabase();

		
		//creating products
		Product eggs = new Product(1, 6);
		Product fish = new Product(10, 40);
		Product oil = new Product(100, 20);
		Product cheese = new Product(8, 13);
		
		// insert products into database
		long eggs_id = db.createProduct(eggs);
		long fish_id = db.createProduct(fish);
		long oil_id = db.createProduct(oil);
		long cheese_id = db.createProduct(cheese);
//		long minus = db.createProduct(eggs);
		
		Log.d("Product", "" +eggs_id);
		Log.d("Product", "" +fish_id);
		Log.d("Product", "" +oil_id);
		Log.d("Product", "" +cheese_id);
//		Log.d("Product", "" +minus);
		Log.d("Product count", "Number in Stock: "+ db.getAllProducts().size());
		
		Stall halaal = new Stall(1, 15);
		Stall vegetarian = new Stall(2, 10);
		Stall chicken = new Stall(3, 30);
		Stall beef = new Stall(4, 25);
		
		long h_id = db.createStall(halaal);
		long v_id = db.createStall(vegetarian);
		long c_id= db.createStall(chicken);
		long b_id = db.createStall(beef);		
		
		Log.d("Stall", "" +halaal);
		Log.d("Stall", "" +vegetarian);
		Log.d("Stall", "" +chicken);
		Log.d("Stall", "" +beef);
		Log.d("Stall count", "Number of stands: "+ db.getAllStalls().size());
		Log.d("halaal stand", ""+halaal);
		
		// make purchases
		long halaal_sale1 = db.makePurchase(halaal.getId(), cheese.getId());
		long halaal_sale2 = db.makePurchase(halaal.getId(), oil.getId());
		long chicken_sale1 = db.makePurchase(chicken.getId(), eggs.getId());
		long halaal_sale3 =  db.makePurchase(halaal.getId(), cheese.getId());
		
//		Log.d("Stall count", "Number of stands: "+ db.getAllPurchases().size());
		//close the database
		db.closeDB();
	}
}
