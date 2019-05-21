package com.zamkovenko.time4child.network;

import java.io.IOException;
import java.net.Socket;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 23.12.2017
 */

public class Client {

    private Socket socket;

    private static Client client;

    public static Client getClient() {
        if (client == null) {
            createInstance();
        }
        return client;
    }

    private static void createInstance() {
        client = new Client();
    }

    private Client() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    socket = new Socket("192.168.0.102", 6000);
                    socket = new Socket("10.0.2.2", 6000);

                    sendMessage("Hello, world");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        try {
            socket.getOutputStream().write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
