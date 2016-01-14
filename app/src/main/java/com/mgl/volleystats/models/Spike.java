package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 10/5/15.
 */
public class Spike extends Play{

    private String type = Spike.class.getSimpleName();

    public Spike(String playerId, int quality, String timeStamp, String matchId, String teamId) {
        super(playerId, quality, timeStamp, matchId, teamId);
    }
}
