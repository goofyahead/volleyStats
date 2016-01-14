package com.mgl.volleystats.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgl.volleystats.R;
import com.mgl.volleystats.base.Credentials;
import com.mgl.volleystats.dialogFragments.PasswordDialogFragment;
import com.mgl.volleystats.models.Team;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

/**
 * Created by goofyahead on 12/30/15.
 */
public class TeamAdapter extends BaseAdapter {

    private static final String TAG = TeamAdapter.class.getName();
    private final LayoutInflater mInflater;
    private LinkedList<Team> teams;
    private Context mContext;

    public TeamAdapter (Context context, LinkedList<Team> teams) {
        this.mInflater = LayoutInflater.from(context);
        this.teams = teams;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return teams.size();
    }

    @Override
    public Object getItem(int position) {
        return teams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.teams_list_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.teamName);
            holder.picture = (ImageView) convertView.findViewById(R.id.teamPicture);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(teams.get(position).getName());
        Log.d(TAG, "image to load is " + Credentials.SERVER_IP + Credentials.API_IMAGES + "teams/" + teams.get(position).getProfilePic());
        Picasso.with(mContext).load(Credentials.SERVER_IP + Credentials.API_IMAGES + "teams/" + teams.get(position).getProfilePic()).into(holder.picture);

        return convertView;
    }

    private class ViewHolder {
        private TextView name;
        private ImageView picture;
    }
}
