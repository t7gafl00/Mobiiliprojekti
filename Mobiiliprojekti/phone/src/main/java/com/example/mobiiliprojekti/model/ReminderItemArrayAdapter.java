package com.example.mobiiliprojekti.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

        TextView item_name = convertView.findViewById(R.id.reminderListItem_name_TextView);
        item_name.setText(reminderitem.getName());
        TextView item_time = convertView.findViewById(R.id.reminderListItem_time_TextView);
        item_time.setText(reminderitem.getTime());
        CheckBox item_checked = convertView.findViewById(R.id.reminderListItem_checked_CheckBox);

        // Set checkboxes
        if (reminderitem.getChecked() == 1) {
            item_checked.setChecked(true);
        } else {
            item_checked.setChecked(false);
        }

        return convertView;
    }
}
