package com.toedro.fao.ui.settings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.toedro.fao.R;
import com.toedro.fao.ui.Utils;
import com.toedro.fao.ui.home.HomeFragment;

import java.sql.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingsFragment extends Fragment {

    EditText time1, time2, time3, time4, time5;
    MaterialCheckBox box1, box2, box3, box4, box5;
    Time mSelectedTime;

    public SharedPreferences sharedPref;
    public static int NOTIFICATION_ID = 0;
    public ArrayList notificationTime;

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
        //*notifications*//
        time1 = (EditText) root.findViewById(R.id.time1);
        time2 = (EditText) root.findViewById(R.id.time2);
        time3 = (EditText) root.findViewById(R.id.time3);
        time4 = (EditText) root.findViewById(R.id.time4);
        time5 = (EditText) root.findViewById(R.id.time5);
        box1 = (MaterialCheckBox) root.findViewById(R.id.checkbox1);
        box2 = (MaterialCheckBox) root.findViewById(R.id.checkbox2);
        box3 = (MaterialCheckBox) root.findViewById(R.id.checkbox3);
        box4 = (MaterialCheckBox) root.findViewById(R.id.checkbox4);
        box5 = (MaterialCheckBox) root.findViewById(R.id.checkbox5);

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
    ////*notifications*/////////
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = 00;//myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = 00;//myCalender.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);
                            time1.setText(new SimpleDateFormat("HH:mm").format(myCalender.getTime()));
                        }}
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = 07;//myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = 00;//myCalender.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);
                            time2.setText(new SimpleDateFormat("HH:mm").format(myCalender.getTime()));
                        }}
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });
        time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = 12;//myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = 00;//myCalender.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);
                            time3.setText(new SimpleDateFormat("HH:mm").format(myCalender.getTime()));
                        }}
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });
        time4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = 19;//myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = 30;//myCalender.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);
                            time4.setText(new SimpleDateFormat("HH:mm").format(myCalender.getTime()));
                        }}
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });
        time5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = myCalender.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);
                            time5.setText(new SimpleDateFormat("HH:mm").format(myCalender.getTime()));
                        }}
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });
        notificationTime = new ArrayList();
        /*box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked)
                    notificationTime.add(time1.getText());
                else
                    notificationTime.remove()
            }
        });*/


        return root;
    }
}