package com.example.gpslogger;


import org.json.JSONException;
import org.json.JSONObject;

public class GPSData
{
    private double longitude;
    private double latitude;
    private double altitude;
    private float accuracy;
    private float speed;
    private String activity;
    private int confidence;
    private int sessionID;
    private String time;

    public GPSData(){
        this.longitude = 0.0;
        this.latitude = 0.0;
        this.altitude = 0.0;
        this.accuracy = 0;
        this.speed = 0;
        this.activity = "";
        this.confidence = 0;
        this.sessionID = 0;
        this.time = "";
    }

    public GPSData(double longitude, double latitude, double altitude, float accuracy,
            float speed, String activity, int confidence, int sessionID, String time){
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.speed = speed;
        this.activity = activity;
        this.confidence = confidence;
        this.sessionID = sessionID;
        this.time = time;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setAltitude(double altitude){
        this.altitude = altitude;
    }

    public void setAccuracy(float accuracy){
        this.accuracy = accuracy;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getAltitude(){
        return this.altitude;
    }

    public float getAccuracy(){
        return this.accuracy;
    }

    public float getSpeed(){
        return this.speed;
    }

    public String getActivity() {
        return this.activity;
    }

    public int getConfidence() {
        return this.confidence;
    }

    public int getSessionID(){
        return this.sessionID;
    }

    public String toJSON(){
        String returnObject;
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
            jsonObject.put("altitude", altitude);
            jsonObject.put("accuracy", accuracy);
            jsonObject.put("speed", speed);
            jsonObject.put("activity", activity);
            jsonObject.put("confidence", confidence);
            jsonObject.put("sessionID", sessionID);
            jsonObject.put("time", time);
            jsonObject.getDouble("latitude");
            returnObject = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            returnObject = "Could not create JSON object!";
        }
        return returnObject;
    }
}
