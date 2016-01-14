package com.mgl.volleystats.dialogFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mgl.volleystats.R;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.Credentials;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.db.DataHelper;
import com.mgl.volleystats.models.Block;
import com.mgl.volleystats.models.Defense;
import com.mgl.volleystats.models.Player;
import com.mgl.volleystats.models.Reception;
import com.mgl.volleystats.models.Serve;
import com.mgl.volleystats.models.Set;
import com.mgl.volleystats.models.Spike;
import com.mgl.volleystats.picasso.CircleTransformation;
import com.mgl.volleystats.utils.Utils;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by goofyahead on 10/23/15.
 */
public class PlayerDialogFragment extends DialogFragment {

    private static final String EXTRA_PLAYER = "EXTRA_PLAYER";
    private static final String EXTRA_MATCH_ID = "EXTRA_MATCH_ID";
    private static String TAG  = PlayerDialogFragment.class.getName();

    @InjectView(R.id.spike_bad)
    ImageView spikebad;
    @InjectView(R.id.spike_neutral)
    ImageView spikeNeutral;
    @InjectView(R.id.spike_good)
    ImageView spikeGood;

    @InjectView(R.id.serve_bad)
    ImageView serveBad;
    @InjectView(R.id.serve_neutral)
    ImageView serveNeutral;
    @InjectView(R.id.serve_good)
    ImageView serveGood;

    @InjectView(R.id.defense_bad)
    ImageView defBad;
    @InjectView(R.id.defense_neutral)
    ImageView defNeutral;
    @InjectView(R.id.defense_good)
    ImageView defGood;

    @InjectView(R.id.block_bad)
    ImageView blockBad;
    @InjectView(R.id.block_neutral)
    ImageView blockNeutral;
    @InjectView(R.id.block_good)
    ImageView blockGood;

    @InjectView(R.id.reception_bad)
    ImageView receBad;
    @InjectView(R.id.reception_neutral)
    ImageView receNeutral;
    @InjectView(R.id.reception_good)
    ImageView receGood;

    @InjectView(R.id.setting_bad)
    ImageView setBad;
    @InjectView(R.id.setting_neutral)
    ImageView setNeutral;
    @InjectView(R.id.setting_good)
    ImageView setGood;

    @InjectView(R.id.player_detail_imageView)
    IconicsImageView playerPic;

    private Player mPlayer;
    private String mMatchId;

    @Inject
    DataHelper dataHelper;
    @Inject
    VolleyStatsApi api;
    @Inject
    VolleyPrefs prefs;

    public static PlayerDialogFragment newInstance(Player player) {
        Log.d(TAG, player.getName());
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
        ((VolleyStatApplication) getActivity().getApplication()).inject(this);
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

        mPlayer = (Player) getArguments().getSerializable(EXTRA_PLAYER);
        mMatchId = getArguments().getString(EXTRA_MATCH_ID);

        Log.d(TAG, "player id " + mPlayer.getId());

        setBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set serve = new Set(mPlayer.getId(), 0, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        setNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set serve = new Set(mPlayer.getId(), 1, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        setGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set serve = new Set(mPlayer.getId(), 2, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        receBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reception serve = new Reception(mPlayer.getId(), 0, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        receNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reception serve = new Reception(mPlayer.getId(), 1, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        receGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reception serve = new Reception(mPlayer.getId(), 2, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        blockBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Block serve = new Block(mPlayer.getId(), 0, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        blockNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Block serve = new Block(mPlayer.getId(), 1, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        blockGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Block serve = new Block(mPlayer.getId(), 2, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        defBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Defense serve = new Defense(mPlayer.getId(), 0, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        defNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Defense serve = new Defense(mPlayer.getId(), 1, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        defGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Defense serve = new Defense(mPlayer.getId(), 2, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });


        serveBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Serve serve = new Serve(mPlayer.getId(), 0, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        serveNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Serve serve = new Serve(mPlayer.getId(), 1, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        serveGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Serve serve = new Serve(mPlayer.getId(), 2, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(serve, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        spikebad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spike spike = new Spike(mPlayer.getId(), 0, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(spike, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        spikeNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spike spike = new Spike(mPlayer.getId(), 1, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(spike, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        spikeGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spike spike = new Spike(mPlayer.getId(), 2, Utils.getTimeStamp(), mMatchId, prefs.getTeamId());
                api.postPlay(spike, cb);
                PlayerDialogFragment.this.dismiss();
            }
        });

        Picasso.with(getActivity()).load(Credentials.SERVER_IP + Credentials.API_IMAGES + "players/" + mPlayer.getPicture()).transform(new CircleTransformation()).into(playerPic);
    }

    private Callback<String> cb = new Callback<String>() {
        @Override
        public void success(String s, Response response) {
            Log.d(TAG, "SUCCESS " + s);
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e(TAG, "ERROR " + error.getMessage());
        }
    };
}
