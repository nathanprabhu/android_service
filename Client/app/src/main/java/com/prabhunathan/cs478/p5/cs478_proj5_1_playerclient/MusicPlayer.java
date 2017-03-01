package com.prabhunathan.cs478.p5.cs478_proj5_1_playerclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MusicPlayer extends AppCompatActivity {

    TextView playbtn;
    TextView prevbtn;
    TextView nextbtn;
    TextView stopbtn;
    TextView songtitle;
    boolean isPlaying = true;
    boolean isStopped = false;

    String[] songNames;
    int songId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //Get the SONGID of the song selected from the mainactivity
        songId = (Integer) getIntent().getExtras().get("SONGID");

        songNames = getResources().getStringArray(R.array.songlist);

        songtitle = (TextView) findViewById(R.id.currentSong);
        playbtn = (TextView) findViewById(R.id.playButton);
        prevbtn = (TextView) findViewById(R.id.prevButton);
        nextbtn = (TextView) findViewById(R.id.nextButton);
        stopbtn = (TextView) findViewById(R.id.stopButton);

        //Set Pause from Play state on click
        songtitle.setText(songNames[songId]);
        playbtn.setText("PAUSE");

        //onclick listener for play button
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(isStopped);
                if (isPlaying == true && isStopped == false) {
                    //Pause when isPlaying
                    isPlaying = false;
                    playbtn.setText("PLAY");
                    Intent newMusicPlayerIntent = new Intent("Pause");
                    newMusicPlayerIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.pause");
                    newMusicPlayerIntent.putExtra("SONGID", songId);
                    startService(newMusicPlayerIntent);
                } else if (isPlaying == false && isStopped == false) {
                    //Resume when paused
                    isPlaying = true;
                    playbtn.setText("PAUSE");
                    Intent newMusicPlayerIntent = new Intent("Resume");
                    newMusicPlayerIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.resume");
                    newMusicPlayerIntent.putExtra("SONGID", songId);
                    startService(newMusicPlayerIntent);
                } else if (isStopped == true) {
                    //Play when isStopped
                    isPlaying = true;
                    isStopped = false;
                    Intent newMusicPlayerIntent = new Intent("play intent");
                    newMusicPlayerIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.play");
                    newMusicPlayerIntent.putExtra("SONGID", songId);
                    startService(newMusicPlayerIntent);
                }
            }
        });

        //Previous button
        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songId>0) {
                    songId = songId - 1;
                }
                songtitle.setText(songNames[songId]);
                playbtn.setText("PAUSE");

                Intent newMusicPlayerIntent = new Intent("play intent");
                newMusicPlayerIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.play");
                newMusicPlayerIntent.putExtra("SONGID", songId);
                startService(newMusicPlayerIntent);
            }
        });

        //Next button
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songId<4) {
                    songId = songId + 1;
                }
                songtitle.setText(songNames[songId]);
                playbtn.setText("PAUSE");
                Intent newMusicPlayerIntent = new Intent("Play intent");
                newMusicPlayerIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.play");
                newMusicPlayerIntent.putExtra("SONGID", songId);
                startService(newMusicPlayerIntent);
            }
        });

        //Stop button
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = false;
                isStopped = true;
                songtitle.setText(songNames[songId]);
                playbtn.setText("PLAY");
                Intent newMusicPlayerIntent = new Intent("Stop Intent");
                newMusicPlayerIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.stop");
                newMusicPlayerIntent.putExtra("SONGID", songId);
                startService(newMusicPlayerIntent);
            }
        });
    }

    //Play selected song
    @Override
    protected void onStart() {
        super.onStart();
        Intent musicPlayerIntent = new Intent("Play intent");
        musicPlayerIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.play");
        musicPlayerIntent.putExtra("SONGID", songId);
        startService(musicPlayerIntent);

    }

    //Stops the current song
    @Override
    public void onDestroy(){
        super.onDestroy();
        isPlaying = false;
        isStopped = true;
        Intent newMusicPlayerIntent = new Intent("stop Intent");
        newMusicPlayerIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.stop");
        newMusicPlayerIntent.putExtra("SONGID", songId);
        startService(newMusicPlayerIntent);
    }

}
