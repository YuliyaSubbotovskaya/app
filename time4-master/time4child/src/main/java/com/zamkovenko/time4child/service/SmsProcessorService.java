package com.zamkovenko.time4child.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.zamkovenko.time4child.R;
import com.zamkovenko.time4child.activity.ConfirmActivity;
import com.zamkovenko.time4child.activity.MainActivity;
import com.zamkovenko.utils.SmsModel;
import com.zamkovenko.time4child.receiver.NotificationReceiver;
import com.zamkovenko.utils.model.Message;
import com.zamkovenko.utils.model.serializer.MessageSerializer;
import com.zamkovenko.utils.model.serializer.SmsMessageSerializer;

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

        proccessMessageToDb(message);
        proccessMessageToAlarm(message);

        stopSelf();

        return START_STICKY;
    }

    private void proccessMessageToDb(Message message) {
        messages.add(message);
    }

    private void proccessMessageToAlarm(Message message) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent notificationReceiverIntent = new Intent(this, NotificationReceiver.class);
        notificationReceiverIntent.putExtra(ConfirmActivity.PARAM_NOTIFICATION_ID, message.getId());
        notificationReceiverIntent.putExtra(ConfirmActivity.PARAM_NOTIFICATION_TITLE, message.getTitle());

        long triggerAtMillis = message.getOnDate().getTime();
        if (triggerAtMillis < System.currentTimeMillis()) {
            triggerAtMillis = System.currentTimeMillis() + 4000;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationReceiverIntent, 0);
        alarmManager.set(AlarmManager.RTC, triggerAtMillis, pendingIntent);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
