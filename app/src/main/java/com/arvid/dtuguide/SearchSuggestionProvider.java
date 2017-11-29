package com.arvid.dtuguide;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Jeppe on 25-11-2017.
 */

public class SearchSuggestionProvider extends ContentProvider {
    public static final String AUTHORITY = "com.arvid.dtuguide.SearchSuggestionProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/rooms");

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ROOMS_CODE = 1;

    private static MatrixCursor matrixCursor;

    static {
        URI_MATCHER.addURI(AUTHORITY, "rooms", ROOMS_CODE);
    }

    /*
    DATABASE STUFF
     */
    private MainDatabaseHelper mOpenHelper;

    // Defines the database name
    private static final String DBNAME = "places";

    // Holds the database object
    private SQLiteDatabase db;

    // A string that defines the SQL statement for creating a table
    private static final String SQL_CREATE_MAIN = "CREATE TABLE " +
            "rooms " +                       // Table's name
            "(" +                           // The columns in the table
            " _id INTEGER PRIMARY KEY, " +
            " name TEXT )";

    @Override
    public boolean onCreate() {
        /*
         * Creates a new helper object. This method always returns quickly.
         * Notice that the database itself isn't created or opened
         * until SQLiteOpenHelper.getWritableDatabase is called
         */
        mOpenHelper = new MainDatabaseHelper(
                getContext()
        );
        db = mOpenHelper.getWritableDatabase();
        mOpenHelper.onUpgrade(db, 1, 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String suggestSelection = null;
        String[] myArgs = null;
        switch (URI_MATCHER.match(uri)) {


            // If the incoming URI was for all of table3
            case ROOMS_CODE:

                if (TextUtils.isEmpty(selectionArgs[0])) {
                    suggestSelection = null;
                    myArgs = null;
                } else {
                    String like = "%" + selectionArgs[0] + "%";
                    myArgs = new String [] { like };
                    suggestSelection = "name LIKE ?";
                }
                break;

            default:
                break;
                // If the URI is not recognized, you should do some error handling here.
        }

        //TODO: Ændre nuværende cursor til en cursor magen til SearchRecentSuggestionsProvider

        // call the code to actually do the query
        return db.query("rooms", projection, suggestSelection, myArgs, null, null, "name COLLATE NOCASE", null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        /*
         * Gets a writeable database. This will trigger its creation if it doesn't already exist.
         *
         */
        //db = mOpenHelper.getReadableDatabase();
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


    /**
     * Helper class that actually creates and manages the provider's underlying data repository.
     */
    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {

        /*
         * Instantiates an open helper for the provider's SQLite data repository
         * Do not do database creation and upgrade here.
         */
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        /*
         * Creates the data repository. This is called when the provider attempts to open the
         * repository and SQLite reports that it doesn't exist.
         */
        public void onCreate(SQLiteDatabase db) {
            // Creates the main table
            db.execSQL(SQL_CREATE_MAIN);
            db.execSQL("INSERT INTO rooms (_id, name) VALUES ('0', 'X.101')");
            db.execSQL("INSERT INTO rooms (_id, name) VALUES ('1', 'V.123')");
            db.execSQL("INSERT INTO rooms (_id, name) VALUES ('2', 'U.438')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + "rooms");

            onCreate(db);
        }
    }
}
