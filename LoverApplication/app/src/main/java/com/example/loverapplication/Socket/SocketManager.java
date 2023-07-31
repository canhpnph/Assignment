package com.example.loverapplication.Socket;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    public static final SocketManager instance = new SocketManager();

    public static SocketManager getInstance() {
        return instance;
    }

    private Socket mSocket;

    private SocketManager() {
        {
            try {
                mSocket = IO.socket("http://192.168.1.4:3001/");
            } catch (URISyntaxException e) {
            }
        }
    }

    public void connect() {
        mSocket.connect();
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void on(String event, Emitter.Listener fn) {
        mSocket.on(event, fn);
    }

    public void emit(String event, Object... args) {
        mSocket.emit(event, args);
    }

    public void disconnect() {
        mSocket.disconnect();
    }

    public void off(String event, Emitter.Listener fn) {
        mSocket.off(event, fn);
    }

}
