package com.prabhunathan.cs478.p5.cs478_proj5_1_playerclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SongHistoryList extends AppCompatActivity {

    ArrayAdapter<String> arrayAdapter;
    String[] rString;

    //This activity on create launches the music player service with the action "gettable"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_history_list);

        //Register TableReceiver to receive the transactions table records.
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.prabhunathan.cs478.p5.cs478_proj5_1_playerclient.receivetable");
        (SongHistoryList.this).registerReceiver(new TableReceiver(), filter);

        Intent getTableIntent = new Intent("get package");
        getTableIntent.setPackage("com.prabhunathan.cs478.p5.cs478_proj5_2_playerservice.gettable");
        getTableIntent.putExtra("SONGID", -1);
        startService(getTableIntent);

    }
    //Displays history in listview
    public class TableReceiver extends BroadcastReceiver {

        public TableReceiver(){
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                rString = intent.getStringArrayExtra("TransactionsTable");
                arrayAdapter = new ArrayAdapter<String>(context, R.layout.table_row, R.id.transaction, rString);

                ListView tableView = (ListView) findViewById(R.id.transactionsList);
                tableView.setAdapter(arrayAdapter);

            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }

        }

    }

}
