package com.example.gpslogger;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService {
    private static String ACTIVITY_ACTION = "com.example.gpslogger.ACTIVITY_ACTION";

    public ActivityRecognitionIntentService() {
        super("ActivityRecognitionIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();
            int confidence = mostProbableActivity.getConfidence();
            String mostProbableActivityName = getActivityName(mostProbableActivity.getType());

            Intent activityResultIntent = new Intent(ACTIVITY_ACTION);
            activityResultIntent.putExtra("Activity", mostProbableActivityName);
            activityResultIntent.putExtra("Confidence", confidence);
            LocalBroadcastManager.getInstance(this).sendBroadcast(activityResultIntent);
        }
    }

    private String getActivityName(int activityType) {
        String activityString = "nothing";
        switch(activityType){
            case DetectedActivity.IN_VEHICLE:
                activityString = "in_vehicle";
                break;
            case DetectedActivity.ON_BICYCLE:
                activityString = "on_bicycle";
                break;
            case DetectedActivity.ON_FOOT:
                activityString = "on_foot";
                break;
            case DetectedActivity.RUNNING:
                activityString = "running";
                break;
            case DetectedActivity.WALKING:
                activityString = "walking";
                break;
            case DetectedActivity.TILTING:
                activityString = "tilting";
                break;
            case DetectedActivity.UNKNOWN:
                activityString = "unknown";
        }
        return activityString;
    }
}
