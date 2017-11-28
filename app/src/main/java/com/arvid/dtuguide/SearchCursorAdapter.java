package com.arvid.dtuguide;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 27-11-2017.
 */

public class SearchCursorAdapter extends ResourceCursorAdapter {

    private Context context;
    //private Cursor cursor;

    public SearchCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
        this.context = context;
        //this.cursor = cursor;
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

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider() != null) { return getFilterQueryProvider().runQuery(constraint); }

        /*
        StringBuilder buffer = null;
        String[] args = null;
        if (constraint != null) {

            buffer = new StringBuilder();
            //buffer.append(query);
            //buffer.append(constraint.toString());
            args = new String[] { constraint.toString().toLowerCase() };


        }
        */
        String[] projection = { "_id", SearchManager.SUGGEST_COLUMN_TEXT_1 };

        //return context.getContentResolver().query(SearchSuggestionProvider.CONTENT_URI, null,
        //        buffer == null ? null : buffer.toString(), args, null);

        Cursor cursor = context.getContentResolver().query(SearchSuggestionProvider.CONTENT_URI, null,
                null, new String[] { constraint.toString().toLowerCase() }, null);

        return cursor;
    }

    /*
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint != null
                        && constraint.toString().length() > 0) {
                    List<String> founded = new ArrayList<String>();
                    for (int i = 0, l = orig.size(); i < l; i++) {
                        if (orig.get(i)
                                .toString()
                                .toLowerCase()
                                .startsWith(
                                        constraint.toString().toLowerCase()))
                            founded.add(orig.get(i));
                    }

                    result.values = founded;
                    result.count = founded.size();
                } else {
                    synchronized (this) {
                        result.values = orig;
                        result.count = orig.size();
                    }

                }
                return result;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                items.clear();
                //items = new ArrayList<String>();
                //items = (ArrayList<String>) results.values;
                items.addAll((ArrayList<String>) results.values);
                notifyDataSetChanged();

            }

        };
    }
    */
}
