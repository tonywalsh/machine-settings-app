package com.freefoam.productionreports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.InputType;

public class ReportsDBAdapter {
	public static final String ROW_ID 	= "_id";
	public static final String LINE 	= "line";
	public static final String PRODUCT 	= "product";
	public static final String COLOUR	= "colour";
	public static final String CREATED 	= "created";
	public static final String ISOPEN 	= "isopen";
	public static final String CREATOR 	= "creator";

	private static final String DATABASE_TABLE = "reports";
    
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
    public ReportsDBAdapter(Context ctx) {
    	this.mCtx = ctx;
	}

    /**
     * Open the reports database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public ReportsDBAdapter open() throws SQLException {
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
     * Create a new Report. If the Report is successfully created return the new
     * rowId for that report, otherwise return a -1 to indicate failure.
     * 
     * @param Report report
     * @return rowId or -1 if failed
     */
    public long createReport(Report report) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(LINE, report.getLine());
    	initialValues.put(PRODUCT, report.getProduct());
    	initialValues.put(COLOUR, report.getColour());
    	initialValues.put(ISOPEN, report.isOpen());
    	initialValues.put(CREATED, report.getCreationDate());
    	initialValues.put(CREATOR, report.getCreator());
    	return this.mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    /**
     * Delete the Report with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteReport(long rowId) {

    	return this.mDb.delete(DATABASE_TABLE, ROW_ID + "=" + rowId, null) > 0; //$NON-NLS-1$
    }

    /**
     * Return a Cursor over the list of all Reports in the database
     * 
     * @return Cursor over all reports
     */
    public List<Report> getAllReports() {
    	List<Report> reports = new ArrayList<Report>();
    	Cursor cursor = this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID, LINE, PRODUCT, COLOUR, CREATED, ISOPEN, CREATOR }, null, null, null, null, ROW_ID+" DESC");
    	cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          Report report = cursorToReport(cursor);
          reports.add(report);
          cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return reports;
    }

    private Report cursorToReport(Cursor c){
    	
    	Report report = new Report(
    				c.getInt(0),		// id
    				c.getString(1),		// line
    				c.getString(2),		// product
    				c.getString(3),		// colour
    				c.getString(4),		// created
    				(c.getInt(5)==1),	// isOpen
    				c.getString(6)		// creator
    			);
    	return report;
    }
    /**
     * Return a Cursor positioned at the Report that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching car, if found
     * @throws SQLException if car could not be found/retrieved
     */
    public Cursor getReport(long rowId) throws SQLException {

    	Cursor mCursor =

    			this.mDb.query(true, DATABASE_TABLE, new String[] { ROW_ID,	LINE, PRODUCT, COLOUR },
    					ROW_ID + "=" + rowId, null, null, null, null, null);
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	return mCursor;
    }

    /**
     * Update the Report.
     * 
     * @param rowId
     * @param line
     * @param product
     * @param colour
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateReport(long rowId, String line, String product, String colour){
    	ContentValues args = new ContentValues();
    	args.put(LINE, line);
    	args.put(PRODUCT, product);
    	args.put(COLOUR, colour);

    	return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }

}
