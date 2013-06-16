package com.freefoam.productionreports;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CheckpointRow extends LinearLayout implements OnFocusChangeListener {
	private Recordable record;
	private RecordsDBAdapter dbAdapter;
	private boolean isEditable = true;
	private int dataType;
	
	private String setValue;
	private String actValue;
	
	private TextView titleTextView = null;
	private TextView unitsTextView = null;
	private RecordEditText setEditText;
	private RecordEditText actEditText;

	public CheckpointRow(Context context) {
		super(context);
	}
	
	public CheckpointRow(Context context, Recordable record, boolean isEditable) {
		super(context);
    
		this.dataType = record.getDataType();
		this.isEditable = isEditable;
		this.record = record;
		this.setValue = record.getSetValue();
		this.actValue = record.getActValue();
	
		View.inflate(context, R.layout.checkpoint_row, this);
        this.setOrientation(HORIZONTAL);

		titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		titleTextView.setText(record.getTitle());
		
		unitsTextView = (TextView) this.findViewById(R.id.unitTextView);
		unitsTextView.setText(record.getUnits());
		
		setEditText = (RecordEditText) this.findViewById(R.id.setTextEdit);
		actEditText = (RecordEditText) this.findViewById(R.id.actTextEdit);
		updateStatusColour();
		setEditText.setText(record.getSetValue());
		
		if(isEditable) {
			setEditText.setInputType(record.getDataType());
			setEditText.setOnFocusChangeListener(this);
		} else {
			setEditText.disable();
		}

		if (record.isDual()) {
			actEditText.setText(record.getActValue());
			actEditText.setIsActualValue(true);
			actEditText.setVisibility(View.VISIBLE);
			if(isEditable) {
				actEditText.setInputType(record.getDataType());
				actEditText.setOnFocusChangeListener(this);
			} else {
				actEditText.disable();
			}
		} else {
			actEditText.setVisibility(View.GONE);
		}

	}
	
	public Recordable getRecord() {
		return record;
	}
	
	public void setRecordsDbAdapter(RecordsDBAdapter rdba) {
		this.dbAdapter = rdba;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		RecordEditText _ret = (RecordEditText) v;
    	if (!hasFocus && dbAdapter != null) {
    		boolean res = false;
    		
    		String _tvText = _ret.getText().toString();
    		if(_ret.isActualValue()) {
    			if(!_tvText.equals(actValue)) {
    				record.setActValue(_tvText);
    				this.actValue = _tvText;
    				res = dbAdapter.updateRecord(record);
    			}
    		} else {
    			if(!_tvText.equals(setValue)) {
    				record.setSetValue(_tvText);
    				this.setValue = _tvText;
    				res = dbAdapter.updateRecord(record);
    			}
    		}
			if (res) {
				Toast.makeText(v.getContext(), "Record updated.", Toast.LENGTH_SHORT).show();
			}
			updateStatusColour();
    	} else {
    		// change background to blue when the edittext gets focus
    		_ret.setBackgroundColor(Color.parseColor("#45a8d2"));
    	}
	}
	
	private void updateStatusColour() {
		// <Blue ==Grey >Red		
		setEditText.setBackgroundColor(Color.parseColor("#eeeeee"));
		actEditText.setBackgroundColor(Color.parseColor("#eeeeee"));

		if (actValue.isEmpty() || setValue.isEmpty()) {
		} else {
			if (dataType == Recordable.INTEGER_INPUT) {
				if (Integer.parseInt(actValue) < Integer.parseInt(setValue)) {
					actEditText.setBackgroundColor(Color.parseColor("#99ccff"));
				} else if (Integer.parseInt(actValue) > Integer.parseInt(setValue)) {
					actEditText.setBackgroundColor(Color.parseColor("#ffcccc"));
				}
			}
			if (dataType == Recordable.FLOAT_INPUT) {
				if (Float.parseFloat(actValue) < Float.parseFloat(setValue)) {
					actEditText.setBackgroundColor(Color.parseColor("#99ccff"));
				} else if (Float.parseFloat(actValue) > Float.parseFloat(setValue)) {
					actEditText.setBackgroundColor(Color.parseColor("#ffcccc"));
				}
			}
		}
	}
	

}
