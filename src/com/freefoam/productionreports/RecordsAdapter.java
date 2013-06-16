package com.freefoam.productionreports;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

public class RecordsAdapter extends BaseAdapter {
	LayoutInflater inflater = null;
	private ArrayList<Recordable> records;
	private RecordsDBAdapter dbAdapter;
	private boolean isEditable = true;
    private Context mContext;
    
	public RecordsAdapter() {
		super();
	}
	
	public RecordsAdapter(Context context, ArrayList<Recordable> records) {
		mContext = context;
		this.records = records;
	}
	
	@Override
	public int getCount() {
		return records.size();
	}

	@Override
	public Object getItem(int position) {
		return records.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void add(Recordable record)
	{
		records.add(record);
	    notifyDataSetChanged();
	}
	
	public void setEditable(boolean editable) {
		this.isEditable = editable;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckpointRow row;
		
		if(convertView == null) {			
			row = new CheckpointRow(mContext, records.get(position), isEditable);
		} else {
			row = (CheckpointRow) convertView;
		}
		row.setRecordsDbAdapter(dbAdapter);
		//row.refreshView(records.get(position));

		return row;
	}
	
	public void setRecordsDbAdapter(RecordsDBAdapter rdba) {
		this.dbAdapter = rdba;
	}

}
