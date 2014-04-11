package com.wanyama.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wanyama.model.Product;

public class SingleHelper extends SQLiteOpenHelper {

	// database info
	private static final String LOG = SingleHelper.class.getName();
	private static final int DB_VERSION = 2;
	private static final String DB_NAME = "product_manager";

	// table values
	public static final String TABLE_PRODUCTS = "products";
	public static final String ID = "_id";
	public static final String COL_CODE = "code";
	public static final String COL_PRICE = "price";

	// create statement
	private static final String CREATE_TABLE_PRODUCTS = "create table "
			+ TABLE_PRODUCTS + " (" + ID
			+ " integer primary key autoincrement, " + COL_CODE
			+ " integer UNIQUE, " + COL_PRICE + " integer);";

	public SingleHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		try {
			db.execSQL(CREATE_TABLE_PRODUCTS);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.w(LOG, "Upgrading database. Existing contents will be lost. ["
				+ oldVersion + "]->[" + newVersion + "]");
		try {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		onCreate(db);

	}
	
	//called to drop all tables and create a new database (same functionality as onUpgrade method)
		public void clearDatabase(SQLiteDatabase db){
			
			try {
				super.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			onCreate(super.getWritableDatabase());
		}

	public long createProduct(Product item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COL_CODE, item.getCode());
		values.put(COL_PRICE, item.getPrice());

		long product_id;
		try {
			product_id = db.insertOrThrow(TABLE_PRODUCTS, null, values);
		} catch (SQLException e) {
			Log.i(LOG, "There was an error inserting: "+item+" (it probably already exists in the database)");
			e.printStackTrace();
			product_id = -99;
		}
		item.setID(product_id); // check if it gives primary key

		return product_id;

	}

	// Fetch all products
	public List<Product> getAllProducts() {
		List<Product> items = new ArrayList<Product>();
		String selectQuery = " SELECT * FROM " + TABLE_PRODUCTS;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// iterate through all rows
		if (c.moveToFirst()) {
			do {
				Product item = new Product(c.getInt((c.getColumnIndex(ID))),
						c.getInt(c.getColumnIndex(COL_CODE)), c.getInt(c
								.getColumnIndex((COL_PRICE))));

				// add product to list
				items.add(item);
			} while (c.moveToNext());
		}

		return items;
	}

	// close database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
}
