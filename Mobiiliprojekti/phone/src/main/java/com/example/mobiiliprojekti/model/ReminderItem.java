package com.example.mobiiliprojekti.model;

import java.io.Serializable;

public class ReminderItem implements Serializable {
    String name = null;
    String time = null;
    int checked = 0;
    int db_id = 0;

    public ReminderItem(String name, String time, int checked) {
        this.name = name;
        this.time = time;
        this.checked = checked;
    }

    public ReminderItem(int db_id, String name, String time, int checked) {
        this.db_id = db_id;
        this.name = name;
        this.time = time;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public long getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }
}
