package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 10/23/15.
 */
public class Reception extends Play {

    private String type = Reception.class.getSimpleName();

    public Reception(String playerId, int quality, String timeStamp, String matchId, String teamId) {
        super(playerId, quality, timeStamp, matchId, teamId);
    }
}
