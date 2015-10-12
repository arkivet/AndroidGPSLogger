package com.example.gpslogger;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    Button startButton;
    TextView longitudeField;
    TextView latitudeField;
    TextView altitudeField;
    TextView accuracyField;
    TextView speedField;

    Chronometer chrono;
    long timePassed;

    GPSHandler gpsHandler;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitudeField = (TextView) findViewById(R.id.longitudeField);
        latitudeField = (TextView) findViewById(R.id.latitudeField);
        altitudeField = (TextView) findViewById((R.id.altitudeField));
        accuracyField = (TextView) findViewById(R.id.accuracyField);
        speedField = (TextView) findViewById(R.id.speedField);

        startButton = (Button) findViewById(R.id.button);
        startButton.setOnClickListener(handler);

        chrono = (Chronometer) findViewById(R.id.chronometer);
        timePassed = 0;
        chrono.setText("00:00:00");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }


    View.OnClickListener handler = new View.OnClickListener(){
        int clicked = 0;

        public void onClick(View v){
            TextView loggingStatusText = (TextView) findViewById(R.id.textView);
            Button b1 = (Button) findViewById(R.id.button);

            //Sets the format of the chronometer to HH:MM:SS
            chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
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
                    b1.setText("Stop");
                    clicked++;
                    SystemClock.setCurrentTimeMillis(timePassed);
                    chrono.setBase(SystemClock.elapsedRealtime() - timePassed);
                    chrono.start();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new MyLocationListener());
                    break;
                case 1:
                    loggingStatusText.setText("Logging Off");
                    b1.setText("Start");
                    clicked--;
                    chrono.stop();
                    timePassed = SystemClock.elapsedRealtime() - chrono.getBase();
                    break;
            }
        }

    };

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) {
            longitudeField.setText(""+location.getLongitude()+"");
            latitudeField.setText(""+location.getLatitude()+"");
            altitudeField.setText(""+location.getAltitude()+"");
            accuracyField.setText(""+location.getAccuracy()+"");
            speedField.setText(""+location.getSpeed()+"");
            gpsHandler.addData(location.getLongitude(), location.getLatitude(),location.getAltitude(),
                    location.getAccuracy(), location.getSpeed());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(),"GPS Status Changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            startButton.setEnabled(true);
            Toast.makeText(getApplicationContext(),"GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            startButton.setEnabled(false);
            Toast.makeText(getApplicationContext(),"GPS Disabled", Toast.LENGTH_SHORT).show();
        }
    }
}
