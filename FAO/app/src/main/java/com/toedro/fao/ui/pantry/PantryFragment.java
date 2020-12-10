package com.toedro.fao.ui.pantry;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.toedro.fao.R;
import com.toedro.fao.App;
import com.toedro.fao.db.Pantry;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment implements OnElementClickListener {

    private PantryListViewAdapter adapter;
    private List dataList = new ArrayList<PantryListData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        for(Pantry p : App.getDBInstance().pantryDAO().getPantry()) {
            dataList.add(new PantryListData(p.getId(), p.getName(), p.getQuantity(), p.getImage()));
        }

        RecyclerView listView = view.findViewById(R.id.list_pantry);
        adapter = new PantryListViewAdapter(this);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setData(dataList);

        for(PantryListData p: adapter.getData()) {
            Log.d("PANTRY", p.getName());
        }
        /*
        List<Pantry> pantryList = App.getDBInstance().pantryDAO().getPantry();

        String[] Items = {};
        String string;
        for(Pantry pantry : pantryList) {
            string = "Ingredient:" + String.valueOf(pantry.getName()) + "in quantity:" + String.valueOf(pantry.getQuantity());
            Items = {Items, string};
        }

        ListView listView = view.findViewById(R.id.list_ingredients);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                Items
        );

        listView.setAdapter(listViewAdapter);
        */
        return view;
    }

    public void updateData() {
        dataList.clear();
        for(Pantry p : App.getDBInstance().pantryDAO().getPantry()) {
            dataList.add(new PantryListData(p.getId(), p.getName(), p.getQuantity(), p.getImage()));
        }
    }

    @Override
    public void onElementEditClick(PantryListData data) {
        final EditText input = new EditText(getContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );
        lp.setMargins(50,0,50,0);
        input.setLayoutParams(lp);
        RelativeLayout container = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT );
        container.setLayoutParams(rlParams);
        container.addView(input);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(String.valueOf(data.getQuantity()));
        new MaterialAlertDialogBuilder(getContext())
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
                                updateData();
                                adapter.setData(dataList);
                            }
                        })
                .show();
    }

    @Override
    public void onElementDeleteClick(PantryListData data) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.pantry_delete_dialog_title)
                .setMessage(R.string.pantry_delete_dialog_message)
                .setNegativeButton(R.string.pantry_delete_dialog_negative, null)
                .setPositiveButton(R.string.pantry_delete_dialog_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.getDBInstance().pantryDAO().removePantry(data.getId());
                                updateData();
                                adapter.setData(dataList);
                            }
                        })
                .show();
    }
}
