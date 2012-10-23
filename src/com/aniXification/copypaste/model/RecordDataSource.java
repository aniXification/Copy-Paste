package com.aniXification.copypaste.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RecordDataSource {
	
	  private SQLiteDatabase database;
	  private MySQLiteHelper dbHelper;
	  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_RECORD, MySQLiteHelper.COLUMN_TIMESPAMP, MySQLiteHelper.COLUMN_FAV };
	  
	  public RecordDataSource(Context context) {
		  dbHelper = new MySQLiteHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public Record createRecord(String record, Long timeStamp, String fav) {
		    ContentValues values = new ContentValues();
		    values.put(MySQLiteHelper.COLUMN_RECORD, record);
		    values.put(MySQLiteHelper.COLUMN_TIMESPAMP, timeStamp);
		    values.put(MySQLiteHelper.COLUMN_FAV, fav);
		    long insertId = database.insert(MySQLiteHelper.TABLE_COPYPASTE, null,values);
		    Cursor cursor = database.query(MySQLiteHelper.TABLE_COPYPASTE, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,null, null, null);
		    cursor.moveToFirst();
		    Record newRecord = cursorToRecord(cursor);
		    cursor.close();
		    return newRecord;
		  }
	  
	  public void deletRecord(Record record) {
		  
		  System.out.println("Id to delete:: " + record.getId());
		  
		  long id = record.getId();
		  
		  System.out.println("the deleteRecord called in databast class!!!");
		  System.out.println("Record deleted with id: " + id);
		  database.delete(MySQLiteHelper.TABLE_COPYPASTE, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	  }
	    
	  public List<Record> getAllRecord(){
		  List<Record> records = new ArrayList<Record>();
		  
		  Cursor cursor = database.query(MySQLiteHelper.TABLE_COPYPASTE, allColumns, null, null, null, null, MySQLiteHelper.COLUMN_TIMESPAMP + " DESC");
		  
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()) {
			  Record record = cursorToRecord(cursor);			  
			  records.add(record);
			  cursor.moveToNext();
		  }
				  
		  //closing the cursor
		  cursor.close();
		  return records;
	  }
	  
	  private Record cursorToRecord(Cursor cursor) {
		  Record record = new Record();
		  record.setId(cursor.getLong(0));
		  record.setRecord(cursor.getString(1));
		  record.setTimestamp(cursor.getLong(2));
		  record.setFav(cursor.getString(3));
		  
		  return record;
	  }
	  
}
