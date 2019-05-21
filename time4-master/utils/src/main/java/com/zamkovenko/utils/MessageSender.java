package com.zamkovenko.utils;

import com.zamkovenko.utils.model.Message;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 17.12.2017
 */

public interface MessageSender {

    void sendMessage(Message message);

}
