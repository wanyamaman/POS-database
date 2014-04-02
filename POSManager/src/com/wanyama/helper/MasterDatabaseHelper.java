package com.wanyama.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

}
