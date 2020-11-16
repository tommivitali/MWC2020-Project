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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RecipesFragment extends Fragment {
    private FirebaseFirestore db;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);

        containerRecipes = (LinearLayout) root.findViewById(R.id.container_recipes);

        List<Recipe> recipeList = App.getDBInstance().recipeDAO().getRecipes();

        for(Recipe recipe : recipeList) {
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
            textView2.setText(recipe.getType());

            linearLayout2.addView(textView1);
            linearLayout2.addView(textView2);
            relativeLayout.addView(linearLayout2);
            relativeLayout.addView(materialButton);
            linearLayout1.addView(imageView);
            linearLayout1.addView(relativeLayout);
            materialCardView.addView(linearLayout1);
            containerRecipes.addView(materialCardView);
        }






























        // Reading from Firebase Cloud Firestore
        db = FirebaseFirestore.getInstance();

        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();


                            }
                        } else {
                            Log.i(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return root;
    }
}