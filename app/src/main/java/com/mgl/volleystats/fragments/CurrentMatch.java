package com.mgl.volleystats.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgl.volleystats.R;
import com.mgl.volleystats.adapters.PlayersAdapter;
import com.mgl.volleystats.adapters.TeamAdapter;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.Credentials;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.dialogFragments.DateDialogFragment;
import com.mgl.volleystats.dialogFragments.PlayerDialogFragment;
import com.mgl.volleystats.models.Match;
import com.mgl.volleystats.models.Player;
import com.mgl.volleystats.models.Position;
import com.mgl.volleystats.models.Positions;
import com.mgl.volleystats.models.Team;
import com.mgl.volleystats.picasso.CircleTransformation;
import com.mikepenz.iconics.utils.Utils;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CurrentMatch extends Fragment implements PlayersAdapter.ItemLongClicked {

    private static final String TAG = CurrentMatch.class.getName();
    private static final String TEAM_ID = "TEAM_ID";
    private static final String PLAYER_MOVED = "PLAYER_MOVED";
    private static final String PLAYER_PAYLOAD = "PLAYER_PAYLOAD";
    private static Match match;
    @Inject
    VolleyPrefs prefs;
    @Inject
    VolleyStatsApi api;

    @InjectView(R.id.setter)
    ImageView position2;
    @InjectView(R.id.position1)
    ImageView position1;
    @InjectView(R.id.position3)
    ImageView position3;
    @InjectView(R.id.position4)
    ImageView position4;
    @InjectView(R.id.position5)
    ImageView position5;
    @InjectView(R.id.position6)
    ImageView position6;

    @InjectView(R.id.rotate)
    IconicsImageView rotate;
    @InjectView(R.id.atq)
    IconicsImageView atq;
    @InjectView(R.id.def)
    IconicsImageView def;
    @InjectView(R.id.base)
    IconicsImageView base;

    @InjectView(R.id.we_serve)
    IconicsImageView weServe;
    @InjectView(R.id.they_serve)
    IconicsImageView theyServe;

    @InjectView(R.id.local_score)
    TextView localScore;
    @InjectView(R.id.visitor_score)
    TextView visitorScore;

    @InjectView(R.id.match_timer)
    TextView matchTimer;
    @InjectView(R.id.match_set_number)
    TextView matchSetNumber;

    @InjectView(R.id.localPoint)
    Button localPoint;
    @InjectView(R.id.visitor_point)
    Button visitorPoint;

    @InjectView(R.id.players)
    RecyclerView selectedPlayers;

    private String mTeamId;
    private int whereIsOne = 1;
    private ImageView [] views = new ImageView[6];
    private Player [] players = new Player[6];

    private HashMap<Integer, LinkedList<Position>> atqPostions;
    private HashMap<Integer, LinkedList<Position>> defPositions;
    private HashMap<Integer, LinkedList<Position>> mappingBase = new HashMap<>();
    private int recording = 0;
    private LinearLayoutManager mSelectedLayoutManager;
    private PlayersAdapter mSelectedAdapter;

    private int set = 1;
    private int scoreLocal = 0;
    private int scoreVisitors = 0;
    private boolean localIsServing = false;
    private boolean visitorIsServing = false;

    private View.OnDragListener listener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    Player player = (Player) event.getClipData().getItemAt(0).getIntent().getSerializableExtra(PLAYER_PAYLOAD);
                    Log.d(TAG, "player back to its list, handle player and add it somehow and notifydataset " + player.getName());
                    Picasso.with(getActivity()).load(Credentials.SERVER_IP + Credentials.API_IMAGES + "players/" + player.getPicture()).transform(new CircleTransformation()).into((ImageView) v);
                    int num = (Integer)v.getTag();
                    if (players[num] == null) {
                        players[num] = player;
                    } else {
                        //replace
                        mSelectedAdapter.addPLayer(players[num]);
                        players[num] = player;
                    }
                    // Dropped, reassign View t ViewGroup
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    };

    public CurrentMatch() {
        // Required empty public constructor
    }

    public static CurrentMatch newInstance(String teamId) {
        CurrentMatch fragment = new CurrentMatch();
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

        mSelectedLayoutManager = new LinearLayoutManager(getActivity());
        selectedPlayers.setLayoutManager(mSelectedLayoutManager);
        mSelectedLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        localPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (set != 3) {
                    if (scoreLocal < 24) { // 1 or 2 set less than 25
                        scoreLocal++;
                        localScore.setText("" + scoreLocal);
                    } else if (Math.abs(scoreLocal - scoreVisitors) < 1) { // local has 25 but less than 2 diff
                        scoreLocal++;
                        localScore.setText("" +scoreLocal);
                    } else { // end of set
                        set++;
                        scoreLocal = 0;
                        scoreVisitors = 0;
                        localScore.setText("" + scoreLocal);
                        visitorScore.setText("" + scoreVisitors);
                        matchSetNumber.setText(String.format("Set %d",set));
                    }
                } else {
                    if (scoreLocal < 14) { // third set to 15
                        scoreLocal++;
                        localScore.setText("" +scoreLocal);
                    } else if (Math.abs(scoreLocal - scoreVisitors) < 1) { // local has 25 but less than 2 diff
                        scoreLocal++;
                        localScore.setText("" +scoreLocal);
                    } else { // end of set
                        set++;
                        scoreLocal = 0;
                        scoreVisitors = 0;
                        localScore.setText("" +scoreLocal);
                        visitorScore.setText("" + scoreVisitors);
                        matchSetNumber.setText(String.format("Set %d",set));
                        // END OF GAME
                    }
                }
            }
        });

        visitorPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (set != 3) {
                    if (scoreVisitors < 24) { // 1 or 2 set less than 25
                        scoreVisitors++;
                        visitorScore.setText("" +scoreVisitors);
                    } else if (Math.abs(scoreLocal - scoreVisitors) < 1){ // local has 25 but less than 2 diff
                        scoreVisitors++;
                        visitorScore.setText("" +scoreVisitors);
                    } else { // end of set
                        set ++;
                        scoreLocal = 0;
                        scoreVisitors = 0;
                        localScore.setText("" +scoreLocal);
                        visitorScore.setText("" +scoreVisitors);
                        matchSetNumber.setText(String.format("Set %d",set));
                    }
                } else {
                    if (scoreVisitors < 14) { // third set to 15
                        scoreVisitors++;
                        visitorScore.setText("" +scoreVisitors);
                    } else if (Math.abs(scoreLocal - scoreVisitors) < 1){ // local has 25 but less than 2 diff
                        scoreVisitors++;
                        visitorScore.setText("" +scoreVisitors);
                    } else { // end of set
                        set ++;
                        scoreLocal = 0;
                        scoreVisitors = 0;
                        localScore.setText("" +scoreLocal);
                        visitorScore.setText("" +scoreVisitors);
                        matchSetNumber.setText(String.format("Set %d",set));
                        // END OF GAME
                    }
                }
            }
        });

        api.getCurrentMatch(mTeamId, new Callback<Match>() {
            @Override
            public void success(Match match, Response response) {
                CurrentMatch.match = match;

                mSelectedAdapter = new PlayersAdapter(match.getPlayers(), getActivity(), CurrentMatch.this);
                selectedPlayers.setAdapter(mSelectedAdapter);

                Log.d(TAG, "match " + match.getId() + " players: " + match.getPlayers().get(0).getName());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error" + error.getMessage());
            }
        });

        api.getDefPositions(mTeamId, new Callback<Positions>() {
            @Override
            public void success(Positions positions, Response response) {
                atqPostions = positions.getStrategies();

                api.getDefPositions(mTeamId, new Callback<Positions>() {
                    @Override
                    public void success(Positions positions, Response response) {
                        defPositions = positions.getStrategies();
                        paintbase();


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Error gettting " + error.getMessage());
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error gettting " + error.getMessage());
            }
        });

        views[0] = position1;
        position1.setTag(0);
        views[1] = position2;
        position2.setTag(1);
        views[2] = position3;
        position3.setTag(2);
        views[3] = position4;
        position4.setTag(3);
        views[4] = position5;
        position5.setTag(4);
        views[5] = position6;
        position6.setTag(5);

        for (int y = 0; y < 6; y++){
            views[y].setOnDragListener(listener);

            final int finalY = y;
            views[y].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayerDialogFragment dialog = PlayerDialogFragment.newInstance(players[finalY]);
                    dialog.show(getFragmentManager(), dialog.getTag());
                }
            });
        }

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenwidth = size.x;

        int width = screenwidth / 4;
        int height = Utils.convertDpToPx(getActivity(), 320) / 3;

        initializeMappingBase(width, height);

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whereIsOne == 6) {
                    whereIsOne = 1;
                } else {
                    whereIsOne++;
                }

                paintbase();
            }
        });

        atq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = 2;
                paintbase();
            }
        });

        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = 0;
                paintbase();
            }
        });

        def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = 1;
                paintbase();
            }
        });
    }

    private void paintbase() {
        switch (recording) {
            case 0:
                for (final Position player : mappingBase.get(whereIsOne)) {
                    views[player.getPlayer()].setX(player.getX() - Utils.convertDpToPx(getActivity(), 40));
                    views[player.getPlayer()].setY(player.getY() - Utils.convertDpToPx(getActivity(), 40));
                    Log.d(TAG, "player " + player.getPlayer() + " in PosX: " + player.getX() + " in PosY: " + player.getY());
                }
                break;
            case 1:
                for (final Position player : defPositions.get(whereIsOne)) {
                    views[player.getPlayer()].setX(player.getX() - Utils.convertDpToPx(getActivity(), 40));
                    views[player.getPlayer()].setY(player.getY() - Utils.convertDpToPx(getActivity(), 40));
                    Log.d(TAG, "player " + player.getPlayer() + " in PosX: " + player.getX() + " in PosY: " + player.getY());
                }
                break;
            case 2:
                for (final Position player : atqPostions.get(whereIsOne)) {
                    views[player.getPlayer()].setX(player.getX() - Utils.convertDpToPx(getActivity(), 40));
                    views[player.getPlayer()].setY(player.getY() - Utils.convertDpToPx(getActivity(), 40));
                    Log.d(TAG, "player " + player.getPlayer() + " in PosX: " + player.getX() + " in PosY: " + player.getY());
                }
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.current_match_fragment, container, false);
        ButterKnife.inject(this, v);
        ((VolleyStatApplication) getActivity().getApplication()).inject(this);
        return v;
    }


    private void initializeMappingBase(int divisionWidth, int divisionHeight) {

        int[] xvalues = {3 * divisionWidth, 3 * divisionWidth, 2 * divisionWidth, divisionWidth, divisionWidth, 2 * divisionWidth};
        int[] yvalues = {2 * divisionHeight, divisionHeight, divisionHeight, divisionHeight, 2 * divisionHeight, 2 * divisionHeight};

        for (int x = 1; x <= 6; x++) {
            //iterate over rotations and shift arrays;
            LinkedList<Position> current = new LinkedList<>();
            for (int p = 0; p < 6; p++) {
                current.add(new Position(xvalues[p], yvalues[p], p));
            }

            xvalues = shift(xvalues);
            yvalues = shift(yvalues);
            mappingBase.put(x, current);
        }
    }

    private int[] shift(int[] xvalues) {
        int saved = xvalues[5];

        for (int x = 5; x > 0 ; x --) {
            xvalues[x] = xvalues[x-1];
        }

        xvalues[0] = saved;
        return xvalues;
    }

    @Override
    public void itemLongClicked(View v, Player player) {
        Log.d(TAG, "player long clicked " + player.getName());
        ClipData data = ClipData.newIntent(TAG, new Intent().putExtra(PLAYER_PAYLOAD, player));
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(data, shadowBuilder, v, 0);
    }
}
