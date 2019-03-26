package com.example.mobiiliprojekti.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderItemDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReminderItemContract.ReminderItem.TABLE_NAME + " (" +
                    ReminderItemContract.ReminderItem._ID + " INTEGER PRIMARY KEY," +
                    ReminderItemContract.ReminderItem.COLUMN_NAME_NAME + " TEXT," +
                    ReminderItemContract.ReminderItem.COLUMN_NAME_TIME + " TEXT," +
                    ReminderItemContract.ReminderItem.COLUMN_NAME_CHECKED + " INTEGER )" ;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ReminderItemContract.ReminderItem.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ReminderItemDatabase.db";

    public ReminderItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
