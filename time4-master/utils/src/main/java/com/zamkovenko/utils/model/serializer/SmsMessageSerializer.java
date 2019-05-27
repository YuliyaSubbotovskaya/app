package com.zamkovenko.utils.model.serializer;

import com.zamkovenko.utils.model.Message;
import com.zamkovenko.utils.model.MessageState;

import java.util.Date;

/**
 * Author: Yevgeniy Zamkovenko
 * Date: 26.11.2017
 */
public class SmsMessageSerializer implements MessageSerializer<String> {

    @Override
    public String serialize(Message message) {
        StringBuilder stringMessage = new StringBuilder();

        long time;
        if (message.getOnDate() != null) {
            time = message.getOnDate().getTime();
        } else {
            time = 0;
        }
        stringMessage.append(time);
        stringMessage.append(".");

        stringMessage.append(message.getVolume());
        stringMessage.append(".");
        stringMessage.append(message.getDuration());
        stringMessage.append(".");
        stringMessage.append(message.getRepeatingDelay());
        stringMessage.append(".");
        stringMessage.append(message.getId());
        stringMessage.append(".");

        stringMessage.append(message.isVibro() ? 1 : 0);
        stringMessage.append(".");
        stringMessage.append(message.isBlinking() ? 1 : 0);
        stringMessage.append(".");
        stringMessage.append(message.getMessageState() == MessageState.DONE ? 1 : 0);
        stringMessage.append(".");

        stringMessage.append(message.getTitle());
        stringMessage.append(".");

        return stringMessage.toString();
    }

    @Override
    public Message deserizlize(String stringMessage) {

        String[] parts = stringMessage.split("\\.");

        assert (parts.length > INDICES.COUNT);

        return new Message().getBuilder()
                .setOnDate(new Date(Long.valueOf(parts[INDICES.DATE])))
                .setDuration(Short.valueOf(parts[INDICES.DURATION]))
                .setVolume(Short.valueOf(parts[INDICES.VOLUME]))
                .setRepeatingDelay(Short.valueOf(parts[INDICES.REPEATING_DELAY]))
                .setId(Short.valueOf(parts[INDICES.ID]))
                .setIsVibro(parts[INDICES.IS_VIBRO].equals("1"))
                .setIsBlinking(parts[INDICES.IS_BLINKING].equals("1"))
                .setMessageState(parts[INDICES.IS_DONE].equals("1") ? MessageState.DONE : MessageState.NOT_DONE)
                .setTitle(String.valueOf(parts[INDICES.TITLE]))
                .build();
    }
}

interface INDICES {
    int DATE = 0;
    int VOLUME = DATE + 1;
    int DURATION = VOLUME + 1;
    int REPEATING_DELAY = DURATION + 1;
    int ID = REPEATING_DELAY + 1;
    int IS_VIBRO = ID + 1;
    int IS_BLINKING = IS_VIBRO + 1;
    int IS_DONE = IS_BLINKING + 1;
    int TITLE = IS_DONE + 1;

    int COUNT = IS_BLINKING + 1;
}