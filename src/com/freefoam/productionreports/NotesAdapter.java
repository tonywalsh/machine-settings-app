package com.freefoam.productionreports;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotesAdapter extends BaseAdapter {
	LayoutInflater inflater = null;
	private List<Note> notes;
	
	public NotesAdapter() {
		super();
	}
	
	public NotesAdapter(Context context, ArrayList<Note> notes) {
		inflater = LayoutInflater.from(context);
		this.notes = notes;
	}
	
	private class ViewHolder {
		TextView timestamp = null;
		TextView noteContent = null;
	}
	
	@Override
	public int getCount() {
		return notes.size();
	}

	@Override
	public Object getItem(int position) {
		return notes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void add(Note note)
	{
	    notes.add(note);
	    notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.note_item, null);
			holder.timestamp = (TextView) convertView.findViewById(R.id.timeStamp);
			holder.noteContent = (TextView) convertView.findViewById(R.id.noteContent);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.timestamp.setText(notes.get(position).getTimestamp());
		holder.noteContent.setText(notes.get(position).getContent());
		return convertView;
	}

}
