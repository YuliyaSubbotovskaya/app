package com.zamkovenko.time4parent.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zamkovenko.utils.model.Message;
import com.zamkovenko.utils.model.serializer.MessageSerializer;
import com.zamkovenko.utils.model.serializer.SmsMessageSerializer;
import com.zamkovenko.time4parent.manager.MessageManager;
import com.zamkovenko.utils.SmsModel;

import java.util.HashSet;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 10.12.2017
 */

public class SmsProcessorService extends Service {

    /**
     * clean:
     * Message{title='Test Title', onDate=Sun Dec 10 18:52:12 EET 2017, volume=3, duration=1000, repeatingDelay=5, id=676, isVibro=false, isBlinking=false}
     * <p>
     * serialized: 1512924732305.3.1000.5.676.0.0.
     */

    public static HashSet<Message> messages = new HashSet<>();

    public static final String PARAM_FROM = "sms_from";
    public static final String PARAM_BODY = "sms_body";

    public static final String DATABASE_NAME = "time4db";

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String from = intent.getExtras().getString(PARAM_FROM);
        String body = intent.getExtras().getString(PARAM_BODY);

        SmsModel sms = new SmsModel(from, body);

        MessageSerializer<String> messageSerializer = new SmsMessageSerializer();
        Message message = messageSerializer.deserizlize(sms.getBody());

        MessageManager.getInstance().markMessageDone(message);

        stopSelf();

        return START_STICKY;
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

}
