package com.example.fao;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private EditText weight, height, test;
    private Button settingsButton;
    private TextView results;
    public int userH = 0, userW = 0; //user's weight and height
    private String[] languages = new String[] {"Italian", "English", "German"};

    //for permissions
    private static final int REQUEST_ACTIVITY_RECOGNITION_PERMISSION = 45;
    private boolean runningQOrLater =
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_homepage,
                R.id.nav_charts,
                R.id.nav_pantry,
                R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //options section
        settingsButton = (Button) findViewById(R.id.settingsButton); //save settings TODO decide how to save settings
        results = (TextView) findViewById(R.id.results); //for testing

        height = (EditText) findViewById(R.id.editHeight);
        weight = (EditText) findViewById(R.id.editWeight);
        /*userH = Integer.parseInt(height.getText().toString()); //crash here
        userW = Integer.parseInt(weight.getText().toString());*/

        AutoCompleteTextView language = (AutoCompleteTextView)findViewById(R.id.Languages);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.languages_items, languages);
        //crashes here
        //language.setText(arrayAdapter.getItem(0).toString(), false); //default value
        //language.setAdapter(arrayAdapter);

        //testing
        /*settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userH = Integer.parseInt(height.getText().toString());
                userW = Integer.parseInt(weight.getText().toString());
                results.setText(userH + " " + userW + " " + language.getText().toString());
            }
        });*/
        // Ask for activity recognition permission
        if (runningQOrLater) {
            getActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Ask for permission
    private void getActivity() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACTIVITY_RECOGNITION},
                    REQUEST_ACTIVITY_RECOGNITION_PERMISSION);
        } else {
            return;        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACTIVITY_RECOGNITION_PERMISSION) {
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getActivity();
            } else {
                Toast.makeText(this,
                        R.string.step_permission_denied,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}