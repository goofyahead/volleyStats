package com.mgl.volleystats.api;


import com.mgl.volleystats.models.Match;
import com.mgl.volleystats.models.Play;
import com.mgl.volleystats.models.Player;
import com.mgl.volleystats.models.Position;
import com.mgl.volleystats.models.Positions;
import com.mgl.volleystats.models.Spike;
import com.mgl.volleystats.models.Team;

import java.util.LinkedList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;


/**
 * Created by goofyahead on 9/8/14.
 */
public interface VolleyStatsApi {

    @GET("/api/teams")
    void getTeams(Callback<LinkedList<Team>> teams);

    @GET("/api/currentMatch/{teamId}")
    void getCurrentMatch(@Path("teamId") String teamId, Callback<Match> match);

    @GET("/api/defpositions/{teamId}")
    void getDefPositions(@Path("teamId") String teamId, Callback<Positions> teams);

    @GET("/api/atqpositions/{teamId}")
    void getAtqPositions(@Path("teamId") String teamId, Callback<Positions> teams);

    @GET("/api/players/{teamId}")
    void getPlayersFromTeam(@Path("teamId") String teamId, Callback<LinkedList<Player>> players);

    @GET("/api/teams/{id}")
    void getTeam(@Path("id")String id, Callback<Team> team);

    @POST("/api/play")
    void postPlay(@Body Play spike, Callback<String> response);

    @POST("/api/matches")
    void postMatch(@Body Match match, Callback<String> response);

    @Multipart
    @POST("/api/players")
    void createPlayer(@Part("player") Player player,
                      @Part("picture") TypedFile fileUpload,
                      Callback<String> response);

    @POST("/api/positions")
    void positions(@Body Positions positions, Callback<String> cb);

}
