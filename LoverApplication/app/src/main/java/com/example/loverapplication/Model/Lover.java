package com.example.loverapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Lover implements Parcelable {
    String _id, name, age, height, weight, phone, about, image;
    LoverType type;
    User loverOf;

    protected Lover(Parcel in) {
        _id = in.readString();
        name = in.readString();
        age = in.readString();
        height = in.readString();
        weight = in.readString();
        phone = in.readString();
        about = in.readString();
        image = in.readString();
        type = in.readParcelable(LoverType.class.getClassLoader());
        loverOf = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Lover> CREATOR = new Creator<Lover>() {
        @Override
        public Lover createFromParcel(Parcel in) {
            return new Lover(in);
        }

        @Override
        public Lover[] newArray(int size) {
            return new Lover[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(name);
        parcel.writeString(age);
        parcel.writeString(height);
        parcel.writeString(weight);
        parcel.writeString(phone);
        parcel.writeString(about);
        parcel.writeString(image);
        parcel.writeParcelable((Parcelable) type, i);
        parcel.writeParcelable((Parcelable) loverOf, i);
    }

    public Lover(String _id, String name, String age, String height, String weight, String phone, String about, String image, LoverType type, User loverOf) {
        this._id = _id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.phone = phone;
        this.about = about;
        this.image = image;
        this.type = type;
        this.loverOf = loverOf;
    }

    public Lover(String name, String age, String height, String weight, String phone, String about, String image, LoverType type, User loverOf) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.phone = phone;
        this.about = about;
        this.image = image;
        this.type = type;
        this.loverOf = loverOf;
    }

    public Lover() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LoverType getType() {
        return type;
    }

    public void setType(LoverType type) {
        this.type = type;
    }

    public User getLoverOf() {
        return loverOf;
    }

    public void setLoverOf(User loverOf) {
        this.loverOf = loverOf;
    }
}
