package com.arvid.dtuguide;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jeppe on 12-01-2018.
 */

public class RecViewHolder extends RecyclerView.ViewHolder {

    public TextView text;
    public TextView text2;
    public ImageView image;

    public RecViewHolder(View itemView, int tagViewId){
        super(itemView);

        text = (TextView)itemView.findViewById(tagViewId);
    }

    public RecViewHolder(View itemView, int nameViewId, int typeViewId, int iconId){
        super(itemView);

        text = (TextView)itemView.findViewById(nameViewId);
        text2 = (TextView)itemView.findViewById(typeViewId);
        image = (ImageView) itemView.findViewById(iconId);
    }
}
