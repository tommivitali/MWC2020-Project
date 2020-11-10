package com.example.fao.ui.charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fao.R;
import com.google.android.material.datepicker.MaterialDatePicker;

public class ChartsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_charts, container, false);

        com.google.android.material.textfield.TextInputLayout  dr = root.findViewById(R.id.chartsTextFieldDateRange);
        dr.getEditText().setText("ciao");

        dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
                MaterialDatePicker picker = builder.build();
                picker.show(getActivity().getSupportFragmentManager(), picker.toString());

            }
        });

        return root;
    }
}