package com.mgl.volleystats.models;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

/**
 * Created by goofyahead on 1/2/16.
 */
public class Match {
    @SerializedName("_id")
    private String id;
    private String location;
    private long dateTs;
    private LinkedList<Player> players;
    private String against;
    private String teamId;
    private String status;

    public Match(String location, long dateTs, LinkedList<Player> players, String against, String teamId, String status) {
        this.location = location;
        this.dateTs = dateTs;
        this.players = players;
        this.against = against;
        this.teamId = teamId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDateTs() {
        return dateTs;
    }

    public void setDateTs(long dateTs) {
        this.dateTs = dateTs;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedList<Player> players) {
        this.players = players;
    }

    public String getAgainst() {
        return against;
    }

    public void setAgainst(String against) {
        this.against = against;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
