package com.mgl.volleystats.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mgl.volleystats.R;
import com.mgl.volleystats.adapters.TeamAdapter;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.dialogFragments.PasswordDialogFragment;
import com.mgl.volleystats.interfaces.OnPasswordGiven;
import com.mgl.volleystats.models.Team;

import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SelectTeam extends Fragment implements OnPasswordGiven {

    private static final String TAG = SelectTeam.class.getName();
    @Inject
    VolleyPrefs prefs;
    @Inject
    VolleyStatsApi api;

    @InjectView(R.id.teamList)
    ListView teamsList;

    private LinkedList<Team> teams;
    private TeamAdapter teamAdapter;
    private Team mTeam;
    private TeamSelected mTeamSelected;

    public interface TeamSelected {
        void onTeamSelected(String id);
    }

    public SelectTeam() {
        // Required empty public constructor
    }

    public static SelectTeam newInstance() {
        SelectTeam fragment = new SelectTeam();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        teamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "selected team id: " + teams.get(position).getId());
                mTeam = teams.get(position);

                DialogFragment newFragment = new PasswordDialogFragment();
                newFragment.show(getActivity().getFragmentManager(), "password");
            }
        });

        api.getTeams(new Callback<LinkedList<Team>>() {
            @Override
            public void success(LinkedList<Team> teams, Response response) {
                Log.d(TAG, "team " + teams.get(0).getName());
                SelectTeam.this.teams = teams;
                teamAdapter = new TeamAdapter(getActivity(), teams);
                teamsList.setAdapter(teamAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "error " + error.getMessage() + " : " + error.getResponse().toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_select_team, container, false);
        ButterKnife.inject(this, v);
        ((VolleyStatApplication) getActivity().getApplication()).inject(this);
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mTeamSelected = (TeamSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddToCart");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTeam = null;
    }

    @Override
    public void passwordGiven(String pass) {
        Log.d(TAG, "are equal? " + pass + " : " +  mTeam.getPassword());
        if ( pass.equalsIgnoreCase(mTeam.getPassword())){
            prefs.setTeamId(mTeam.getId());
            mTeamSelected.onTeamSelected(mTeam.getId());
        } else {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.wrong_pass)
                    .setTitle(R.string.team_not_selected);

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }

}
