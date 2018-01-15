package com.arvid.dtuguide;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jeppe on 12-01-2018.
 */

public class RecViewHolder extends RecyclerView.ViewHolder {

    public TextView tagText;

    public RecViewHolder(View itemView, int layout){
        super(itemView);

        tagText = (TextView)itemView.findViewById(layout);
    }
}
