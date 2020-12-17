package com.toedro.fao.ui.wannaeat;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
/**
 * This class receive the id of a recipe from an intent, and then display the details after adding
 * the calories in the DB and removing quantities for each ingredients in pantry.
 */
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

        // Shows the recipe details
        Recipe recipe = App.getDBInstance().recipeDAO().getRecipe(recipeID);
        ((TextView) v.findViewById(R.id.detail_title)).setText(recipe.getName());
        ((TextView) v.findViewById(R.id.detail_content)).setText(recipe.getText());
        Picasso.get().load(recipe.getImage()).into((ImageView) v.findViewById(R.id.detail_image));
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_recipeDetailFragment_to_nav_homepage);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        return v;
    }
}