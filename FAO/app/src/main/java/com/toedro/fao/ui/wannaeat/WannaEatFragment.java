package com.toedro.fao.ui.wannaeat;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.toedro.fao.App;
import com.toedro.fao.Preferences;
import com.toedro.fao.R;
import com.toedro.fao.db.Pantry;
import com.toedro.fao.db.RecipeQueryResult;
import com.toedro.fao.ui.Utils;
import com.toedro.fao.ui.settings.SettingsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RecipesFragment class is the class that creates the related fragment allowing to watch a
 * list of cards with all the recipes that satisfy the following requirements: all required recipes
 * are in the Pantry, and the calories are based on how much the user burnt till now. Since we don't
 * have a fixed number of recipes we have to also generate the layout dynamically, getting the
 * LinearLayout container and inflating all the elements inside. Since it has to generate a layout
 * programmatically very similar to the recipes fragment, this class is pretty close to the recipes
 * one.
 */
public class WannaEatFragment extends Fragment {

    LinearLayout containerRecipes;

    ImageView imageView;
    LinearLayout linearLayout1, linearLayout2;
    MaterialCardView materialCardView;
    TextView textView1, textView2;
    RelativeLayout relativeLayout;
    MaterialButton materialButton;
    double min, max;

    /**
     * Create a MaterialCardView with an 8dp margin.
     * @return a MaterialCardView
     */
    private MaterialCardView createMaterialCardView() {
        MaterialCardView newCard = new MaterialCardView(getContext());
        MaterialCardView.LayoutParams layoutParams = new MaterialCardView.LayoutParams(
                MaterialCardView.LayoutParams.MATCH_PARENT,
                MaterialCardView.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(
                Utils.convertDpToPixel(8),
                Utils.convertDpToPixel(8),
                Utils.convertDpToPixel(8),
                Utils.convertDpToPixel(8) );
        newCard.setLayoutParams(layoutParams);
        return newCard;
    }

    /**
     * Create an ImageView with a 150dp fixed height and that crops the image source at the center.
     * @return an ImageView
     */
    private ImageView createImageView() {
        ImageView newImage = new ImageView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Utils.convertDpToPixel(150) );
        newImage.setLayoutParams(layoutParams);
        newImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return newImage;
    }

