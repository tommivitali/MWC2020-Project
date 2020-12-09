package com.toedro.fao.ui.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.toedro.fao.R;
import com.toedro.fao.ui.home.HomePagerData;

import java.util.ArrayList;
import java.util.List;

public class PantryListViewAdapter extends RecyclerView.Adapter<PantryViewHolder> {

    private List<PantryListData> listData = new ArrayList<PantryListData>();
    private LayoutInflater layoutInflater;

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View v = layoutInflater.inflate(R.layout.listview_content, parent, false);
        return new PantryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        holder.bind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setData(List<PantryListData> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    public List<PantryListData> getData() {
        return listData;
    }
}
