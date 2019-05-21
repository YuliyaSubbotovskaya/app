package com.zamkovenko.utils;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 31.12.2017
 */

public abstract class MessageReceiver implements OnMessageRecievedListener {

    public abstract void processMessage(String message);

    @Override
    public void OnMessageReceive(String message) {
        processMessage(message);
    }
}
