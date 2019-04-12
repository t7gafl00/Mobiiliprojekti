package com.example.mobiiliprojekti.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mobiiliprojekti.R;

import java.util.ArrayList;
import java.util.List;

public class DropDownMenuAdapter extends ArrayAdapter<String> {

    public DropDownMenuAdapter(Context context, ArrayList<String> strings){
        super(context, 0, strings);
        Log.d("kimmo", "Adapterin luontifunktio");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        String stringCategory = getItem(position).toString();

        if (view == null) {
            Log.d("", "null view");
            int layoutId = R.layout.drop_down_menu_item_custom;
            view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }

        TextView categoryText = view.findViewById(R.id.txtCategory);
        categoryText.setText(stringCategory);

        return view;
    }
}