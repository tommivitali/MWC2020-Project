package com.toedro.fao.ui.pantry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toedro.fao.R;
import androidx.annotation.NonNull;


import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.toedro.fao.App;
import com.toedro.fao.db.IngredientDAO;
import com.toedro.fao.db.Pantry;
import com.toedro.fao.db.RecipeQueryResult;

import java.util.ArrayList;
import java.util.List;

public class IngredientsFragment extends Fragment {

    Button add;
    EditText quantity, campo1, campo2, campo3;
    AutoCompleteTextView product;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ingredients, container, false);

        product = (AutoCompleteTextView) root.findViewById(R.id.keywordsDropdown);//(EditText) root.findViewById(R.id.editText1);
        quantity = (EditText) root.findViewById(R.id.editText2);
        campo1 = (EditText) root.findViewById(R.id.editText3);
        campo2 = (EditText) root.findViewById(R.id.editText4);
        campo3 = (EditText) root.findViewById(R.id.editText5);
        add = (Button) root.findViewById(R.id.button);

        //Dropdown code
        List<String> keywords = new ArrayList<String>();
        // ingredients from database TODO fix it
        for (String keys : App.getDBInstance().ingredientDAO().getKeywords()) {
            keywords.add(keys);
        }
        String defaultKeywordValue = keywords.get(0);
        // Fill the dropdown
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), R.layout.dropdown_languages_item, keywords);
        AutoCompleteTextView keywordsDropdown = root.findViewById(R.id.keywordsDropdown);
        keywordsDropdown.setAdapter(adapter1);
        keywordsDropdown.setText(defaultKeywordValue, false);
        // If someone changes the value of the dropdown then ...
        keywordsDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getText().toString().isEmpty() || quantity.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Add the ingredient and its quantity!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), quantity.getText().toString() + "grams of" + product.getText().toString() + "added to the pantry!"
                            , Toast.LENGTH_SHORT).show();

                    // add to local db

                    // name --> ad as none
                    String name = String.valueOf(keywordsDropdown.getText());//"Gocciole";
                    String quant1 = quantity.getText().toString();
                    Integer quantity = Integer.parseInt(quant1);
                    // Integer energy_100g = nutriments.getInt("energy-kcal_100g"); ---> recuperare calorie (???)
                    Integer energy_100g = App.getDBInstance().ingredientDAO().getCals_100g(name);//10;
                    // String barcode = root.getString("code"); --> add as none?
                    String barcode = "123456789";
                    // String image = product.getString("image_front_url");
                    String image = "data:image/jpeg;base64,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";
                    String keywords = product.getText().toString();

                    Pantry pantries = new Pantry(name, quantity, energy_100g, keywords, barcode, image);
                    App.getDBInstance().pantryDAO().addPantry(pantries);

                }
            }
        });

        return root;
    }
}

