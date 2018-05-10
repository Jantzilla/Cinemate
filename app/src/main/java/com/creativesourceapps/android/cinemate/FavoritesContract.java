package com.creativesourceapps.android.cinemate;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 A contract class is a public final class that contains constant definitions for the URIs,
 column names, MIME types, and other meta-data that related to the provider.
 The class establishes a contract between the provider and other applications by ensuring that the provider
 can be correctly accessed even if there are changes to the actual values of URIs, column names, and so forth.
 */

class FavoritesContract {
    public static final int FAVORITE = 10;
    public static final int FAVORITE_ID = 20;

    public static final String FAVORITE_TABLE_NAME = "favorite";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIE_NAME = "name";
    public static final String COLUMN_MOVIE_ID = "movieId";
    public static final String COLUMN_MOVIE_IMAGE = "image";

    //Authority is unique string for the app.
    public static final String AUTHORITY = "com.creativesourceapps.android.cinemate";

    public static final Uri FAVORITE_URI = Uri.parse("content://" + AUTHORITY + "/" + FAVORITE_TABLE_NAME);


    private static final String CREATE_TABLE = "CREATE TABLE "
            + FAVORITE_TABLE_NAME
            + " (" + COLUMN_ID
            + " INTEGER primary key autoincrement, "
            + COLUMN_MOVIE_NAME + " TEXT not null, "
            + COLUMN_MOVIE_ID + " TEXT, "
            + COLUMN_MOVIE_IMAGE + " TEXT)";

    public static void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}