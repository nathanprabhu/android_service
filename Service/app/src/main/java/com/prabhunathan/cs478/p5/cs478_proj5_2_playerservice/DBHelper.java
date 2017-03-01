package com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prabhunathan Gnanasekaran on 11/30/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    final static String TABLE_NAME = "Transactions";
    final static String SONG_ID = "_id";
    final static String TIME = "time";
    final static String SONG_STATUS = "current_state";
    final static String TYPE = "type";
    final static String ITEM_NO = "item_no";
    final static String[] columns = {SONG_ID, TIME, SONG_STATUS, TYPE, ITEM_NO};
    final private Context myContext;

    // Creates the database
    public DBHelper(Context context) {
        super(context, "songHistoryDB", null, 1);
        this.myContext = context;
    }

    // Create Table
    final private static String CREATE_CMD =

            "CREATE TABLE songHistory (" + SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TIME + " TEXT NOT NULL, "
                    + SONG_STATUS + " TEXT NOT NULL, "
                    + TYPE + " TEXT NOT NULL, "
                    + ITEM_NO + " INTEGER NOT NULL)";




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    //Delete DB
    void deleteDatabase() {
        myContext.deleteDatabase("TransactionsDB");
    }

}
