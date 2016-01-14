package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 1/10/16.
 */
public class Defense extends Play {

    private String type = Defense.class.getSimpleName();

    public Defense(String playerId, int quality, String timeStamp, String matchId, String teamId) {
        super(playerId, quality, timeStamp, matchId, teamId);
    }
}
