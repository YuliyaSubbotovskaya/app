package com.zamkovenko.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

    private OnMessageRecievedListener recievedListener;

    private DataInputStream in;
    private DataOutputStream out;

    private Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;

        try {

            in = new DataInputStream(this.socket.getInputStream());
            out = new DataOutputStream(this.socket.getOutputStream());

            new Thread(new ListeningRunnable()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        new Thread(new SenderRunnable(message)).start();
    }
    
    public void clear() {
        try {
            in.close();
            out.close();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setRecievedListener(OnMessageRecievedListener recievedListener) {
        this.recievedListener = recievedListener;
    }

    class ListeningRunnable implements Runnable {
        @Override
        public void run() {
            try {

                while (true) {
                    String line = in.readUTF();
                    recievedListener.OnMessageReceive(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class SenderRunnable implements Runnable {

        private String message;

        SenderRunnable(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {

                out.writeUTF(message);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
