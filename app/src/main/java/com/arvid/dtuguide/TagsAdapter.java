package com.arvid.dtuguide;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 12-01-2018.
 */

public class TagsAdapter extends RecyclerView.Adapter<RecViewHolder>{

    private List<String> mList;

    public TagsAdapter(List<String> mList){
        this.mList = mList;
    }

    public int getItemCount(){
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    public RecViewHolder onCreateViewHolder(ViewGroup viewGroup, int position){

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_tag, viewGroup, false);
        RecViewHolder pvh = new RecViewHolder(v);
        return pvh;
    }

    public void onBindViewHolder(RecViewHolder holder, int i){
        holder.tagText.setText(mList.get(i));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}