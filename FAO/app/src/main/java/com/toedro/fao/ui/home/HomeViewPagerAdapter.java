package com.toedro.fao.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.toedro.fao.R;

import java.util.ArrayList;
import java.util.List;
/**
 * The HomeViewPagerAdapter class is the adapter of the HomeViewHolder class, and do all the
 * operations to make the homepage's view pager working.
 * Documentation at the following link: https://developer.android.com/guide/topics/ui/layout/recyclerview
 */
public class HomeViewPagerAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private List<HomePagerData> listData = new ArrayList<HomePagerData>();
    private LayoutInflater layoutInflater;

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View v = layoutInflater.inflate(R.layout.pager_content, parent, false);
        return new HomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.bind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setData(List<HomePagerData> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    public List<HomePagerData> getData() {
        return listData;
    }
}
