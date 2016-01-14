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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mgl.volleystats.R;
import com.mgl.volleystats.adapters.TeamAdapter;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.Constants;
import com.mgl.volleystats.base.Credentials;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.dialogFragments.PasswordDialogFragment;
import com.mgl.volleystats.interfaces.OnPasswordGiven;
import com.mgl.volleystats.models.Team;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TeamView extends Fragment {

    private static final String TAG = TeamView.class.getName();
    private static final String TEAM_ID = "TEAM_ID";
    @Inject
    VolleyPrefs prefs;
    @Inject
    VolleyStatsApi api;

    @InjectView(R.id.team_home_image)
    ImageView teamPicture;
    @InjectView(R.id.team_home_name)
    TextView teamName;

    private LinkedList<Team> teams;
    private TeamAdapter teamAdapter;
    private Team mTeam;
    private TeamSelected mTeamSelected;
    private String mTeamId;

    public interface TeamSelected {
        void onTeamSelected(String id);
    }

    public TeamView() {
        // Required empty public constructor
    }

    public static TeamView newInstance(String teamId) {
        TeamView fragment = new TeamView();
        Bundle args = new Bundle();
        args.putString(TEAM_ID, teamId);
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

        mTeamId = getArguments().getString(TEAM_ID);

        api.getTeam(mTeamId, new Callback<Team>() {
            @Override
            public void success(Team team, Response response) {
                if (getActivity() != null) {
                    Log.d(TAG, "INFO TEAM: " + team.getName());
                    Picasso.with(getActivity()).load(Credentials.SERVER_IP + Credentials.API_IMAGES + "teams/" + team.getProfilePic()).into(teamPicture);
                    teamName.setText(team.getName());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.team_home_fragment, container, false);
        ButterKnife.inject(this, v);
        ((VolleyStatApplication) getActivity().getApplication()).inject(this);
        return v;
    }



}
