package com.freefoam.productionreports;

import android.text.InputType;

public class BooleanRecord extends BasicRecord {

	private Boolean setValue;
	private Boolean actValue;
	public static final int INPUT_TYPE = InputType.TYPE_CLASS_TEXT;

	
	public BooleanRecord(int recordId, String title, String units) {
		super(recordId, title, units);
		// TODO Auto-generated constructor stub
	}

	public BooleanRecord(int recordId, String title, String units, Boolean set, Boolean act) {
		super(recordId, title, units);
		setValue = set;
		actValue = act;
	}
	
	//accept the (Visitor) Recorder
	public void accept(Recorder recorder) {
		recorder.record(this);
	}
	
	public Boolean getSetValue() {
		return setValue;
	}

	public void setSetValue(Boolean setValue) {
		this.setValue = setValue;
	}

	public Boolean getActValue() {
		return actValue;
	}

	public void setActValue(Boolean actValue) {
		this.actValue = actValue;
	}

}
