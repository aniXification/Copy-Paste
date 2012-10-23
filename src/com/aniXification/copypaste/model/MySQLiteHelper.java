package com.aniXification.copypaste.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_COPYPASTE = "COPYPASTE_RECORDS";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_RECORD = "record";
	public static final String COLUMN_FAV = "fav";
	public static final String COLUMN_TIMESPAMP = "timestamp";

	private static final String DATABASE_NAME = "COPYPASTE";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_COPYPASTE + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, " 
			+ COLUMN_RECORD + " text, " 
			+ COLUMN_TIMESPAMP + " text, " 
			+ COLUMN_FAV + " text);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.w(MySQLiteHelper.class.getName(),
			   "Upgrading database from version " + oldVersion + " to "
			    + newVersion + ", which will destroy all old data");
			    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COPYPASTE);
			    onCreate(db);

	}

}
