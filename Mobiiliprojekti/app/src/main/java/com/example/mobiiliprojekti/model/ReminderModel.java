package com.example.mobiiliprojekti.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mobiiliprojekti.model.db.ReminderItemContract;
import com.example.mobiiliprojekti.model.db.ReminderItemDbHelper;

public class ReminderModel {

    ReminderItemDbHelper mDbHelper = null;

    public ReminderModel(Context context) {
        this.mDbHelper = new ReminderItemDbHelper(context);
    }

    /* Insert ReminderItem into db */
    public void addReminderItemToDb(ReminderItem addable) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ReminderItemContract.ReminderItem.COLUMN_NAME_NAME, addable.name);
        values.put(ReminderItemContract.ReminderItem.COLUMN_NAME_TIME, addable.time);
        values.put(ReminderItemContract.ReminderItem.COLUMN_NAME_CHECKED, addable.checked);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ReminderItemContract.ReminderItem.TABLE_NAME, null, values);
    }

    /* Delete ReminderItem from db */
    public void deleteReminderItemFromDb(ReminderItem deletable) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = ReminderItemContract.ReminderItem.COLUMN_NAME_NAME + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {deletable.getName()};
        // Issue SQL statement.
        int deletedRows = db.delete(ReminderItemContract.ReminderItem.TABLE_NAME, selection, selectionArgs);
    }

    /* Get items from db based on a filter value and order them by time of day
     *  The filter value could be used to display items based on what category they belong to */
    public Cursor getReminderItemsList(int filter_value) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = null;

        switch (filter_value) {
            case 0:
                cursor = db.rawQuery(
                        "SELECT name, time, checked FROM reminderItems ORDER BY strftime('%HH:%MM', time) ASC", new String[0]);
                break;

            case 1:
                // The query here could be useful to filter objects based on what category they belong to
                /*
                Cursor cursor = db.rawQuery(
                        "SELECT name, description, (strftime('%d.%m.%Y', date)) AS date, checked FROM toDoItems " +
                                "WHERE date = date('now')", new String[0]);
                                */
                break;

        }

        return cursor;
    }

    /* Set the checked field for an item in db based on checked_value */
    public void setChecked(ReminderItem ReminderItem, int checked_value) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("checked", checked_value);

        String where = ReminderItemContract.ReminderItem.COLUMN_NAME_NAME + " LIKE ?";
        String[] whereArgs = {ReminderItem.getName()};

        db.update(ReminderItemContract.ReminderItem.TABLE_NAME, cv, where, whereArgs);
    }
}
