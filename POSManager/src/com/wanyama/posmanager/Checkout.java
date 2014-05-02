package com.wanyama.posmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wanyama.helper.MasterDatabaseAdapter;
import com.wanyama.model.Product;

public class Checkout extends Activity implements OnItemClickListener {

	private MasterDatabaseAdapter db;
	private Button checkoutBtn;
	private Button menuReturnBtn;
	private ListView list;
	private List<String> orderArray;
	private StringTokenizer tokens;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.checkout_orders);

	    // initiate database
	    db = new MasterDatabaseAdapter(getApplicationContext());
	    
	    checkoutBtn = (Button) findViewById(R.id.checkoutBtn);
	    menuReturnBtn = (Button) findViewById(R.id.menuReturnBtn);
	    list = (ListView) findViewById(R.id.list_view);
	    
	    // setup order list
	    orderArray = new ArrayList<String>();
	    getReceipt(); // fills up orderArray with receipt output
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, orderArray);
	    list.setAdapter(adapter);
	    list.setOnItemClickListener(this);
	    
	    // navigate to product menu
	    menuReturnBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				db.closeDB();
				Intent launchMenu = new Intent(Checkout.this,
						ProductMenu.class);
				startActivity(launchMenu);
				
			}
		});
	    
	    
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		String code;
		String viewString = ((TextView) view).getText().toString();
		tokens = new StringTokenizer(viewString);
		
		// iterate through list view text
		tokens.nextToken(); // skip code label
		code = tokens.nextToken(); // product code value
		
		// close database before navigating 
		db.closeDB();
		
		Intent launchOrderItem = new Intent(Checkout.this,
				OrderItem.class);
		launchOrderItem.putExtra("code", code);	// pass code to another activity
		startActivity(launchOrderItem);	// launch new activity
		
	}
	
	// displays receipt of ordered items
	public List<String> getReceipt(){
		String temp = "";
		Product item = null;
		
		// fetch list of products
		List<Product> products = db.getAllProducts();
		Log.i("Checkout", "fetched all products + count=" + products.size());
		// get iterator for product list
		Iterator<Product> iterator = products.iterator();
		while (iterator.hasNext()){
			item = iterator.next();
			// format receipt display before outputting
			temp = "CODE: " +item.getCode() + " @R"+item.getPrice() +"  x"+db.countOrders(item.getCode());
			Log.i("Checkout", temp);
			orderArray.add(temp);
		}
		
		Log.i("Checkout", "Size of orderArray: "+orderArray.size());
		return orderArray;
		
	}

}
