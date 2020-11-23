package com.toedro.fao.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.toedro.fao.R;
import com.toedro.fao.ui.Utils;

public class SettingsFragment extends Fragment {
    public SharedPreferences sharedPref;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        // Get the SharedPreferences language value (if exists)
        String defaultLanguageValue = getResources().getString(R.string.saved_language_default_key);
        String languageShared = sharedPref.getString(getString(R.string.saved_language_saved_key), defaultLanguageValue);
        // Fill the languages dropdown
        String[] languages = new String[] {"Italian", "English"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_languages_item, languages);
        AutoCompleteTextView editTextFilledExposedDropdown = root.findViewById(R.id.languages_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setText(languageShared, false);
        // If someone changes the value of the dropdown then save it to the SharedPreferences
        editTextFilledExposedDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.saved_language_saved_key), s.toString());
                editor.apply();
            }
        });

        // Get the height and weight value and, if exists, put it in the textEdit
        Integer defaultHeightValue = getResources().getInteger(R.integer.saved_height_default_key);
        Integer defaultWeightValue = getResources().getInteger(R.integer.saved_weight_default_key);
        Integer heightShared = sharedPref.getInt(getString(R.string.saved_height_saved_key), defaultHeightValue);
        Integer weightShared = sharedPref.getInt(getString(R.string.saved_weight_saved_key), defaultWeightValue);
        TextInputEditText editTextHeight = root.findViewById(R.id.editHeight);
        TextInputEditText editTextWeight = root.findViewById(R.id.editWeight);
        editTextHeight.setText(heightShared.toString());
        editTextWeight.setText(weightShared.toString());
        editTextHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0) return;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.saved_height_saved_key), Integer.parseInt(s.toString()));
                editor.apply();
            }
        });
        editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0) return;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.saved_weight_saved_key), Integer.parseInt(s.toString()));
                editor.apply();
            }
        });

        // Get the home progress type and, if exists, select it in the RadioGroup
        String defaultHomeProgressTypeValue = getResources().getString(R.string.saved_home_progress_type_default_key);
        String homeProgressTypeShared = sharedPref.getString(getString(R.string.saved_home_progress_type_saved_key), defaultHomeProgressTypeValue);
        RadioGroup rGroup = root.findViewById(R.id.progressGroup);
        rGroup.check(Utils.progressTypeHomeToId(ProgressTypeHome.valueOf(homeProgressTypeShared)));
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.saved_home_progress_type_saved_key), Utils.idToProgressTypeHome(checkedId).toString());
                editor.apply();
            }
        });

        return root;
    }
}