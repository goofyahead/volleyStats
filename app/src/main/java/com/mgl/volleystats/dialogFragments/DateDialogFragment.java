package com.mgl.volleystats.dialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mgl.volleystats.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by goofyahead on 1/1/16.
 */
public class DateDialogFragment extends DialogFragment {

    @InjectView(R.id.date_picker)
    DatePicker date;
    // Use this instance of the interface to deliver action events
    DateSelected mListener;

    public DateDialogFragment (DateSelected mListener) {
        this.mListener = mListener;
    }

    public interface DateSelected {
        void onDateSelected(int day, int month, int year);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v =inflater.inflate(R.layout.dialog_date, null);

        ButterKnife.inject(this, v);
        builder.setView(v);

        builder.setMessage(R.string.set_date)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDateSelected(date.getDayOfMonth(), date.getMonth(), date.getYear());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}