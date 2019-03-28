package com.example.mobiiliprojekti.model.db;

import android.provider.BaseColumns;

public class ReminderItemContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ReminderItemContract() {}

    /* Inner class that defines the table contents */
    public static class ReminderItem implements BaseColumns {
        public static final String TABLE_NAME = "reminderItems";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_CHECKED = "checked";
    }
}
