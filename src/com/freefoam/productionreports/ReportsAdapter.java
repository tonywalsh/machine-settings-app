package com.freefoam.productionreports;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ReportsAdapter extends BaseAdapter {
	Context ctx;
	LayoutInflater inflater = null;
	private List<Report> reports;
	
	public ReportsAdapter() {
		super();
	}
	
	public ReportsAdapter(Context context, ArrayList<Report> reports) {
		this.ctx = context;
		inflater = LayoutInflater.from(context);
		this.reports = reports;
	}
	
	private class ViewHolder {
		TextView productName = null;
		GridView checkpointGrid = null;
		TextView productionLine = null;
		TextView operator = null;
		TextView reportTime = null;
	}
	
	@Override
	public int getCount() {
		return reports.size();
	}

	@Override
	public Object getItem(int position) {
		return reports.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void add(Report report)
	{
	    reports.add(0,report);
	    notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.report_item, null);
			holder.productName = (TextView) convertView.findViewById(R.id.productName);
			holder.checkpointGrid = (GridView) convertView.findViewById(R.id.checkpointGrid);
			holder.checkpointGrid.setFocusable(false);
			holder.productionLine = (TextView) convertView.findViewById(R.id.productionLine);
			holder.operator = (TextView) convertView.findViewById(R.id.operator);
			holder.reportTime = (TextView) convertView.findViewById(R.id.reportTime);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.productName.setText(reports.get(position).getProduct());
		holder.checkpointGrid.setAdapter(new ReportCheckpointsAdapter(ctx, reports.get(position).getCheckpointTimestamps(), this));
		holder.checkpointGrid.setFocusable(false);
		holder.productionLine.setText(reports.get(position).getLine());		
		holder.operator.setText(reports.get(position).getCreator());
		holder.reportTime.setText(reports.get(position).getCreationDate());

		return convertView;
	}

}
