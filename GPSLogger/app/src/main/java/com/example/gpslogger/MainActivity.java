package com.example.gpslogger;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>
{
    private Button startStopButton;
    private Button resetButton;
    private Chronometer chrono;
    private long timePassed;
    private GPSHandler gpsHandler;
    private LocationManager locationManager;
    private SessionIDManager sessionIDManager;
    private NotificationHandler notificationHandler;
    private ActivityRecognitionBroadcastReceiver activityRecognitionBroadcastReceiver;
    private GoogleApiClient googleApiClient;
    private boolean gpsEnabled;
    private int sessionID;
    private int clicked;
    private String activity;
    private int confidence;
    private String time;
    private DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        activity = "nothing";
        confidence = -1;
        clicked = 0;
        gpsHandler = new GPSHandler();
        sessionIDManager = new SessionIDManager();
        activityRecognitionBroadcastReceiver = new ActivityRecognitionBroadcastReceiver();

        try {
            sessionID = sessionIDManager.generateSessionID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gpsHandler.setClicked(clicked);

        startStopButton = (Button) findViewById(R.id.start_stop_button);
        startStopButton.setOnClickListener(handler);
        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setEnabled(false);

        chrono = (Chronometer) findViewById(R.id.chronometer);
        timePassed = 0;
        chrono.setText("00:00:00");

        notificationHandler = new NotificationHandler(getApplicationContext(), 1);
        notificationHandler.createNotification();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //noinspection ResourceType
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new MyLocationListener(gpsHandler));
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gpsEnabled){
            startStopButton.setEnabled(true);
        } else {
            startStopButton.setEnabled(false);
            enableGPS(false);
        }



        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    public void enableGPS(final boolean gpsEnabled){
        if(!gpsEnabled){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Activate GPS in settings");
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    enableGPS(false);
                }
            });
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
                }
            });
            dialog.show();
        }
    }

    private View.OnClickListener handler = new View.OnClickListener(){

        public void onClick(View v){
            TextView loggingStatusText = (TextView) findViewById(R.id.loggingStatusTextView);

            //Sets the format of the chronometer to HH:MM:SS
            chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()
            {
                @Override
                public void onChronometerTick(Chronometer chronoArg) {
                    long time = SystemClock.elapsedRealtime() - chronoArg.getBase();
                    int h = (int) (time / 3600000);
                    int m = (int) (time - h * 3600000) / 60000;
                    int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                    String hh = h < 10 ? "0" + h : h + "";
                    String mm = m < 10 ? "0" + m : m + "";
                    String ss = s < 10 ? "0" + s : s + "";
                    chronoArg.setText(hh + ":" + mm + ":" + ss);
                }
            });

            switch(clicked){
                case 0:
                    loggingStatusText.setText("Logging On");
                    startStopButton.setText("Stop");
                    resetButton.setEnabled(false);
                    notificationHandler = new NotificationHandler(getApplicationContext(), clicked);
                    notificationHandler.createNotification();
                    gpsHandler.setClicked(clicked);
                    clicked++;
                    SystemClock.setCurrentTimeMillis(timePassed);
                    chrono.setBase(SystemClock.elapsedRealtime() - timePassed);
                    chrono.start();

                    break;
                case 1:
                    loggingStatusText.setText("Logging Off");
                    startStopButton.setText("Start");
                    resetButton.setEnabled(true);
                    notificationHandler = new NotificationHandler(getApplicationContext(), clicked);
                    notificationHandler.createNotification();
                    gpsHandler.setClicked(clicked);
                    clicked--;
                    chrono.stop();
                    timePassed = SystemClock.elapsedRealtime() - chrono.getBase();
                    break;
            }
        }
    };

    public void resetApp(View v) throws Exception {
        TextView loggingStatusText = (TextView) findViewById(R.id.loggingStatusTextView);
        loggingStatusText.setText("Logging Off");
        startStopButton.setText("Start");
        resetButton.setEnabled(false);
        chrono.stop();
        clicked = 0;
        timePassed = 0;
        SystemClock.setCurrentTimeMillis(timePassed);
        chrono.setBase(SystemClock.elapsedRealtime() - timePassed);
        chrono.setText("00:00:00");
        sessionID = sessionIDManager.generateSessionID();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            notificationHandler = new NotificationHandler(getApplicationContext(), 2);
            notificationHandler.createNotification();
            if(googleApiClient.isConnected()) {
                removeActivityUpdates(getCurrentFocus());
            }
            finish();
            System.exit(0);
            return true;
        } else if(id == R.id.action_settings) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(activityRecognitionBroadcastReceiver, new IntentFilter("com.example.gpslogger.ACTIVITY_ACTION"));
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void requestActivityUpdates(View view) {
        if (!googleApiClient.isConnected()) {
            Toast.makeText(this, "GoogleApiClient not yet connected", Toast.LENGTH_SHORT).show();
        } else {
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, 0, getActivityDetectionPendingIntent()).setResultCallback(this);
        }
    }

    public void removeActivityUpdates(View view) {
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(googleApiClient, getActivityDetectionPendingIntent()).setResultCallback(this);
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, ActivityRecognitionIntentService.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(Status status) {

    }

    public class ActivityRecognitionBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("com.example.gpslogger.ACTIVITY_ACTION")) {
                Bundle extras = intent.getExtras();
                if(extras != null){
                    activity = extras.getString("Activity");
                    confidence = extras.getInt("Confidence");
                }
            }
        }
    }

    private class MyLocationListener implements LocationListener
    {
        private GPSHandler gpsHandler;

        public MyLocationListener(GPSHandler gpsHandler){
            this.gpsHandler = gpsHandler;
        }
        @Override
        public void onLocationChanged(Location location) {
            requestActivityUpdates(getCurrentFocus());
            time = dateFormat.format(new Date());
            if(gpsHandler.getClicked() == 0) {
                gpsHandler.addData(location.getLongitude(), location.getLatitude(), location.getAltitude(),
                        location.getAccuracy(), location.getSpeed(), activity, confidence, sessionID, time);
                new NetworkConnection(getApplicationContext(), gpsHandler).execute();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(),"GPS Status Changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            startStopButton.setEnabled(true);
            Toast.makeText(getApplicationContext(),"GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            startStopButton.setEnabled(false);
            Toast.makeText(getApplicationContext(),"GPS Disabled", Toast.LENGTH_SHORT).show();
        }
    }
}