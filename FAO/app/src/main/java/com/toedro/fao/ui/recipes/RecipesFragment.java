package com.toedro.fao.ui.recipes;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.Recipe;
import com.toedro.fao.ui.Utils;

import com.squareup.picasso.Picasso;

import java.util.List;
/**
 * The RecipesFragment class is the class that creates the relative fragment allowing to watch a
 * list of cards with all the recipes. Since we don't have a fixed number of recipes we have to also
 * generate the layout dynamically, getting the LinearLayout container and inflating all the
 * elements inside.
 */
public class RecipesFragment extends Fragment {

    LinearLayout containerRecipes;

    ImageView imageView;
    LinearLayout linearLayout1, linearLayout2;
    MaterialCardView materialCardView;
    TextView textView1, textView2, textViewContent;
    RelativeLayout relativeLayout;
    MaterialButton materialButton;

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
     * Create the button to show/hide the recipe content.
     * @param textViewContent textview in which the function puts the recipe content
     * @param content a string with the recipe content
     * @return a MaterialButton that initially shows "View", and when clicked put the content
     * parameter string into the textViewContent parameter textview
     */
    private MaterialButton createMaterialButton(TextView textViewContent, String content) {
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
        newButton.setText(R.string.recipes_button_view);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newButton.getText().toString().equals(getString(R.string.recipes_button_view))) {
                    textViewContent.setText(content);
                    newButton.setText(R.string.recipes_button_hide);
                } else {
                    textViewContent.setText("");
                    newButton.setText(R.string.recipes_button_view);
                }
            }
        });
        return newButton;
    }

    /**
     * Create an empty text view with an 8dp margin on all the sides but the upper one, with 88dp.
     * @return an empty TextView
     */
    private TextView createTextViewContent() {
        TextView textView = new TextView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.setMargins(
                Utils.convertDpToPixel(8),
                Utils.convertDpToPixel(88),
                Utils.convertDpToPixel(8),
                Utils.convertDpToPixel(8) );
        textView.setLayoutParams(layoutParams);
        textView.setText("");
        return textView;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        // Get the main container
        containerRecipes = (LinearLayout) root.findViewById(R.id.container_recipes);
        // Get the recipes from the DB
        List<Recipe> recipeList = App.getDBInstance().recipeDAO().getRecipes();
        // For each recipe create the layout components, arrange and fill them up
        for(Recipe recipe : recipeList) {
            materialCardView    = createMaterialCardView();
            imageView           = createImageView();
            linearLayout1       = createLinearLayout1();
            linearLayout2       = createLinearLayout2();
            relativeLayout      = createRelativeLayout();
            textView1           = createTextView1();
            textView2           = createTextView2();
            textViewContent     = createTextViewContent();
            materialButton      = createMaterialButton(textViewContent, recipe.getText());

            // Picasso library to show an image in an ImageView from the URL
            Picasso.get().load(recipe.getImage()).into(imageView);
            textView1.setText(recipe.getName());
            textView2.setText(recipe.getType());

            relativeLayout.addView(textViewContent);
            linearLayout2.addView(textView1);
            linearLayout2.addView(textView2);
            relativeLayout.addView(linearLayout2);
            relativeLayout.addView(materialButton);
            linearLayout1.addView(imageView);
            linearLayout1.addView(relativeLayout);
            materialCardView.addView(linearLayout1);
            containerRecipes.addView(materialCardView);
        }

        return root;
    }
}