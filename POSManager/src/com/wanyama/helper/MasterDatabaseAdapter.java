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
import android.widget.Toast;

import com.wanyama.model.Product;
import com.wanyama.model.Stall;

// Adapter class controls access to database methods
public class MasterDatabaseAdapter  {

	MasterDatabaseHelper helper;
	Context ctx;
	
	public MasterDatabaseAdapter (Context context) {
		helper = new MasterDatabaseHelper(context);
		ctx =context;
	}
	
	
	/********************************************************
	 * CRUD (Create, Read, Update, Delete) Method definitions
	 *********************************************************/
////////////////////	
// CREATE METHODS //
////////////////////
	
	// insert a stall
	public long createStall(Stall stall){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(MasterDatabaseHelper.KEY_STALL_NUMBER, stall.getNumber());	//booth number
		values.put(MasterDatabaseHelper.KEY_STALL_BALANCE, stall.getBalance());	//stand bill
		
		// primary key generated
		long stall_id;
		try {
			stall_id = db.insertOrThrow(MasterDatabaseHelper.TABLE_STALLS, null, values);
		} catch (SQLException e) {
			// alert user of failure
			Toast.makeText(ctx, "failed to insert stall number: "+ stall.getNumber(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			stall_id = -1;	// negative value indicates error to calling method
		}
		stall.setId(stall_id);
		
		return stall_id;
	}
	
	//insert a product 
	public long createProduct(Product item){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(MasterDatabaseHelper.KEY_PRODUCT_CODE, item.getCode());
		values.put(MasterDatabaseHelper.KEY_PRODUCT_PRICE, item.getPrice());
		
		long product_id;
		try {
			product_id = db.insertOrThrow(MasterDatabaseHelper.TABLE_PRODUCTS, null, values);
		} catch (SQLException e) {
			// alert user of failure
			Toast.makeText(ctx, "failed to insert product with code: "+ item.getCode(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			product_id =-1;
		}
		item.setID(product_id);
		
		return product_id;
		
	}
///////////////////////////
// END OF CREATE METHODS //
///////////////////////////
	
////////////////////
//	READ METHODS //
///////////////////	
	// fetch a stall
	public Stall getStallById(long stall_id){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + MasterDatabaseHelper.TABLE_STALLS + " WHERE " + MasterDatabaseHelper.KEY_ID + " = " + stall_id;
		
		Log.e(MasterDatabaseHelper.LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c != null)
			c.moveToFirst();
		
		Stall stand = new Stall(c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_ID)), c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_STALL_NUMBER)), 
				c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_STALL_BALANCE)));
		
		return stand;
		
	}
	
	public Stall getStallByNumber(int stall_number){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + MasterDatabaseHelper.TABLE_STALLS + " WHERE " + MasterDatabaseHelper.KEY_STALL_NUMBER + " = " + stall_number;
		
		Log.e(MasterDatabaseHelper.LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c != null)
			c.moveToFirst();
		
		Stall stand = new Stall(c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_ID)), c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_STALL_NUMBER)), 
				c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_STALL_BALANCE)));
		
		return stand;
		
	}
	
	// fetch all Stalls
	public List<Stall> getAllStalls() {
		List<Stall> stalls = new ArrayList<Stall>();
		String selectQuery = "SELECT * FROM " + MasterDatabaseHelper.TABLE_STALLS;
		
		Log.e(MasterDatabaseHelper.LOG, selectQuery);
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		// adding rows to list
		if (c.moveToFirst()) {
			do{
				Stall stand = new Stall(c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_ID)), c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_STALL_NUMBER)), 
				c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_STALL_BALANCE)));
				
				stalls.add(stand);
			} while (c.moveToNext());
		}
		return stalls;
	}
	
	// fetch a product based on its ID
	public Product getProductById(long product_id){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + MasterDatabaseHelper.TABLE_PRODUCTS + " WHERE " + MasterDatabaseHelper.KEY_ID + " = " + product_id;
		
		Log.e(MasterDatabaseHelper.LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c != null)
			c.moveToFirst();
		
		Product item = new Product(c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_ID)), c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_PRODUCT_CODE)), 
				c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_PRODUCT_PRICE)));
		
		return item;
	}
	// lookup product based on its code
	public Product getProductByCode(int product_code){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + MasterDatabaseHelper.TABLE_PRODUCTS + " WHERE " + MasterDatabaseHelper.KEY_PRODUCT_CODE + " = " + product_code;
		
		Log.e(MasterDatabaseHelper.LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c != null)
			c.moveToFirst();
		
		Product item = new Product(c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_ID)), c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_PRODUCT_CODE)), 
				c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_PRODUCT_PRICE)));
		
		return item;
	}
	// Fetch all products
		public List<Product> getAllProducts() {
			List<Product> items = new ArrayList<Product>();
			String selectQuery = " SELECT * FROM " + MasterDatabaseHelper.TABLE_PRODUCTS;
			
			Log.e(MasterDatabaseHelper.LOG, selectQuery);
			
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor c = db.rawQuery(selectQuery, null);
			
			// iterate through all rows
			if (c.moveToFirst()) {
				do {
					Product item = new Product(c.getInt((c.getColumnIndex(MasterDatabaseHelper.KEY_ID))), c.getInt(c.getColumnIndex(MasterDatabaseHelper.KEY_PRODUCT_CODE)),
							c.getInt(c.getColumnIndex((MasterDatabaseHelper.KEY_PRODUCT_PRICE))));
					
					// add product to list
					items.add(item);
				}while (c.moveToNext());
			}
			
			return items;
		}
