package com.example.mobiiliprojekti.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mobiiliprojekti.R;

import java.util.ArrayList;

public class DropDownMenuAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private Context context;
    private int resource;
    private ArrayList<String> items;

    public DropDownMenuAdapter(Context context, @LayoutRes int resource, ArrayList<String> strings){
        super(context, 0, strings);
        Log.d("kimmo", "Adapterin luontifunktio");
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.resource = resource;
        items = strings;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){

        View view = inflater.inflate(resource, parent, false);

        String stringCategory = getItem(position);
        Log.d("kimmo", "stringCategory: " + stringCategory);
        TextView textView = view.findViewById(R.id.txtCategory);
        textView.setText(stringCategory);

        switch (textView.getText().toString().toLowerCase()){
            case("drink"):
                textView.setTextColor(ContextCompat.getColor(context, R.color.drinkWater));
                break;
            case("eat"):
                textView.setTextColor(ContextCompat.getColor(context, R.color.eat));
                break;
            case("medication"):
                textView.setTextColor(ContextCompat.getColor(context, R.color.medicine));
                break;
            case("shower"):
                textView.setTextColor(ContextCompat.getColor(context, R.color.shower));
                break;
            case("social"):
                textView.setTextColor(ContextCompat.getColor(context, R.color.meeting));
                break;
            case("toilet"):
                textView.setTextColor(ContextCompat.getColor(context, R.color.toilet));
                break;
            case("warning"):
                textView.setTextColor(ContextCompat.getColor(context, R.color.alert));
                break;
        }
        Log.d("kimmo", "switch completed");

        return view;
    }
}