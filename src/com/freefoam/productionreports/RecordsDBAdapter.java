package com.freefoam.productionreports;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecordsDBAdapter {
	public static final String ROW_ID 		= "_id";
	public static final String CHECKPOINTID = "checkpoint_id";
	public static final String SECTION		= "section";
	public static final String TITLE		= "title";
	public static final String DATATYPE		= "datatype";
	public static final String UNITS		= "units";
	public static final String DUAL			= "is_dual";
	public static final String SETVAL		= "setval";
	public static final String ACTVAL		= "actval";

	private static final String DATABASE_TABLE = "records";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		DatabaseHelper(Context context) {
		   super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    }
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
     * 
     * @param ctx
     *            the Context within which to work
     */
    public RecordsDBAdapter(Context ctx) {
    	this.mCtx = ctx;
	}

    /**
     * Open the records database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public RecordsDBAdapter open() throws SQLException {
    	this.mDbHelper = new DatabaseHelper(this.mCtx);
    	this.mDb = this.mDbHelper.getWritableDatabase();
    	return this;
    }

    /**
     * close return type: void
     */
    public void close() {
    	this.mDbHelper.close();
    }

    /**
     * Create a new db Record from a Record object. If the Record is successfully created return the new
     * rowId for that record, otherwise return a -1 to indicate failure.
     * 
     * @param Record record
     * @return rowId or -1 if failed
     */
    public long createRecord(BasicRecord record) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(CHECKPOINTID, record.getCheckpointId());
    	initialValues.put(SECTION, record.getSection());
    	initialValues.put(TITLE, record.getTitle());
    	initialValues.put(DATATYPE, record.getDataType());
       	initialValues.put(UNITS, record.getUnits());
        initialValues.put(DUAL, record.isDual());
    	initialValues.put(SETVAL, record.getSetValue());
    	initialValues.put(ACTVAL, record.getActValue());

    	long res =  this.mDb.insert(DATABASE_TABLE, null, initialValues);
    	if (res>-1) {
    		record.setRecordId(res);
    	}
    	return res;
    }

    /**
     * Delete the Record with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteRecord(long rowId) {

    	return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all Records in the database
     * 
     * @return Cursor over all records
     */
    public Cursor getAllRecords() {

    	return this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID, CHECKPOINTID,
    			SECTION, TITLE, SETVAL, ACTVAL }, null, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of all Records for a Report in the database
     * 
     * @return Cursor over all records
     */
    public List<Recordable> getAllRecordsForCheckpoint(long checkpointId) {
    	List<Recordable> records = new ArrayList<Recordable>();
    	Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID, CHECKPOINTID,
    			SECTION, TITLE, DATATYPE, UNITS, DUAL, SETVAL, ACTVAL }, CHECKPOINTID + "=" + checkpointId, null, null, null, null, null);
    	mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
          Recordable record = cursorToRecord(mCursor);
          records.add(record);
          mCursor.moveToNext();
        }
        // Make sure to close the cursor
        mCursor.close();
        return records;
    }

    /**
     * Return a Cursor over the list of all Records for a Report in the database
     * filtered by checkpointId and section
     * 
     * @return Cursor over all records
     */
    public List<Recordable> getAllRecordsForCheckpointSection(long checkpointId, String section) {
    	List<Recordable> records = new ArrayList<Recordable>();
    	Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID, CHECKPOINTID,
    			SECTION, TITLE, DATATYPE, UNITS, DUAL, SETVAL, ACTVAL }, CHECKPOINTID + "=" + checkpointId + " AND " + SECTION + "='" + section + "'", null, null, null, null, null);
    	mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
          Recordable record = cursorToRecord(mCursor);
          records.add(record);
          mCursor.moveToNext();
        }
        // Make sure to close the cursor
        mCursor.close();
        return records;
    }
    
    private Recordable cursorToRecord(Cursor c){
    	Recordable record = new BasicRecord(
    				c.getInt(0),		// id
    				c.getInt(1),		// checkpointId
    				c.getString(2),		// section
    				c.getString(3),		// title
    				c.getInt(4),		// datatype
    				c.getString(5),		// units
    				(c.getInt(6)==1),	// dual
    				c.getString(7),		// setval
    				c.getString(8)		// actval
    			);
    	//record.setDbAdapter(this);
    	return record;
    }
    /**
     * Return a Cursor positioned at the Record that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching record, if found
     * @throws SQLException if record could not be found/retrieved
     */
    public Cursor getRecord(long rowId) throws SQLException {

    	Cursor mCursor =

    			this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, CHECKPOINTID,
    	    			SECTION, TITLE, DATATYPE, UNITS, DUAL, SETVAL, ACTVAL}, ROW_ID + "=" + rowId, null, null, null, null, null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }

    /**
     * Update the Record.
     * 
     * @param rowId
     * @param checkpointId
     * @param section
     * @param title
     * @param dataType
     * @param units
     * @param dual
     * @param setval
     * @param actval
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateRecord(long rowId, int checkpointId, String section, String title, int dataType, String units, int dual,
    		String setval, String actval){
    	ContentValues args = new ContentValues();
    	args.put(CHECKPOINTID, checkpointId);
    	args.put(SECTION, section);
    	args.put(TITLE, title);
    	args.put(DATATYPE, dataType);
    	args.put(UNITS, units);
    	args.put(DUAL, dual);
    	args.put(SETVAL, setval);
    	args.put(ACTVAL, actval);

    	return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }
    
    // update db using BasicRecord object
    public boolean updateRecord(Recordable record){
    	long rowId = record.getRecordId();
    	ContentValues updateValues = new ContentValues();
    	Log.i("updateRecord()","row id: " + rowId + ", setVal: " + record.getSetValue() + ", getVal: " + record.getActValue());
    	updateValues.put(SETVAL, record.getSetValue());
    	updateValues.put(ACTVAL, record.getActValue());
    	return this.mDb.update(DATABASE_TABLE, updateValues, ROW_ID + "=" + rowId, null) >0;
    }
}
