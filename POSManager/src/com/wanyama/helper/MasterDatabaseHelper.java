package com.wanyama.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

import com.wanyama.model.Product;
import com.wanyama.model.Stall;

public class MasterDatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = MasterDatabaseHelper.class.getName();
	
	// Database Version
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
	private static final String DATABASE_NAME = "posMasterManager";
	
	// Table Names
	private static final String TABLE_STALLS = "stalls";
	private static final String TABLE_PRODUCTS = "products";
	private static final String TABLE_PURCHASE = "purchases";
	
	//Shared Column Names
	private static final String KEY_ID = "id";
//	private static final String KEY_CREATED_AT = "created_at";
	
	// Stalls table column names
	private static final String KEY_STALL_NUMBER = "stall_number";
	private static final String KEY_STALL_BALANCE = "stall_balance";
	
	// Products table column names
	private static final String KEY_PRODUCT_CODE = "product_code";
	private static final String KEY_PRODUCT_PRICE = "product_price";
	
	// Purchase table column names
	private static final String KEY_STALL_ID = "stall_id";
	private static final String KEY_PRODUCT_ID = "product_id";
	/**** add time of purchase later ****/
	
	// SQL Create tables statement
	// STALL table
	private static final String CREATE_TABLE_STALLS = "CREATE TABLE "+ TABLE_STALLS+ "("+ KEY_ID+ " INTEGER PRIMARY KEY,"
			+ KEY_STALL_NUMBER+ " INTEGER,"+ KEY_STALL_BALANCE+");";
	
	// PRODUCTS table
	private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "+ TABLE_PRODUCTS+ "("+ KEY_ID+ " INTEGER PRIMERY KEY,"
			+ KEY_PRODUCT_CODE+ " INTEGER,"+ KEY_PRODUCT_PRICE+ " INTEGER"+ ");";
	
	// PURCHASE table
	private static final String CREATE_TABLE_PURCHASE = "CREATE TABLE " + TABLE_PURCHASE+ "("+ KEY_ID+ " INTEGER PRIMARY KEY,"
			+ KEY_STALL_ID+ " INTEGER,"+ KEY_PRODUCT_ID+ " INTEGER"+ ");";
	
	// Constructor
	public MasterDatabaseHelper(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
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
			// make custom message class to toast error
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
			
			e.printStackTrace();
		}
		
		// create new tables
		onCreate(db);
	}
	
	/********************************************************
	 * CRUD (Create, Read, Update, Delete) Method definitions
	 *********************************************************/
	
	// create a stall
	public long createStall(Stall stall, long[] product_ids){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_STALL_NUMBER, stall.getNumber());	//booth number
		values.put(KEY_STALL_BALANCE, stall.getBalance());	//stand bill
		
		// primary key generated
		long stall_id = db.insert(TABLE_STALLS, null, values);
		
		return stall_id;
	}
	
	// fetch a stall
	public Stall getStall(long stall_id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_STALLS + " WHERE " + KEY_ID + " = " + stall_id;
		
		Log.e(LOG, selectQuery);
		
		Cursor c = db.rawQuery(selectQuery, null);
		
		if (c != null)
			c.moveToFirst();
		
		Stall stand = new Stall(c.getInt(c.getColumnIndex(KEY_ID)), c.getInt(c.getColumnIndex(KEY_STALL_NUMBER)), 
				c.getInt(c.getColumnIndex(KEY_STALL_BALANCE)));
		
		return stand;
		
	}
	
	// fetch all Stalls
	public List<Stall> getAllStalls() {
		List<Stall> stalls = new ArrayList<Stall>();
		String selectQuery = "SELECT * FROM " + TABLE_STALLS;
		
		Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		// adding rows to list
		if (c.moveToFirst()) {
			do{
				Stall stand = new Stall(c.getInt(c.getColumnIndex(KEY_ID)), c.getInt(c.getColumnIndex(KEY_STALL_NUMBER)), 
				c.getInt(c.getColumnIndex(KEY_STALL_BALANCE)));
				
				stalls.add(stand);
			} while (c.moveToNext());
		}
		return stalls;
	}
	
	// Update a stall
	public int updateStall(Stall stand){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_STALL_NUMBER, stand.getNumber());
		values.put(KEY_STALL_BALANCE, stand.getBalance());
		
		// update row
		return db.update(TABLE_STALLS, values, KEY_ID + " = ?", new String[] {String.valueOf(stand.getId()) });
	}
	
	// Delete a stall
	public void deleteStall(long stall_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_STALLS, KEY_ID + " = ?", new String[] {String.valueOf(stall_id)});
	}
	
	
	//creating a product 
	public long createProduct(Product item){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_CODE, item.getCode());
		values.put(KEY_PRODUCT_PRICE, item.getPrice());
		
		long product_id = db.insert(TABLE_PRODUCTS, null, values);
		
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
				Product item = new Product(c.getInt((c.getColumnIndex(KEY_ID))), c.getInt(c.getColumnIndex(KEY_PRODUCT_CODE)),
						c.getInt(c.getColumnIndex((KEY_PRODUCT_PRICE))));
				
				// add product to list
				items.add(item);
			}while (c.moveToNext());
		}
		
		return items;
	}
	
	// Updating a Product
	public int updateProduct(Product item) {
		SQLiteDatabase db =  this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_CODE, item.getCode());
		values.put(KEY_PRODUCT_PRICE, item.getPrice());
		
		//update row
		return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?", new String[] { String.valueOf(item.getId())});
	}
	
	// 
	public long makePurchase(long stall_id, long product_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_STALL_ID, stall_id);
		values.put(KEY_PRODUCT_ID, product_id);
//		values.put(KEY_CREATED_AT,  getDateTime());
		
		long _id = db.insert(TABLE_PURCHASE, null, values);
		
		return _id;
	}
	
	// delete an order, must be approved by master (TO BE ADDED)
	// needs verification
	public int removePurchase(long id, long product_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_ID, product_id);
		
		//updating row
		return db.update(TABLE_STALLS, values, KEY_ID + " = ?", new String[] { String.valueOf(id)});
	}
	
	/******* IMPLEMENT LATER
	// transaction time
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
	**********/
	
	// close database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
	// implement later: 
	// deleting a product from the list of product removes product from purchase table
	// deleting a stall, removes all purchases from that stall on the purchase table

}
