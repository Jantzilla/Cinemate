package com.creativesourceapps.android.cinemate;

public class Movie {
    String versionName;
    String versionNumber;
    int image; // drawable reference id

    public Movie(String vName, String vNumber, int image)
    {
        this.versionName = vName;
        this.versionNumber = vNumber;
        this.image = image;
    }

}