    /**
     * Create a simple LinearLayout.
     * @return an empty LinearLayout
     */
    private LinearLayout createLinearLayout1() {
        LinearLayout newLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );
        newLayout.setLayoutParams(layoutParams);
        newLayout.setOrientation(LinearLayout.VERTICAL);
        return newLayout;
    }

    /**
     * Create a simple LinearLayout with 16dp paddings.
     * @return an empty LinearLayout
     */
    private LinearLayout createLinearLayout2() {
        LinearLayout newLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );
        newLayout.setLayoutParams(layoutParams);
        newLayout.setOrientation(LinearLayout.VERTICAL);
        newLayout.setPadding(
                Utils.convertDpToPixel(16),
                Utils.convertDpToPixel(16),
                Utils.convertDpToPixel(16),
                Utils.convertDpToPixel(16) );
        return newLayout;
    }

    /**
     * Create an empty RelativeLayout.
     * @return an empty RelativeLayout
     */
    private RelativeLayout createRelativeLayout() {
        RelativeLayout newLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(0, Utils.convertDpToPixel(8), 0, 0);
        newLayout.setLayoutParams(layoutParams);
        return newLayout;
    }

    /**
     * Class MyTitle that simply extends an AppCompatTextView but with the textAppearanceHeadline6.
     */
    private class MyTitle extends androidx.appcompat.widget.AppCompatTextView {
        public MyTitle(Context context) {
            super(context, null, R.attr.textAppearanceHeadline6);
        }
    }

    /**
     * Create an empty MyTitle text view, suitable to show the recipe name.
     * @return an empty MyTitle text view
     */
    private TextView createTextView1() {
        TextView newText = new MyTitle(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        newText.setLayoutParams(layoutParams);
        return newText;
    }

    /**
     * Class MyDescription that simply extends an AppCompatTextView but with the textAppearanceBody2.
     */
    private class MyDescription extends androidx.appcompat.widget.AppCompatTextView {
        public MyDescription(Context context) {
            super(context, null, R.attr.textAppearanceBody2);
        }
    }

    /**
     * Create an empty MyDescription text view, suitable to show the recipe category.
     * @return an empty MyDescription text view
     */
    private TextView createTextView2() {
        TextView newText = new MyDescription(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(0, Utils.convertDpToPixel(8), 0, 0);
        newText.setLayoutParams(layoutParams);

        newText.setTextColor(Color.GRAY);

        return newText;
    }

    /**
     * Class MyMaterialButton that simply extends a MaterialButton without borders.
     */
    private class MyMaterialButton extends MaterialButton {
        public MyMaterialButton(Context context) {
            super(context, null, R.attr.borderlessButtonStyle);
        }
    }

    /**
     * Create the button to show the fragment with the recipe content.
     * @param recipeID the id of the recipe, since we have to show a specific recipe at the click of
     *                 the button
     * @return a MaterialButton that initially shows "Eat It!", then a dialog and if the user
     * answers positively we navigate to the recipeDetail fragment
     */
    private MaterialButton createMaterialButton(String recipeID) {
        MaterialButton newButton = new MyMaterialButton(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(
                Utils.convertDpToPixel(8),
                Utils.convertDpToPixel(8),
                Utils.convertDpToPixel(8),
                Utils.convertDpToPixel(8) );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        newButton.setLayoutParams(layoutParams);
        newButton.setPadding(0, Utils.convertDpToPixel(16), 0, 0);
        newButton.setText(R.string.wannaeat_button_eat);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(R.string.wannaeat_dialog_title)
                        .setMessage(R.string.wannaeat_dialog_message)
                        .setNegativeButton(R.string.wannaeat_dialog_negative, null)
                        .setPositiveButton(R.string.wannaeat_dialog_positive,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("recipeId", recipeID);
                                        Navigation.findNavController(getView()).navigate(R.id.action_wannaEatFragment_to_recipeDetailFragment, bundle);
                                    }
                                })
                        .show();
            }
        });

        return newButton;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wanna_eat, container, false);

        Double bmr = Utils.calculateDailyBMR(getActivity(), getContext());
        Double stepsCals = Utils.convertStepsToCal(App.getDBInstance().stepDAO()
                        .getDaySteps(new SimpleDateFormat(getString(R.string.date_layout_DB))
                                .format(new Date())),
                getActivity(), getContext());
        Double mealsCals = App.getDBInstance().caloriesDao().getSumCalories(
                new SimpleDateFormat(getString(R.string.date_layout_DB)).format(new Date()));
        if (mealsCals == null) mealsCals = 0.0; //in case null

        Double kcals = bmr + stepsCals - mealsCals;

        //switch value range kcals (to ask a dietician)
        switch(Preferences.getCalChoice(getActivity(), getContext())) {
            case DIMAGRIRE:
                min = 0.0;
                max = kcals - 150;
                break;
            case INGRASSARE:
                min = 0.0;
                max = kcals + 200;
                break;
            default: //MANTENERE
                min = 0.0;
                max = kcals;
        }


        if (kcals <= 0) {
            Snackbar.make(root, R.string.wannaeat_noeat, Snackbar.LENGTH_LONG).show();
        } else {
            // Get the main container
            containerRecipes = (LinearLayout) root.findViewById(R.id.container_wannaeat);
            // Get the recipes from the DB and for each recipe create the layout components,
            // arrange and fill them up
            for (RecipeQueryResult recipe : App.getDBInstance().recipeDAO().getRecipes(min, max)) {
                materialCardView    = createMaterialCardView();
                imageView           = createImageView();
                linearLayout1       = createLinearLayout1();
                linearLayout2       = createLinearLayout2();
                relativeLayout      = createRelativeLayout();
                textView1           = createTextView1();
                textView2           = createTextView2();
                materialButton      = createMaterialButton(recipe.getId());

                // Picasso library to show an image in an ImageView from the URL
                Picasso.get().load(recipe.getImage()).into(imageView);
                textView1.setText(recipe.getName());
                textView2.setText("Kcal: " + String.valueOf(recipe.getKcal()));

                linearLayout2.addView(textView1);
                linearLayout2.addView(textView2);
                relativeLayout.addView(linearLayout2);
                relativeLayout.addView(materialButton);
                linearLayout1.addView(imageView);
                linearLayout1.addView(relativeLayout);
                materialCardView.addView(linearLayout1);
                containerRecipes.addView(materialCardView);
            }
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_wannaEatFragment_to_nav_homepage);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        return root;
    }
}