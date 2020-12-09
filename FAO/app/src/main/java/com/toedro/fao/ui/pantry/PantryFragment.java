package com.toedro.fao.ui.pantry;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.Pantry;
import com.toedro.fao.ui.Utils;

import java.util.List;

public class PantryFragment extends Fragment {

    LinearLayout containerPantries;
    ImageView imageView;
    LinearLayout linearLayout1, linearLayout2;
    MaterialCardView materialCardView;
    TextView textView1, textView2, textView3;
    RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pantry, container, false);

        containerPantries = (LinearLayout) root.findViewById(R.id.pantry_recipes);

        List<Pantry> pantryList = App.getDBInstance().pantryDAO().getPantry();

        for(Pantry pantry : pantryList) {
            materialCardView    = createMaterialCardView();
            imageView           = createImageView();
            linearLayout1       = createLinearLayout1();
            linearLayout2       = createLinearLayout2();
            relativeLayout      = createRelativeLayout();
            textView1           = createTextView1();
            textView2           = createTextView2();
            textView3           = createTextView3();

            Picasso.get().load(pantry.getImage()).into(imageView);
            textView1.setText(pantry.getName());
            textView2.setText("Cal on 100g = " + String.valueOf(pantry.getKcal()));
            textView3.setText("Quantity: " + String.valueOf(pantry.getQuantity()));

            linearLayout2.addView(textView1);
            linearLayout2.addView(textView2);
            linearLayout2.addView(textView3);
            relativeLayout.addView(linearLayout2);
            linearLayout1.addView(imageView);
            linearLayout1.addView(relativeLayout);
            materialCardView.addView(linearLayout1);
            containerPantries.addView(materialCardView);
        }

        return root;
    }
    public PantryFragment() {

    }

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
                Utils.convertDpToPixel(50) ); //getting smaller
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
        TextView newText = new PantryFragment.MyTitle(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        newText.setLayoutParams(layoutParams);
        return newText;
    }

    private class MyDescription extends androidx.appcompat.widget.AppCompatTextView {
        public MyDescription(Context context) {
            super(context, null, R.attr.textAppearanceBody1);
        }
    }
    private TextView createTextView2() {
        TextView newText = new PantryFragment.MyDescription(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(0, Utils.convertDpToPixel(8), 0, 0);
        newText.setLayoutParams(layoutParams);
        newText.setTextColor(Color.GRAY);

        return newText;
    }
    private TextView createTextView3() {
        TextView newText = new PantryFragment.MyDescription(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins(0, Utils.convertDpToPixel(8), 0, 0);
        newText.setLayoutParams(layoutParams);
        newText.setTextColor(Color.GRAY);

        return newText;
    }
}
