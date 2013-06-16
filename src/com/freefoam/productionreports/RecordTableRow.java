package com.freefoam.productionreports;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableRow;

public class RecordTableRow extends TableRow {
	Recordable record;
	
	public RecordTableRow(Context context) {
		super(context);
	}
	
	public RecordTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Recordable getRecord() {
		return record;
	}

	public void setRecord(Recordable record) {
		this.record = record;
	}

	
}
