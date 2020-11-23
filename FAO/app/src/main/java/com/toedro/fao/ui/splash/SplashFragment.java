package com.toedro.fao.ui.splash;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.Recipe;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SplashFragment extends Fragment {
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                            }
                            App.getDBInstance().recipeDAO().addRecipes(recipes);
                        } else {
                            Snackbar.make(view, R.string.splash_error, Snackbar.LENGTH_LONG).show();
                        }
                        Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_nav_homepage);
                    }
                });

    }
}