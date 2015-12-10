package com.example.gpslogger;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class SessionIDManager extends AppCompatActivity
{
    private int sessionID = 0;
    private String sessionIDData;
    private static String fileName = "sessionID.txt";
    private static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.gpslogger/files/" + fileName;
    private static File file = new File(filePath);

    public void SessionIDManager(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public int generateSessionID() throws Exception {
        checkIfFileExists();

        FileOutputStream fileOut = new FileOutputStream(file, true);
        OutputStreamWriter outWriter = new OutputStreamWriter(fileOut);
        Scanner fileScanner = new Scanner(file);

        if(!fileScanner.hasNext()){
            sessionID++;
            sessionIDData = Integer.toString(sessionID) + System.getProperty("line.separator");
            outWriter.write(sessionIDData);
            outWriter.close();
        } else {
            while (fileScanner.hasNext()) {
                String line = fileScanner.nextLine();
                sessionID = Integer.parseInt(line);
            }
            sessionID++;
            sessionIDData = Integer.toString(sessionID) + System.getProperty("line.separator");
            outWriter.write(sessionIDData);
        }
        outWriter.close();
        fileOut.close();
        fileScanner.close();
        return sessionID;
    }

    public void checkIfFileExists() throws Exception {
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(!file.exists() && !file.isDirectory()){
            file.createNewFile();
        }
    }
}