package com.toedro.fao.ui.pantry;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.toedro.fao.R;

public class PantryViewHolder extends RecyclerView.ViewHolder {

    TextView textViewContent;
    Button buttonEdit;
    Button buttonDelete;

    public PantryViewHolder(View root) {
        super(root);
        textViewContent = root.findViewById(R.id.pantry_list_content);
        buttonDelete    = root.findViewById(R.id.pantry_button_delete);
        buttonEdit      = root.findViewById(R.id.pantry_button_edit);
    }

    public void bind(PantryListData data) {
        textViewContent.setText(data.getName() + " - " + data.getQuantity() + "g");
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
