package com.freefoam.productionreports;

import android.text.InputType;

public interface Recordable {
	
	public static final int INTEGER_INPUT = InputType.TYPE_CLASS_NUMBER;
	public static final int FLOAT_INPUT = InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL;
	public static final int STRING_INPUT = InputType.TYPE_CLASS_TEXT;

	public long getRecordId();
	public String getSection();
	public String getTitle();
	public String getUnits();
	public void setSetValue(String setValue);
	public void setActValue(String actValue);
	public String getSetValue();
	public String getActValue();
	public int getDataType();
	public boolean isDual();
}
