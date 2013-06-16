package com.freefoam.productionreports;

public class BasicRecord implements Recordable {
	private long recordId;
	private long checkpointId;
	private String section;
	private String title;
	private int dataType;
	private String units;
	private boolean dual; // true by default
	private String setValue;
	private String actValue;
	public BasicRecord() {
	}
	
	// ctor for record from db
	public BasicRecord(long recordId, long checkpointId, String section, String title, int dataType, String units, boolean dual, String setValue, String actValue) {
		super();
		this.recordId = recordId;
		this.checkpointId = checkpointId;
		this.section = section;
		this.title = title;
		this.dataType = dataType;
		this.units = units;
		this.dual = dual;
		this.setValue = setValue;
		this.actValue = actValue;
	}


	// ctor for new record, no values
	public BasicRecord(long checkpointId, String section, String title, int dataType, String units, boolean dual) {
		super();
		this.checkpointId = checkpointId;
		this.section = section;
		this.title = title;
		this.dataType = dataType;
		this.units = units;
		this.dual = dual;
		this.setValue = "";
		this.actValue = "";
	}
	
	// ctor for new record, DUAL by Default
	public BasicRecord(long checkpointId, String section, String title, int dataType, String units) {
		this(checkpointId, section, title, dataType, units, true);
	}
	
	public BasicRecord(int dataType) {
		super();
		this.dataType = dataType;
	}

	public BasicRecord(Recordable rec) {
		super();
		this.section = rec.getSection();
		this.title = rec.getTitle();
		this.dataType = rec.getDataType();
		this.dual = rec.isDual();
		this.units = rec.getUnits();
		this.actValue = rec.getActValue();
		this.setValue = rec.getSetValue();
	}

	public String toString() {
		return "" + title + ": " + actValue + ", " + setValue;
	}

	public boolean isDual() {
		return dual;
	}


	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public long getRecordId() {
		return recordId;
	}
	
	public long getCheckpointId() {
		return checkpointId;
	}
	
	public void setCheckpointId(long cpid) {
		this.checkpointId = cpid;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public String getSection() {
		return section;
	}
	
	public String getTitle() {
		return title;
	}
	
	protected void setTitle(String title) {
		this.title = title;
	}
	
	public int getDataType() {
		return dataType;
	}
	
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
	public void setActValue(String actValue) {
		this.actValue = actValue;
	}

	public void setSetValue(String setValue) {
		this.setValue = setValue;
	}

	public String getActValue() {
		return actValue;
	}


	public String getSetValue() {
		return setValue;
	}
	

}
