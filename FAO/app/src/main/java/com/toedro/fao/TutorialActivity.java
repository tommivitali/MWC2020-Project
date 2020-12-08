package com.toedro.fao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class TutorialActivity extends AppIntro {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("com.toedro.fao", MODE_PRIVATE);
        addSlide(AppIntroFragment.newInstance(
                "Ciao a tutti!",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt"
        ));
        addSlide(AppIntroFragment.newInstance(
                "Seconda pagina!",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt"
        ));
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Decide what to do when the user clicks on "Skip"
        prefs.edit().putBoolean("firstrun", false).commit();
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Decide what to do when the user clicks on "Done"
        prefs.edit().putBoolean("firstrun", false).commit();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        getSupportActionBar().show();
    }
}