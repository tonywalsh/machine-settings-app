package com.freefoam.productionreports;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.util.Log;

public class Checkpoint {
	private long recordId;
	private long reportId;
	private String title;
	private String timestamp;
	private String productColour;
	private boolean isOpen;
	private List<Recordable> records;
	private CheckpointsDBAdapter checkpointsDbHelper;
	private RecordsDBAdapter recordsDbHelper;
	private Checkpoint templateCheckpoint;

	public Checkpoint() {
		super();
		this.isOpen = true;
		this.timestamp = new SimpleDateFormat("HH:mm").format(new Date());
		this.productColour = "White";
		records = new ArrayList<Recordable>();
	}
	
	// ctor for record from db
	public Checkpoint(long reportId, String title) {
		this();
		this.reportId = reportId;
		this.title = title;
		Log.i("Checkpoint::ctor", "Creating checkpoint: " + title);
	}
	
	// ctor for record from db
	public Checkpoint(long recordId, long reportId, String title, String created) {
		this(reportId,title);
		this.recordId = recordId;
		this.timestamp = created;
	}

	// ctor for record from db
	public Checkpoint(long recordId, long reportId, String title, boolean isOpen, String created, String productColour) {
		this(reportId,title);
		this.recordId = recordId;
		this.timestamp = created;
		this.isOpen = isOpen;
		this.productColour = productColour;
	}

	// Checkpoint that can be filled from the db
	public Checkpoint(Checkpoint prev) {
		this(prev.reportId, "" + (Integer.parseInt(prev.title) + 1));
		this.checkpointsDbHelper = prev.checkpointsDbHelper;
		this.recordsDbHelper = prev.recordsDbHelper;
		this.productColour = prev.productColour;
		this.templateCheckpoint = prev;
	}

	public void cloneRecordsFromTemplate() {
		for(Recordable record: templateCheckpoint.getRecords())
        {
			BasicRecord new_rec = new BasicRecord(record);
			new_rec.setCheckpointId(this.recordId);
			recordsDbHelper.createRecord(new_rec);
			records.add(new_rec);
			//Log.i("cloneRecordsFromTemplate",new_rec.toString());
        }	
	}
	
	// load from the db
	public void loadData() {
		Checkpoint cp = checkpointsDbHelper.getCheckpoint(recordId);
		this.reportId = cp.reportId;
		this.timestamp = cp.timestamp;
		this.title = cp.title;
		this.productColour = cp.productColour;
		this.records = new ArrayList<Recordable>();
		records = recordsDbHelper.getAllRecordsForCheckpoint(recordId);
        
        if (records.isEmpty()) {
        	createNewRecordSet();
        } 
	}
	
	public void setDbAdapter(CheckpointsDBAdapter cdba){
		this.checkpointsDbHelper = cdba;
	}
	
	public void setRecordsDbAdapter(RecordsDBAdapter rdba){
		this.recordsDbHelper = rdba;
	}

