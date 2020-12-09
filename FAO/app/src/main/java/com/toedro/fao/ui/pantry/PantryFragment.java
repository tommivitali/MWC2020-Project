package com.toedro.fao.ui.pantry;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.toedro.fao.R;
import com.toedro.fao.App;
import com.toedro.fao.db.Pantry;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment {

    private PantryListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        List data = new ArrayList<PantryListData>();
        for(Pantry p : App.getDBInstance().pantryDAO().getPantry()) {
            data.add(new PantryListData(p.getId(), p.getName(), p.getQuantity()));
        }

        RecyclerView listView = view.findViewById(R.id.list_pantry);
        adapter = new PantryListViewAdapter();
        listView.setAdapter(adapter);
        adapter.setData(data);

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
}
