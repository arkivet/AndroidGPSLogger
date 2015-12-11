package com.example.gpslogger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkConnection extends AsyncTask<Void, Void, Void>
{
    private GPSHandler gpsHandler;
    private Context context;
    private Socket socket;
    private OutputStream dataOut;
    private GPSData data;
    private String dataString;

    public NetworkConnection(Context context, GPSHandler gpsHandler){
        this.context = context;
        this.gpsHandler = gpsHandler;
    }

    public boolean hasConnection(){
        boolean hasConnection = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnected()){
            hasConnection = true;
        }
        return hasConnection;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(hasConnection()) {
            try {
                data = gpsHandler.getGPSData();
                socket = new Socket("46.101.51.19", 8899);
                System.out.println("Connected to server");
                dataString = gpsHandler.getJSONDataString(data);
                System.out.println(dataString);
                dataOut = new DataOutputStream(socket.getOutputStream());
                dataOut.write(dataString.getBytes());
                dataOut.flush();
                socket.close();
                System.out.println("Socket sent!");
            } catch (IOException e) {
                e.printStackTrace();
                gpsHandler.addData(data);
            }
        } else{
            System.out.println("No active connection");
        }
        return null;
    }
}