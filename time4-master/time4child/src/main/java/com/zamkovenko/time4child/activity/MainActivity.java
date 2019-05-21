package com.zamkovenko.time4child.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.zamkovenko.time4child.R;
import com.zamkovenko.time4child.receiver.NotificationReceiver;
import com.zamkovenko.time4child.utils.SimUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupLog();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        RequestPermission();

        receiver = new NotificationReceiver();

        registerReceiver(receiver, new IntentFilter(NotificationReceiver.ACTION));

        CheckForSimSettings();

        CheckForParentNumber();

        finish();
    }

    private void CheckForSimSettings() {
        if (!SimUtils.IsSimChosen(this)) {
            Intent chooseSimIntent = new Intent(getApplicationContext(), ChooseSimCardActivity.class);
            startActivity(chooseSimIntent);
        }
    }

    private void CheckForParentNumber() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String parentPhone = prefs.getString(EnterParentPhoneActivity.PARAM_PARENT_PHONE, "");
        if (parentPhone.equals("")) {
            Intent parentPhoneIntent = new Intent(this, EnterParentPhoneActivity.class);
            startActivity(parentPhoneIntent);
        }
    }

    private void RequestPermission() {
        // Request the permission immediately here for the first time run
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission_group.SMS,
                    Manifest.permission_group.STORAGE,
                    Manifest.permission.READ_PHONE_STATE,

                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.BROADCAST_SMS,
                    Manifest.permission.RECEIVE_SMS,
            }, 0);
        }
    }

    private void SetupLog() {
        if ( isExternalStorageWritable() ) {
            File logFile = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                logFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
                        + String.valueOf(new Date(System.currentTimeMillis())).replace(" ", "_") + "_log.txt");
            }

            Log.d(getClass().getSimpleName(),("LOGGING: " + logFile));

            // clear the previous logcat and then write the new one to the file
            try {
                Runtime.getRuntime().exec( "logcat -c");
                Runtime.getRuntime().exec( "logcat -f " + logFile);

            } catch ( IOException e ) {

                e.printStackTrace();
            }
        }
    }

    public boolean isExternalStorageWritable() {

        String state = Environment.getExternalStorageState();

        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {

            return true;

        }

        return false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void SetupSmsReceiver(){
        final String myPackageName = getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
                Intent intent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        myPackageName);
                startActivity(intent);
            }
        }
    }
}
