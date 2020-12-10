package com.toedro.fao.ui.pantry;

import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.ui.Utils;

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
                final EditText input = new EditText(buttonEdit.getContext());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT );
                lp.setMargins(50,0,50,0);
                input.setLayoutParams(lp);
                RelativeLayout container = new RelativeLayout(buttonEdit.getContext());
                RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT );
                container.setLayoutParams(rlParams);
                container.addView(input);

                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setText(String.valueOf(data.getQuantity()));
                new MaterialAlertDialogBuilder(buttonEdit.getContext())
                        .setTitle(R.string.pantry_edit_dialog_title)
                        .setMessage(R.string.pantry_edit_dialog_message)
                        .setView(container)
                        .setNegativeButton(R.string.pantry_edit_dialog_negative, null)
                        .setPositiveButton(R.string.pantry_edit_dialog_positive,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        App.getDBInstance().pantryDAO().setQuantity(data.getId(),
                                                Integer.parseInt(input.getText().toString()));
                                    }
                                })
                        .show();
            }
        });
    }
}
