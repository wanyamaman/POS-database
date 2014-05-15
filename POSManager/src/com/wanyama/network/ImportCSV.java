package com.wanyama.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import au.com.bytecode.opencsv.CSVReader;

import com.wanyama.database.DatabaseAdapter;

public class ImportCSV extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int starId) {
		
		Log.i("ImportCSV", "service started");
		// start thread
		importOrdersToCSV thread = new importOrdersToCSV();
		thread.execute();

		return START_STICKY;
	}

	public class importOrdersToCSV extends AsyncTask<Void, Void, Void> {

		private File dbFile;
		private DatabaseAdapter dbHelper;
		private File directory;
		private File file;
		private boolean isExtStorageAvailable;
		private String state;
		private CSVReader reader;

		@Override
		protected Void doInBackground(Void... params) {
			
			// initiate variables
			dbFile = getDatabasePath("posMasterManager.sqlite");
			dbHelper = new DatabaseAdapter(getApplicationContext());
			String [] nextLine;
			
			// prepare to read
			isExtStorageAvailable = false;
			state = Environment.getExternalStorageState();
			
			// wait till external storage is writable
			while (!isExtStorageAvailable) {
				if (Environment.MEDIA_MOUNTED.equals(state)) {
					
					file = new File(Environment.getExternalStorageDirectory(), "sales2.csv");
					isExtStorageAvailable = true;
				} else {
					isExtStorageAvailable = false;
				}
			}
			
		//	directory = new File(Environment.getExternalStorageDirectory(), "sales2.csv");
			try {
				reader = new CSVReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				Log.w("ImportCSV", "could not find file to read");
				e.printStackTrace();
			}
			
			try {
				while ((nextLine = reader.readNext())!= null){
					Log.i("ImportCSV", nextLine[0]+" "+nextLine[1]+" "+nextLine[2]);
				}
			} catch (IOException e) {
				Log.i("ImportCSV.java", "problem reading reader.readNext");
				e.printStackTrace();
			}

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(null);
			
			stopSelf();
		}

	}

}
