package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 10/23/15.
 */
public class Block extends Play {
    private String type = Block.class.getSimpleName();

    public Block(String playerId, int quality, String timeStamp, String matchId, String teamId) {
        super(playerId, quality, timeStamp, matchId, teamId);
    }
}
