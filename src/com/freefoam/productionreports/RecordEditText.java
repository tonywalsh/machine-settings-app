package com.freefoam.productionreports;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class RecordEditText extends EditText {

	private Boolean isActualValue;
	
	public RecordEditText(Context context) {
		super(context);
		this.isActualValue = false;
	}

	public RecordEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.isActualValue = false;
	}

	public RecordEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.isActualValue = false;
	}

	public Boolean isActualValue() {
		return isActualValue;
	}

	public void setIsActualValue(Boolean isActualValue) {
		this.isActualValue = isActualValue;
	}

	public void disable() {
		this.setEnabled(false);
		this.setFocusable(false);
		this.setBackgroundColor(0xFF888888);
	}

}
