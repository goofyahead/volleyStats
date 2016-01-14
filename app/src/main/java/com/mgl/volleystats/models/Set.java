package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 10/23/15.
 */
public class Set extends Play {

    private String type = Set.class.getName();

    public Set(String playerId, int quality, String timeStamp, String matchId, String teamId) {
        super(playerId, quality, timeStamp, matchId, teamId);
    }
}
