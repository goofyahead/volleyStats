package com.mgl.volleystats.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mgl.volleystats.R;
import com.mgl.volleystats.adapters.PlayersAdapter;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.dialogFragments.DateDialogFragment;
import com.mgl.volleystats.dialogFragments.TimePickerDialog;
import com.mgl.volleystats.models.Match;
import com.mgl.volleystats.models.Player;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewMatch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewMatch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewMatch extends Fragment implements PlayersAdapter.ItemLongClicked, DateDialogFragment.DateSelected, TimePickerDialog.TimeSelected {

    private static final String TAG = NewMatch.class.getName();
    private static final String PLAYER_PAYLOAD = "PLAYER_PAYLOAD";
    @Inject
    VolleyStatsApi api;
    @Inject
    VolleyPrefs prefs;

    @InjectView(R.id.new_match_button_finish)
    Button matchFinished;
    @InjectView(R.id.horizontalScrollView)
    RecyclerView horizontalList;
    @InjectView(R.id.pick_date_text)
    TextView pickDate;
    @InjectView(R.id.pick_time_text)
    TextView pickTime;
    @InjectView(R.id.selected_players_scrollView)
    RecyclerView selectedPlayers;
    @InjectView(R.id.opposing_team)
    EditText oposingTeam;
    @InjectView(R.id.place_venue)
    EditText venue;

    private LinearLayoutManager mLayoutManager;
    private PlayersAdapter mAdapter;
    private LinearLayoutManager mSelectedLayoutManager;
    private PlayersAdapter mSelectedAdapter;
    private int selectedMonth;
    private int selectedYear;
    private int selectedDay;
    private int selectedHour;
    private int selectedMinute;

    public NewMatch() {
        // Required empty public constructor
    }

    public static NewMatch newInstance() {
        NewMatch fragment = new NewMatch();
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

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        horizontalList.setLayoutManager(mLayoutManager);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mSelectedLayoutManager = new LinearLayoutManager(getActivity());
        selectedPlayers.setLayoutManager(mSelectedLayoutManager);
        mSelectedLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mSelectedAdapter = new PlayersAdapter(new LinkedList<Player>(), getActivity(), NewMatch.this);
        selectedPlayers.setAdapter(mSelectedAdapter);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment dialog = new DateDialogFragment(NewMatch.this);
                dialog.show(getFragmentManager(), dialog.getTag());
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(NewMatch.this);
                dialog.show(getFragmentManager(), dialog.getTag());
            }
        });

        api.getPlayersFromTeam(prefs.getTeamId(), new Callback<LinkedList<Player>>() {
            @Override
            public void success(LinkedList<Player> players, Response response) {
                Log.d(TAG, "# players available: " + players.size());
                // specify an adapter (see also next example)
                mAdapter = new PlayersAdapter(players, getActivity(), NewMatch.this);
                horizontalList.setAdapter(mAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "something failed " + error.getMessage());
            }
        });

        horizontalList.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        int colorFrom = getResources().getColor(R.color.background_holo_light);
                        int colorTo = getResources().getColor(R.color.info_blue);
                        int duration = 500;
                        ObjectAnimator.ofObject(horizontalList, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo, colorFrom)
                                .setDuration(duration)
                                .start();
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        Player player = (Player) event.getClipData().getItemAt(0).getIntent().getSerializableExtra(PLAYER_PAYLOAD);
                        Log.d(TAG, "player back to its list, handle player and add it somehow and notifydataset " + player.getName());
                        mAdapter.addPLayer(player);
                        // Dropped, reassign View to ViewGroup
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        });

        selectedPlayers.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        int colorFrom = getResources().getColor(R.color.background_holo_light);
                        int colorTo = getResources().getColor(R.color.info_blue);
                        int duration = 500;
                        ObjectAnimator.ofObject(selectedPlayers, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo, colorFrom)
                                .setDuration(duration)
                                .start();
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        Player player = (Player) event.getClipData().getItemAt(0).getIntent().getSerializableExtra(PLAYER_PAYLOAD);
                        Log.d(TAG, "player back to its list, handle player and add it somehow and notifydataset " + player.getName());
                        mSelectedAdapter.addPLayer(player);
                        // Dropped, reassign View to ViewGroup
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        });

        matchFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateAndTime = new DateTime(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                Log.d(TAG, "DATE IS: " + dateAndTime.toString(fmt) + " " + dateAndTime.getMillis());
                DateTime fromMilis = new DateTime(dateAndTime.getMillis());
                Log.d(TAG, "from milis " + fromMilis.toString(fmt));

                Match current = new Match(venue.getText().toString(), dateAndTime.getMillis(),  mSelectedAdapter.getPlayers(), oposingTeam.getText().toString(), prefs.getTeamId(), "ONGOING");
                api.postMatch(current, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.d(TAG, s);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_match, container, false);
        ButterKnife.inject(this, v);
        ((VolleyStatApplication) getActivity().getApplication()).inject(this);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void itemLongClicked(View v, Player player) {
        Log.d(TAG, "player long clicked " + player.getName());
        ClipData data = ClipData.newIntent(TAG, new Intent().putExtra(PLAYER_PAYLOAD, player));
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(data, shadowBuilder, v, 0);
    }

    @Override
    public void onDateSelected(int day, int month, int year) {
        Log.d(TAG, "date selected");
        pickDate.setText(String.format("%02d/%02d/%04d",day, month + 1, year));
        this.selectedDay = day;
        this.selectedMonth = month + 1;
        this.selectedYear = year;
    }

    @Override
    public void onTimeSelected(int hour, int minutes) {
        Log.d(TAG, "time selected");
        pickTime.setText(String.format("%02d:%02d", hour, minutes));
        this.selectedHour = hour;
        this.selectedMinute = minutes;
    }

    public interface OnFragmentInteractionListener {
        void onMatchCreated(Match match);
    }

}
