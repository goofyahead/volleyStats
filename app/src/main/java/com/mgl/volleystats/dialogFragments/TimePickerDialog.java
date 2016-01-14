package com.mgl.volleystats.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.mgl.volleystats.R;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by goofyahead on 1/1/16.
 */
public class TimePickerDialog extends DialogFragment {

    @InjectView(R.id.timePicker)
    TimePicker time;
    // Use this instance of the interface to deliver action events
    TimeSelected mListener;

    public TimePickerDialog(TimeSelected mListener) {
        this.mListener = mListener;
    }

    public interface TimeSelected {
        void onTimeSelected(int hour, int minutes);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v =inflater.inflate(R.layout.dialog_time, null);

        ButterKnife.inject(this, v);
        builder.setView(v);

        time.setIs24HourView(true);
        time.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        builder.setMessage(R.string.set_date)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onTimeSelected(time.getCurrentHour(), time.getCurrentMinute());
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