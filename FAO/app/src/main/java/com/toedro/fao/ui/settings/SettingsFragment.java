package com.toedro.fao.ui.settings;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.toedro.fao.MainActivity;
import com.toedro.fao.R;
import com.toedro.fao.ui.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * The SettingsFragment class handles the setting view and save in SharedPreferences the local User's attributes and choices:
 * height, weight, age, sex, App's language, kcal range, notifications (up to 5) and a button to re-watch the tutorial.
 */
public class SettingsFragment extends Fragment {

    public SharedPreferences sharedPref; //added static to change stuff from home

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        root.findViewById(R.id.buttonShowTutorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.action_nav_settings_to_tutorialActivity);
            }
        });

        // Get the SharedPreferences language value (if exists)
        String defaultLanguageValue = getString(R.string.saved_language_default_key);
        String languageShared = sharedPref.getString(getString(R.string.saved_language_saved_key), defaultLanguageValue);
        // Fill the languages dropdown
        String[] languages = new String[] {"Italian", "English"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), R.layout.dropdown_languages_item, languages);
        AutoCompleteTextView editTextFilledExposedDropdown = root.findViewById(R.id.languages_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter1);
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
        // Get the SharedPreferences sex value (if exists)
        String defaultSexValue = getString(R.string.saved_sex_default_key);
        String sexShared = sharedPref.getString(getString(R.string.saved_sex_saved_key), defaultSexValue);
        // Fill the sex dropdown
        String[] sex = new String[] {"Male", "Female"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), R.layout.dropdown_sex_item, sex);
        AutoCompleteTextView editTextFilledExposedDropdownS = root.findViewById(R.id.sex_dropdown);
        editTextFilledExposedDropdownS.setAdapter(adapter2);
        editTextFilledExposedDropdownS.setText(sexShared, false);
        // If someone changes the value of the dropdown then save it to the SharedPreferences
        editTextFilledExposedDropdownS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.saved_sex_saved_key), s.toString());
                editor.apply();
            }
        });

        // Get the age, height and weight value and, if exists, put it in the textEdit
        Integer defaultHeightValue = getResources().getInteger(R.integer.saved_height_default_key);
        Integer defaultWeightValue = getResources().getInteger(R.integer.saved_weight_default_key);
        Integer defaultAgeValue = getResources().getInteger(R.integer.saved_age_default_key);
        double defaultCalValue = 0.0;
        Integer heightShared = sharedPref.getInt(getString(R.string.saved_height_saved_key), defaultHeightValue);
        Integer weightShared = sharedPref.getInt(getString(R.string.saved_weight_saved_key), defaultWeightValue);
        Integer ageShared = sharedPref.getInt(getString(R.string.saved_age_saved_key), defaultAgeValue);
        Integer minCalShared = sharedPref.getInt(getString(R.string.saved_min_cal_saved_key), (int) defaultCalValue);
        Integer maxCalShared = sharedPref.getInt(getString(R.string.saved_max_cal_saved_key), (int) defaultCalValue);
        TextInputEditText editTextHeight = root.findViewById(R.id.editHeight);
        TextInputEditText editTextWeight = root.findViewById(R.id.editWeight);
        TextInputEditText editTextAge = root.findViewById(R.id.editAge);
        editTextHeight.setText(heightShared.toString());
        editTextWeight.setText(weightShared.toString());
        editTextAge.setText(ageShared.toString());


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
        editTextAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 0) return;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.saved_age_saved_key), Integer.parseInt(s.toString()));
                editor.apply();
            }
        });

        // Get the choice type and, if exists, select it in the RadioGroup
        String defaultChoiceProgressTypeValue = getString(R.string.saved_choice_progress_type_default_key);
        String choiceProgressTypeShared = sharedPref.getString(getString(R.string.saved_choice_progress_type_saved_key), defaultChoiceProgressTypeValue);
        RadioGroup cGroup = root.findViewById(R.id.choiceGroup);
        cGroup.check(Utils.ChoiceTypeSettingsToId(ChoiceTypeSettings.valueOf(choiceProgressTypeShared)));
        cGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.saved_choice_progress_type_saved_key), Utils.idToChoiceTypeSettings(checkedId).toString());
                editor.apply();
            }
        });

        notificationSetup(R.integer.saved_hour_1_default_key, R.integer.saved_minute_1_default_key,
                R.string.saved_hour_1_saved_key, R.string.saved_minute_1_saved_key,
                R.integer.saved_notification_1_default_key, R.string.saved_notification_1_saved_key,
                root.findViewById(R.id.time1), root.findViewById(R.id.checkbox1), false);
        notificationSetup(R.integer.saved_hour_2_default_key, R.integer.saved_minute_2_default_key,
                R.string.saved_hour_2_saved_key, R.string.saved_minute_2_saved_key,
                R.integer.saved_notification_2_default_key, R.string.saved_notification_2_saved_key,
                root.findViewById(R.id.time2), root.findViewById(R.id.checkbox2), false);
        notificationSetup(R.integer.saved_hour_3_default_key, R.integer.saved_minute_3_default_key,
                R.string.saved_hour_3_saved_key, R.string.saved_minute_3_saved_key,
                R.integer.saved_notification_3_default_key, R.string.saved_notification_3_saved_key,
                root.findViewById(R.id.time3), root.findViewById(R.id.checkbox3), false);
        notificationSetup(R.integer.saved_hour_4_default_key, R.integer.saved_minute_4_default_key,
                R.string.saved_hour_4_saved_key, R.string.saved_minute_4_saved_key,
                R.integer.saved_notification_4_default_key, R.string.saved_notification_4_saved_key,
                root.findViewById(R.id.time4), root.findViewById(R.id.checkbox4), false);
        notificationSetup(R.integer.saved_hour_5_default_key, R.integer.saved_minute_5_default_key,
                R.string.saved_hour_5_saved_key, R.string.saved_minute_5_saved_key,
                R.integer.saved_notification_5_default_key, R.string.saved_notification_5_saved_key,
                root.findViewById(R.id.time5), root.findViewById(R.id.checkbox5), false);

        return root;
    }

    /**
     * @param h: hours
     * @param m: minutes
     * @return Calendar setted at time in input
     */
    private String timeIntToString(int h, int m) {
        Calendar myCalender = Calendar.getInstance();
        myCalender.set(Calendar.HOUR_OF_DAY, h);
        myCalender.set(Calendar.MINUTE, m);
        return (new SimpleDateFormat("HH:mm")).format(myCalender.getTime());
    }

    /**
     * @param defaultHour: default hours of said notification
     * @param defaultMinute: default minutes of said notification
     * @param savedKeyHour: setted hours of said notification
     * @param savedKeyMinute: setted minutes of said notification
     * @param defaultNotification: default notification time
     * @param savedKeyNotification: setted notification time
     * @param editTextTime: edit text
     * @param checkBox: checkbox
     * @param last: to set permanently a default time when datepicker is opened
     * get changed data and reload notifications
     */
    private void notificationSetup(int defaultHour, int defaultMinute,
                                   int savedKeyHour, int savedKeyMinute,
                                   int defaultNotification, int savedKeyNotification,
                                   EditText editTextTime, MaterialCheckBox checkBox,
                                   boolean last) {

        Integer defaultHourValue            = getResources().getInteger(defaultHour);
        Integer defaultMinuteValue          = getResources().getInteger(defaultMinute);
        Integer defaultNotificationValue    = getResources().getInteger(defaultNotification);
        Integer hourShared                  = sharedPref.getInt(getString(savedKeyHour), defaultHourValue);
        Integer minuteShared                = sharedPref.getInt(getString(savedKeyMinute), defaultMinuteValue);
        Integer notificationShared          = sharedPref.getInt(getString(savedKeyNotification), defaultNotificationValue);

        editTextTime.setText(timeIntToString(hourShared, minuteShared));
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = last ?
                        Calendar.getInstance().HOUR_OF_DAY :
                        Integer.parseInt(editTextTime.getText().toString().split(":")[0]);
                int minute = last ?
                        Calendar.getInstance().MINUTE :
                        Integer.parseInt(editTextTime.getText().toString().split(":")[1]);
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            editTextTime.setText(timeIntToString(hourOfDay, minute));

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(getString(savedKeyHour), hourOfDay);
                            editor.putInt(getString(savedKeyMinute), minute);
                            editor.apply();
                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Choose hour:");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
                //reload notifications
                MainActivity.setNotifications(getContext(), com.toedro.fao.Preferences.getNotificationsHours(getActivity(), getContext()),
                        MainActivity.getNotificationId());
            }
        });

        checkBox.setChecked(notificationShared == 1);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(savedKeyNotification), isChecked ? 1 : 0);
                editor.apply();
                //reload notifications
                MainActivity.setNotifications(getContext(), com.toedro.fao.Preferences.getNotificationsHours(getActivity(), getContext())
                        ,MainActivity.getNotificationId());
            }
        });
    }
}