package com.arvid.dtuguide.activity.main;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arvid.dtuguide.R;

/**
 * Created by Jeppe on 27-11-2017.
 */

public class SearchCursorAdapter extends ResourceCursorAdapter {
    private Context context;
    private Cursor cursor;

    public SearchCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
        this.context = context;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTV = (TextView) view.findViewById(R.id.search_item_name);
        TextView typeTV = (TextView) view.findViewById(R.id.search_item_type);
        ImageView iconV = (ImageView) view.findViewById(R.id.search_item_icon);
        ImageView favoriteV = (ImageView) view.findViewById(R.id.search_item_favorite);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(Provider.CURSOR_COLUMNS.NAME.toString()));
        String subText = cursor.getString(cursor.getColumnIndex(Provider.CURSOR_COLUMNS.SUBTEXT.toString()));
        String type = cursor.getString(cursor.getColumnIndex(Provider.CURSOR_COLUMNS.TYPE.toString()));
        boolean recent = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Provider.CURSOR_COLUMNS.RECENT.toString())));
        boolean favorite = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Provider.CURSOR_COLUMNS.FAVORITE.toString())));

        nameTV.setText(name);
        typeTV.setText(subText);

        if(recent) {
            iconV.setImageResource(R.drawable.ic_history_black_24dp);
        }
        else {
            switch (type) {
                case "Person":
                    iconV.setImageResource(R.drawable.ic_perm_identity_black_24dp);
                    break;
                case "Location":
                    iconV.setImageResource(R.drawable.ic_place_black_24dp);
            }
        }
        if(favorite) {
            favoriteV.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        else {
            favoriteV.setImageResource(0);
        }
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider() != null) { return getFilterQueryProvider().runQuery(constraint); }

        cursor = context.getContentResolver().query(Provider.CONTENT_URI, null, null, new String[] { constraint.toString().toLowerCase()}, null);

        return cursor;
    }

    public String getItemName(int position) {
        cursor.moveToPosition(position);
        return cursor.getString(cursor.getColumnIndex("name"));
    }


}
