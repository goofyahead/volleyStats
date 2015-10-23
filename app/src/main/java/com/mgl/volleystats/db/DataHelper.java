package com.mgl.volleystats.db;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.models.Spike;

import java.io.File;
import java.util.LinkedList;

import javax.inject.Inject;

public class DataHelper {

	private static final String TAG = DataHelper.class.getName();
	private DbHelper openHelper;
    private Context mContext;
    @Inject
    DownloadManager dm;


    public DataHelper(Context context) {
        mContext = context;
        ((VolleyStatApplication) context.getApplicationContext()).inject(this);
        openHelper = new DbHelper(context);
	}

	public void saveSpike (Spike spike) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbHelper.PLAYER, spike.getPlayerId());
		values.put(DbHelper.QUALITY, spike.getQuality());
		values.put(DbHelper.SYNCED, 0);
		values.put(DbHelper.TIMESTAMP, spike.getTimeStamp());
		values.put(DbHelper.MATCH_ID, spike.getMatchId());

		db.insert(DbHelper.TABLE_NAME_SPIKES, null, values);
		db.close();
	}
	
//	public Cursor getDishCursor () {
//		SQLiteDatabase db = openHelper.getWritableDatabase();
//		return db.query(DbHelper.TABLE_NAME_DISHES, null, null, null, null, null, DbHelper.DISH_NAME + " ASC");
//	}
//
//	public Cursor getDishCursor (String selection, String args) {
//		SQLiteDatabase db = openHelper.getWritableDatabase();
//		final String SELECTION = selection + "=?";
//	    final String[] SELECTION_ARGS = {args};
//		return db.query(DbHelper.TABLE_NAME_DISHES, null, SELECTION, SELECTION_ARGS, null, null, DbHelper.DISH_NAME + " ASC");
//	}
//
//	public Dish getDishById (String id) {
//		return getDishFromCursor(getDishCursor(DbHelper.DISH_ID, id));
//	}
//
//	private Dish getDishFromCursor (Cursor cursor) {
//		Dish searchedDish = null;
//		 if (cursor.moveToNext()) {
//			 String video = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_VIDEO));
//			 String name = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_NAME));
//			 float price = cursor.getFloat(cursor.getColumnIndex(DbHelper.DISH_PRICE));
//			 String description = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_DESCRIPTION));
//			 String id = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_ID));
//			 String image = cursor.getString(cursor.getColumnIndex(DbHelper.DISH_IMAGE));
//			 boolean demo = cursor.getInt(cursor.getColumnIndex(DbHelper.DISH_DEMO)) == 0 ? false : true;
//			 searchedDish = new Dish(id, name, description, price, image, video, demo, null, null, null, null);
//		 }
//		cursor.close();
//		return searchedDish;
//	}
}
