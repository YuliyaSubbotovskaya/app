package com.zamkovenko.utils.model;

import java.util.Date;

/**
 * Author: Yevgeniy Zamkovenko
 * Date: 26.11.2017
 */
public final class Message {

    private String title;

    private Date onDate;

    private short volume;
    private short duration;
    private short repeatingDelay;
    private short id;

    private boolean isVibro;
    private boolean isBlinking;

    private MessageState messageState;

    public String getTitle() {
        return title;
    }

    public Date getOnDate() {
        return onDate;
    }

    public short getVolume() {
        return volume;
    }

    public short getDuration() {
        return duration;
    }

    public short getRepeatingDelay() {
        return repeatingDelay;
    }

    public boolean isVibro() {
        return isVibro;
    }

    @Override
    public String toString() {
        return "Message{" +
                "title='" + title + '\'' +
                ", onDate=" + onDate +
                ", volume=" + volume +
                ", duration=" + duration +
                ", repeatingDelay=" + repeatingDelay +
                ", id=" + id +
                ", isVibro=" + isVibro +
                ", isBlinking=" + isBlinking +
                '}';
    }

    public boolean isBlinking() {
        return isBlinking;
    }

    public MessageBuilder getBuilder() {
        return new MessageBuilder();
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public MessageState getMessageState() {
        return messageState;
    }

    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    public boolean IsDone() {
        return messageState == MessageState.DONE;
    }

    public class MessageBuilder{

        private MessageBuilder() {
        }

        public MessageBuilder setTitle(String title) {
            Message.this.title = title;
            return this;
        }
        public MessageBuilder setOnDate(Date onDate) {
            Message.this.onDate = onDate;
            return this;
        }
        public MessageBuilder setVolume(short volume) {
            Message.this.volume = volume;
            return this;
        }
        public MessageBuilder setDuration(short duration) {
            Message.this.duration = duration;
            return this;
        }
        public MessageBuilder setId(short id) {
            Message.this.id = id;
            return this;
        }
        public MessageBuilder setRepeatingDelay(short repeatingDelay) {
            Message.this.repeatingDelay = repeatingDelay;
            return this;
        }
        public MessageBuilder setIsVibro(boolean isVibro) {
            Message.this.isVibro = isVibro;
            return this;
        }
        public MessageBuilder setIsBlinking(boolean isBlinking) {
            Message.this.isBlinking = isBlinking;
            return this;
        }
        public MessageBuilder setMessageState(MessageState messageState) {
            Message.this.messageState = messageState;
            return this;
        }

        public Message build() {
            return Message.this;
        }
    }

}
