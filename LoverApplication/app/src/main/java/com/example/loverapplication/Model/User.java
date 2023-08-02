package com.example.loverapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String _id, username, fullname, date, phone, password, image, token, tokenFCM, new_password;

    protected User(Parcel in) {
        _id = in.readString();
        username = in.readString();
        fullname = in.readString();
        date = in.readString();
        phone = in.readString();
        password = in.readString();
        image = in.readString();
        token = in.readString();
        tokenFCM = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(username);
        dest.writeString(fullname);
        dest.writeString(date);
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeString(image);
        dest.writeString(token);
        dest.writeString(tokenFCM);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(String _id, String username, String fullname, String date, String phone, String password, String image, String token, String tokenFCM) {
        this._id = _id;
        this.username = username;
        this.fullname = fullname;
        this.date = date;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.token = token;
        this.tokenFCM = tokenFCM;
    }

    public User() {
    }

    public User(String username, String phone, String password, String image) {
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.image = image;
    }

    public User(String username, String fullname, String date, String phone, String password, String image) {
        this.username = username;
        this.fullname = fullname;
        this.date = date;
        this.phone = phone;
        this.password = password;
        this.image = image;
    }

    public User( String username, String password, String tokenFCM) {
        this.username = username;
        this.password = password;
        this.tokenFCM = tokenFCM;
    }

    public User(String password, String new_password) {
        this.password = password;
        this.new_password = new_password;
    }



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenFCM() {
        return tokenFCM;
    }

    public void setTokenFCM(String tokenFCM) {
        this.tokenFCM = tokenFCM;
    }
}
