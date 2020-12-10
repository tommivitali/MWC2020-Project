package com.toedro.fao.ui.pantry;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;
import com.toedro.fao.App;
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
                new MaterialAlertDialogBuilder(buttonDelete.getContext())
                        .setTitle(R.string.pantry_delete_dialog_title)
                        .setMessage(R.string.pantry_delete_dialog_message)
                        .setNegativeButton(R.string.pantry_delete_dialog_negative, null)
                        .setPositiveButton(R.string.pantry_delete_dialog_positive,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        App.getDBInstance().pantryDAO().removePantry(data.getId());
                                    }
                                })
                        .show();
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
