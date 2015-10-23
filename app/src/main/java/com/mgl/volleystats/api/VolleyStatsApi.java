package com.mgl.volleystats.api;


import com.mgl.volleystats.models.Spike;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;


/**
 * Created by goofyahead on 9/8/14.
 */
public interface VolleyStatsApi {

//    @GET("/api/ordersToPrint")
//    void getOrdersToPrint(Callback<PrintableOrder> order);

    @POST("/api/spikes")
    void postSpike(@Body Spike spike, Callback<String> response);

}
