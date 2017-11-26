package com.arvid.dtuguide;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by Jeppe on 25-11-2017.
 */

public class SearchSuggestionProvider extends ContentProvider {
    public static final String AUTHORITY = "com.arvid.dtuguide.SearchSuggestionProvider";
    public static final Uri ROOMS_URI = Uri.parse("content://" + AUTHORITY + "/rooms");

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static HashMap<String, String> ROOMS_PROJECTION_MAP;

    private static final int CODE_ROOMS_DIR = 1;
    private static final int CODE_ROOMS_ITEM = 2;

    static {
        MATCHER.addURI(AUTHORITY, "rooms", CODE_ROOMS_DIR);
        MATCHER.addURI(AUTHORITY, "rooms/#", CODE_ROOMS_ITEM);
    }

    /* Database variables */
    private MyDatabaseHelper databaseHelper;
    private static final String DBNAME = "places";
    private SQLiteDatabase db;
    private static final String ROOMS_TABLE_NAME = "rooms";

    @Override
    public boolean onCreate() {
        databaseHelper = new MyDatabaseHelper(
                getContext(),
                DBNAME
        );
        databaseHelper.addRoom("X.101");
        databaseHelper.addRoom("X.102");
        databaseHelper.addRoom("V.101");
        databaseHelper.addRoom("V.102");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ROOMS_TABLE_NAME);

        switch (MATCHER.match(uri)) {
            case CODE_ROOMS_DIR:
                //qb.setProjectionMap(ROOMS_PROJECTION_MAP);
                break;
            case CODE_ROOMS_ITEM:
                //qb.appendWhere("_id=" + uri.getPathSegments().get(1));
                break;
            default:
                // error handling
                break;
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = "name";
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

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
