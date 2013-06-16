package com.freefoam.productionreports;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckpointTabView extends LinearLayout {
	
	public CheckpointTabView(Context context) {
		super(context);
	}
	
	public CheckpointTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
    
	public void setText(String title, String timeStamp) {
		TextView _title = (TextView) this.findViewById(R.id.checkpointTitle);
		_title.setText(title);
		TextView _timeStamp = (TextView) this.findViewById(R.id.checkpointTimestamp);
		_timeStamp.setText(timeStamp);
	}

}
