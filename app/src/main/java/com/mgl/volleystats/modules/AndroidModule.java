package com.mgl.volleystats.modules;

/**
 * Created by goofyahead on 9/8/14.
 */

import android.app.DownloadManager;
import android.content.Context;


import com.mgl.volleystats.activities.CreatePlayerActivity;
import com.mgl.volleystats.activities.MainActivity;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.Credentials;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.db.DataHelper;
import com.mgl.volleystats.dialogFragments.PlayerDialogFragment;
import com.mgl.volleystats.fragments.CurrentMatch;
import com.mgl.volleystats.fragments.DefinePositions;
import com.mgl.volleystats.fragments.NewMatch;
import com.mgl.volleystats.fragments.SelectTeam;
import com.mgl.volleystats.fragments.TeamView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module(injects = {
        MainActivity.class,
        DataHelper.class,
        CreatePlayerActivity.class,
        SelectTeam.class,
        TeamView.class,
        NewMatch.class,
        DefinePositions.class,
        CurrentMatch.class,
        PlayerDialogFragment.class
},
        library = true)
public class AndroidModule {
    private final Context mContext;

    public AndroidModule(Context context) {
        this.mContext = context;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mContext;
    }

//    @Provides
//    @Singleton
//    RequestQueue provideQueue() {
//        return Volley.newRequestQueue(mContext);
//    }
//
//    @Provides
//    @Singleton
//    ImageLoader provideImageLoader(RequestQueue mRequestQueue) {
//        return new ImageLoader(mRequestQueue, new BitmapLruCache(50));
//    }
//
    @Provides
    @Singleton
    VolleyPrefs provideSharedPrefs() {
        return new VolleyPrefs(mContext);
    }


    @Provides
    @Singleton
    DataHelper provideDataHelper () {
        return new DataHelper(mContext);
    }

    @Provides
    @Singleton
    DownloadManager provideDownloadManager() {
        return (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(Credentials.SERVER_IP)
                .build();
    }

    @Provides
    @Singleton
    VolleyStatsApi provideApi() {
        return provideRestAdapter().create(VolleyStatsApi.class);
    }
}