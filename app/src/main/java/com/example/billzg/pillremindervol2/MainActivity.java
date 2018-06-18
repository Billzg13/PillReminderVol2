package com.example.billzg.pillremindervol2;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.billzg.pillremindervol2.Database.MyDbHandler;
import com.example.billzg.pillremindervol2.model.Pills;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton myFabRefresh;
    private FloatingActionButton myFab;
    private Intent i;
    private ListView mListView;
    private ArrayList<Pills> myPills;
    private CustomArrayAdapter myAdapter;
    private MyDbHandler myDbHandler;


    Calendar c;
    Calendar test;
    AlarmManager alarmMgr;
    PendingIntent pendingIntent;

    //notif_channel
    private String channel_id;
    private CharSequence name;
    private int importance;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        channel_id = "my_channel_01";
        name = "my_name";
        importance = NotificationManager.IMPORTANCE_HIGH;
        description = "this is a test";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        i = new Intent(this, AddPill.class);
        myDbHandler = new MyDbHandler(this, null, null, 1);
        mListView = (ListView) findViewById(R.id.myListView);
        myPills = new ArrayList<>();
        //myPills is the ArrayList for the Pills and we populate it from the database
        myPills = myDbHandler.databaseToArraylist();


        myAdapter = new CustomArrayAdapter(this, 1, myPills);
        mListView.setAdapter(myAdapter);

        myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(i);
                    }
                }
        );

        myFabRefresh = (FloatingActionButton) findViewById(R.id.fabRefresh);
        myFabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPills = myDbHandler.databaseToArraylist();
                myAdapter = new CustomArrayAdapter(MainActivity.this, 1, myPills);
                mListView.setAdapter(myAdapter);

            }
        });

    }

    //this is just for testing it will get deleted
    public void makeAlarm(View view) {
        c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE,53);
        c.set(Calendar.SECOND, 0);

        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("something", "anyvalue");
        pendingIntent = PendingIntent.getBroadcast(this, 100,
                intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        System.out.println("it comes here and the alarm is set for "+c.getTimeInMillis()+" the alarm time is set for "+c.getTime());
        test = Calendar.getInstance();
        test.setTimeInMillis(System.currentTimeMillis());

        System.out.println("current time is :"+test.getTimeInMillis()+" and the current time is "+test.getTime());

    }
}