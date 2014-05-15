package com.wanyama.configuration;

import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wanyama.database.DatabaseAdapter;
import com.wanyama.model.Product;
import com.wanyama.posmanager.R;

public class ViewProducts extends Activity implements AdapterView.OnItemClickListener {

	// instance variables
	private DatabaseAdapter db;
	private Context ctx;
	private ListView list;
	private List<Product> listArray;
	private StringTokenizer tokens;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.master_list_products);
	
	    list = (ListView) findViewById(R.id.list_view);
	    ctx = this;
	    db = new DatabaseAdapter(ctx);
	    // fetch array of products
	    listArray = db.getAllProducts();
	    
	    // generate list of product objects
	    ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(ctx, android.R.layout.simple_list_item_1, listArray);
	    list.setAdapter(adapter);
	    list.setOnItemClickListener(this);
	    	    
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		// fetch selection product code and price
		String code;
		String price = "bad fetch";
		String viewString = ((TextView) view).getText().toString();
		
		// iterate through selection text
		tokens = new StringTokenizer(viewString);
		tokens.nextToken();  // skips 'Code' label
		code = tokens.nextToken(); // fetch code VALUE
		tokens.nextToken(); // skips 'Price' label
		price = tokens.nextToken(); // fetch price VALUE
		
		// close database after navigating
		db.closeDB();
		// launch edit screen
		Intent launchEditProduct = new Intent(ViewProducts.this, EditProduct.class);
		launchEditProduct.putExtra("code", code); 
		launchEditProduct.putExtra("price", price);
		startActivity(launchEditProduct);

	}
}
