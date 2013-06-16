package com.freefoam.productionreports;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class NewReportDialog extends DialogFragment {
	AutoCompleteTextView _product_tv;
	InstantAutoCompleteTextView _lines_tv;
	InstantAutoCompleteTextView _colours_tv;
	InstantAutoCompleteTextView _operator_tv;
	boolean colourSet = false;
	boolean operatorSet = false;
	boolean productSet = false;
	boolean lineSet = false;
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NewReportDialogListener {
        public void onDialogPositiveClick(NewReportDialog dialog);
        public void onDialogNegativeClick(NewReportDialog dialog);
    }

    // Use this instance of the interface to deliver action events
    NewReportDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NewReportDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.start_report_dialog, null);

        // Products Drop down
        // Needs to be AutoComplete because there are so many of them
        ArrayAdapter<CharSequence> _products_adapter = ArrayAdapter.createFromResource(
          	dialogView.getContext(), R.array.products, R.layout.new_report_autocomplete_item );
       	_products_adapter.setDropDownViewResource( R.layout.new_report_autocomplete_item );
       	_product_tv = (AutoCompleteTextView) dialogView.findViewById(R.id.selectProduct);
        _product_tv.setAdapter( _products_adapter );
        _product_tv.addTextChangedListener(new TextWatcher() {
     	    public void afterTextChanged(Editable s) {
     	    	productSet = true;
     	    	checkPositiveButton();
     	    }
     	    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
     	    public void onTextChanged(CharSequence s, int start, int before, int count){}
     	});

        // Lines Drop down
        // List of Extruders
        ArrayAdapter<CharSequence> _lines_adapter = ArrayAdapter.createFromResource(
      		  dialogView.getContext(), R.array.production_lines, R.layout.new_report_autocomplete_item );
        _lines_adapter.setDropDownViewResource( R.layout.new_report_autocomplete_item );
       	_lines_tv = (InstantAutoCompleteTextView) dialogView.findViewById(R.id.selectLine);
        _lines_tv.setAdapter( _lines_adapter );
        _lines_tv.setInputType(InputType.TYPE_NULL);
        _lines_tv.addTextChangedListener(new TextWatcher() {
     	    public void afterTextChanged(Editable s) {
     	    	lineSet = true;
     	    	checkPositiveButton();
     	    }
     	    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
     	    public void onTextChanged(CharSequence s, int start, int before, int count){}
     	});
     	
        // Operators Drop down
        // List of Operators
        // TODO put in DB and use code to finish product code
        ArrayAdapter<CharSequence> _operators_adapter = ArrayAdapter.createFromResource(
        		  dialogView.getContext(), R.array.operators, R.layout.new_report_autocomplete_item );
        _operators_adapter.setDropDownViewResource( R.layout.new_report_autocomplete_item );
       	_operator_tv = (InstantAutoCompleteTextView) dialogView.findViewById(R.id.selectOperator);
       	_operator_tv.setAdapter( _operators_adapter );
       	_operator_tv.setInputType(InputType.TYPE_NULL);
       	_operator_tv.addTextChangedListener(new TextWatcher() {
     	    public void afterTextChanged(Editable s) {
     	    	operatorSet = true;
     	    	checkPositiveButton();
     	    }
     	    public void beforeTextChanged(CharSequence s, int start, int count, int after){}
     	    public void onTextChanged(CharSequence s, int start, int before, int count){}
     	});
     	
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setView(dialogView)
			.setTitle("New Extrusion Report")
			.setPositiveButton(R.string.btn_create_report, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogPositiveClick(NewReportDialog.this);
			}
		})
		.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Send the negative button event back to the host activity
                mListener.onDialogNegativeClick(NewReportDialog.this);
				NewReportDialog.this.getDialog().cancel();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}
        
	public void onResume() {
		super.onResume();
		checkPositiveButton();
	}
	
	private void checkPositiveButton() {
		AlertDialog ad = (AlertDialog)this.getDialog();
		Button b = (Button)ad.getButton(AlertDialog.BUTTON_POSITIVE);
		b.setEnabled(false);
		if (operatorSet && productSet && lineSet) {
			b.setEnabled(true);
		}
	}
	
	public String getLine() {
		return _lines_tv.getText().toString();
	}

	public String getProduct() {
		return _product_tv.getText().toString();
	}

	public String getOperator() {
		return _operator_tv.getText().toString();
	}
	
	public String getColour() {
		return _colours_tv.getText().toString();
	}

}
