package com.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PDbAdapter 
{
	public static final String KEY_NUMBER = "quantity";
	public static final String KEY_DATE = "date";
	public static final String KEY_ROWID  = "_id";
	
	private static final String TAG = "PDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String DATABASE_CREATE = "create table numbers (_id integer primary key autoincrement," +
												   "quantity integer not null, date integer unique not null);";
	
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "numbers";
	private static final int DATABASE_VERSION = 2;
	
	private final Context mContext;
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
		
	}
	
	public PDbAdapter(Context cx)
	{
		this.mContext = cx;
		mDbHelper = new DatabaseHelper(mContext);
		
	}
	
	public PDbAdapter open() throws SQLException 
	{
		
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		mDbHelper.close();
	}
	
	public long insertQuantity(long date, int quantity) throws SQLException
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_NUMBER, quantity);
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
		
	}
	
	
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_DATE,
                KEY_NUMBER}, null, null, null, null, null);
    }
	/*
	public Cursor fetchNumbers(long rowId) throws SQLException 
	{
		Cursor mCursor =
				
				mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
						KEY_DATE, KEY_NUMBER}, KEY_ROWID + "=" + rowId, null,
						null, null, null, null);
		
		if(mCursor != null)
		{
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	/*private Cursor getLastRowidCursor()
	{
		return
				mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_DATE, KEY_NUMBER}, "SELECT last_insert_rowid();", null, null, null, null, null);
	}*/
	
	public long getLastDate()
	{	
		Cursor c = fetchAllNotes();
		if(c.moveToLast())
		{
			return c.getLong(c.getColumnIndex(KEY_DATE));
		}
		else
		{
			return 0;
		}

		
	}

	public boolean plusPlus() throws SQLException
	{
		

		Cursor c =  fetchAllNotes();
		ContentValues args = new ContentValues();
		int rowId = 0;
		if(c.moveToLast())
		{
			rowId = c.getInt(c.getColumnIndex(KEY_ROWID));
			int tempQ =c.getInt(c.getColumnIndex(KEY_NUMBER));
			tempQ++;
			args.put(KEY_NUMBER, tempQ);
		}
		else
		{
			insertQuantity(System.currentTimeMillis(), 1);
		}
		
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
}
