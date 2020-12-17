package com.toedro.fao.ui.pantry;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.toedro.fao.R;
/**
 * provide the structure of the list that refers to each element in the pantry
 * and to its values
 * the class moreover provides the methods to call to return the value of each variable of an ingredient
 */
public class PantryViewHolder extends RecyclerView.ViewHolder {

    TextView textViewContent;
    Button buttonEdit;
    Button buttonDelete;
    ImageView image;

    public PantryViewHolder(View root) {
        super(root);
        textViewContent = root.findViewById(R.id.pantry_list_content);
        buttonDelete    = root.findViewById(R.id.pantry_button_delete);
        buttonEdit      = root.findViewById(R.id.pantry_button_edit);
        image           = root.findViewById(R.id.pantry_image);
        image.getLayoutParams().height = image.getLayoutParams().width = 80;
    }

    public void bind(PantryListData data, OnElementClickListener listener) {
        textViewContent.setText(data.getName() + " - " + data.getQuantity() + "g");
        Picasso.get().load(data.getImage()).into(image);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onElementDeleteClick(data);
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onElementEditClick(data);

            }
        });
    }
}