/////////////////////////
// END OF READ METHODS //
/////////////////////////
		
////////////////////
// UPDATE METHODS //
////////////////////
		
	// Update a stall
	public int updateStall(Stall stand){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(MasterDatabaseHelper.KEY_STALL_NUMBER, stand.getNumber());
		values.put(MasterDatabaseHelper.KEY_STALL_BALANCE, stand.getBalance());
		
		// update row
		return db.update(MasterDatabaseHelper.TABLE_STALLS, values, MasterDatabaseHelper.KEY_ID + " = ?", new String[] {String.valueOf(stand.getId()) });
	}
	
	// Updating a Product
		public int updateProduct(Product item) {
			SQLiteDatabase db =  helper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(MasterDatabaseHelper.KEY_PRODUCT_CODE, item.getCode());
			values.put(MasterDatabaseHelper.KEY_PRODUCT_PRICE, item.getPrice());
			
			//update row
			return db.update(MasterDatabaseHelper.TABLE_PRODUCTS, values, MasterDatabaseHelper.KEY_ID + " = ?", new String[] { String.valueOf(item.getId())});
		}
///////////////////////////
// END OF UPDATE METHODS //
///////////////////////////

////////////////////
// DELETE METHODS //
////////////////////
	// Delete a stall
	public void deleteStall(long stall_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(MasterDatabaseHelper.TABLE_STALLS, MasterDatabaseHelper.KEY_ID + " = ?", new String[] {String.valueOf(stall_id)});
	}
	
	// delete a product
	public void deleteProduct(long product_id){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(MasterDatabaseHelper.TABLE_PRODUCTS, MasterDatabaseHelper.KEY_ID + " = ?", new String[] {String.valueOf(product_id)});
	}
	
	// DELETE DATABASE TABLES
	public void dropDatabase(){
		helper.clearDatabase();
	}
