package com.freefoam.productionreports;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MachineSettingsActivity extends Activity  {
	private long reportId;
	private Bundle genericBundle;
	
	private List<Checkpoint> checkpoints;
	private CheckpointsDBAdapter checkpointsDbHelper;
	private RecordsDBAdapter recordsDbHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Intent intent = getIntent();
    	genericBundle = new Bundle();
    	
    	reportId = Integer.parseInt(intent.getStringExtra("reportId"));
    	
    	genericBundle.putLong("reportId",Integer.parseInt(intent.getStringExtra("reportId")));
    	genericBundle.putString("productName",intent.getStringExtra("productName"));
    	genericBundle.putString("operator",intent.getStringExtra("operator"));
    	genericBundle.putString("productionLine",intent.getStringExtra("productionLine"));

        // now open the checkpoints table
        checkpointsDbHelper = new CheckpointsDBAdapter(this);
        checkpointsDbHelper.open();
        
        // Open the Records db and fill the list
        recordsDbHelper = new RecordsDBAdapter(this);
        recordsDbHelper.open();
    	fillCheckpointsForReport();
    	
    	// Specify that tabs should be displayed in the action bar.
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        for(Checkpoint cp: checkpoints)
        {
        	addCheckpointTab(cp);
        	cp.setRecordsDbAdapter(recordsDbHelper);
        }
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_settings);
        
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        if (checkpointsDbHelper != null) {
        	checkpointsDbHelper.close();
        }
        
        if (recordsDbHelper != null) {
        	recordsDbHelper.close();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_machine_settings, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.new_checkpoint:
            	confirmAddCheckpointDialog();
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmAddCheckpointDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);

    	builder.setMessage(R.string.confirm_new_checkpoint)
    	       .setTitle(R.string.new_checkpoint_dialog_title)
    	       .setPositiveButton(R.string.ok_new_checkpoint, new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		addCheckpoint();
    	        		selectNewCheckpoint();
    	        	}
    	        })
    	        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    	        	public void onClick(DialogInterface dialog, int id) {
    	        		// User cancelled the dialog
    	        	}
    	        });
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
    
    private void addCheckpoint() {
    	// create a new checkpoint using the previous checkpoint as a template
    	Checkpoint cp = createNewCheckpoint(checkpoints.get(checkpoints.size()-1));
    	
    	cp.setRecordsDbAdapter(recordsDbHelper);
    	
    	checkpoints.add(cp);
    	
    	addCheckpointTab(cp);
    }
    
    private void addCheckpointTab(Checkpoint cp) {
    	final ActionBar actionBar = getActionBar();
    	ActionBar.Tab reportTab = actionBar.newTab();
    	CheckpointTabView cpTabView =(CheckpointTabView)getLayoutInflater().inflate(R.layout.checkpoint_tab_view, null);
    	cpTabView.setText(cp.getTitle(),cp.getTimestamp());
    	reportTab.setCustomView(cpTabView);
    	genericBundle.putLong("checkpointId",cp.getRecordId());

    	Fragment checkpointFragment = new CheckpointFragment();
    	((CheckpointFragment) checkpointFragment).setArguments(genericBundle);
    	
    	((CheckpointFragment) checkpointFragment).setCheckpoint(cp);
    	
    	((CheckpointFragment) checkpointFragment).setRecordsDBHelper(recordsDbHelper);
    	reportTab.setTabListener(new ReportTabsListener(checkpointFragment));
    	actionBar.addTab(reportTab);
    }
    
    private void selectNewCheckpoint() {
    	final ActionBar actionBar = getActionBar();
    	actionBar.setSelectedNavigationItem(actionBar.getTabCount()-1);
    }
    
    private void fillCheckpointsForReport() {
    	Log.i("fillCheckpointsForReport()","Starting..");
    	
    	checkpoints = new ArrayList<Checkpoint>();
    	
        // Get all of the records from the database for this checkpoint and create the item list
        checkpoints = checkpointsDbHelper.getAllCheckpointsForReport(reportId);
        
        if (checkpoints.isEmpty()) {
        	checkpoints.add(createNewCheckpoint());
        }
    	Log.i("fillCheckpointsForReport()","Finishing..");

    }
    
    // Create a new Checkpoint.
    // This will typically be called for a new report (first checkpoint).
    private Checkpoint createNewCheckpoint() {
    	Checkpoint checkpoint = new Checkpoint(reportId, "" + (checkpoints.size()+1));
    	
    	checkpoint.setDbAdapter(checkpointsDbHelper);

    	// add record to DB
    	checkpoint.createRecord();

    	return checkpoint;
    }
    
    private Checkpoint createNewCheckpoint(Checkpoint previous) {
    	Checkpoint checkpoint = new Checkpoint(previous);
    	checkpoint.setRecordsDbAdapter(recordsDbHelper);
    	checkpoint.createRecord();
    	checkpoint.cloneRecordsFromTemplate();
    	previous.close();
    	previous.save();
    	return checkpoint;
    }
}
