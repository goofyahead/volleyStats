package com.mgl.volleystats.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by goofyahead on 10/23/15.
 */
public class Player implements Serializable{
    private String teamId;
    private String name;
    private String picture;
    @SerializedName("_id")
    private String id;
    private int number;
    private String mainPos;

    public Player(String teamId, String name, int number, String mainPos) {
        this.teamId = teamId;
        this.name = name;
        this.number = number;
        this.mainPos = mainPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMainPos() {
        return mainPos;
    }

    public void setMainPos(String mainPos) {
        this.mainPos = mainPos;
    }
}
