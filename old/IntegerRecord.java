package com.freefoam.productionreports;

import android.text.InputType;

public class IntegerRecord extends BasicRecord {
	private Integer setValue;
	private Integer actValue;
	public static final int INPUT_TYPE = InputType.TYPE_CLASS_NUMBER;

	public IntegerRecord(int recordId, String title, String units) {
		super(recordId, title,units, true);
	}
	
	public IntegerRecord(int recordId, String title, String units, Boolean dual) {
		super(recordId, title,units, dual);
	}

	public IntegerRecord(int recordId, String title, String units, int set) {
		super(recordId, title,units, false);
		setValue = set;
	}

	public IntegerRecord(int recordId, String title, String units, int set, int act) {
		super(recordId, title,units,true);
		setValue = set;
		actValue = act;
	}

	//accept the (Visitor) Recorder
	public void accept(Recorder recorder) {
		recorder.record(this);
	}
	
	public Integer getSetValue() {
		return setValue;
	}

	public void setSetValue(Integer setValue) {
		this.setValue = setValue;
	}

	public Integer getActValue() {
		return actValue;
	}

	public void setActValue(Integer actValue) {
		this.actValue = actValue;
	}

}
