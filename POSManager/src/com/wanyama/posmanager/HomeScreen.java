package com.wanyama.posmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends Activity {

	Context ctx;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mode);
		ctx = this;

		TextView header = (TextView) findViewById(R.id.header);
		Button master = (Button) findViewById(R.id.master);
		Button slave = (Button) findViewById(R.id.slave);

		master.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ctx, "Master Mode Selected", Toast.LENGTH_LONG)
						.show();
			}
		});

		slave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ctx, "Slave Mode Selected", Toast.LENGTH_LONG)
						.show();
			}
		});
	}

}
