package com.mgl.volleystats.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{

	private Context mContext;
	private static final String TAG = DbHelper.class.getName();

	private static final String DATABASE_NAME = "VolleyStats";
	private static final int DATABASE_VERSION = 1;

    //COMMON FIELD TO ALL TABLES
    public static final String SYNCED = "SYNCED";
    public static final String QUALITY = "QUALITY";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String PLAYER = "PLAYER_ID";
    public static final String MATCH_ID = "MATCH_ID";

    // ATTACK TABLES

    // SPIKES TABLE
	public static final String TABLE_NAME_SPIKES = "SPIKES_TABLE";
	public static final String SPIKE_ID = "_id";

    // SERVE TABLE
    public static final String TABLE_NAME_SERVES = "SERVES_TABLE";
    public static final String SERVE_ID = "_id";

    // SET TABLE
    public static final String TABLE_NAME_SETS = "SETS_TABLE";
    public static final String SET_ID = "_id";

    // DEFENSIVE TABLES

    //RECEPTION TABLE
    public static final String TABLE_NAME_RECEPTION = "RECEPTION_TABLE";
    public static final String RECEPTION_ID = "_id";

    // BLOCK TABLE
    public static final String TABLE_NAME_BLOCK = "BLOCK_TABLE";
    public static final String BLOCK_ID = "_id";

    // GENERIC TABLES

    //FAULTS
    public static final String TABLE_NAME_FAULTS = "FAULTS_TABLE";
    public static final String FAULT_ID = "_id";

    //POINTS
    public static final String TABLE_NAME_POINTS = "POINTS_TABLE";
    public static final String POINTS_ID = "_id";

    // PLAYERS MUST BE CREATED WITH CONNECTION TO GET A CORRECT ID
    public static final String TABLE_NAME_PLAYERS = "PLAYERS_TABLE";
    public static final String PLAYER_ID = "_id";
    public static final String PLAYER_NUMBER = "PLAYER_NUMBER";
    public static final String PLAYER_NAME = "PLAYER_NAME";
    public static final String PLAYER_PICTURE = "PLAYER_PIC";
    public static final String PLAYER_MAIN_POSITION = "PLAYER_MAIN_POS";

    public static final String CREATE_POINTS = "CREATE TABLE "
            + TABLE_NAME_POINTS + " ("
            + POINTS_ID + " TEXT PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + TIMESTAMP + " TEXT, "
            + MATCH_ID + " TEXT, "
            + SYNCED + " INTEGER, "
            + PLAYER + " TEXT) ";

    public static final String CREATE_FAULTS = "CREATE TABLE "
            + TABLE_NAME_FAULTS + " ("
            + FAULT_ID + " TEXT PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + TIMESTAMP + " TEXT, "
            + MATCH_ID + " TEXT, "
            + SYNCED + " INTEGER, "
            + PLAYER + " TEXT) ";

    public static final String CREATE_BLOCKS = "CREATE TABLE "
            + TABLE_NAME_BLOCK + " ("
            + BLOCK_ID + " TEXT PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + QUALITY + " INTEGER, "
            + TIMESTAMP + " TEXT, "
            + MATCH_ID + " TEXT, "
            + SYNCED + " INTEGER, "
            + PLAYER + " TEXT) ";

    public static final String CREATE_RECEPTION = "CREATE TABLE "
            + TABLE_NAME_RECEPTION + " ("
            + RECEPTION_ID + " TEXT PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + QUALITY + " INTEGER, "
            + TIMESTAMP + " TEXT, "
            + MATCH_ID + " TEXT, "
            + SYNCED + " INTEGER, "
            + PLAYER + " TEXT) ";

    public static final String CREATE_SETS = "CREATE TABLE "
            + TABLE_NAME_SETS + " ("
            + SET_ID + " TEXT PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + QUALITY + " INTEGER, "
            + TIMESTAMP + " TEXT, "
            + MATCH_ID + " TEXT, "
            + SYNCED + " INTEGER, "
            + PLAYER + " TEXT) ";

    public static final String CREATE_SERVES = "CREATE TABLE "
            + TABLE_NAME_SERVES + " ("
            + SERVE_ID + " TEXT PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + QUALITY + " INTEGER, "
            + TIMESTAMP + " TEXT, "
            + MATCH_ID + " TEXT, "
            + SYNCED + " INTEGER, "
            + PLAYER + " TEXT) ";

	public static final String CREATE_SPIKES = "CREATE TABLE "
            + TABLE_NAME_SPIKES + " ("
            + SPIKE_ID + " TEXT PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + QUALITY + " INTEGER, "
            + TIMESTAMP + " TEXT, "
            + SYNCED + " INTEGER, "
            + MATCH_ID + " TEXT, "
            + PLAYER + " TEXT) ";

    public static final String CREATE_PLAYERS = "CREATE TABLE "
            + TABLE_NAME_PLAYERS + " ("
            + PLAYER_ID + " TEXT PRIMARY KEY NOT NULL, "
            + PLAYER_NUMBER + " INTEGER, "
            + PLAYER_NAME + " TEXT, "
            + PLAYER_MAIN_POSITION + " TEXT, "
            + PLAYER_PICTURE + " TEXT, "
            + SYNCED + " INTEGER) ";
		

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
//		context.deleteDatabase(DATABASE_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "creating database");
		db.execSQL(DbHelper.CREATE_SPIKES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Upgrading database, this will drop tables and recreate");
        db.execSQL("DROP TABLE IF EXISTS " + DbHelper.CREATE_SPIKES);
        onCreate(db);
	}

	public boolean deleteDB() {
		return mContext.deleteDatabase(DATABASE_NAME);
	}
}
