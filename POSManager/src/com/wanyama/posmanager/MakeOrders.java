/***********************8
 * Source code: http://paragchauhan2010.blogspot.com/
 * Original Author:
 * Edited by: William Opondo
 * 21/04/2014
 */

package com.wanyama.posmanager;

import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.wanyama.configuration.InsertProduct;
import com.wanyama.configuration.Settings;
import com.wanyama.database.DatabaseAdapter;

public class MakeOrders extends Activity {
	
	private Button genCSV;
	private Button menuReturn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.make_order);
	    
	    genCSV = (Button) findViewById(R.id.WriteCSV);
	    menuReturn = (Button) findViewById(R.id.menuReturn);
	    
	    genCSV.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			writeCSV csvThread = new writeCSV();
			csvThread.execute();
				
			}
		});
	    
	    menuReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent launchHomeScreen = new Intent(MakeOrders.this,
						HomeScreen.class);
				startActivity(launchHomeScreen);
				
			}
		});
	
	  
	}
	
	public class writeCSV extends AsyncTask<Void, Void, Void>{

		private File dbFile;
		private DatabaseAdapter dbhelper;
		private File directory;
		private File file;
		private String success;
		
		@Override
		protected void onPreExecute(){
			success = "unsuccessful";
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			// setup storage directory and access to database file
			dbFile = getDatabasePath("posMasterManager.sqlite");
			dbhelper = new DatabaseAdapter(getApplicationContext());
			directory = new File(Environment.getExternalStorageDirectory(), "");
			
			// check if directory exists
			if (!directory.exists())
				directory.mkdirs();
			
			// create a new file
			file = new File(directory, "products.csv");
			
			// write database contents to file
			try 
	        {
	            file.createNewFile();                
	            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
	            // get cursor object of table data
	            Cursor c = dbhelper.getProductsCursor();
	            csvWrite.writeNext(c.getColumnNames());
	            while(c.moveToNext())
	            {
	               //Which column you want to exprort ///VERIFY number of orders
	            	// reads: ID + PRODUCT CODE + PRODUCT PRICE
	                String arrStr[] ={c.getString(0),c.getString(1), c.getString(2)};
	                csvWrite.writeNext(arrStr);
	            }
	            // close stream and objects
	            csvWrite.close();
	            c.close();
	            // raise success notification flag
	            success = "successful";
	        }
	        catch(Exception sqlEx)
	        {
	            Log.e("MakeOrders", sqlEx.getMessage(), sqlEx);
	            success = "failed";
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(null);
			// alert the user
			Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
		}
		
	}

}
