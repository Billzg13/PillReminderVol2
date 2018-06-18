package com.example.billzg.pillremindervol2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billzg.pillremindervol2.Database.MyDbHandler;
import com.example.billzg.pillremindervol2.model.Pills;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Pills> {
    private Context context;
    private ArrayList<Pills> dataList = new ArrayList<>();
    private MyDbHandler myDbHandler;

    //alarm
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Pills> data) {
        super(context, 0, data);
        this.context = context;
        this.dataList = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null){
            listItem = LayoutInflater.from(this.context).inflate(R.layout.my_custom_show, parent, false);
        }

        final Pills currentPill = dataList.get(position);

        //widgets
        TextView nameView = (TextView) listItem.findViewById(R.id.nameTxtView);
        TextView quantityView = (TextView) listItem.findViewById(R.id.qualityTxtView);
        TextView whenView = (TextView)listItem.findViewById(R.id.whenTxtView);

        nameView.setText(currentPill.getName());
        quantityView.setText(""+currentPill.getQuantity());
        whenView.setText(currentPill.getWhen());

        //cancel the Alarm on longClick
        listItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                myDbHandler = new MyDbHandler(context, null, null, 1);
                int requestCode = myDbHandler.FetchIdFromNameAndQuantity(currentPill.getName(), currentPill.getQuantity());
                if (cancelCurrentPillsAlarm(requestCode)){
                    Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "its not canceled", Toast.LENGTH_SHORT).show();
                }


                return true;
            }
        });


        return listItem;
    }

    public Boolean cancelCurrentPillsAlarm(int reqCode){

        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("nId", reqCode);
        pendingIntent = PendingIntent.getBroadcast(context, reqCode,
                intent, 0);
        alarmManager.cancel(pendingIntent);
        System.out.println("it comes here with reqcode : "+reqCode);

        return true;
    }

}
