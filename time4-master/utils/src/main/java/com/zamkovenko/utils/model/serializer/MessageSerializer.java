package com.zamkovenko.utils.model.serializer;

import com.zamkovenko.utils.model.Message;

/**
 * Author: Yevgeniy Zamkovenko
 * Date: 26.11.2017
 */
public interface MessageSerializer<T> {

    T serialize(Message message);

    Message deserizlize(T message);
}
