package com.mgl.volleystats.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by goofyahead on 1/8/16.
 */
public class Positions {
    private HashMap <Integer, LinkedList<Position>> strategies;
    private String type;
    private String teamId;

    public Positions(HashMap<Integer, LinkedList<Position>> strategies, String type, String teamId) {
        this.strategies = strategies;
        this.type = type;
        this.teamId = teamId;
    }

    public HashMap<Integer, LinkedList<Position>> getStrategies() {
        return strategies;
    }

    public void setStrategies(HashMap<Integer, LinkedList<Position>> strategies) {
        this.strategies = strategies;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
