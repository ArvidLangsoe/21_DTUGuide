package com.arvid.dtuguide;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jeppe on 27-11-2017.
 */

public class SearchCursorAdapter extends ResourceCursorAdapter {

    private int layout;

    public SearchCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
        this.layout = layout;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.textView2);
        name.setText(cursor.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1)));

        ImageView image = (ImageView) view.findViewById(R.id.imageView1);
        //image.setImageResource(cursor.getInt(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_ICON_1)));

        if((cursor.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_ICON_1))) == null) {
            image.setImageResource(R.drawable.ic_room_black_24dp);
        }
        else {
            image.setImageResource(R.drawable.ic_history_black_24dp);
        }
    }
}
