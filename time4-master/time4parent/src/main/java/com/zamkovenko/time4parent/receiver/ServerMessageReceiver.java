package com.zamkovenko.time4parent.receiver;

import com.zamkovenko.utils.model.Message;
import com.zamkovenko.utils.model.serializer.MessageSerializer;
import com.zamkovenko.utils.model.serializer.SmsMessageSerializer;
import com.zamkovenko.time4parent.manager.MessageManager;
import com.zamkovenko.utils.MessageReceiver;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 31.12.2017
 */

public class ServerMessageReceiver extends MessageReceiver {

    @Override
    public void processMessage(String message) {

        MessageSerializer<String> messageSerializer = new SmsMessageSerializer();
        Message newMessage = messageSerializer.deserizlize(message);

        MessageManager.getInstance().markMessageDone(newMessage);
    }
}
