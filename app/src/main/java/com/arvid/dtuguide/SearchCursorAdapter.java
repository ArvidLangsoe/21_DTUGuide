package com.arvid.dtuguide;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 23-11-2017.
 */

public class SearchCursorAdapter extends CursorAdapter implements Filterable {

    private ArrayList<String> items;
    private ArrayList<String> orig;

    public SearchCursorAdapter(Context context, Cursor cursor, ArrayList<String> items) {
        super(context, cursor, false);
        this.items = new ArrayList<String>();
        this.items = items;
        orig = new ArrayList<String>();
        orig.addAll(items);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //return LayoutInflater.from(context).inflate(R.layout.searchview_suggestions_item, parent, false);
        //ViewHolder holder = new ViewHolder();
        //View v = null;
        //LayoutInflater inflater = (LayoutInflater) context
        //      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //v = inflater.inflate(R.layout.searchview_suggestions_item, parent, false);
        //holder.textView = (TextView) v.findViewById(R.id.textView2);

        //v.setTag(holder);
        return LayoutInflater.from(context).inflate(R.layout.searchview_suggestions_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.textView2);

        //String text = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        //tv.setText(text);
        //ViewHolder holder = (ViewHolder) view.getTag();
        //holder.textView.setText(items.get(cursor.getPosition()));
        String text = items.get(cursor.getPosition());
        tv.setText(text);
    }

    public class ViewHolder {
        public TextView textView;
    }

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
}
