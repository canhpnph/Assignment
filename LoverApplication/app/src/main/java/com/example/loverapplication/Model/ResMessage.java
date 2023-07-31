package com.example.loverapplication.Model;

public class ResMessage {
    public int code;
    public String message;

    public ResMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResMessage() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
