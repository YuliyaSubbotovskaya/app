package com.zamkovenko.time4child;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.zamkovenko.time4child.activity.EnterParentPhoneActivity;
import com.zamkovenko.time4child.utils.SmsUtils;
import com.zamkovenko.utils.model.Message;
import com.zamkovenko.utils.model.serializer.MessageSerializer;
import com.zamkovenko.utils.model.serializer.SmsMessageSerializer;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 09.01.2018
 */

public class SendMessageTask extends AsyncTask<Message, Void, Void> {

    private Context m_context;

    public SendMessageTask(Context context) {
        this.m_context = context;
    }

    @Override
    protected Void doInBackground(Message... messages) {

        Message message = messages[0];

        Log.d(SendMessageTask.class.getSimpleName(), "Start to send message: " + message);

        MessageSerializer<String> messageSerializer = new SmsMessageSerializer();
//        SocketClientManager.getInstance().sendMessage(messageSerializer.serialize(message));

        if (ContextCompat.checkSelfPermission(m_context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(SendMessageTask.class.getSimpleName(),"Need SEND_SMS permission");
            return null;
        }
        else{
            Log.d(SendMessageTask.class.getSimpleName(),"SEND_SMS permission is OK");
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(m_context);
        String parentPhone = prefs.getString(EnterParentPhoneActivity.PARAM_PARENT_PHONE, "");
        SmsUtils.sendSMS(parentPhone, messageSerializer.serialize(message), m_context);

        return null;
    }
}
