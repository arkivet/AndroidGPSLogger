package com.example.gpslogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class GPSHandler extends AppCompatActivity{
    private Queue queue;
    private GPSData data;
    private int clicked;

    public void GPSHandler(){
        queue = new Queue();
        data = new GPSData();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addData(double longitude, double latitude, double altitude, float accuracy,
            float speed, String activity, int confidence, int sessionID, String time){
        data = new GPSData(longitude, latitude, altitude, accuracy, speed, activity, confidence, sessionID, time);
        //queue.enQueue(data);

    }

    public void setClicked(int clicked){
        this.clicked = clicked;
    }

    public int getClicked() {
        return this.clicked;
    }
    public String getJSONData(){
        return data.toJSON();
    }
}

/*
Idé för detta är att istället för att spara objekten i en länkad lista i Queue
Så tar jag informationen, konverterar den till JSON och lägger JSON objekten i en arraylist
Listan skickar jag med till nätverksdelen för att kunna extrahera objekten och skicka iväg dem till servern
 */





