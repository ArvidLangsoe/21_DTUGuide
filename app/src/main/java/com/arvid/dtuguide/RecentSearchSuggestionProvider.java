package com.arvid.dtuguide;


import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;

/**
 * Created by Jeppe on 23-11-2017.
 */

public class RecentSearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.arvid.dtuguide.RecentSearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }


}
