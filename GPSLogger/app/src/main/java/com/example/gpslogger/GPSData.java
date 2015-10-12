package com.example.gpslogger;


public class GPSData
{
    private double longitude;
    private double latitude;
    private double altitude;
    private float accuracy;
    private float speed;

    public GPSData(){
        this.longitude = 0;
        this.latitude = 0;
        this.altitude = 0.0;
        this.accuracy = 0;
        this.speed = 0;
    }

    public GPSData(double longitude, double latitude, double altitude, float accuracy, float speed){
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.speed = speed;
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
}
