package com.example.gpslogger;

import android.support.v7.app.AppCompatActivity;

public class GPSHandler extends AppCompatActivity{
    Queue queue;
    GPSData data;
    public void addData(double longitude, double latitude, double altitude, float accuracy, float speed){
        data = new GPSData(longitude, latitude, altitude, accuracy, speed);
        queue.enQueue(data);
    }
}
