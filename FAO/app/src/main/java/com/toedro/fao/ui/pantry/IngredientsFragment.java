package com.toedro.fao.ui.pantry;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.Pantry;

import java.util.ArrayList;
import java.util.List;

/**
 * The IngredientsFragment class is the fragment handling the manual insertion of ingredients in the local Pantry.
 * it has mandatory name and quantity of product (from list of available ingredients), the option to add barcode and 2 blanks
 * EditTexts to create space in the view, plus the button to submit onClick
 */
public class IngredientsFragment extends Fragment {

    Button add;
    EditText quantity, barcode, campo2, campo3;
    AutoCompleteTextView product;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ingredients, container, false);

        product = (AutoCompleteTextView) root.findViewById(R.id.keywordsDropdown);//(EditText) root.findViewById(R.id.editText1);
        quantity = (EditText) root.findViewById(R.id.editText2);
        barcode = (EditText) root.findViewById(R.id.editText3);
        campo2 = (EditText) root.findViewById(R.id.editText4);
        campo3 = (EditText) root.findViewById(R.id.editText5);
        add = (Button) root.findViewById(R.id.button);

        campo2.setVisibility(View.INVISIBLE);
        campo3.setVisibility(View.INVISIBLE);

        //Dropdown code
        List<String> names = new ArrayList<String>();
        // ingredients from database
        for (String keys : App.getDBInstance().ingredientDAO().getNames()) { //getNames does not work
            names.add(String.valueOf(keys));
        }
        String defaultKeywordValue = names.get(0);
        // Fill the dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_languages_item, names);
        AutoCompleteTextView keywordsDropdown = root.findViewById(R.id.keywordsDropdown);
        keywordsDropdown.setAdapter(adapter);
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
                    Toast.makeText(getContext(), quantity.getText().toString() + " grams of " + product.getText().toString() + " added to the pantry!"
                            , Toast.LENGTH_SHORT).show();

                    // add to local db

                    // name --> ad as none
                    String name = String.valueOf(keywordsDropdown.getText());//"Gocciole";
                    String quant1 = quantity.getText().toString();
                    Integer quantities = 0;
                    try {
                        quantities = Integer.parseInt(quant1);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "quantity must be a number!", Toast.LENGTH_SHORT).show();
                    }

                    Integer energy_100g = App.getDBInstance().ingredientDAO().getCals_100g(name);//10;
                    String bar;
                    if (barcode.getText().toString().isEmpty()) {
                        bar = "";//"123456789";
                    }else{
                        bar = barcode.getText().toString();
                    }
                    // String image = product.getString("image_front_url");
                    String image = "https://i.ibb.co/WGp6RSc/ingredient.png";
                    String keywords = App.getDBInstance().ingredientDAO().getKeywords(product.getText().toString()).toString();
                    keywords = keywords.substring(1, keywords.length() -1);

                    Pantry pantries = new Pantry(name, quantities, energy_100g, keywords, bar, image);
                    if(quantities > 0) {
                        App.getDBInstance().pantryDAO().addPantry(pantries);
                    }
                }
            }
        });

        return root;
    }
}

