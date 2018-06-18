package com.example.billzg.pillremindervol2;

/*
                this is how i parse (string)"time" to calendar
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(simpleDateFormat.parse(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("PARSED TIME IS: "+cal.get(Calendar.HOUR_OF_DAY)+" "+cal.get(Calendar.MINUTE));
                System.out.println("TIME IS all together: "+cal.getTime());
                */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.billzg.pillremindervol2.Database.MyDbHandler;
import com.example.billzg.pillremindervol2.model.Pills;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddPill extends AppCompatActivity {

    //widgets
    private EditText etName;
    private EditText etQuantity;
    private EditText timeText;
    private Button addPillB;

    private String pillName;
    private String pillQuantity;

    private SimpleDateFormat simpleDateFormat;
    private String time;
    private Calendar calender;
    private MyDbHandler myDbHandler;
    private Pills myPill;

    //timepickerDialog
    private TimePickerDialog timePickerDialog;
    private int userHour;
    private int userMinute;

    //alarm
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pill);

        etName = (EditText) findViewById(R.id.pillEt);
        etQuantity = (EditText) findViewById(R.id.quantityEt);
        timeText = (EditText) findViewById(R.id.timeEt);
        addPillB = (Button) findViewById(R.id.addPillButton);
        myDbHandler = new MyDbHandler(this, null, null, 1);

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                timePickerDialog = new TimePickerDialog(AddPill.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        userHour = hourOfDay;
                        userMinute = minute;
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });


        addPillB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hello from start of onClick");

                pillName = etName.getText().toString();
                pillQuantity = etQuantity.getText().toString();

                calender = Calendar.getInstance();
                calender.set(Calendar.HOUR_OF_DAY, userHour);
                calender.set(Calendar.MINUTE, userMinute);
                calender.set(Calendar.SECOND, 0);
                simpleDateFormat = new SimpleDateFormat("h:mm a");
                time = simpleDateFormat.format(calender.getTime()); //this is current time i have to make it so the
                System.out.println("users time is: "+time);
                //user can set his own time this is also a String for the Constructor
                myPill = new Pills(pillName, Double.parseDouble(pillQuantity), time);
                /// /add pill to database
                myDbHandler.AddPill(myPill);

                //alarm process
                System.out.println("alarm process starts here");
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.HOUR_OF_DAY, userHour);
                c.set(Calendar.MINUTE, userMinute);
                c.set(Calendar.SECOND, 0);


                int notificationId ;
                //pass a unique id for each notification, this id is the _ID from the database
                notificationId = myDbHandler.FetchIdFromNameAndQuantity(myPill.getName(), myPill.getQuantity());
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(AddPill.this, AlarmReceiver.class);
                intent.putExtra("nId", notificationId);
                pendingIntent = PendingIntent.getBroadcast(AddPill.this, notificationId,
                        intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                System.out.println("ALARM SET FOR "+c.getTime());

                System.out.println("alarm process ends here");


            }
        });



    }

}