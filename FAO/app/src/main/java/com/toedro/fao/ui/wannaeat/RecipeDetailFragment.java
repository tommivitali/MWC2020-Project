package com.toedro.fao.ui.wannaeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.Calories;
import com.toedro.fao.db.Ingredient;
import com.toedro.fao.db.Recipe;
import com.toedro.fao.db.RecipeIngredientsQueryResult;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RecipeDetailFragment extends Fragment {

    String recipeID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        recipeID = getArguments().getString("recipeId");
        Log.d("DETAIL", recipeID);

        // Add calories in DB
        App.getDBInstance().caloriesDao().addCalories(new Calories(
                Double.valueOf(App.getDBInstance().recipeDAO().getCalories(recipeID)),
                new SimpleDateFormat(getString(R.string.date_layout_DB)).format(new Date()),
                String.valueOf(Calendar.getInstance().getTimeInMillis())
        ));
        // Remove quantities for each ingredients in Pantry
        for(RecipeIngredientsQueryResult i: App.getDBInstance().recipeIngredientsDAO().getIngredientsRecipe(recipeID)) {
            App.getDBInstance().pantryDAO().subQuantity(i.getKeywords(), i.getQuantity());
        }

        Recipe recipe = App.getDBInstance().recipeDAO().getRecipe(recipeID);
        ((TextView) v.findViewById(R.id.detail_title)).setText(recipe.getName());
        ((TextView) v.findViewById(R.id.detail_content)).setText(recipe.getText());
        Picasso.get().load(recipe.getImage()).into((ImageView) v.findViewById(R.id.detail_image));

        return v;
    }
}