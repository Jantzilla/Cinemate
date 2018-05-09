package com.creativesourceapps.android.cinemate;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    final int id;
    final String voteAverage;
    final String title;
    final String poster;
    final String overview;
    final String releaseDate;

    public Movie(int id, String voteAverage, String title, String poster, String overview, String releaseDate)
    {
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        voteAverage = in.readString();
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(voteAverage);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}