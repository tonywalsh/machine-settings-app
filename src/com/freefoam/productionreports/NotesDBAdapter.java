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

public class NotesDBAdapter {
	public static final String ROW_ID 		= "_id";
	public static final String CHECKPOINTID = "checkpoint_id";
	public static final String CONTENT		= "title";
	public static final String CREATED		= "created";

	private static final String DATABASE_TABLE = "notes";

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
    public NotesDBAdapter(Context ctx) {
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
    public NotesDBAdapter open() throws SQLException {
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
    public long createRecord(Note note) {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(CHECKPOINTID, note.getCheckpointId());
    	initialValues.put(CONTENT, note.getContent());
    	initialValues.put(CREATED, note.getTimestamp());

    	long res =  this.mDb.insert(DATABASE_TABLE, null, initialValues);
    	if (res>-1) {
    		note.setRecordId(res);
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
    			CONTENT, CREATED }, null, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of all Records for a Report in the database
     * 
     * @return Cursor over all records
     */
    public List<Note> getAllNotesForCheckpoint(long checkpointId) {
    	List<Note> notes = new ArrayList<Note>();
    	Cursor mCursor = this.mDb.query(DATABASE_TABLE, new String[] { ROW_ID, CHECKPOINTID,
    			CONTENT, CREATED }, CHECKPOINTID + "=" + checkpointId, null, null, null, null, null);
    	mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
          Note note = cursorToNote(mCursor);
          notes.add(note);
          mCursor.moveToNext();
        }
        // Make sure to close the cursor
        mCursor.close();
        return notes;
    }

    private Note cursorToNote(Cursor c){
    	Note note = new Note(
    				c.getInt(0),		// id
    				c.getInt(1),		// checkpointId
    				c.getString(2),		// content
    				c.getString(3)		// timestamp
    			);
    	//record.setDbAdapter(this);
    	return note;
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
    	    			CONTENT, CREATED}, ROW_ID + "=" + rowId, null, null, null, null, null);
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
     * @param content
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateRecord(long rowId, int checkpointId, String content){
    	ContentValues args = new ContentValues();
    	args.put(CHECKPOINTID, checkpointId);
    	args.put(CONTENT, content);

    	return this.mDb.update(DATABASE_TABLE, args, ROW_ID + "=" + rowId, null) >0; 
    }
    
    // update db using BasicRecord object
    public boolean updateRecord(Note note){
    	long rowId = note.getRecordId();
    	ContentValues updateValues = new ContentValues();
    	Log.i("updateNote()","row id: " + rowId + ", content: " + note.getContent() + ", timestamp: " + note.getTimestamp());
    	updateValues.put(CONTENT, note.getContent());
    	return this.mDb.update(DATABASE_TABLE, updateValues, ROW_ID + "=" + rowId, null) >0;
    }
}
