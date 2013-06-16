package com.freefoam.productionreports;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class NewNoteDialog extends DialogFragment {
	EditText _note_content;
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NewNoteDialogListener {
        public void onDialogPositiveClick(NewNoteDialog dialog);
        public void onDialogNegativeClick(NewNoteDialog dialog);
    }

    // Use this instance of the interface to deliver action events
    NewNoteDialogListener mListener;
    
    public void setListener(NewNoteDialogListener listener) {
        mListener = listener;
    }
    

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.note_dialog, null);
        _note_content = (EditText) dialogView.findViewById(R.id.noteContent);   	
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setView(dialogView)
			.setTitle("New Note")
			.setPositiveButton(R.string.btn_create_note, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mListener.onDialogPositiveClick(NewNoteDialog.this);
			}
		})
		.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Send the negative button event back to the host activity
                mListener.onDialogNegativeClick(NewNoteDialog.this);
				NewNoteDialog.this.getDialog().cancel();
			}
		});
		// Create the AlertDialog object and return it
		return builder.create();
	}
	
	public String getContent() {
		return _note_content.getText().toString();
	}
	
}
