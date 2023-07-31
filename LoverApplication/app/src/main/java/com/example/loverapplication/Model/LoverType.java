package com.example.loverapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class LoverType implements Parcelable {
    String _id, name, description;

    protected LoverType(Parcel in) {
        _id = in.readString();
        name = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoverType> CREATOR = new Creator<LoverType>() {
        @Override
        public LoverType createFromParcel(Parcel in) {
            return new LoverType(in);
        }

        @Override
        public LoverType[] newArray(int size) {
            return new LoverType[size];
        }
    };

    public LoverType(String _id, String name, String description) {
        this._id = _id;
        this.name = name;
        this.description = description;
    }

    public LoverType() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
