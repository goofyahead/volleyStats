package com.mgl.volleystats.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgl.volleystats.R;
import com.mgl.volleystats.base.Credentials;
import com.mgl.volleystats.models.Play;
import com.mgl.volleystats.models.Player;
import com.mgl.volleystats.picasso.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

/**
 * Created by goofyahead on 1/4/16.
 */
public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {

    private final Context mContext;
    private LinkedList<Player> players;
    private ItemLongClicked mInterface;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlayersAdapter(LinkedList<Player> myDataset, Context context, ItemLongClicked mInterface) {
        this.players = myDataset;
        this.mContext = context;
        this.mInterface = mInterface;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlayersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.playerName.setText(players.get(position).getName());
        Picasso.with(mContext).load(Credentials.SERVER_IP + Credentials.API_IMAGES + "players/" + players.get(position).getPicture()).transform(new CircleTransformation()).into(holder.playerPic);

        holder.playerPic.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                mInterface.itemLongClicked(v, players.get(position));
                players.remove(position);
                notifyDataSetChanged();
                return false;
            }

        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return players.size();
    }

    public void addPLayer(Player player) {
        players.add(player);
        notifyDataSetChanged();
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView playerName;
        public ImageView playerPic;

        public ViewHolder(View v) {
            super(v);
            playerName = (TextView) v.findViewById(R.id.player_item_name);
            playerPic = (ImageView) v.findViewById(R.id.player_item_picture);
        }
    }

    public interface ItemLongClicked {
        void itemLongClicked(View v, Player player);
    }
}