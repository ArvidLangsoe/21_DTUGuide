package com.arvid.dtuguide;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jeppe on 25-11-2017.
 */

public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.arvid.dtuguide.SearchSuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;
    public static final Uri URI = Uri.parse("content://" + SearchSuggestionProvider.AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY);
    public String[] customSearchResults;

    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);

        customSearchResults = new String[] { "X.101" };
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final Cursor recentsCursor = super.query(uri, projection, selection, selectionArgs, sortOrder);
        final Cursor customResultsCursor = queryCache(recentsCursor, selectionArgs[0]);
        return new MergeCursor(new Cursor[]{recentsCursor, customResultsCursor});
    }

    private Cursor queryCache(Cursor recentsCursor, String userQuery) {
        final MatrixCursor arrayCursor = new MatrixCursor(recentsCursor.getColumnNames());

        final int formatColumnIndex = recentsCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_FORMAT);
        final int iconColumnIndex = recentsCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1);
        final int textColumnIndex = recentsCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
        final int queryColumnIndex = recentsCursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_QUERY);
        final int idIndex = recentsCursor.getColumnIndex("_id");

        final int columnCount = recentsCursor.getColumnCount();

        // Populate data here
        int startId = Integer.MAX_VALUE;

        for (String customSearchResult : customSearchResults) {
            final Object[] newRow = new Object[columnCount];
            if (formatColumnIndex >= 0) newRow[formatColumnIndex] = 0;
            if (iconColumnIndex >= 0) newRow[iconColumnIndex] = null;
            if (textColumnIndex >= 0) newRow[textColumnIndex] = customSearchResult;
            if (queryColumnIndex >= 0) newRow[queryColumnIndex] = customSearchResult;
            newRow[idIndex] = startId--;
            arrayCursor.addRow(newRow);
        }

        return arrayCursor;
    }
}
