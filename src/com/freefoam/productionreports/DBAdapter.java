package com.freefoam.productionreports;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    public static final String DATABASE_NAME = "productionReports";

    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_REPORTS = "create table reports (_id integer primary key autoincrement, "
    + ReportsDBAdapter.LINE+ " TEXT,"
    + ReportsDBAdapter.PRODUCT+ " TEXT,"
    + ReportsDBAdapter.COLOUR+ " TEXT,"
    + ReportsDBAdapter.CREATED+ " TEXT,"
    + ReportsDBAdapter.ISOPEN+ " TEXT,"
    + ReportsDBAdapter.CREATOR+ " TEXT"
    + ");";

    private static final String CREATE_TABLE_CHECKPOINTS = "create table checkpoints (_id integer primary key autoincrement, "
    	    + CheckpointsDBAdapter.REPORTID+" INTEGER,"
    		+ CheckpointsDBAdapter.TITLE+ " TEXT,"
    		+ CheckpointsDBAdapter.ISOPEN+ " TEXT,"
    		+ CheckpointsDBAdapter.CREATED+ " TEXT,"
    		+ CheckpointsDBAdapter.PRODUCT_COLOUR+ " TEXT"
    + ");";

    private static final String CREATE_TABLE_RECORDS = "create table records (_id integer primary key autoincrement, "
    + RecordsDBAdapter.CHECKPOINTID+" INTEGER,"
    + RecordsDBAdapter.SECTION+" TEXT,"
    + RecordsDBAdapter.TITLE+" TEXT,"
    + RecordsDBAdapter.DATATYPE+" INTEGER,"
    + RecordsDBAdapter.UNITS+" TEXT,"
    + RecordsDBAdapter.DUAL+" INTEGER,"
    + RecordsDBAdapter.SETVAL+" TEXT,"
    + RecordsDBAdapter.ACTVAL+" TEXT"+ ");";

    private static final String CREATE_TABLE_NOTES = "create table notes (_id integer primary key autoincrement, "
    + NotesDBAdapter.CHECKPOINTID+" INTEGER,"
    + NotesDBAdapter.CONTENT+" TEXT,"
    + NotesDBAdapter.CREATED+ " TEXT"+ ");";

    private final Context context; 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    /**
     * Constructor
     * @param ctx
     */
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            Log.i("DBAdapter::onCreate","creating table reports" );
            db.execSQL(CREATE_TABLE_REPORTS);
            db.execSQL(CREATE_TABLE_CHECKPOINTS);
            db.execSQL(CREATE_TABLE_RECORDS);          
            db.execSQL(CREATE_TABLE_NOTES);          
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {               
            // Adding any table mods to this guy here
        }
    } 

   /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DBAdapter
     */
    public DBAdapter open() throws SQLException 
    {
        //Log.i("DBAdapter.open","Calling getWritableDatabase()" );
        this.db = this.DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * close the db 
     * return type: void
     */
    public void close() 
    {
        this.DBHelper.close();
    }
}