package com.example.mobiiliprojekti.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mobiiliprojekti.R;

import java.util.ArrayList;

public class ReminderItemArrayAdapter extends ArrayAdapter<ReminderItem> {

    public ReminderItemArrayAdapter(Context context, ArrayList<ReminderItem> reminderItems) {
        super(context, 0, reminderItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReminderItem reminderitem = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_list_item, parent, false);
        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.color.darkBorder);
        }
        else {
            convertView.setBackgroundResource(R.color.lightBorder);
        }

        TextView item_name = convertView.findViewById(R.id.reminderListItem_name_TextView);
        item_name.setText(reminderitem.getName());
        item_name.setTextColor(Color.WHITE);

        TextView item_time = convertView.findViewById(R.id.reminderListItem_time_TextView);
        item_time.setText(reminderitem.getTime());

        TextView item_category = convertView.findViewById(R.id.reminderListItem_category_TextView);
        item_category.setText(reminderitem.getCategory());
        item_category.setTextColor(Color.WHITE);

        //item_category.getResources().getColor(R.color.colorWhite);
        CheckBox item_checked = convertView.findViewById(R.id.reminderListItem_checked_CheckBox);

        // Set checkboxes
        if (reminderitem.getChecked() == 1) {
            item_checked.setChecked(true);
        } else {
            item_checked.setChecked(false);
        }

        //Set textcolor based on the category
        switch (reminderitem.getCategory()) {   //Drink, Eat, Medication, Shower, Social, Toilet, Alert
            case "drink":
                item_time.setTextColor(ContextCompat.getColor(getContext(), R.color.drinkWater));
                break;
            case "eat":
                item_time.setTextColor(ContextCompat.getColor(getContext(), R.color.eat));
                break;
            case "medication":
                item_time.setTextColor(ContextCompat.getColor(getContext(), R.color.medicine));;
                break;
            case "shower":
                item_time.setTextColor(ContextCompat.getColor(getContext(), R.color.shower));
                break;
            case "social":
                item_time.setTextColor(ContextCompat.getColor(getContext(), R.color.meeting));
                break;
            case "toilet":
                item_time.setTextColor(ContextCompat.getColor(getContext(), R.color.toilet));
                break;
            case "warning":
                item_time.setTextColor(ContextCompat.getColor(getContext(), R.color.alert));
                break;
        }

        return convertView;
    }
}
