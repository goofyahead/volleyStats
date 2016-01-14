package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 10/23/15.
 */
public class Serve extends Play{

    private String type = Serve.class.getSimpleName();

    public Serve(String playerId, int quality, String timeStamp, String matchId, String teamId) {
        super(playerId, quality, timeStamp, matchId, teamId);
    }
}
