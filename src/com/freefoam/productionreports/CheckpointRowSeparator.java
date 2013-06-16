package com.freefoam.productionreports;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckpointRowSeparator extends LinearLayout {
	
	private TextView titleTextView = null;
	
	public	CheckpointRowSeparator(Context context, String title) {
		super(context);
		View.inflate(context, R.layout.checkpoint_row_separator, this);

		titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		titleTextView.setText(title);
	}

}
