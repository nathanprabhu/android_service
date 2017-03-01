package com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Prabhunathan Gnanasekaran on 11/30/2016.
 */
public class MusicPlayer extends IntentService {

    //Declaring and Initializing music lists, media player, position, and last status
    static MediaPlayer mediaPlayer = new MediaPlayer();
    int[] songs = {R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5, R.raw.song6};
    static int currentIndex = 0;
    static String lastStatus = "-NA-";

    //Object for DB Helper
    DBHelper myDBHelper;

    public MusicPlayer() {
        super("MusicPlayer");
        myDBHelper = new DBHelper(this);
    }

    //Implementing AIDL
    private final IMusicPlayer.Stub musicPlayerBinder = new IMusicPlayer.Stub() {
        // Play song
        @Override
        public void play(int songID) throws RemoteException {
            if(!(mediaPlayer.isPlaying())) {
                mediaPlayer.start();
            }
        }
        //Pause song
        @Override
        public void pause(int songID) throws RemoteException {
            mediaPlayer.pause();
            currentIndex = mediaPlayer.getCurrentPosition();
        }
        //Resume song
        @Override
        public void resume(int songID) throws RemoteException {
            mediaPlayer.seekTo(currentIndex);
            mediaPlayer.start();
        }
        //Stop song
        @Override
        public void stop(int songID) throws RemoteException {
            currentIndex = 0;
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        //Get song history
        @Override
        public void getTable() throws RemoteException {
            Cursor table =  myDBHelper.getWritableDatabase().query(DBHelper.TABLE_NAME,
                    DBHelper.columns, null, new String[] {}, null, null,
                    null);
            table.moveToFirst();
            String[] results = new String[table.getCount()];
            int i = 0;
            while (!(table.isAfterLast())) {
                results[i] = table.getString(0) + " " + table.getString(1) + " " + table.getString(2) + " " + table.getString(3) + " " + table.getString(4);
                System.out.println(results[i]);
                i++;
                table.moveToNext();
            }
            Intent x = new Intent("com.prabhunathan.cs478.p5.cs478_proj5_1_playerclient.receivetable");
            x.putExtra("TransactionsTable", results);
            System.out.println(x);
            sendBroadcast(x);
        }
    };

    //Return null onBind
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Delete DB on unbind
    @Override
    public boolean onUnbind(Intent intent) {
        myDBHelper.getWritableDatabase().close();
        myDBHelper.deleteDatabase();
        super.onDestroy();
        return super.onUnbind(intent);
    }

    //Play/Pause/Resume/Stop/Get History based on action from Client
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (intent != null) {
                final String action = intent.getAction();
                final Integer songID = (Integer) intent.getExtras().get("SONGID");

                if(!(action.endsWith("gettable"))) {
                    saveHistory(action, songID);
                }

                if (action == "com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.play") {
                    lastStatus = "Playing song " + songID.toString();
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.release();
                    }
                    mediaPlayer = MediaPlayer.create(this, songs[songID]);
                    musicPlayerBinder.play(songID);
                }
                else if (action == "com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.pause") {
                    lastStatus = "Paused song " + songID.toString();
                    musicPlayerBinder.pause(songID);
                }
                else if (action == "com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.resume") {
                    lastStatus = "Resumed playing song " + songID.toString();
                    musicPlayerBinder.resume(songID);
                }
                else if (action == "com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.stop") {
                    lastStatus = "Stopped song " + songID.toString();
                    musicPlayerBinder.stop(songID);
                }
                else if (action == "com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.gettable") {
                    musicPlayerBinder.getTable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Save history in DB
    private void saveHistory(String action, int songID){

        ContentValues values = new ContentValues();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        values.put(DBHelper.TIME, dateFormat.format(cal.getTime()));
        values.put(DBHelper.SONG_STATUS, lastStatus);
        values.put(DBHelper.TYPE, action.substring(action.lastIndexOf(".") + 1, action.length()));
        values.put(DBHelper.ITEM_NO, Integer.toString(songID));

        myDBHelper.getWritableDatabase().insert(DBHelper.TABLE_NAME, null, values);
    }
}
