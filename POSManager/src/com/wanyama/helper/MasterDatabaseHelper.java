package com.wanyama.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wanyama.model.Product;
import com.wanyama.model.Stall;

public class MasterDatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "MasterDbHelper";
	
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
		db.execSQL(CREATE_TABLE_PRODUCTS);
		db.execSQL(CREATE_TABLE_STALLS);
		db.execSQL(CREATE_TABLE_PURCHASE);

	}

	//upgrade database tables
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// delete older tables before upgrading
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STALLS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE);
		
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
	
	
	//creating a product INCOMPLETE
	public long createProduct(Product item){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_CODE, item.getCode());
		values.put(KEY_PRODUCT_PRICE, item.getPrice());
		
		long product_id = db.insert(TABLE_PRODUCTS, null, values);
		
		return product_id;
		
	}

}
