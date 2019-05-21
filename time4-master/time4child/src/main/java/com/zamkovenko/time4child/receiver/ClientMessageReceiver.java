package com.zamkovenko.time4child.receiver;

import android.content.Context;
import android.content.Intent;

import com.zamkovenko.time4child.service.SmsProcessorService;
import com.zamkovenko.utils.Constants;
import com.zamkovenko.utils.MessageReceiver;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 31.12.2017
 */

public class ClientMessageReceiver extends MessageReceiver {

    private Context context;

    public ClientMessageReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void processMessage(String message) {

        Intent smsServiceIntent = new Intent(context, SmsProcessorService.class);
        smsServiceIntent.putExtra(SmsProcessorService.PARAM_FROM, Constants.DEFAULT_PHONE_NUMBER);
        smsServiceIntent.putExtra(SmsProcessorService.PARAM_BODY, message);
        context.startService(smsServiceIntent);

    }
}
