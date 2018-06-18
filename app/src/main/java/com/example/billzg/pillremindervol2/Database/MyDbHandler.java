package com.example.billzg.pillremindervol2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.billzg.pillremindervol2.model.Pills;

import java.util.ArrayList;

public class MyDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pillreminderapp.db";
    private static final String create_table_pills = "create table pills (" +
            "_id integer primary key autoincrement," +
            "name text," +
            "quantity real," +
            "whencol text);";

    public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_pills);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pills ");
        onCreate(db);
    }

    public void AddPill(Pills pill){
        System.out.println("from AddPill: ");
        System.out.println("this is the pill.name"+pill.getName());
        System.out.println("this is the pill.quantity"+pill.getQuantity());
        System.out.println("this is the pill.when"+pill.getWhen());

        ContentValues values = new ContentValues();
        values.put("name", pill.getName());
        values.put("quantity", pill.getQuantity());
        values.put("whencol", pill.getWhen());
        SQLiteDatabase db = getWritableDatabase();
        db.insert("pills", null, values);
        db.close();
    }


    //use this to populate the ArrayList in the MainActivity
    public ArrayList<Pills> databaseToArraylist() {

        ArrayList<Pills> myList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        //Cursor points to a location in your results
        Cursor recordSet;
        recordSet = db.rawQuery("select * from pills", null);
        //Move to the first row in your results
        recordSet.moveToFirst();

        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("name")) != null) {
                Pills myPill = new Pills();

                myPill.setName(recordSet.getString(recordSet.getColumnIndex("name")));
                myPill.setQuantity(recordSet.getDouble(recordSet.getColumnIndex("quantity")));
                myPill.setWhen(recordSet.getString(recordSet.getColumnIndex("whencol")));

                myList.add(myPill);

            }
            recordSet.moveToNext();
        }

        db.close();
        recordSet.close();
        if (!myList.isEmpty()){
            return myList;
        }
        else{
            //return something so that it's not null
            return myList;
        }
    }


    public int FetchIdFromNameAndQuantity(String name, double quantity){
        SQLiteDatabase db = getWritableDatabase();
        //Cursor points to a location in your results
        Cursor recordSet;
        recordSet = db.rawQuery("select _id from pills where name=? and quantity=?", new String[]{name, ""+quantity});
        //Move to the first row in your results
        recordSet.moveToFirst();
        int id = recordSet.getInt(recordSet.getColumnIndex("_id"));
        recordSet.close();
        db.close();

        return id;
    }



}