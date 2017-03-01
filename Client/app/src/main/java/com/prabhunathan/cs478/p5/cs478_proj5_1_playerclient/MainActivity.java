package com.prabhunathan.cs478.p5.cs478_proj5_1_playerclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ListView songslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] songtitle = getResources().getStringArray(R.array.songlist);
        //Display songs in list view
        songslist = (ListView) findViewById(R.id.myListView);
        songslist.setAdapter(new MySongAdapter(songtitle));
        //Launch Music Player Activity on song click
        songslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent playerIntent = new Intent(getApplicationContext(), MusicPlayer.class);
                playerIntent.putExtra("SONGID", position);
                startActivity(playerIntent);
            }
        });
        //Clicking on History will displaay song history
        ImageView songhistory = (ImageView) findViewById(R.id.dbIcon);
        songhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent songhistoryindent = new Intent(getApplicationContext(), SongHistoryList.class);
                startActivity(songhistoryindent);
            }
        });
    }

    //This class populates the list of songs in the album
    class MySongAdapter extends BaseAdapter {

        String[] songNames;
        LayoutInflater layoutInflater;

        MySongAdapter(String[] sNames) {
            super();
            songNames = sNames;
            layoutInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return songNames.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //Each Row is inflated and the song name is set
            convertView = layoutInflater.inflate(R.layout.row, null);
            TextView songName = (TextView) (convertView.findViewById(R.id.songName));
            songName.setText(songNames[position]);
            return convertView;
        }
    }

}
