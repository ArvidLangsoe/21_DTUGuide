package com.arvid.dtuguide;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Jeppe on 25-11-2017.
 */

public class SearchSuggestionProvider extends ContentProvider {
    private final static String AUTHORITY = "com.arvid.dtuguide.SearchSuggestionProvider";
    private final static String URL = "content://" + AUTHORITY + "/rooms";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ROOMS = 1;
    private static final int ROOMS_ID = 2;
    private static final int ROOMS_NAME = 3;

    static {
        sUriMatcher.addURI(AUTHORITY, "rooms", ROOMS);
        sUriMatcher.addURI(AUTHORITY, "rooms/#", ROOMS_ID);
        sUriMatcher.addURI(AUTHORITY, "rooms/#", ROOMS_NAME);
    }

    /* Database variables */
    private MyDatabaseHelper databaseHelper;
    private static final String DBNAME = "places";
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        databaseHelper = new MyDatabaseHelper(
                getContext()
        );
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case ROOMS:

                break;
            case ROOMS_ID:
                break;
            case ROOMS_NAME:
                break;
            default:
                // error handling
                break;
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
