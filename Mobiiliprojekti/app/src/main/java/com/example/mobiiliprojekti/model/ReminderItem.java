package com.example.mobiiliprojekti.model;

public class ReminderItem {
    String name = null;
    String time = null;
    int checked = 0;

    public ReminderItem(String name, String time, int checked) {
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
}