	public RecordsDBAdapter getRecordsDbAdapter(){
		return this.recordsDbHelper;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getRecordId() {
		return recordId;
	}
	
	public long getReportId() {
		return reportId;
	}
	
	
	public String getTitle() {
		return title;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public String getProductColour() {
		return productColour;
	}

	public void setProductColour(String productColour) {
		this.productColour = productColour;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void close() {
		this.isOpen = false;
	}
	
	public long createRecord() {
		return checkpointsDbHelper.createCheckpoint(this);
	}
	
	public boolean save() {
		return checkpointsDbHelper.updateCheckpoint(this);
	}
	
    public List<Recordable> getRecords() {
		records = recordsDbHelper.getAllRecordsForCheckpoint(recordId);
        
        if (records.isEmpty()) {
        	createNewRecordSet();
        } 
    	return records;
    }
    
    public List<Recordable> getRecords(String section) {
    	List<Recordable> _records = recordsDbHelper.getAllRecordsForCheckpointSection(recordId,section);
        
        if (_records.isEmpty()) {
        	createNewRecordSet();
        	_records.clear();
        	_records = recordsDbHelper.getAllRecordsForCheckpointSection(recordId,section);
        } 
    	return _records;
    }

    
    private void createNewRecordSet() {
    	records.add(createNewRecord(this.recordId, "Main", "Weight", Recordable.FLOAT_INPUT, "Kg/m", false));
    	records.add(createNewRecord(this.recordId, "Main", "Haul Off Speed", Recordable.FLOAT_INPUT, "m/min", false));
    	records.add(createNewRecord(this.recordId, "Main", "Haul Off Force", Recordable.FLOAT_INPUT,"KN", false));
    	records.add(createNewRecord(this.recordId, "Main", "Screw Speed", Recordable.FLOAT_INPUT,"rpm", false));
    	records.add(createNewRecord(this.recordId, "Main", "Doser Feeder", Recordable.FLOAT_INPUT,"rpm", false));
    	records.add(createNewRecord(this.recordId, "Main", "Torque", Recordable.FLOAT_INPUT,"%", false));
    	records.add(createNewRecord(this.recordId, "Main", "Masterbatch", Recordable.FLOAT_INPUT,"rpm", false));
    	records.add(createNewRecord(this.recordId, "Main", "Melt Pressure", Recordable.FLOAT_INPUT,"bar", false));
    	records.add(createNewRecord(this.recordId, "Main", "Melt Temp", Recordable.FLOAT_INPUT,"oC", false));
    	records.add(createNewRecord(this.recordId, "Main", "De-Gas", Recordable.FLOAT_INPUT,"mBar", false));
    	records.add(createNewRecord(this.recordId, "Main", "Barrel Temp 1", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Barrel Temp 2", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Barrel Temp 3", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Barrel Temp 4", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Barrel Temp 5", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Adaptor 1", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Adaptor 2", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 1", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 2", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 3", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 4", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 5", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 6", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 7", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 8", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 9", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 10", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Die Temp 11", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Main", "Vac Pump 1", Recordable.FLOAT_INPUT,"mBar", false));
    	records.add(createNewRecord(this.recordId, "Main", "Vac Pump 2", Recordable.FLOAT_INPUT,"mBar", false));
    	records.add(createNewRecord(this.recordId, "Main", "Vac Pump 3", Recordable.FLOAT_INPUT,"mBar", false));
    	records.add(createNewRecord(this.recordId, "Main", "Vac Pump 4", Recordable.FLOAT_INPUT,"mBar", false));
    	records.add(createNewRecord(this.recordId, "Main", "Heater Top Near", Recordable.FLOAT_INPUT,"mBar", true));
    	records.add(createNewRecord(this.recordId, "Main", "Heater Top Far", Recordable.FLOAT_INPUT,"mBar", true));
    	records.add(createNewRecord(this.recordId, "Main", "Heater Bottom Near", Recordable.FLOAT_INPUT,"mBar", true));
    	records.add(createNewRecord(this.recordId, "Main", "Heater Bottom Far", Recordable.FLOAT_INPUT,"mBar", true));
    	
    	records.add(createNewRecord(this.recordId, "Coex", "Screw Speed", Recordable.FLOAT_INPUT,"rpm", false));
    	records.add(createNewRecord(this.recordId, "Coex", "Doser Feeder", Recordable.FLOAT_INPUT,"rpm", false));
    	records.add(createNewRecord(this.recordId, "Coex", "Torque", Recordable.FLOAT_INPUT,"%", false));
    	records.add(createNewRecord(this.recordId, "Coex", "Masterbatch", Recordable.FLOAT_INPUT,"rpm", false));
    	records.add(createNewRecord(this.recordId, "Coex", "Melt Pressure", Recordable.FLOAT_INPUT,"bar", false));
    	records.add(createNewRecord(this.recordId, "Coex", "Melt Temp", Recordable.FLOAT_INPUT,"oC", false));
    	records.add(createNewRecord(this.recordId, "Coex", "Heater Bottom Far", Recordable.FLOAT_INPUT,"mBar", true));
    	records.add(createNewRecord(this.recordId, "Coex", "Barrel Temp 1", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Coex", "Barrel Temp 2", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Coex", "Barrel Temp 3", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Coex", "Barrel Temp 4", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Coex", "Adaptor 1", Recordable.FLOAT_INPUT,"oC", true));
    	records.add(createNewRecord(this.recordId, "Coex", "Adaptor 2", Recordable.FLOAT_INPUT,"oC", true)); 	

    }
    
    private BasicRecord createNewRecord(long checkpointId, String section, String title, int inputType, String units, boolean dual) {
    	BasicRecord record = new BasicRecord(checkpointId, section, title, inputType, units, dual);
    	// add record to DB
    	recordsDbHelper.createRecord(record);
    	//record.(recordsDbHelper);
    	return record;
    }

}
