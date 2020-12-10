package com.toedro.fao.ui.pantry;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.toedro.fao.R;

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
        image.getLayoutParams().height = 100;
        image.getLayoutParams().width = 100;
    }

    public void bind(PantryListData data) {
        textViewContent.setText(data.getName() + " - " + data.getQuantity() + "g");
        Picasso.get().load(data.getImage()).into(image);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Pantry", "delete");
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Pantry", "edit");
            }
        });
    }
}
