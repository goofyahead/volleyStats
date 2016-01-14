package com.mgl.volleystats.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by goofyahead on 10/1/15.
 */
public class VolleyPrefs {
    private static final String TEAM_ID = "TEAM_ID";
    private final SharedPreferences prefs;

    public VolleyPrefs(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getTeamId() {
        return prefs.getString(TEAM_ID, "none");
    }

    public void setTeamId(String teamId) {
        prefs.edit().putString(TEAM_ID, teamId).commit();
    }

}
