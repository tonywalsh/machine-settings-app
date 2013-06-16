package com.freefoam.productionreports;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReportCheckpointsAdapter extends BaseAdapter {
	Context ctx;
	LayoutInflater inflater = null;
	private List<String> checkpoints;
	private ReportsAdapter parentAdapter;
	
	public ReportCheckpointsAdapter() {
		super();
	}
	
	public ReportCheckpointsAdapter(Context context, ArrayList<String> checkpoints, ReportsAdapter parentAdapter) {
		this.ctx = context;
		inflater = LayoutInflater.from(context);
		this.checkpoints = checkpoints;
		this.parentAdapter = parentAdapter;
	}
	
	private class ViewHolder {
		//TextView checkpointTitle = null;
		TextView timestamp = null;
	}
	
	@Override
	public int getCount() {
		if (checkpoints==null)
			return 0;
		else
			return checkpoints.size();
	}

	@Override
	public Object getItem(int position) {
		return checkpoints.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void add(String checkpoint)
	{
	    checkpoints.add(checkpoint);
	    notifyDataSetChanged();
	    if (parentAdapter != null) {
	    	parentAdapter.notifyDataSetChanged();
	    }
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.report_checkpoint_item, null);
			//holder.checkpointTitle = (TextView) convertView.findViewById(R.id.checkpointTitle);
			holder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		//holder.checkpointTitle.setText(checkpoints.get(position).getTitle());
		holder.timestamp.setText(checkpoints.get(position));

		return convertView;
	}

}
