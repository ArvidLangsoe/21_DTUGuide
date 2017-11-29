package com.arvid.dtuguide;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Jeppe on 25-11-2017.
 */

public class RecentSearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.arvid.dtuguide.RecentSearchSuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY);

    public RecentSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
