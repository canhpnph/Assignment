package com.example.loverapplication.Model;

public class MessageModel {
    String _id, message;
    String username;

    public MessageModel(String _id, String username, String message) {
        this._id = _id;
        this.username = username;
        this.message = message;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(User id_sender) {
        this.username = username;
    }
}
