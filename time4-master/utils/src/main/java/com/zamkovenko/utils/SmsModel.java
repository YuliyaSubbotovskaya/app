package com.zamkovenko.utils;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 10.12.2017
 */

public final class SmsModel {

    private String from;
    private String body;

    public SmsModel(String from, String body) {
        this.from = from;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }
}
