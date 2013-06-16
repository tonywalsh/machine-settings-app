package com.freefoam.productionreports;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckpointFragment extends Fragment implements NewNoteDialog.NewNoteDialogListener {
	private long checkpointId;
	private String productName;
	private String operator;
	private String productionLine;
	private Checkpoint checkpoint;
	
	private NotesAdapter notesAdapter;
	//private RecordsAdapter recordsAdapter;
	
	private ArrayList<Recordable> records;
	private List<Recordable> coexRecords;
	private ArrayList<Note> notes;
	private RecordsDBAdapter recordsDbHelper;
	private CheckpointsDBAdapter checkpointsDbHelper;
	private NotesDBAdapter notesDbHelper;
	private ProgressDialog pd;
	
	private LinearLayout recordsList;
	private ListView notesList;
	private View mainView;
	private TextView colours_tv;

	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	
        // Inflate the layout for this fragment and update the report details
        mainView =  inflater.inflate(R.layout.fragment_machine_settings, container, false);
        TextView name_tv = (TextView)mainView.findViewById(R.id.descProductName);
        TextView operator_tv = (TextView)mainView.findViewById(R.id.descOperator);
        TextView line_tv = (TextView)mainView.findViewById(R.id.descProductionLine);
    	colours_tv = (TextView)mainView.findViewById(R.id.descProductColour);
    	if (checkpoint.isOpen()) {
    		colours_tv.setOnClickListener(new OnClickListener() 
    		{
            	@Override
            	public void onClick(View v) 
            	{    
            		launchColourSelect();
            	}
    		});
    	}
    	//TextView colour_tv = (TextView)mainView.findViewById(R.id.descProductColour);
        name_tv.setText(productName);
        operator_tv.setText(operator);
        line_tv.setText(productionLine);
       	
        recordsList = (LinearLayout)mainView.findViewById(R.id.records_list);
        
        notesList = (ListView)mainView.findViewById(R.id.note_list);
        notesList.setAdapter(notesAdapter);
        final ImageButton noteButton = (ImageButton) mainView.findViewById(R.id.NewNoteBtn);
        noteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showNewNoteDialog(v);
            }
        });
        setRetainInstance(true);
        new DbAsyncTask().execute();
        return mainView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        productionLine = bundle.getString("productionLine");
        productName = bundle.getString("productName");
        operator = bundle.getString("operator");
        checkpointId = bundle.getLong("checkpointId");
        
        // Open the Notes table and fill the list
        notesDbHelper = new NotesDBAdapter(getActivity());
        notesDbHelper.open();

        getNotes();
        
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (notesDbHelper != null) {
        	notesDbHelper.close();
        }
    }
    
    public void onResume() {
        super.onResume();
    }
    
    public void setCheckpoint(Checkpoint cp){
    	this.checkpoint = cp;
    }
    
    private void getCheckpointRecords() {
    	records = new ArrayList<Recordable>();
    	coexRecords = new ArrayList<Recordable>();
    	 
        //checkpoint = checkpointsDbHelper.getCheckpoint(checkpointId);
        checkpoint.setRecordsDbAdapter(recordsDbHelper);
        
        records = (ArrayList<Recordable>)checkpoint.getRecords("Main");
        coexRecords = (ArrayList<Recordable>)checkpoint.getRecords("Coex");
    }
    
    private void buildCheckpointRecordsList() {
    	
        for(Recordable record: records) {
        	CheckpointRow cpr = new CheckpointRow(getActivity(), record, checkpoint.isOpen());
        	cpr.setRecordsDbAdapter(recordsDbHelper);
        	recordsList.addView(cpr);
        }
        
        recordsList.addView(new CheckpointRowSeparator(getActivity(),"Coextruder"));
        
        for(Recordable record: coexRecords) {
        	CheckpointRow cpr = new CheckpointRow(getActivity(), record, checkpoint.isOpen());
        	cpr.setRecordsDbAdapter(recordsDbHelper);
        	recordsList.addView(cpr);
        }       
    }
	
	public void setCheckpointsDBHelper(CheckpointsDBAdapter cdba) {
		this.checkpointsDbHelper = cdba;
	}
	
	public void setRecordsDBHelper(RecordsDBAdapter rdba) {
		this.recordsDbHelper = rdba;
	}
	
	// Create a new note
	public void showNewNoteDialog(View view) {
		// Create an instance of the dialog fragment and show it
	    NewNoteDialog dialog = new NewNoteDialog();
	    dialog.setListener(this);
	    dialog.show(getFragmentManager(), "NewNoteDialog");
	}
	
    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NewReportDialog.NewReportDialogListener interface
    @Override
    public void onDialogPositiveClick(NewNoteDialog dialog) {
        // User touched the dialog's positive button
    	Note note = new Note(checkpointId, dialog.getContent());
    	notesAdapter.add(note);
    	long res = notesDbHelper.createRecord(note);
    	if (res==-1) {
    		Toast.makeText(getActivity(), "Failed to make new note", Toast.LENGTH_LONG).show();
    	} else {
    		note.setRecordId(res);
    		Toast.makeText(getActivity(), "New note successfully created.", Toast.LENGTH_LONG).show();
    	}
    }

    @Override
    public void onDialogNegativeClick(NewNoteDialog dialog) {
        // User touched the dialog's negative button
    	Toast.makeText(getActivity(), "New Note Cancelled", Toast.LENGTH_LONG).show();
    }
    
    private void getNotes() {
        // Get all of the notes from the database for this checkpoint and create the item list
        notes = (ArrayList<Note>)notesDbHelper.getAllNotesForCheckpoint(checkpointId);
        //Log.i("getNotes()", "Checkpoint Id " + checkpointId);
        notesAdapter = new NotesAdapter(getActivity(), notes);

    }
    
    private void launchColourSelect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a Colour");
        final CharSequence[] col_arr = getResources().getStringArray(R.array.colours);
        builder.setSingleChoiceItems(col_arr, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	colours_tv.setText(col_arr[item]);
            	checkpoint.setProductColour((String)col_arr[item]);
            	checkpoint.save();
            	dialog.dismiss();;
            }
        });

        AlertDialog alert = builder.create();   
        alert.show();
    }
    
    private class DbAsyncTask extends AsyncTask<String, String, String> {
    	
    	private ProgressDialog progDialog;
    	
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        	//Log.i("onPreExecute()","starting");
            progDialog = new ProgressDialog(getActivity());
            progDialog.setMessage("Loading...");
            progDialog.setIndeterminate(true);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(true);
            progDialog.show();
        }
        
        protected String doInBackground(String... args) {
        	//Log.i("doInBackground()","starting");
            // Get all of the records from the database for this checkpoint and create the item list
        	records = new ArrayList<Recordable>();
        	coexRecords = new ArrayList<Recordable>();
            checkpoint.setRecordsDbAdapter(recordsDbHelper);
            
            records = (ArrayList<Recordable>)checkpoint.getRecords("Main");
            coexRecords = (ArrayList<Recordable>)checkpoint.getRecords("Coex");
        	//Log.i("doInBackground()","done");
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            //super.onPostExecute(unused);
            //Log.i("onPostExecute()","started");
            progDialog.dismiss();
            // build the records list;
            buildCheckpointRecordsList();
            if(mainView!=null) {
            	colours_tv.setText(checkpoint.getProductColour());
            }
            
        }
   }

}