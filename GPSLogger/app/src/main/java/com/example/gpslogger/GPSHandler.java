package com.example.gpslogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GPSHandler extends AppCompatActivity{
    private List<GPSData> dataList = new ArrayList<GPSData>();
    private GPSData data;
    private int clicked;

    public void GPSHandler(){
        data = new GPSData();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addData(double longitude, double latitude, double altitude, float accuracy,
            float speed, String activity, int confidence, int sessionID, String time){
        data = new GPSData(longitude, latitude, altitude, accuracy, speed, activity, confidence, sessionID, time);
        dataList.add(data);
    }

    public void addData(GPSData dataObject){
        dataList.add(dataObject);
    }

    public void setClicked(int clicked){
        this.clicked = clicked;
    }

    public int getClicked() {
        return this.clicked;
    }

    public boolean listIsEmpty() {
        return dataList.isEmpty();
    }

    public GPSData getGPSData() {
        GPSData tempData = null;
        if(!dataList.isEmpty()) {
            tempData = dataList.get(0);
            dataList.remove(0);
        }
        return tempData;
    }

    public String getJSONDataString(GPSData data){
        String returnData;
        returnData = data.toJSON();
        System.out.println(dataList.size());
        return returnData;
    }
}





