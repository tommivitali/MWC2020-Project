package com.toedro.fao.ui.home;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.stepcounter.StepCounterListener;
import com.toedro.fao.ui.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The HomeFragment class is the class of the homepage fragment. It holds buttons to navigate to all
 * others Fragments and initialize the stepcounter listener. It also has a viewpager holding steps
 * done the current day and corresponding calories.
 */
public class HomeFragment extends Fragment {

    private MaterialButton buttonStartStop;

    MaterialButtonToggleGroup materialButtonToggleGroup;

    // Accelerometer sensor
    private Sensor mSensorACC;
    // Step Detector sensor
    private Sensor mSensorStepDetector;
    // Listener and SensorManager
    private SensorManager mSensorManager;
    private StepCounterListener listener;
    // Adapter for the view pager
    private HomeViewPagerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Button to add an ingredient manually: on click it brings to the ingredients fragment
        MaterialButton buttonAddIngredient = (MaterialButton) root.findViewById(R.id.buttonAddIngredient);
        buttonAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_nav_homepage_to_ingredientsFragment);
            }
        });

        // Button to scan a barcode: on click it brings to the scanBarcode fragment
        MaterialButton buttonScanBarcode = (MaterialButton) root.findViewById(R.id.buttonScanBarcode);
        buttonScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_nav_homepage_to_scanBarcodeFragment);
            }
        });

        // Button to get a recipe: on click it brings to the wannaEat fragment
        MaterialButton buttonWannaEat = (MaterialButton) root.findViewById(R.id.buttonWannaEat);
        buttonWannaEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_homepage_to_wannaEatFragment);
            }
        });

        // Get the steps completed till now from the DB
        int stepsCompleted = App.getDBInstance().stepDAO().getDaySteps(
                new SimpleDateFormat(getString(R.string.date_layout_DB))
                        .format(new Date()));
        // Initial data of the view pager in a list of HomePagerData elements
        List data = new ArrayList<HomePagerData>();
        data.add(new HomePagerData(R.drawable.ic_calories,
                String.valueOf(Utils.convertStepsToCal(stepsCompleted, getActivity(), getContext())),
                getString(R.string.home_calories_description)));
        data.add(new HomePagerData(R.drawable.ic_steps,
                String.valueOf(stepsCompleted),
                getString(R.string.home_steps_description)));
        // View Pager to show steps and calories
        ViewPager2 viewPager = root.findViewById(R.id.homeViewPager);
        adapter = new HomeViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        adapter.setData(data);
        // This is used to show an indicator of the view pager "page" shown at the moment
        TabLayout progressPager = root.findViewById(R.id.progressViewPager);
        new TabLayoutMediator(progressPager, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();

        // Get the start/stop button from the fragment view
        buttonStartStop = (MaterialButton) root.findViewById(R.id.buttonStartStopStepcounter);
        //  Get an instance of the sensor manager
        mSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        // Get an instance of Accelerometer sensor
        mSensorACC = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        // Get an instance of Step Detector sensor
        mSensorStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        // Get listener instance, and check if it is already activated
        listener = StepCounterListener.getInstance(stepsCompleted, adapter, getContext(), getActivity());
        if(listener.isActive()) {
            buttonStartStop.setChecked(true);
            buttonStartStop.setText(R.string.home_stop_stepcounter);
            buttonStartStop.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop));
        }
        // Set what to do when the start/stop button is clicked
        buttonStartStop.addOnCheckedChangeListener(new MaterialButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MaterialButton button, boolean isChecked) {
                if (isChecked) {
                    // Check if the step detector sensor exists, if not use the accelerometer
                    if (mSensorStepDetector != null) {
                        // Register the Step Detector listener
                        listener.activate();
                        mSensorManager.registerListener(listener, mSensorStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
                    } else if (mSensorACC != null) {
                        // Register the Accelerometer listener
                        listener.activate();
                        mSensorManager.registerListener(listener, mSensorACC, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {
                        Snackbar.make(getView(), R.string.home_sensors_not_available, Snackbar.LENGTH_LONG).show();
                        button.setChecked(false);
                        return;
                    }
                    // Change button text and icon
                    buttonStartStop.setText(R.string.home_stop_stepcounter);
                    buttonStartStop.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_stop));
                } else {
                    // Unregister the listener
                    listener.deactivate();
                    mSensorManager.unregisterListener(listener);
                    // Change button text and icon
                    buttonStartStop.setText(R.string.home_start_stepcounter);
                    buttonStartStop.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_play));
                }
            }
        });

        return root;
    }
}
