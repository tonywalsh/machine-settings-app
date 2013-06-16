package com.freefoam.productionreports;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
		implements NewReportDialog.NewReportDialogListener {
	
	private ReportsDBAdapter reportsDbHelper;
	private CheckpointsDBAdapter checkpointsDbHelper;
	private ReportsAdapter reportsAdapter;
	private ArrayList<Report> reports;
	private ListView reportList;
    	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        reportList = (ListView)findViewById(R.id.report_list);
        // this will create the database if it doesn't already exist
        DBAdapter dba = new DBAdapter(this);
        dba.open();
        dba.close();
        
        // now open the reports db
        reportsDbHelper = new ReportsDBAdapter(this);
        reportsDbHelper.open();
        // now open the checkpoints table
        checkpointsDbHelper = new CheckpointsDBAdapter(this);
        checkpointsDbHelper.open();
 
       // fill the view
        fillData();
        Log.i("onCreate","Complete");

    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy","Destroying");
        if (reportsDbHelper != null) {
        	reportsDbHelper.close();
        }
        
        if (checkpointsDbHelper != null) {
        	checkpointsDbHelper.close();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void showNewReportDialog(View view) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NewReportDialog();
        dialog.show(getFragmentManager(), "NewReportDialog");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NewReportDialog.NewReportDialogListener interface
    @Override
    public void onDialogPositiveClick(NewReportDialog dialog) {
        // User touched the dialog's positive button
    	Report report = new Report(
    			dialog.getLine(),
    			dialog.getProduct(),
    			dialog.getOperator());
    	long res = reportsDbHelper.createReport(report);
    	if (res==-1) {
    		Toast.makeText(getBaseContext(), "Failed to make new report", Toast.LENGTH_LONG).show();
    	} else {
    		report.setRecordId(res);
    		// create an initial checkpoint
    		report.addCheckpoint(createNewCheckpoint(res));
        	reportsAdapter.add(report);
    		Toast.makeText(getBaseContext(), "New report successfully created.", Toast.LENGTH_LONG).show();
    	}
    }
    
    private Checkpoint createNewCheckpoint(long reportId) {
    	Checkpoint checkpoint = new Checkpoint(reportId, "1");
    	
    	checkpoint.setDbAdapter(checkpointsDbHelper);

    	// add record to DB
    	checkpoint.createRecord();

    	return checkpoint;
    }
    
    @Override
    public void onDialogNegativeClick(NewReportDialog dialog) {
        // User touched the dialog's negative button
    	Toast.makeText(getBaseContext(), "New Report Cancelled", Toast.LENGTH_LONG).show();
    }
    
    private void fillData() {
        // Get all of the reports from the database and create the item list
        reports = (ArrayList<Report>)reportsDbHelper.getAllReports();
        
        // for each report get the checkpoint timestamps from the checkpoints table
        for (Report report: reports) {
        	report.setCheckpointTimestamps((ArrayList<String>)checkpointsDbHelper.getAllTimestampsForReport(report.getRecordId()));
        }
        reportsAdapter = new ReportsAdapter(this, reports);
        reportList.setAdapter(reportsAdapter);
        
        reportList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	
                Report rep = (Report)reportList.getItemAtPosition(position);
                
                Intent intent = new Intent(getApplicationContext(),MachineSettingsActivity.class);

                // Add report db id to Intent 
                intent.putExtra("reportId","" + rep.getRecordId());
                intent.putExtra("productName",rep.getProduct());
                intent.putExtra("operator",rep.getCreator());
                intent.putExtra("productionLine",rep.getLine());
                startActivity(intent);                
            }
        });
    }
}
