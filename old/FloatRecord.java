package com.freefoam.productionreports;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.InputType;

public class FloatRecord extends BasicRecord {

	private Float setValue;
	private Float actValue;
	public static final int INPUT_TYPE = InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL;
	
	public FloatRecord(int recordId, String title, String units) {
		super(recordId, title, units,true);
		// TODO Auto-generated constructor stub
	}
	
	public FloatRecord(int recordId, String title, String units, Boolean dual) {
		super(recordId, title, units, dual);
		// TODO Auto-generated constructor stub
	}

	public FloatRecord(int recordId, String title, String units, Float set) {
		super(recordId, title, units, false);
		this.setValue=set;
	}

	public FloatRecord(int recordId, String title, String units, Float set, Float act) {
		super(recordId, title, units, true);
		this.setValue=set;
		this.actValue=act;
	}
	
	//accept the (Visitor) Recorder
	public void accept(Recorder recorder) {
		recorder.record(this);
	}
	
	public Float getSetValue() {
		return setValue;
	}

	public void setSetValue(Float setValue) {
		this.setValue = setValue;
	}

	public Float getActValue() {
		return actValue;
	}

	public void setActValue(Float actValue) {
		this.actValue = actValue;
	}



}
