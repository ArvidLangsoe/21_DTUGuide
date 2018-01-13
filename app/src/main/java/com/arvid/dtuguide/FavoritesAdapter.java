package com.arvid.dtuguide;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arvid.dtuguide.data.Searchable;

import java.util.List;

/**
 * Created by Jeppe on 14-01-2018.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<RecViewHolder>{

    private List<Searchable> mList;
    private int layout;

    public FavoritesAdapter(List<Searchable> mList, int layout){
        this.mList = mList;
        this.layout = layout;
    }

    public int getItemCount(){
        if (mList != null)
            return mList.size();
        else
            return 0;
    }

    public RecViewHolder onCreateViewHolder(ViewGroup viewGroup, int position){

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        RecViewHolder pvh = new RecViewHolder(v, R.id.recycler_item_fav_name);
        return pvh;
    }

    public void onBindViewHolder(RecViewHolder holder, int i){
        //holder.tagText.setText(mList.get(i).getName());
        holder.tagText.setText("haha");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
