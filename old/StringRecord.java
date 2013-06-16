package com.freefoam.productionreports;

import android.text.InputType;

public class StringRecord extends BasicRecord {
	private String setValue;
	private String actValue;
	public static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT;

	public StringRecord(int recordId, String title, String units) {
		super(recordId, title, units, true);
		// TODO Auto-generated constructor stub
	}

	public StringRecord(int recordId, String title, String units,String set) {
		super(recordId, title, units, false);
		this.setValue=set;
	}

	public StringRecord(int recordId, String title, String units,String set,String act) {
		super(recordId, title, units, true);
		this.setValue=set;
		this.actValue=act;
	}
	
	//accept the (Visitor) Recorder
	public void accept(Recorder recorder) {
		recorder.record(this);
	}
	
	public String getSetValue() {
		return setValue;
	}

	public void setSetValue(String setValue) {
		this.setValue = setValue;
	}

	public String getActValue() {
		return actValue;
	}

	public void setActValue(String actValue) {
		this.actValue = actValue;
	}

}
