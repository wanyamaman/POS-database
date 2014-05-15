package com.wanyama.posmanager;

import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wanyama.configuration.SlaveSetup;
import com.wanyama.database.DatabaseAdapter;
import com.wanyama.model.Product;
import com.wanyama.posmanager.R.color;

public class InventoryList extends Activity implements OnItemClickListener   {

	private DatabaseAdapter db;
	private Button basketBtn;
	private Button setupReturnBtn;
	private ListView list;
	private List<Product> productArray;
	private StringTokenizer tokens;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.slave_list_purchase);
	    
	    // initiate database
	    db = new DatabaseAdapter(getApplicationContext());
	    
	    // initiate layout variables
	    basketBtn = (Button) findViewById(R.id.basketBtn);
	    setupReturnBtn = (Button) findViewById(R.id.setupBtn);
	    list = (ListView) findViewById(R.id.list_view);
	    list.setBackgroundColor(color.black);
	    
	    // set up inventory list
	    productArray = db.getAllProducts();
	    ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(getApplicationContext(), android.R.layout.simple_list_item_1, productArray);
	    list.setAdapter(adapter);
	    list.setOnItemClickListener(this);
	    
	    //populate list with available products
	    
	    
	    // navigate to shopping basket view
	    basketBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				db.closeDB();
				Intent launchSlaveSetup = new Intent(InventoryList.this,
						Checkout.class);
				startActivity(launchSlaveSetup);
			}
		});
	    
	    // return to setup menu
	    setupReturnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				db.closeDB();
				Intent launchSlaveSetup = new Intent(InventoryList.this,
						SlaveSetup.class);
				startActivity(launchSlaveSetup);
				
			}
		});
	    
	
	
	}

	// open item quantity menu
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		String code;
		String viewString = ((TextView) view).getText().toString();
		tokens = new StringTokenizer(viewString);
		
		// iterate through list view text
		tokens.nextToken(); // skip 'CODE' label
		code = tokens.nextToken(); // product code VALUE
		
		// close database before navigating 
		db.closeDB();
		
		Intent launchOrderItem = new Intent(InventoryList.this,
				OrderItem.class);
		launchOrderItem.putExtra("code", code);	// pass code to another activity
		startActivity(launchOrderItem);	// launch new activity
		
		
		
	}

}
