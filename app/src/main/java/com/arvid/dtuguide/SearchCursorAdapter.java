package com.arvid.dtuguide;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.Log;
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

    public SearchCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
        this.context = context;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.textView2);
        //name.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        name.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        //name.setText("-");


        //ImageView image = (ImageView) view.findViewById(R.id.imageView1);
        //image.setImageResource(cursor.getInt(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_ICON_1)));


    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (getFilterQueryProvider() != null) { return getFilterQueryProvider().runQuery(constraint); }

        //Cursor cursorRecent = context.getContentResolver().query(RecentSearchSuggestionProvider.CONTENT_URI, null, null, new String[] { constraint.toString().toLowerCase() }, null);
        //Cursor cursorRooms = context.getContentResolver().query(SearchSuggestionProvider.CONTENT_URI, null, null, new String[] { constraint.toString().toLowerCase() }, null);
        //Cursor mergedCursor = new MergeCursor(new Cursor[] { cursorRecent, cursorRooms });

        //Cursor cursor = context.getContentResolver().query(RecentSearchSuggestionProvider.CONTENT_URI, null,
        //    null, new String[] { constraint.toString().toLowerCase() }, null);
        Cursor cursor = context.getContentResolver().query(Provider.CONTENT_URI, null, null, new String[] { constraint.toString().toLowerCase()}, null);

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
                    int i = 0;
                    for (String s : list) {
                        if (list.get(i)
                                .toString()
                                .toLowerCase()
                                .contains(
                                        constraint.toString().toLowerCase()))
                            founded.add(list.get(i));
                        i++;
                    }

                    result.values = founded;
                    result.count = founded.size();
                } else {
                    synchronized (this) {
                        result.values = list;
                        result.count = list.size();
                    }

                }

                return result;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                //items.clear();
                //items = new ArrayList<String>();
                //items = (ArrayList<String>) results.values;
                //items.addAll((ArrayList<String>) results.values);
                notifyDataSetChanged();

            }

        };
    }
    */
}
