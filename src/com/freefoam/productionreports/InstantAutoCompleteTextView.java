package com.freefoam.productionreports;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;


public class InstantAutoCompleteTextView extends AutoCompleteTextView {

	public InstantAutoCompleteTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InstantAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InstantAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        // hide the keyboard
        // TODO Allow user to display keyboard with long press?
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
        	      Context.INPUT_METHOD_SERVICE);
        	imm.hideSoftInputFromWindow(getWindowToken(), 0);
        if (focused) {
            super.performFiltering("", 67);
            showDropDown();
        }
    }
}

