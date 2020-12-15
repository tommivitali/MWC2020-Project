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
 * The HomeViewPagerAdapter class ...
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