///////////////////////////
// END OF DELETE METHODS //
///////////////////////////
	
	// 
	public long makePurchase(long stall_id, long product_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(MasterDatabaseHelper.KEY_STALL_ID, stall_id);
		values.put(MasterDatabaseHelper.KEY_PRODUCT_ID, product_id);
//		values.put(KEY_CREATED_AT,  getDateTime());
		
		long _id = db.insert(MasterDatabaseHelper.TABLE_PURCHASE, null, values);
		
		return _id;
	}
	
	/*
	 * look through cursor for table name, store product foreign key in list
	 * use list to loop through product table, return products
	 * use list of products to calculate bill
	// Fetch all products from a table
	public List<Product> getAllProductsFromTable(Stall stand) {
		List<Product> items = new ArrayList<Product>();
		String selectQuery = " SELECT * FROM " + TABLE_PURCHASE;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// iterate through all rows
		if (c.moveToFirst()) {
			do {
				Product item = new Product(c.getInt((c.getColumnIndex())),
						c.getInt(c.getColumnIndex(COL_CODE)), c.getInt(c
								.getColumnIndex((COL_PRICE))));

				// add product to list
				items.add(item);
			} while (c.moveToNext());
		}

		return items;
	}
	
	*/
	// delete an order, must be approved by master (TO BE ADDED)
	// needs verification
	public int removePurchase(long id, long product_id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(MasterDatabaseHelper.KEY_PRODUCT_ID, product_id);
		
		//updating row
		return db.update(MasterDatabaseHelper.TABLE_STALLS, values, MasterDatabaseHelper.KEY_ID + " = ?", new String[] { String.valueOf(id)});
	}
	
	/******* IMPLEMENT LATER
	// transaction time
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
		fetch products of a particular price
	}
	**********/
	
	// close database
	public void closeDB() {
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
	// implement later: 
	// deleting a product from the list of product removes product from purchase table
	// deleting a stall, removes all purchases from that stall on the purchase table
	// prevent the addition of duplicate products
	// initiate primary key correctly

	static class MasterDatabaseHelper extends SQLiteOpenHelper {
		// Logcat tag
		private static final String LOG = MasterDatabaseAdapter.class.getName();
		// Database Version
		private static final int DATABASE_VERSION = 1;
		// Database Name
		private static final String DATABASE_NAME = "posMasterManager";
		// Table Names
		private static final String TABLE_STALLS = "stalls";
		private static final String TABLE_PRODUCTS = "products";
		private static final String TABLE_PURCHASE = "purchases";
		//Shared Column Names
		private static final String KEY_ID = "_id";
//		private static final String KEY_CREATED_AT = "created_at";
		// Stalls table column names
		private static final String KEY_STALL_NUMBER = "number";
		private static final String KEY_STALL_BALANCE = "balance";
		// Products table column names
		private static final String KEY_PRODUCT_CODE = "code";
		private static final String KEY_PRODUCT_PRICE = "price";
		
		// Purchase table column names
		private static final String KEY_STALL_ID = "stall_id";
		private static final String KEY_PRODUCT_ID = "product_id";
		/**** add time of purchase later ****/
		
		// SQL Create tables statement
		// STALL table
		private static final String CREATE_TABLE_STALLS = "CREATE TABLE "+ TABLE_STALLS+ "("+ KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_STALL_NUMBER+ " INTEGER NOT NULL UNIQUE,"+ KEY_STALL_BALANCE+ " INTEGER NOT NULL" +");";
		// PRODUCTS table
		private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "+ TABLE_PRODUCTS+ "("+ KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_PRODUCT_CODE+ " INTEGER NOT NULL UNIQUE,"+ KEY_PRODUCT_PRICE+ " INTEGER NOT NULL"+ ");";
		// PURCHASE table
		private static final String CREATE_TABLE_PURCHASE = "CREATE TABLE " + TABLE_PURCHASE+ "("+ KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_STALL_ID+ " INTEGER NOT NULL,"+ KEY_PRODUCT_ID+ " INTEGER NOT NULL"+ ");";
		
		private Context ctx;
		// Constructor
		public MasterDatabaseHelper(Context ctx){
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
			this.ctx = ctx;
		}
		
		// create database tables
		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create tables
			try {
				db.execSQL(CREATE_TABLE_PRODUCTS);
				db.execSQL(CREATE_TABLE_STALLS);
				db.execSQL(CREATE_TABLE_PURCHASE);
			} catch (SQLException e) {
				Toast.makeText(ctx, "failed to create the database", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

		}

		//upgrade database tables
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// delete older tables before upgrading
			
			try {
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_STALLS);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE);
			} catch (SQLException e) {
				Toast.makeText(ctx, "failed to upgrade the database", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
			// create new tables
			onCreate(db);
		}
		
		//called to drop all tables and create a new database (same functionality as onUpgrade method)
		public void clearDatabase(){
			try {
				super.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
				super.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_STALLS);
				super.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE);
			} catch (SQLException e) {
				Toast.makeText(ctx, "failed to clear the database", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
			onCreate(super.getWritableDatabase());
		}
		
	}
}

