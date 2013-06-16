package com.freefoam.productionreports;

import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

public class TableRowRecorder implements Recorder {
	private TableRow tableRow;
	private Activity parentActivity;
	
	// constructor
	public TableRowRecorder(Activity context) {
		super();
		parentActivity = context;
	}
	
	private void setupRowDefaults(String title, String units) {
		tableRow = (TableRow) parentActivity.getLayoutInflater().inflate(R.layout.table_row, null);
		TextView _title = (TextView) parentActivity.getLayoutInflater().inflate(R.layout.record_title_cell, tableRow, false);
		_title.setText(title);
        tableRow.addView(_title);
		TextView _units = (TextView) parentActivity.getLayoutInflater().inflate(R.layout.record_unit_cell, tableRow, false);
		_units.setText(units);
        tableRow.addView(_units);
	}
	
	private EditText editText(int inputType, Boolean dual) {
		EditText _et = (EditText) parentActivity.getLayoutInflater().inflate(R.layout.record_edit_cell, tableRow,false);
        _et.setInputType(inputType);
        
        if (!dual) {
        	TableRow.LayoutParams params = (TableRow.LayoutParams)_et.getLayoutParams();
			params.span = 2;
			_et.setLayoutParams(params);
        }
        
        tableRow.addView(_et);
        return _et;
	}

	@Override
	public void record(BasicRecord record) {
		setupRowDefaults(record.getTitle(), record.getUnits());
	}

	@Override
	public void record(IntegerRecord record) {
		setupRowDefaults(record.getTitle(), record.getUnits());

		EditText _setval = editText(record.INPUT_TYPE, record.getDual());
		_setval.setText((record.getSetValue() != null ? record.getSetValue().toString() : ""));

		if (record.getDual()) {
			EditText _actval = editText(record.INPUT_TYPE, true);
			_actval.setText((record.getActValue() != null ? record.getActValue().toString() : ""));
			_actval.setTextColor(record.statusColour());
		}
	}

	@Override
	public void record(StringRecord record) {
		setupRowDefaults(record.getTitle(), record.getUnits());
		
		EditText _setval = editText(InputType.TYPE_TEXT_VARIATION_NORMAL, record.getDual());
		_setval.setText(record.getTitle());
		
		if (record.getDual()) {
			EditText _actval = editText(InputType.TYPE_TEXT_VARIATION_NORMAL, true);
			_actval.setText(record.getTitle());
		}
	}

	@Override
	public void record(FloatRecord record) {
		setupRowDefaults(record.getTitle(), record.getUnits());

		EditText _setval = editText(record.INPUT_TYPE, record.getDual());
		_setval.setText((record.getSetValue() != null ? record.getSetValue().toString() : ""));
		_setval.setHint("0.0");
		
		if (record.getDual()) {
			EditText _actval = editText(record.INPUT_TYPE, true);
			_actval.setText((record.getActValue() != null ? record.getActValue().toString() : ""));
			_actval.setTextColor(record.statusColour());
			_actval.setHint("0.0");
		}
	}

	@Override
	public void record(BooleanRecord record) {
		setupRowDefaults(record.getTitle(), record.getUnits());
	}
	
	public TableRow getTableRow() {
		return tableRow;
	}

}
