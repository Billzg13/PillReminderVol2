package com.example.billzg.pillremindervol2;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //toast for test
        System.out.println("it comes in onReceive");


        //values gets passed from intent to here !! it works
        Bundle name = intent.getExtras();
        int notificationId = name.getInt("nId");
        System.out.println("intent i got from MainActivity???  "+notificationId);


        System.out.println("notifaction process begins here");

        //notification
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String channel_id = "my_channel_01";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.notific)
                .setContentTitle("My notification with ID "+notificationId)
                .setContentText("i received the notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());

        System.out.println("notification process ends here");


    }
}