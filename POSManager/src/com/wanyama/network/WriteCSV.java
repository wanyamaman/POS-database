package com.wanyama.network;

import java.io.File;
import java.io.FileWriter;

import com.wanyama.helper.MasterDatabaseAdapter;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

public class WriteCSV extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int starId){
		exportOrdersToCSV thread = new exportOrdersToCSV();
		thread.execute();
		
		return START_STICKY;
	}

	public class exportOrdersToCSV extends AsyncTask<Void, Void, Void> {

		private File dbFile;
		private MasterDatabaseAdapter dbhelper;
		private File directory;
		private File file;
		private String success;

		@Override
		protected void onPreExecute() {
			success = "unsuccessful";
		}

		@Override
		protected Void doInBackground(Void... params) {

			// setup storage directory and access to database file
			dbFile = getDatabasePath("posMasterManager.sqlite");
			dbhelper = new MasterDatabaseAdapter(getApplicationContext());
			directory = new File(Environment.getExternalStorageDirectory(), "");

			// check if directory exists
			if (!directory.exists())
				directory.mkdirs();

			// create a new file
			file = new File(directory, "sales2.csv");

			// write database contents to file
			try {
				file.createNewFile();
				CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

				// get cursor object of table data
				Cursor c = dbhelper.getPurchaseCursor();
				csvWrite.writeNext(c.getColumnNames());
				while (c.moveToNext()) {
					// Which column you want to export ///VERIFY number of
					// orders
					// reads: ID + PURCHASE NUMBER + PURCHASE CODE
					String arrStr[] = { c.getString(0), c.getString(1),
							c.getString(2) };
					csvWrite.writeNext(arrStr);
				}
				// close stream and objects
				csvWrite.close();
				c.close();
				// raise success notification flag
				success = "successful";
			} catch (Exception sqlEx) {
				Log.i("Checkout", sqlEx.getMessage(), sqlEx);
				success = "failed";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(null);
			// alert the user
			Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG)
					.show();
			// stop service
			stopSelf();
		}
	}
}
