package com.mgl.volleystats.models;

/**
 * Created by goofyahead on 10/23/15.
 */
public class Play {
    private String playerId;
    private int quality;
    private String timeStamp;
    private String matchId;

    public Play(String playerId, int quality, String timeStamp, String matchId) {
        this.playerId = playerId;
        this.quality = quality;
        this.timeStamp = timeStamp;
        this.matchId = matchId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
