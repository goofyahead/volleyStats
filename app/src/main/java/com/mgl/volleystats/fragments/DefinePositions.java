package com.mgl.volleystats.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgl.volleystats.R;
import com.mgl.volleystats.adapters.TeamAdapter;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.models.Player;
import com.mgl.volleystats.models.Position;
import com.mgl.volleystats.models.Positions;
import com.mgl.volleystats.models.Team;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.LinkedList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DefinePositions extends Fragment {

    private static final String TAG = DefinePositions.class.getName();
    private static final String TEAM_ID = "TEAM_ID";
    private static final String PLAYER_MOVED = "PLAYER_MOVED";

    @Inject
    VolleyPrefs prefs;
    @Inject
    VolleyStatsApi api;

    private int whereIsOne = 1;
    private TextView [] views = new TextView[6];

    private HashMap<Integer, LinkedList<Position>> mappingBase = new HashMap<>();
    private HashMap<Integer, LinkedList<Position>> mappingAtq = new HashMap<>();
    private HashMap<Integer, LinkedList<Position>> mappingRcv = new HashMap<>();

    @InjectView(R.id.positions_court)
    RelativeLayout court;
    @InjectView(R.id.rotation)
    Button rotation;
    @InjectView(R.id.save_rotation)
    Button saveRotation;

    @InjectView(R.id.base)
    Button base;
    @InjectView(R.id.atq)
    Button atq;
    @InjectView(R.id.def)
    Button def;

    @InjectView(R.id.setter)
    TextView position2;
    @InjectView(R.id.position1)
    TextView position1;
    @InjectView(R.id.position3)
    TextView position3;
    @InjectView(R.id.position4)
    TextView position4;
    @InjectView(R.id.position5)
    TextView position5;
    @InjectView(R.id.position6)
    TextView position6;

    private TextView pos1;
    private int recording;

    public interface TeamSelected {
        void onTeamSelected(String id);
    }

    public DefinePositions() {
        // Required empty public constructor
    }

    public static DefinePositions newInstance(String teamId) {
        DefinePositions fragment = new DefinePositions();
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

        Resources r = getResources();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenwidth = size.x;
        int screenheight = size.y;

        int width = screenwidth / 4;
        int height = Utils.convertDpToPx(getActivity(), 300) / 3;

        initializeMappingBase(width, height);

        Log.d(TAG, "Size of window is: " + width + " : " + height);

        court.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        Log.d(TAG, "Drag started");
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.d(TAG, "Left: " + event.getX() + " Top: " + event.getY() + " text " + event.getClipData().getItemAt(0).getIntent().getIntExtra(PLAYER_MOVED, -1));
                        views[event.getClipData().getItemAt(0).getIntent().getIntExtra(PLAYER_MOVED, -1)].setX(event.getX() - Utils.convertDpToPx(getActivity(), 40));
                        views[event.getClipData().getItemAt(0).getIntent().getIntExtra(PLAYER_MOVED, -1)].setY(event.getY() - Utils.convertDpToPx(getActivity(), 40));
                        views[event.getClipData().getItemAt(0).getIntent().getIntExtra(PLAYER_MOVED, -1)].setVisibility(View.VISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        });

        views[0] = position1;
        views[1] = position2;
        views[2] = position3;
        views[3] = position4;
        views[4] = position5;
        views[5] = position6;

        for (final Position player : mappingBase.get(whereIsOne)){
            views[player.getPlayer()].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipData data = ClipData.newIntent(TAG, new Intent().putExtra(PLAYER_MOVED, player.getPlayer()));
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, shadowBuilder, v, 0);
                    return false;
                }
            });
        }

        paintbase();

        rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whereIsOne == 6) {
                    whereIsOne = 1;
                } else {
                    whereIsOne++;
                }

                Log.d(TAG, "one is on " + whereIsOne);
                paintbase();
            }
        });

        base.setPressed(true);

        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = 0;
                def.setPressed(false);
                atq.setPressed(false);
                paintbase();
            }
        });

        def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = 1;
                base.setPressed(false);
                atq.setPressed(false);
                paintbase();
            }
        });

        atq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording = 2;
                base.setPressed(false);
                def.setPressed(false);
                paintbase();
            }
        });

        saveRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (recording){
                    case 0:
                        Log.d(TAG, "cant override base formation");
                        break;
                    case 1:
                        LinkedList<Position> current = new LinkedList<>();
                        for (int p = 0; p < 6; p++) {
                            current.add(new Position((int)views[p].getX() + Utils.convertDpToPx(getActivity(), 40), (int)views[p].getY() + Utils.convertDpToPx(getActivity(), 40), p));
                        }

                        mappingRcv.put(whereIsOne, current);

                        api.positions(new Positions(mappingRcv, "DEF", prefs.getTeamId()), new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                Log.d(TAG, "SUCCESS " + s);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(TAG, "ERRROR " + error.getMessage());
                            }
                        });

                        break;
                    case 2:
                        LinkedList<Position> current2 = new LinkedList<>();
                        for (int p = 0; p < 6; p++) {
                            current2.add(new Position((int)views[p].getX() + Utils.convertDpToPx(getActivity(), 40), (int)views[p].getY() + Utils.convertDpToPx(getActivity(), 40), p));
                        }

                        mappingAtq.put(whereIsOne, current2);

                        api.positions(new Positions(mappingAtq, "ATQ", prefs.getTeamId()), new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                Log.d(TAG, "SUCCESS " + s);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e(TAG, "ERRROR " + error.getMessage());
                            }
                        });

                        break;
                }
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
                for (final Position player : mappingRcv.get(whereIsOne)) {
                    views[player.getPlayer()].setX(player.getX() - Utils.convertDpToPx(getActivity(), 40));
                    views[player.getPlayer()].setY(player.getY() - Utils.convertDpToPx(getActivity(), 40));
                    Log.d(TAG, "player " + player.getPlayer() + " in PosX: " + player.getX() + " in PosY: " + player.getY());
                }
                break;
            case 2:
                for (final Position player : mappingAtq.get(whereIsOne)) {
                    views[player.getPlayer()].setX(player.getX() - Utils.convertDpToPx(getActivity(), 40));
                    views[player.getPlayer()].setY(player.getY() - Utils.convertDpToPx(getActivity(), 40));
                    Log.d(TAG, "player " + player.getPlayer() + " in PosX: " + player.getX() + " in PosY: " + player.getY());
                }
                break;
        }

    }

    private void initializeMappingBase(int divisionWidth, int divisionHeight) {

        int [] xvalues = { 3 * divisionWidth, 3 * divisionWidth, 2 * divisionWidth, divisionWidth, divisionWidth, 2 * divisionWidth};
        int [] yvalues = { 2 * divisionHeight, divisionHeight, divisionHeight, divisionHeight, 2 * divisionHeight, 2 * divisionHeight};

        for (int x = 1; x <= 6; x++) {
            //iterate over rotations and shift arrays;
            LinkedList<Position> current = new LinkedList<>();
            for (int p = 0; p < 6; p++) {
                current.add(new Position(xvalues[p], yvalues[p], p));
            }

            xvalues = shift(xvalues);
            yvalues = shift(yvalues);
            mappingBase.put(x, current);
            // atq and def shoudl be taken from server
            mappingAtq.put(x, current);
            mappingRcv.put(x, current);
        }

        api.getDefPositions(prefs.getTeamId(), new Callback<Positions>() {
            @Override
            public void success(Positions positions, Response response) {
                if (positions == null) {
                    Log.d(TAG, "there are no positions saved");
                } else {
                    Log.d(TAG, "success " + positions.getType());
                    mappingRcv = positions.getStrategies();
                    paintbase();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error" + error.getMessage());
            }
        });

        api.getAtqPositions(prefs.getTeamId(), new Callback<Positions>() {
            @Override
            public void success(Positions positions, Response response) {
                if (positions == null) {
                    Log.d(TAG, "there are no positions saved");
                } else {
                    Log.d(TAG, "success " + positions.getType());
                    mappingAtq = positions.getStrategies();
                    paintbase();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error " + error.getMessage());
            }
        });
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.define_positions, container, false);
        ButterKnife.inject(this, v);
        ((VolleyStatApplication) getActivity().getApplication()).inject(this);
        return v;
    }



}
