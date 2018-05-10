package com.creativesourceapps.android.cinemate;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 *  Contains ContentProvider class methods.
 */

public class FavoritesContentProvider extends ContentProvider {

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    private final String[] tableColumns = {FavoritesContract.COLUMN_ID, FavoritesContract.COLUMN_MOVIE_NAME, FavoritesContract.COLUMN_MOVIE_ID, FavoritesContract.COLUMN_MOVIE_IMAGE,};

    /*
     * Defines a handle to the database helper object. The MainDatabaseHelper class is defined
     * in a following snippet.
     */
    private MySQLiteOpenHelper favoritesDataBaseHelper;

    // Holds the database object
    private SQLiteDatabase db;

    //add the URIs which is going to used by this provider.
    static {
        matcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.FAVORITE_TABLE_NAME, FavoritesContract.FAVORITE);
        matcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.FAVORITE_TABLE_NAME + "/#", FavoritesContract.FAVORITE_ID);
    }


    @Override
    public boolean onCreate() {
        favoritesDataBaseHelper = new MySQLiteOpenHelper(getContext());
        return true;
    }



    @Nullable
    @Override // query() Must return a Cursor object
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int matchedUriType = matcher.match(uri);

        if (projection != null &&!Arrays.asList(tableColumns).containsAll(Arrays.asList(projection))) {
            throw new IllegalArgumentException("No Column found in Projection.");
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FavoritesContract.FAVORITE_TABLE_NAME);
        switch (matchedUriType) {
            case FavoritesContract.FAVORITE:

                break;

            case FavoritesContract.FAVORITE_ID:
                queryBuilder.appendWhere(FavoritesContract.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        db = favoritesDataBaseHelper.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        db = favoritesDataBaseHelper.getWritableDatabase();

        int matchedUriType = matcher.match(uri);

        long newId;

        switch (matchedUriType) {
            case FavoritesContract.FAVORITE:
                newId = db.insert(FavoritesContract.FAVORITE_TABLE_NAME, null, contentValues);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }


// We cn use this as wellUri.parse(EmployeeContract.EMPLOYEE_URI.toString() + "/" + newId);

        return ContentUris.withAppendedId(uri, newId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowDeleted = 0;

        db = favoritesDataBaseHelper.getWritableDatabase();

                if (selection != null && !selection.isEmpty()) {

                    rowDeleted = db.delete(
                            FavoritesContract.FAVORITE_TABLE_NAME, FavoritesContract.COLUMN_MOVIE_ID + "=" + selection, null);
                }

        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowUpdated;

        db = favoritesDataBaseHelper.getWritableDatabase();

        int matchedUriType = matcher.match(uri);

        switch (matchedUriType) {
            case FavoritesContract.FAVORITE:
                rowUpdated = db.update(FavoritesContract.FAVORITE_TABLE_NAME, contentValues, selection, selectionArgs);
                break;

            case FavoritesContract.FAVORITE_ID:

                String idTobeUpdated = uri.getLastPathSegment();

                if (selection != null && !selection.isEmpty()) {
                    rowUpdated = db.update(
                            FavoritesContract.FAVORITE_TABLE_NAME,
                            contentValues,
                            FavoritesContract.COLUMN_ID + "=" + idTobeUpdated + " and " + selection,
                            selectionArgs);
                } else {
                    rowUpdated = db.update(
                            FavoritesContract.FAVORITE_TABLE_NAME,
                            contentValues,
                            FavoritesContract.COLUMN_ID + "=" + idTobeUpdated,
                            null);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowUpdated;
    }
}