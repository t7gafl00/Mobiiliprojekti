package com.example.mobiiliprojekti.model;

import java.io.Serializable;

public class ReminderItem implements Serializable {
    int db_id = 0;
    String time = null;
    String category = null;
    String name = null;
    int checked = 0;

    public ReminderItem(String time, String name, String category, int checked) {
        this.time = time;
        this.name = name;
        this.category = category;
        this.checked = checked;
    }

    public ReminderItem(int db_id, String time, String name, String category, int checked) {
        this.db_id = db_id;
        this.time = time;
        this.name = name;
        this.category = category;
        this.checked = checked;
    }

    public long getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
