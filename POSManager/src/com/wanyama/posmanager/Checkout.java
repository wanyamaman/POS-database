package com.wanyama.posmanager;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.wanyama.database.DatabaseAdapter;
import com.wanyama.model.Product;
import com.wanyama.network.ExportCSV;
import com.wanyama.posmanager.R.color;

public class Checkout extends Activity implements OnItemClickListener {

	private DatabaseAdapter db;
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
		db = new DatabaseAdapter(getApplicationContext());

		checkoutBtn = (Button) findViewById(R.id.purchaseBtn);
		menuReturnBtn = (Button) findViewById(R.id.menuReturnBtn);
		list = (ListView) findViewById(R.id.list_view);
		list.setBackgroundColor(color.black);

		// setup order list
		orderArray = new ArrayList<String>();
		getReceipt(); // fills up orderArray with receipt output
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_list_item_1,
				orderArray);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);

		// navigate to product menu
		menuReturnBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				db.closeDB();
				Intent launchMenu = new Intent(Checkout.this, InventoryList.class);
				startActivity(launchMenu);

			}
		});
		
		checkoutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), ExportCSV.class);
				startService(i);
				
				//exportOrdersToCSV thread = new exportOrdersToCSV();
				//thread.execute();
				
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

		Intent launchOrderItem = new Intent(Checkout.this, OrderItem.class);
		launchOrderItem.putExtra("code", code); // pass code to another activity
		startActivity(launchOrderItem); // launch new activity

	}

	// displays receipt of ordered items
	public List<String> getReceipt() {
		String temp = "";
		Product item = null;

		// fetch list of products
		List<Product> products = db.getAllProducts();
		Log.i("Checkout", "fetched all products + count=" + products.size());
		// get iterator for product list
		Iterator<Product> iterator = products.iterator();
		while (iterator.hasNext()) {
			item = iterator.next();
			// format receipt display before outputting
			temp = "CODE: " + item.getCode() + " @R" + item.getPrice() + "  x"
					+ db.countProductOrders(item.getCode());
			Log.i("Checkout", temp);
			orderArray.add(temp);
		}

		Log.i("Checkout", "Size of orderArray: " + orderArray.size());
		return orderArray;

	}

	public class exportOrdersToCSV extends AsyncTask<Void, Void, Void> {

		private File dbFile;
		private DatabaseAdapter dbhelper;
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
			dbhelper = new DatabaseAdapter(getApplicationContext());
			directory = new File(Environment.getExternalStorageDirectory(), "");

			// check if directory exists
			if (!directory.exists())
				directory.mkdirs();

			// create a new file
			file = new File(directory, "sales.csv");

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
		protected void onPostExecute(Void result){
			super.onPostExecute(null);
			// alert the user
			Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
		}

	}
	
	/*
	public class ServerThread extends AsyncTask<Void, String, Void>{

		// status of transfer operation
				private String status = "unsuccessful";
				private Socket client;
				private String line;
				private DataInputStream dis;   // socket input stream
				private DataOutputStream dos;  // socket output stream
				private int length;			   // length of byte array
				private byte[] b_name;		   // filename byte array
				private byte[] b_file;		   // file byte array
				private String file_name;	   // file name
				private FileOutputStream fos;  // file output stream
				private BufferedOutputStream bos;
				
				// set display before execution
				public void onPreExecute(){
					line = null;
					if (SERVERIP != null){
						connectionStatus.setText("Listening on IP: " + SERVERIP);
					}
					else {
						connectionStatus.setText("Couldn't detect internet connection.");
					}
				}
				
				// update UI with connection status info
				@Override
				protected void  onProgressUpdate(String...stat) {
					connectionStatus.setText(stat[0]);
				}
				@Override
				protected Void doInBackground(Void...params){
					
					// try connect to client
					try {
						// open port for listening
						serverSocket = new ServerSocket(SERVERPORT);
					} catch (IOException e) {
						Log.d("ServerActivity", "S: Error creating ServerSocket ");
						e.printStackTrace();
					}
					
					// listen
					while (true) {
						try {
							client = serverSocket.accept();
							publishProgress("Connected");
							
							
							dis = new DataInputStream(client.getInputStream());
							dos = new DataOutputStream(client.getOutputStream());
							
							// get length of name array
							length = dis.readInt();
							b_name = new byte[length];
							// read name from stream
							dis.readFully(b_name);
							// convert name back to string
							file_name = new String(b_name);
							
					//		publishProgress(file_name);
							
							// receive file
							length = dis.readInt();
							b_file = new byte[length];
							dis.readFully(b_file);
							
							// write out file
							fos = new FileOutputStream(file_name);
							bos = new BufferedOutputStream(fos);
							bos.write(b_file, 0, b_name.length);
							// close streams
							bos.flush();
							bos.close();
							fos.flush();
							fos.close();
							
						} catch (IOException e) {
							publishProgress("Error accepting clients");
							e.printStackTrace();
						} catch (Exception e){
							publishProgress("Oops. Connection interrupted. Please reconnect your phones.");
							e.printStackTrace();
						}
					}
					
					
				}
				
				@Override
				protected void onPostExecute(Void result){
					super.onPostExecute(result);
					name = file_name;
					writeFName.setText(file_name);
				}
				
			} 

	}*/

}
