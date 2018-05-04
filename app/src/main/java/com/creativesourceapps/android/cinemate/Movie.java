package com.creativesourceapps.android.cinemate;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.picasso.RequestCreator;

public class Movie implements Parcelable {
    String versionName;
    String versionNumber;
    int id;
    RequestCreator image;

    public Movie(String vName, String vNumber, int id, RequestCreator image)
    {
        this.versionName = vName;
        this.versionNumber = vNumber;
        this.id = id;
        this.image = image;
    }

    protected Movie(Parcel in) {
        versionName = in.readString();
        versionNumber = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(versionName);
        dest.writeString(versionNumber);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}