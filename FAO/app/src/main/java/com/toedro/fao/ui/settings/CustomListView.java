package com.toedro.fao.ui.settings;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CustomListView extends LinearLayout {

    public CustomListView(Context context) {
        super(context);

        setOrientation(LinearLayout.HORIZONTAL);

        CheckBox checkBox = new CheckBox(context);
        EditText text = new EditText(context);

        addView(checkBox);
        addView(text);
    }
}

