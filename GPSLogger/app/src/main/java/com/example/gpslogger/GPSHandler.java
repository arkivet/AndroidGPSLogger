package com.example.gpslogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class GPSHandler extends AppCompatActivity{
    private ArrayList<GPSData> dataList;
    private GPSData data;
    private int clicked;

    public void GPSHandler(){
        dataList = new ArrayList<GPSData>();
        data = new GPSData();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addData(double longitude, double latitude, double altitude, float accuracy,
            float speed, String activity, int confidence, int sessionID, String time){
        data = new GPSData(longitude, latitude, altitude, accuracy, speed, activity, confidence, sessionID, time);
        dataList.add(data);
    }

    public void setClicked(int clicked){
        this.clicked = clicked;
    }

    public int getClicked() {
        return this.clicked;
    }
    public String getJSONDataString(){
        String returnData = "";
        if(dataList != null){
            returnData = dataList.get(0).toJSON();
        }
        return returnData;
    }
}





