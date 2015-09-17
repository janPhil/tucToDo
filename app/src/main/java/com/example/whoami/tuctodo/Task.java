package com.example.whoami.tuctodo;

import android.app.Activity;

/**
 * Created by whoami on 10.09.15.
 */
public class Task extends Activity {

    //Attributes

    public String getDescription() {
        return description;
    }

    private String description;

    public String getDate() {
        return date;
    }

    private String date;

    public String getTime() {
        return time;
    }

    private String time;

    public String getPlace() {
        return place;
    }

    private String place;

    public String getTypeOfTask() {
        return typeOfTask;
    }

    private String typeOfTask;

    public String getColor() {
        return color;
    }

    private String color;

    Task(String desc, String da, String ti, String pl, String ty){

        this.description = desc;
        if (da != null) {
            this.date = da;
        } else {
            this.date = "00 00 0000";
        }
        this.time = ti;
        this.place = pl;
        this.typeOfTask = ty;

        switch (typeOfTask) {
            case "Arbeit":
                this.color = "#ff8080";
            case "Freizeit":
                this.color = "#b4eeb4";
            case "Andere":
                this.color = "#315a97";
            default:
                this.color = "#c0c0c0";
        }
    }

    public String printTask(){

        String output = this.description + " " + this.date + " " + this.time + " " + this.place + " " + this.typeOfTask;
        return output;
    }

}
