package com.toedro.fao.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.Ingredient;
import com.toedro.fao.db.Recipe;
import com.toedro.fao.db.RecipeIngredients;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
/**
 * The SplashFragment class is the class that handles the download of data from remote DB ...
 */
public class SplashFragment extends Fragment {
    FirebaseFirestore db;
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefs = getActivity().getSharedPreferences("com.toedro.fao", MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String intentString = getActivity().getIntent().getStringExtra(getString(R.string.notification_action2));

        // Reading from Firebase Cloud Firestore
        db = FirebaseFirestore.getInstance();

        db.collection("recipes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            Snackbar.make(view, R.string.splash_error, Snackbar.LENGTH_LONG).show();
                        } else if(value != null) {
                            List<Recipe> recipes = new ArrayList<Recipe>();
                            for (DocumentSnapshot document : value.getDocuments()) {
                                recipes.add(new Recipe(document.getId(),
                                        document.getString("name"),
                                        document.getString("text"),
                                        document.getString("type"),
                                        document.getString("image")));
                                List<RecipeIngredients> recipeIngredients = new ArrayList<RecipeIngredients>();
                                for (Map m : (List<Map>) document.get("Ingredients")) {
                                    DocumentReference docRef = (DocumentReference) m.get("key");
                                    recipeIngredients.add(new RecipeIngredients(
                                            document.getId(),
                                            ((DocumentReference) m.get("key")).getId(),
                                            ((Long) m.get("quantity")).intValue()));
                                }
                                App.getDBInstance().recipeIngredientsDAO().addRecipeIngredients(recipeIngredients);
                            }
                            App.getDBInstance().recipeDAO().addRecipes(recipes);
                        } else {
                            Snackbar.make(view, R.string.splash_error, Snackbar.LENGTH_LONG).show();
                        }

                        if (prefs.getBoolean("firstrun", true)) {
                            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_tutorialActivity);
                        }

                        if (intentString != null && intentString.equals(getString(R.string.notification_action2))) {
                            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_wannaEatFragment);
                        } else {
                            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_nav_homepage);
                        }
                    }
                });

        db.collection("ingredients")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            Snackbar.make(view, R.string.splash_error, Snackbar.LENGTH_LONG).show();
                        } else if (value != null) {
                            List<Ingredient> ingredients = new ArrayList<Ingredient>();
                            for (DocumentSnapshot document : value.getDocuments()) {
                                ingredients.add(new Ingredient(document.getId(),
                                        document.getLong("calories").intValue(),
                                        (List<String>) document.get("keywords"),
                                        document.getString("name"),
                                        document.getDouble("quantity")));
                            }
                            App.getDBInstance().ingredientDAO().addIngredients(ingredients);
                        } else {
                            Snackbar.make(view, R.string.splash_error, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}