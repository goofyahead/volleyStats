package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 10/23/15.
 */
public class Point {
    private String playerId;
    private String timeStamp;
    private String matchId;
    private String teamId;

    public Point(String playerId, String timeStamp, String matchId, String teamId) {
        this.playerId = playerId;
        this.timeStamp = timeStamp;
        this.matchId = matchId;
        this.teamId = teamId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
