package com.wanyama.configuration;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wanyama.helper.MasterDatabaseAdapter;
import com.wanyama.model.Product;
import com.wanyama.posmanager.R;

public class EditProduct extends Activity implements AdapterView.OnItemClickListener {

	// instance variables
	private MasterDatabaseAdapter db;
	private Context ctx;
	private ListView list;
	private List<Product> listArray;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.list_products);
	
	    list = (ListView) findViewById(R.id.list_view);
	    ctx = this;
	    db = new MasterDatabaseAdapter(ctx);
	    listArray = db.getAllProducts();
	    
	    ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(ctx, android.R.layout.simple_list_item_1, listArray);
	    list.setAdapter(adapter);
	    list.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	
		
	}
}
