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

import com.wanyama.configuration.SlaveMain;
import com.wanyama.helper.MasterDatabaseAdapter;
import com.wanyama.model.Product;

public class ProductMenu extends Activity implements OnItemClickListener   {

	private MasterDatabaseAdapter db;
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
	    db = new MasterDatabaseAdapter(getApplicationContext());
	    
	    // initiate layout variables
	    basketBtn = (Button) findViewById(R.id.basketBtn);
	    setupReturnBtn = (Button) findViewById(R.id.setupBtn);
	    list = (ListView) findViewById(R.id.list_view);
	    
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
				Intent launchSlaveSetup = new Intent(ProductMenu.this,
						Checkout.class);
				startActivity(launchSlaveSetup);
			}
		});
	    
	    // return to setup menu
	    setupReturnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchSlaveSetup = new Intent(ProductMenu.this,
						SlaveMain.class);
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
		tokens.nextToken(); // skip code label
		code = tokens.nextToken(); // product code value
		Intent launchOrderItem = new Intent(ProductMenu.this,
				OrderItem.class);
		launchOrderItem.putExtra("code", code);	// pass code to another activity
		startActivity(launchOrderItem);	// launch new activity
		
		
		
	}

}
