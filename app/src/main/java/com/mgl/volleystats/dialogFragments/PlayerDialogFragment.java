package com.mgl.volleystats.dialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mgl.volleystats.R;
import com.mgl.volleystats.models.Player;

import butterknife.ButterKnife;

/**
 * Created by goofyahead on 10/23/15.
 */
public class PlayerDialogFragment extends DialogFragment{

    private static final String EXTRA_PLAYER = "EXTRA_PLAYER";

    public static PlayerDialogFragment newInstance(Player player) {
        PlayerDialogFragment f = new PlayerDialogFragment();
        Bundle newBundle = new Bundle();
        newBundle.putSerializable(EXTRA_PLAYER, player);
        f.setArguments(newBundle);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.player_fragment_dialog, null);

        ButterKnife.inject(this, v);
        builder.setView(v);
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        try {
//            mCallback = (SelectQuantityInterface) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement SelectQuantityInterface");
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
