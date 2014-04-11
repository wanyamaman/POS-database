package com.wanyama.configuration;

import com.wanyama.helper.MasterDatabaseAdapter;
import com.wanyama.posmanager.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class InsertProduct extends Activity {
	
	private MasterDatabaseAdapter dbAdapter;
	private Context ctx;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.add_product);
	
	    // TODO Auto-generated method stub
	}

}
