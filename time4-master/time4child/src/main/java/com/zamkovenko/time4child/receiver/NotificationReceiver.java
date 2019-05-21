package com.zamkovenko.time4child.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zamkovenko.time4child.activity.ConfirmActivity;
import com.zamkovenko.time4child.service.SmsProcessorService;
import com.zamkovenko.utils.model.Message;

import java.util.HashSet;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 10.12.2017
 */

/**
 * Should be call when it's need to alarm user about task
 */
public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.zamkovenko.time4.notification";

    public static final String PARAM_DB_NAME = "notifictaion_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        short id = intent.getExtras().getShort(ConfirmActivity.PARAM_NOTIFICATION_ID, (short) -1);

        HashSet<Message> messages = SmsProcessorService.messages;

        for (Message message : messages) {

            if (message.getId() == id) {

                Intent confirmIntent = new Intent(context, ConfirmActivity.class);
                confirmIntent.putExtra(ConfirmActivity.PARAM_NOTIFICATION_ID, id);
                confirmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(confirmIntent);
            }
        }
    }
}
