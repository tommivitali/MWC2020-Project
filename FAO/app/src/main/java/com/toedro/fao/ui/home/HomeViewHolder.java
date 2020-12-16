package com.toedro.fao.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.toedro.fao.R;

/**
 * This class extends the ViewHolder class in such a way that we can show informations in the
 * homepage's view pager.
 */
public class HomeViewHolder extends RecyclerView.ViewHolder {
    TextView textViewContent;
    TextView textViewDescription;
    ImageView imageView;

    /**
     * This constructor take a view as a parameter and get two textview and an imageview to access
     * it in a simple way from the bind method.
     * @param root a view that follows the pager_content layout specifications
     */
    public HomeViewHolder(View root) {
        super(root);
        textViewContent = root.findViewById(R.id.stepsCount);
        textViewDescription = root.findViewById(R.id.kindOfCount);
        imageView = root.findViewById(R.id.imageCount);
    }

    /**
     * This method get the data from an HomePagerData object, and set them into the views.
     * @param data HomePagerData object with all the data of a single viewpager page
     */
    public void bind(HomePagerData data) {
        textViewContent.setText(data.getContent());
        textViewDescription.setText(data.getDescription());
        imageView.setImageResource(data.getImage());
    }
}
