package com.toedro.fao.ui.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

public class SettingsFragment extends Fragment {

    EditText time1, time2, time3, time4, time5;
    MaterialCheckBox box1, box2, box3, box4, box5;
    Time mSelectedTime;

    public static int NOTIFICATION_ID = 0;
//TODO put in db
    private static int[][] notificationTime;

    public SharedPreferences sharedPref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //initializing notification time
        notificationTime = new int[5][2];

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


        //*notifications*//
        time1 = (EditText) root.findViewById(R.id.time1); time1.setText("00:00");
        time2 = (EditText) root.findViewById(R.id.time2); time2.setText("07:00");
        time3 = (EditText) root.findViewById(R.id.time3); time3.setText("12:00");
        time4 = (EditText) root.findViewById(R.id.time4); time4.setText("19:30");
        time5 = (EditText) root.findViewById(R.id.time5); time5.setText("00:00");
        box1 = (MaterialCheckBox) root.findViewById(R.id.checkbox1);
        box2 = (MaterialCheckBox) root.findViewById(R.id.checkbox2);
        box3 = (MaterialCheckBox) root.findViewById(R.id.checkbox3);
        box4 = (MaterialCheckBox) root.findViewById(R.id.checkbox4);
        box5 = (MaterialCheckBox) root.findViewById(R.id.checkbox5);

    ////*notifications*/////////
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = Integer.parseInt(time1.getText().toString().split(":")[0]);//myCalender.get(Calendar.HOUR_OF_DAY);
                int minute = Integer.parseInt(time1.getText().toString().split(":")[1]);//myCalender.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);//myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCalender.set(Calendar.MINUTE, minute);//myCalender.set(Calendar.MINUTE, minute);
                            time1.setText(new SimpleDateFormat("HH:mm").format(myCalender.getTime()));
                        }}
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
                //on change notifications attempt
                if(box1.isChecked()){
                    notificationTime[0][0] = Integer.parseInt(time1.getText().toString().split(":")[0]);
                    notificationTime[0][1] = Integer.parseInt(time1.getText().toString().split(":")[1]);
                }
            }
        });
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalender = Calendar.getInstance();
                int hour = Integer.parseInt(time2.getText().toString().split(":")[0]);
                int minute = Integer.parseInt(time2.getText().toString().split(":")[1]);
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
                int hour = Integer.parseInt(time3.getText().toString().split(":")[0]);
                int minute = Integer.parseInt(time3.getText().toString().split(":")[1]);
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
                int hour = Integer.parseInt(time4.getText().toString().split(":")[0]);
                int minute = Integer.parseInt(time4.getText().toString().split(":")[1]);
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

        box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    ///can't access class attribute from here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    notificationTime[0][0] = Integer.parseInt(time1.getText().toString().split(":")[0]);
                    notificationTime[0][1] = Integer.parseInt(time1.getText().toString().split(":")[1]);
                }else
                    notificationTime[0] = null;
            }
        });
        box2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    LocalTime localTime = LocalTime.parse(time2.getText().toString(), DateTimeFormatter.ofPattern("HH:mm"));
                    notificationTime[1][0] = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
                    notificationTime[1][1] = localTime.get(ChronoField.MINUTE_OF_HOUR);
                }
                else
                    notificationTime[1] = null;
            }
        });
        box3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    LocalTime localTime = LocalTime.parse(time3.getText().toString(), DateTimeFormatter.ofPattern("HH:mm"));
                    notificationTime[2][0] = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
                    notificationTime[2][1] = localTime.get(ChronoField.MINUTE_OF_HOUR);
                }
                else
                    notificationTime[2] = null;
            }
        });
        box4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    LocalTime localTime = LocalTime.parse(time4.getText().toString(), DateTimeFormatter.ofPattern("HH:mm"));
                    notificationTime[3][0] = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
                    notificationTime[3][1] = localTime.get(ChronoField.MINUTE_OF_HOUR);
                }
                else
                    notificationTime[3] = null;
            }
        });
        box5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    LocalTime localTime = LocalTime.parse(time5.getText().toString(), DateTimeFormatter.ofPattern("HH:mm"));
                    notificationTime[4][0] = localTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
                    notificationTime[4][1] = localTime.get(ChronoField.MINUTE_OF_HOUR);
                }
                else
                    notificationTime[4] = null;
            }
        });


        return root;
    }

    public static int[][] getNotificationTime() {
        return notificationTime;
    }
}