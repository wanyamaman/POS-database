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
import com.wanyama.model.Stall;
import com.wanyama.posmanager.R;

public class ViewStalls extends Activity implements AdapterView.OnItemClickListener {

	private DatabaseAdapter db;
	private Context ctx;
	private ListView list;	// layout list
	private List<Stall> listArray;	// array of stall entries
	private StringTokenizer tokens;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.master_list_stalls);
	    
	    list = (ListView) findViewById(R.id.listV);
	    ctx = this;
	    db = new DatabaseAdapter(ctx);
	    // fetch array of stalls
	    listArray = db.getAllStalls();
	    
	    // generate list of stall objects
	    ArrayAdapter<Stall> adapter = new ArrayAdapter<Stall>(ctx ,android.R.layout.simple_list_item_1, listArray);
	    list.setAdapter(adapter);
	    list.setOnItemClickListener(this);
	    	   
	}
	
	// respond to list selection by launching edit screen
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		// fetch selection stall number
		String number;
		String viewString = ((TextView) view).getText().toString();
		
		// iterate through selection text
		tokens = new StringTokenizer(viewString);
		tokens.nextToken(); // skip 'Number' label
		number = tokens.nextToken(); // fetch number VALUE
		
		// close database before navigating
		db.closeDB();
		// launch edit screen
		Intent launchEditStall = new Intent(ViewStalls.this, EditStall.class);
		launchEditStall.putExtra("number", number); // pass stall number to another activity
		startActivity(launchEditStall);
		
	}

}
