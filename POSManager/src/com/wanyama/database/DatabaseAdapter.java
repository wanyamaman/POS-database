package com.wanyama.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import com.wanyama.model.Product;
import com.wanyama.model.Stall;

// Adapter class controls access to database methods
public class DatabaseAdapter {

	private DatabaseHelper helper;
	private Context ctx;
	SQLiteDatabase dbRead;
	SQLiteDatabase dbWrite;

	public DatabaseAdapter(Context context) {
		helper = new DatabaseHelper(context);
		ctx = context;
		dbRead = helper.getReadableDatabase();
		dbWrite = helper.getWritableDatabase();
	}

	// access to readable version of database
	public SQLiteDatabase getReadable() {
		return dbRead;
	}

	/********************************************************
	 * CRUD (Create, Read, Update, Delete) Method definitions
	 *********************************************************/
	// //////////////////
	// CREATE METHODS  //
	// //////////////////

	// insert a stall
	public long createStall(Stall stall) {

		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_STALL_NUMBER, stall.getNumber()); // booth
																				// number
		values.put(DatabaseHelper.KEY_STALL_BALANCE, stall.getBalance()); // stand
																				// bill

		// primary key generated
		long stall_id;
		try {
			stall_id = dbWrite.insertOrThrow(DatabaseHelper.TABLE_STALLS,
					null, values);
		} catch (SQLException e) {
			// alert user of failure
			Toast.makeText(
					ctx,
					"failed to insert stall number: "
							+ stall.getNumber()
							+ ".\nPlease ensure that number is not already in use.",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
			stall_id = -1; // negative value indicates error to calling method
		}
		stall.setId(stall_id);

		return stall_id;
	}

	// insert a product
	public long createProduct(Product item) {

		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_PRODUCT_CODE, item.getCode());
		values.put(DatabaseHelper.KEY_PRODUCT_PRICE, item.getPrice());

		long product_id;
		try {
			product_id = dbWrite.insertOrThrow(
					DatabaseHelper.TABLE_PRODUCTS, null, values);
		} catch (SQLException e) {
			// alert user of failure
			Toast.makeText(ctx,
					"failed to insert product with code: " + item.getCode(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
			product_id = -1;
		}
		item.setID(product_id);

		return product_id;

	}

	// /////////////////////////
	// END OF CREATE METHODS //
	// /////////////////////////

	// //////////////////
	// READ METHODS //
	// /////////////////
	// fetch a stall by its database ID
	public Stall getStallById(long stall_id) {

		String selectQuery = "SELECT * FROM "
				+ DatabaseHelper.TABLE_STALLS + " WHERE "
				+ DatabaseHelper.KEY_ID + " = " + stall_id;

		Log.e(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Stall stand = new Stall(
				c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ID)),
				c.getInt(c
						.getColumnIndex(DatabaseHelper.KEY_STALL_NUMBER)),
				c.getInt(c
						.getColumnIndex(DatabaseHelper.KEY_STALL_BALANCE)));

		return stand;

	}

	// fetch a stall by its stall number
	public Stall getStallByNumber(int stall_number) {

		String selectQuery = "SELECT * FROM "
				+ DatabaseHelper.TABLE_STALLS + " WHERE "
				+ DatabaseHelper.KEY_STALL_NUMBER + " = " + stall_number;

		Log.e(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Stall stand = new Stall(
				c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ID)),
				c.getInt(c
						.getColumnIndex(DatabaseHelper.KEY_STALL_NUMBER)),
				c.getInt(c
						.getColumnIndex(DatabaseHelper.KEY_STALL_BALANCE)));

