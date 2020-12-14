package com.toedro.fao;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class TutorialActivity extends AppIntro {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("com.toedro.fao", MODE_PRIVATE);
        addSlide(AppIntroFragment.newInstance(
                "Welcome to FAO!",
                "FAO is the best app that lets you eat healthy. Follow us for a brief tutorial",
                R.drawable.ic_balance_foreground
        ));
        addSlide(AppIntroFragment.newInstance(
                "Menu",
                "You can navigate through the various section of the app via the menu. Lets see together the main functionalities!",
                R.drawable.navigation
        ));
        addSlide(AppIntroFragment.newInstance(
                "Stepcounter",
                "FAO measures the calories you burn during the day with a sophisticated integrated pedometer, startable in the homepage where you can also see your progress.",
                R.drawable.stepcounter
        ));
        addSlide(AppIntroFragment.newInstance(
                "Charts",
                "Here you can see your stepcounter progress in the desired dates interval, either by calories burnt walking, steps taken or both.\n" +
                        "Try to improve day by day!",
                R.drawable.charts
        ));
        addSlide(AppIntroFragment.newInstance(
                "Pantry",
                "Where your available ingredients are recorded. You can also manually handle it from here. \n" +
                        "Add everything you can use to prepare a delicious meal!",
                R.drawable.pantry
        ));
        addSlide(AppIntroFragment.newInstance(
                "Recipes",
                "FAO provides a section with lots of healthy and tasty at the same time recipes. There you can see a list of all.",
                R.drawable.recipes
        ));
        addSlide(AppIntroFragment.newInstance(
                "Settings: Physical data",
                "Where you can modify your personal data, important to your user experience without worrying about privacy, since stored locally./n" +
                        "Select your favourite language and re-read this tutorial if need be!",
                R.drawable.settings
        ));
        addSlide(AppIntroFragment.newInstance(
                "Settings: Notifications",
                "FAO can remember you when to eat, and then suggests you what to eat based on your activity./n" +
                        "here you can schedule notifications. Go to set now the correct notifications to not lose even one!",
                R.drawable.settings2
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