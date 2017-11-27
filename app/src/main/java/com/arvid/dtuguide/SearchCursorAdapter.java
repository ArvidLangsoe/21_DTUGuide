package com.arvid.dtuguide;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jeppe on 27-11-2017.
 */

public class SearchCursorAdapter extends CursorAdapter {

    private int layout;

    public SearchCursorAdapter(Context context, Cursor cursor, int layout, int flags) {
        super(context, cursor, flags);
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
