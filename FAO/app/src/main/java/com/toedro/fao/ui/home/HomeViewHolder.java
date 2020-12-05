package com.toedro.fao.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.toedro.fao.R;

public class HomeViewHolder extends RecyclerView.ViewHolder {
    TextView textViewContent;
    TextView textViewDescription;
    ImageView imageView;

    public HomeViewHolder(View root) {
        super(root);
        textViewContent = root.findViewById(R.id.stepsCount);
        textViewDescription = root.findViewById(R.id.kindOfCount);
        imageView = root.findViewById(R.id.imageCount);
    }

    public void bind(HomePagerData data) {
        textViewContent.setText(data.getContent());
        textViewDescription.setText(data.getDescription());
        imageView.setImageResource(data.getImage());
    }
}
