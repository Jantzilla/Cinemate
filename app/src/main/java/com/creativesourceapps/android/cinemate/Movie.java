package com.creativesourceapps.android.cinemate;

import com.squareup.picasso.RequestCreator;

public class Movie {
    String versionName;
    String versionNumber;
    RequestCreator image; // drawable reference id

    public Movie(String vName, String vNumber, RequestCreator image)
    {
        this.versionName = vName;
        this.versionNumber = vNumber;
        this.image = image;
    }

}