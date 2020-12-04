package com.toedro.fao.ui.wannaeat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.squareup.picasso.Picasso;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.RecipeQueryResult;
import com.toedro.fao.ui.Utils;
import com.toedro.fao.ui.recipes.RecipesFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WannaEatFragment extends Fragment {

    LinearLayout containerRecipes;

    ImageView imageView;
    LinearLayout linearLayout1, linearLayout2;
    MaterialCardView materialCardView;
    TextView textView1, textView2;
    RelativeLayout relativeLayout;
    MaterialButton materialButton;

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

    private ImageView createImageView() {
        ImageView newImage = new ImageView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Utils.convertDpToPixel(150) );
        newImage.setLayoutParams(layoutParams);
        newImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return newImage;
    }

    private LinearLayout createLinearLayout1() {
        LinearLayout newLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );
        newLayout.setLayoutParams(layoutParams);
        newLayout.setOrientation(LinearLayout.VERTICAL);
        return newLayout;
    }

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

    private RelativeLayout createRelativeLayout() {
        RelativeLayout newLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(0, Utils.convertDpToPixel(8), 0, 0);
        newLayout.setLayoutParams(layoutParams);
        return newLayout;
    }

    private class MyTitle extends androidx.appcompat.widget.AppCompatTextView {
        public MyTitle(Context context) {
            super(context, null, R.attr.textAppearanceHeadline6);
        }
    }

    private TextView createTextView1() {
        TextView newText = new MyTitle(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        newText.setLayoutParams(layoutParams);
        return newText;
    }

    private class MyDescription extends androidx.appcompat.widget.AppCompatTextView {
        public MyDescription(Context context) {
            super(context, null, R.attr.textAppearanceBody2);
        }
    }

    private TextView createTextView2() {
        TextView newText = new MyDescription(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(0, Utils.convertDpToPixel(8), 0, 0);
        newText.setLayoutParams(layoutParams);

        /*
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int color = typedValue.data;
        newText.setTextColor(color);
        */

        newText.setTextColor(Color.GRAY);

        return newText;
    }

    private class MyMaterialButton extends MaterialButton {
        public MyMaterialButton(Context context) {
            super(context, null, R.attr.borderlessButtonStyle);
        }
    }

    private MaterialButton createMaterialButton() {
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
        newButton.setText(R.string.action_1);
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

        Log.d("KCAL", "BMR -> " + String.valueOf(bmr));
        Log.d("KCAL", "STEPS -> " + String.valueOf(stepsCals));
        Log.d("KCAL", "MEALS -> " + String.valueOf(mealsCals));
        Log.d("KCAL", String.valueOf(kcals));

        if (kcals <= 0) {

        } else {
            containerRecipes = (LinearLayout) root.findViewById(R.id.container_wannaeat);
            for (RecipeQueryResult recipe : App.getDBInstance().recipeDAO().getRecipes(0.0, 1000.0)) {
                Log.d("RECIPE", "1");
                materialCardView    = createMaterialCardView();
                imageView           = createImageView();
                linearLayout1       = createLinearLayout1();
                linearLayout2       = createLinearLayout2();
                relativeLayout      = createRelativeLayout();
                textView1           = createTextView1();
                textView2           = createTextView2();
                materialButton      = createMaterialButton();

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

        return root;
    }
}