package com.wanyama.posmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.wanyama.configuration.Settings;
import com.wanyama.configuration.SlaveSetup;
import com.wanyama.network.ImportCSV;

public class HomeScreen extends Activity {

	private Context ctx;
	private Button master;
	private Button slave;
	private Button importBtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		ctx = this;

//		TextView header = (TextView) findViewById(R.id.header);
		master = (Button) findViewById(R.id.master);
		slave = (Button) findViewById(R.id.slave);
		importBtn = (Button) findViewById(R.id.importBtn);

		master.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ctx, "Master Mode Selected", Toast.LENGTH_LONG)
						.show();
				Intent launchMasterMain = new Intent(HomeScreen.this, Settings.class);
				startActivity(launchMasterMain);
			}
		});

		slave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ctx, "Slave Mode Selected", Toast.LENGTH_LONG)
						.show();
				Intent launchSlaveSetup = new Intent(HomeScreen.this, SlaveSetup.class);
				startActivity(launchSlaveSetup);
			}
		});
		
		importBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), ImportCSV.class);
				Log.i("HomeScreen", "About to start service");
				startService(i);
				
				
			}
		});
	}

}
