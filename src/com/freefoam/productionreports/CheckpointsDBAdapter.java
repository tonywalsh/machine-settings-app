package com.freefoam.productionreports;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CheckpointsDBAdapter {
	public static final String ROW_ID 		= "_id";
	public static final String REPORTID		= "report_id";
	public static final String TITLE		= "title";
	public static final String ISOPEN 		= "isopen";
	public static final String CREATED		= "created";
	public static final String PRODUCT_COLOUR= "product_colour";

	private static final String DATABASE_TABLE = "checkpoints";

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
    public CheckpointsDBAdapter(Context ctx) {
    	this.mCtx = ctx;
	}

    /**
     * Open the checkpoints database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public CheckpointsDBAdapter open() throws SQLException {
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
     * Create a new db Checkpoint from a Checkpoint object. If the Checkpoint is successfully created return the new
     * rowId for that checkpoint, otherwise return a -1 to indicate failure.
     * 
     * @param Checkpoint cp
     * @return rowId or -1 if failed
     */
    public long createCheckpoint(Checkpoint cp) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(REPORTID, cp.getReportId());
    	initialValues.put(TITLE, cp.getTitle());
    	initialValues.put(ISOPEN, cp.isOpen());
    	initialValues.put(CREATED, cp.getTimestamp());
    	initialValues.put(PRODUCT_COLOUR, cp.getProductColour());
    	long res = this.mDb.insert(DATABASE_TABLE, null, initialValues);
    	if (res>-1) {
    		cp.setRecordId(res);
    	}
    	return res;
    }

    /**
     * Delete the Checkpoint with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteCheckpoint(long rowId) {

    	return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all Checkpoint in the database
     * 
     * @return Cursor over all checkpoints
     */
    public Cursor getAllCheckpoints() {

    	return this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID, REPORTID,
    			TITLE, ISOPEN, CREATED, PRODUCT_COLOUR }, null, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of all Checkpoints for a Report in the database
     * 
     * @return Cursor over all checkpoints
     */
    public List<Checkpoint> getAllCheckpointsForReport(long reportId) {
    	List<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
    	Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID, REPORTID,
    			TITLE, ISOPEN, CREATED, PRODUCT_COLOUR }, REPORTID + "=" + reportId, null, null, null, null, null);
    	mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
          Checkpoint cp = cursorToCheckpoint(mCursor);
          checkpoints.add(cp);
          mCursor.moveToNext();
        }
        // Make sure to close the cursor
        mCursor.close();
        return checkpoints;
    }

    public List<String> getAllTimestampsForReport(long reportId) {
    	List<String> timestamps = new ArrayList<String>();

    	Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { CREATED }, REPORTID + "=" + reportId, null, null, null, null, null);
    	mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
        	timestamps.add(mCursor.getString(0)); // created
        	mCursor.moveToNext();
        }
    	return timestamps;
    }
    
    private Checkpoint cursorToCheckpoint(Cursor c){
    	Checkpoint cp = new Checkpoint(
    				c.getInt(0),		// id
    				c.getInt(1),		// reportId
    				c.getString(2),		// title
    				(c.getInt(3)==1),	// isOpen
    				c.getString(4),		// created
    				c.getString(5)		// product_colour
    			);
    	cp.setDbAdapter(this);
    	return cp;
    }
    /**
     * Return a Checkpoint that matches the given rowId
     * @param rowId
     * @return Checkpoint positioned to matching record, if found
     * @throws SQLException if record could not be found/retrieved
     */
    public Checkpoint getCheckpoint(long rowId) throws SQLException {

    	Cursor mCursor =

    			this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID, REPORTID,
    	    			TITLE, ISOPEN, CREATED, PRODUCT_COLOUR}, ROW_ID + "=" + rowId, null, null, null, null, null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return cursorToCheckpoint(mCursor);
    }

    /**
     * Update the Checkpoint.
     * 
     * @param rowId
     * @param report_id
     * @param title
     * @param created
     * @return true if the checkpoint was successfully updated, false otherwise
     */
    public boolean updateCheckpoint(long rowId, int report_id, String title, int isOpen, int created, String productColour){
    	ContentValues args = new ContentValues();
    	args.put(REPORTID, report_id);
    	args.put(TITLE, title);
    	args.put(ISOPEN, isOpen);
    	args.put(CREATED, created);
    	args.put(PRODUCT_COLOUR, productColour);

    	return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }
    
    // update db using Checkpoint object
    public boolean updateCheckpoint(Checkpoint cp){
    	long rowId = cp.getRecordId();
    	ContentValues updateValues = new ContentValues();
    	updateValues.put(TITLE, cp.getTitle());
    	updateValues.put(ISOPEN,cp.isOpen());
    	updateValues.put(PRODUCT_COLOUR,cp.getProductColour());
    	return this.mDb.update(DATABASE_TABLE, updateValues, ROW_ID + "=" + rowId, null) >0;
    }
}