		return stand;

	}

	// fetch all Stalls
	public List<Stall> getAllStalls() {
		List<Stall> stalls = new ArrayList<Stall>();
		String selectQuery = "SELECT * FROM "
				+ DatabaseHelper.TABLE_STALLS;

		Log.e(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		// adding rows to list
		if (c.moveToFirst()) {
			do {
				Stall stand = new Stall(
						c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ID)),
						c.getInt(c
								.getColumnIndex(DatabaseHelper.KEY_STALL_NUMBER)),
						c.getInt(c
								.getColumnIndex(DatabaseHelper.KEY_STALL_BALANCE)));

				stalls.add(stand);
			} while (c.moveToNext());
		}
		return stalls;
	}

	// count the number of stalls
	public long countStalls() {

		return DatabaseUtils.queryNumEntries(dbRead,
				DatabaseHelper.TABLE_STALLS);
	}

	// count the number of products
	public long countProducts() {
		return DatabaseUtils.queryNumEntries(dbRead,
				DatabaseHelper.TABLE_PRODUCTS);
	}

	// count the total number of orders for all products
	public long countTotalOrders() {
		return DatabaseUtils.queryNumEntries(dbRead,
				DatabaseHelper.TABLE_ORDERS);
	}

	/*
	 * public long countItemOrders(String code){
	 * 
	 * String selectQuery = "SELECT COUNT(*) FROM " +
	 * DatabaseHelper.TABLE_ORDERS+ " WHERE " +
	 * DatabaseHelper.KEY_ORDER_CODE+ " = " + code;
	 * 
	 * Log.e(DatabaseHelper.LOG, selectQuery);
	 * 
	 * Cursor c = dbRead.rawQuery(selectQuery, null);
	 * 
	 * return DatabaseUtils.queryNumEntries(dbRead,
	 * DatabaseHelper.TABLE_ORDERS, code);
	 * 
	 * }
	 */

	// count the number or orders for a particular product
	public int countProductOrders(int code) {

		String selectQuery = " SELECT * FROM "
				+ DatabaseHelper.TABLE_ORDERS + " WHERE "
				+ DatabaseHelper.KEY_ORDER_CODE + " = " + code;

		Log.i(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		if (c != null) {
			if (c.moveToFirst()) { // true if record is found
				return c.getCount(); // return count
			} else { // no records where found in table. i.e empty cursor
				return 0;
			}
		} else {
			Log.w(DatabaseHelper.LOG,
					"Null cursor returned from countOrders");
			return 0;
		}

		/*
		 * not ideal solution, not tested if (c == null) return 0; // no orders
		 * exist else { return c.getCount(); // return number of orders }
		 */
	}

	// fetch a product based on its ID
	public Product getProductById(long product_id) {

		String selectQuery = "SELECT * FROM "
				+ DatabaseHelper.TABLE_PRODUCTS + " WHERE "
				+ DatabaseHelper.KEY_ID + " = " + product_id;

		Log.e(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Product item = new Product(
				c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ID)),
				c.getInt(c
						.getColumnIndex(DatabaseHelper.KEY_PRODUCT_CODE)),
				c.getInt(c
						.getColumnIndex(DatabaseHelper.KEY_PRODUCT_PRICE)));

		return item;
	}

	// lookup product based on its code from product table
	public Product getProductByCode(int product_code) {

		String selectQuery = "SELECT * FROM "
				+ DatabaseHelper.TABLE_PRODUCTS + " WHERE "
				+ DatabaseHelper.KEY_PRODUCT_CODE + " = " + product_code;

		Log.e(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Product item = new Product(
				c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ID)),
				c.getInt(c
						.getColumnIndex(DatabaseHelper.KEY_PRODUCT_CODE)),
				c.getInt(c
						.getColumnIndex(DatabaseHelper.KEY_PRODUCT_PRICE)));

		return item;
	}

	// Fetch all products
	public List<Product> getAllProducts() {
		List<Product> items = new ArrayList<Product>();
		String selectQuery = " SELECT * FROM "
				+ DatabaseHelper.TABLE_PRODUCTS;

		Log.e(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		// iterate through all rows
		if (c.moveToFirst()) {
			do {
				Product item = new Product(
						c.getInt((c.getColumnIndex(DatabaseHelper.KEY_ID))),
						c.getInt(c
								.getColumnIndex(DatabaseHelper.KEY_PRODUCT_CODE)),
						c.getInt(c
								.getColumnIndex((DatabaseHelper.KEY_PRODUCT_PRICE))));

				// add product to list
				items.add(item);
			} while (c.moveToNext());
		}

		return items;
	}

	// returns a cursor of all purchase entries
	public Cursor getPurchaseCursor() {

		String selectQuery = " SELECT * FROM "
				+ DatabaseHelper.TABLE_ORDERS;
		Log.i(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		return c;

	}

	// returns a cursor of product entries
	public Cursor getProductsCursor() {

		String selectQuery = " SELECT * FROM "
				+ DatabaseHelper.TABLE_PRODUCTS;
		Log.i(DatabaseHelper.LOG, selectQuery);

		Cursor c = dbRead.rawQuery(selectQuery, null);

		return c;

	}

	// ///////////////////////
	// END OF READ METHODS //
	// ///////////////////////

	// //////////////////
	// UPDATE METHODS //
	// //////////////////

	// Update a stall
	public int updateStall(Stall stand) {

		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_STALL_NUMBER, stand.getNumber());
		values.put(DatabaseHelper.KEY_STALL_BALANCE, stand.getBalance());

		// update row
		return dbWrite.update(DatabaseHelper.TABLE_STALLS, values,
				DatabaseHelper.KEY_ID + " = ?",
				new String[] { String.valueOf(stand.getId()) });
	}

	// Updating a Product
	public int updateProduct(Product item) {

		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_PRODUCT_CODE, item.getCode());
		values.put(DatabaseHelper.KEY_PRODUCT_PRICE, item.getPrice());

		// update row
		return dbWrite.update(DatabaseHelper.TABLE_PRODUCTS, values,
				DatabaseHelper.KEY_ID + " = ?",
				new String[] { String.valueOf(item.getId()) });
	}

	// /////////////////////////
	// END OF UPDATE METHODS //
	// /////////////////////////

	// //////////////////
	// DELETE METHODS //
	// //////////////////
	// Delete a stall
	public void deleteStall(long stall_id) {
		dbWrite.delete(DatabaseHelper.TABLE_STALLS,
				DatabaseHelper.KEY_ID + " = ?",
				new String[] { String.valueOf(stall_id) });
	}

	// delete a product
	public void deleteProduct(long product_id) {
		dbWrite.delete(DatabaseHelper.TABLE_PRODUCTS,
				DatabaseHelper.KEY_ID + " = ?",
				new String[] { String.valueOf(product_id) });
	}

	// delete all Stall records
	public void deleteAllStalls() {
		try {
			dbWrite.delete(DatabaseHelper.TABLE_STALLS, null, null);
		} catch (Exception e) {
			Toast.makeText(ctx, "Failed to delete all stalls",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	// delete all Product records
	public void deleteAllProducts() {
		try {
			dbWrite.delete(DatabaseHelper.TABLE_PRODUCTS, null, null);
		} catch (Exception e) {
			Toast.makeText(ctx, "Failed to delete all products",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	// delete all Order records
	public void deleteAllOrders() {
		try {
			dbWrite.delete(DatabaseHelper.TABLE_ORDERS, null, null);
		} catch (Exception e) {
			Toast.makeText(ctx, "Failed to delete all purchases",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	// remove an ordered item
	public void deleteOrderByCode(int code) {
		dbWrite.delete(DatabaseHelper.TABLE_ORDERS,
				DatabaseHelper.KEY_ORDER_CODE + " = " + code, null);
	}

	// DELETE DATABASE TABLES
	public void dropDatabase() {
		helper.clearDatabase();
	}

	// /////////////////////////
	// END OF DELETE METHODS //
	// /////////////////////////

	//
	public long placeOrder(int stall_num, int product_code) {

		DatabaseHelper db = new DatabaseHelper(ctx);
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_ORDER_NUMBER, stall_num);
		values.put(DatabaseHelper.KEY_ORDER_CODE, product_code);
		// values.put(KEY_CREATED_AT, getDateTime());

		long _id = db.getWritableDatabase().insert(
				DatabaseHelper.TABLE_ORDERS, null, values);
		return _id;
	}

	/*
	 * look through cursor for table name, store product foreign key in list use
	 * list to loop through product table, return products use list of products
	 * to calculate bill // Fetch all products from a table public List<Product>
	 * getAllProductsFromTable(Stall stand) { List<Product> items = new
	 * ArrayList<Product>(); String selectQuery = " SELECT * FROM " +
	 * TABLE_ORDERS;
	 * 
	 * Log.e(LOG, selectQuery);
	 * 
	 * SQLiteDatabase db = this.getReadableDatabase(); Cursor c =
	 * db.rawQuery(selectQuery, null);
	 * 
	 * // iterate through all rows if (c.moveToFirst()) { do { Product item =
	 * new Product(c.getInt((c.getColumnIndex())),
	 * c.getInt(c.getColumnIndex(COL_CODE)), c.getInt(c
	 * .getColumnIndex((COL_PRICE))));
	 * 
	 * // add product to list items.add(item); } while (c.moveToNext()); }
	 * 
	 * return items; }
	 */
	// delete an order, must be approved by master (TO BE ADDED)
	// needs verification
	public int removeOrder(long id, int product_code) {
		int numberAffected;

		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_ORDER_CODE, product_code);
		// updating row
		numberAffected = dbWrite.update(DatabaseHelper.TABLE_ORDERS,
				values, DatabaseHelper.KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		// close database
		closeDB();
		return numberAffected;
	}

	/*******
	 * IMPLEMENT LATER // transaction time private String getDateTime() {
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
	 * Locale.getDefault()); Date date = new Date(); return
	 * dateFormat.format(date); fetch products of a particular price }
	 **********/

	// close database
	public void closeDB() {

		if (dbRead != null && dbRead.isOpen())
			dbRead.close();

		if (dbWrite != null && dbWrite.isOpen())
			dbWrite.close();
	}

	// implement later:
	// deleting a product from the list of product removes product from purchase
	// table
	// deleting a stall, removes all purchases from that stall on the purchase
	// table

	static class DatabaseHelper extends SQLiteOpenHelper {
		// Logcat tag
		private static final String LOG = DatabaseAdapter.class.getName();
		// Database Version
		private static final int DATABASE_VERSION = 3;
		// Database Name
		private static final String DATABASE_NAME = "posMasterManager";
		// Table Names
		private static final String TABLE_STALLS = "stalls";
		private static final String TABLE_PRODUCTS = "products";
		private static final String TABLE_ORDERS = "orders";
		// Shared Column Names
		private static final String KEY_ID = "_id";
		// private static final String KEY_CREATED_AT = "created_at";
		// Stalls table column names
		private static final String KEY_STALL_NUMBER = "number";
		private static final String KEY_STALL_BALANCE = "balance";
		// Products table column names
		private static final String KEY_PRODUCT_CODE = "code";
		private static final String KEY_PRODUCT_PRICE = "price";

		// Purchase table column names
		private static final String KEY_ORDER_NUMBER = "stall_number";
		private static final String KEY_ORDER_CODE = "product_code";
		/**** add time of purchase later ****/

		// SQL Create tables statement
		// STALL table
		private static final String CREATE_TABLE_STALLS = "CREATE TABLE "
				+ TABLE_STALLS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_STALL_NUMBER
				+ " INTEGER NOT NULL UNIQUE," + KEY_STALL_BALANCE
				+ " INTEGER NOT NULL" + ");";
		// PRODUCTS table
		private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
				+ TABLE_PRODUCTS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PRODUCT_CODE
				+ " INTEGER NOT NULL UNIQUE," + KEY_PRODUCT_PRICE
				+ " INTEGER NOT NULL" + ");";
		// PURCHASE table
		private static final String CREATE_TABLE_ORDERS = "CREATE TABLE "
				+ TABLE_ORDERS + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ORDER_NUMBER
				+ " INTEGER NOT NULL," + KEY_ORDER_CODE
				+ " INTEGER NOT NULL" + ");";

		private Context ctx;

		// Constructor
		public DatabaseHelper(Context ctx) {
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
				db.execSQL(CREATE_TABLE_ORDERS);
			} catch (SQLException e) {
				Toast.makeText(ctx, "failed to create the database",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

		}

		// upgrade database tables
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// delete older tables before upgrading

			try {
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_STALLS);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
			} catch (SQLException e) {
				Toast.makeText(ctx, "failed to upgrade the database",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

			// create new tables
			onCreate(db);
		}

		// called to drop all tables and create a new database (same
		// functionality as onUpgrade method)
		public void clearDatabase() {
			try {
				super.getWritableDatabase().execSQL(
						"DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
				super.getWritableDatabase().execSQL(
						"DROP TABLE IF EXISTS " + TABLE_STALLS);
				super.getWritableDatabase().execSQL(
						"DROP TABLE IF EXISTS " + TABLE_ORDERS);
			} catch (SQLException e) {
				Toast.makeText(ctx, "failed to clear the database",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

			onCreate(super.getWritableDatabase());
		}

	}
}
