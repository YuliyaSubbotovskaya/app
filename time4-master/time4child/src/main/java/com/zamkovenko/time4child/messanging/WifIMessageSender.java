package com.zamkovenko.time4child.messanging;

import com.zamkovenko.time4child.manager.SocketClientManager;
import com.zamkovenko.utils.model.Message;
import com.zamkovenko.utils.model.serializer.MessageSerializer;
import com.zamkovenko.utils.model.serializer.SmsMessageSerializer;
import com.zamkovenko.utils.MessageSender;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 09.01.2018
 */

public class WifIMessageSender implements MessageSender {

    @Override
    public void sendMessage(Message message) {
        MessageSerializer<String> messageSerializer = new SmsMessageSerializer();
        SocketClientManager.getInstance().sendMessage(messageSerializer.serialize(message));
    }
}
