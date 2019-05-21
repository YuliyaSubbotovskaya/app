package com.zamkovenko.time4child.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.zamkovenko.time4child.receiver.ClientMessageReceiver;
import com.zamkovenko.time4child.activity.EnterParentIpActivity;
import com.zamkovenko.utils.Connection;
import com.zamkovenko.utils.Constants;

import java.io.IOException;
import java.net.Socket;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 18.12.2017
 */

public class SocketClientManager implements Runnable {

    @SuppressLint("StaticFieldLeak")
    private static SocketClientManager instance;

    public static SocketClientManager getInstance() {
        return instance;
    }

    private Context context;

    private Connection connection;

    public SocketClientManager(Context context) {
        this.context = context;
        instance = this;
    }

    public void sendMessage(String message) {

        if (connection != null) {
            connection.sendMessage(message);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                if (connection != null) {
                    continue;
                }

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String ip = prefs.getString(EnterParentIpActivity.PARAM_PARENT_IP, "");
                String tempUrl = "10.0.2.2";
//                tempUrl = Constants.LOCALHOST;
//                tempUrl = "localhost";

//                String url = ip.equals("") ? tempUrl : ip;

                Log.d(getClass().getSimpleName(),("url: " + tempUrl + ":" + Constants.SERVER_PORT));

                Socket socket = new Socket(tempUrl, 5554);

                Log.d(getClass().getSimpleName(), ("socket accept"));

                connection = new Connection(socket);
                connection.setRecievedListener(new ClientMessageReceiver(context));
            }

        } catch (IOException e) {
            context.startActivity(new Intent(context, EnterParentIpActivity.class));
            if (connection != null) {
                connection.clear();
            }
        }
    }
}
