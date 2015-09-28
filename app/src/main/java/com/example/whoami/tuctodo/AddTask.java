package com.example.whoami.tuctodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.example.whoami.tuctodo.db.TaskContract;
import com.example.whoami.tuctodo.db.TaskDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by whoami on 06.09.15.
 */
public class AddTask extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private String LOG_TAG = getClass().getSimpleName();
    private TaskDBHelper dbHelper;
    private String description;
    private String place;
    private String typeOfTask;
    private boolean startButtonClicked = false;
    private boolean stopButtonClicked = false;
    private boolean beginDateButtonClicked = false;
    private boolean endDateButtonClicked = false;
    private Dialog startTimeDialog;
    private Dialog endTimeDialog;
    private EditText taskEditText;
    private EditText placeEditText;
    private Button beginDateButton;
    private Button endDateButton;
    private Button startTime;
    private Button stopTime;
    private Button addButton;
    private Calendar beginDate;
    private String beginDateString;
    private Calendar endDate;
    private String endDateString;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        beginDateButton = (Button) findViewById(R.id.beginDate);
        beginDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginDateButtonClicked = true;
                Dialog dateDialog = onCreateCalendarDialog();
                dateDialog.show();
            }
        });

        endDateButton = (Button) findViewById(R.id.endDate);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDateButtonClicked = true;
                Dialog dateDialog = onCreateCalendarDialog();
                dateDialog.show();
            }
        });

        addButton = (Button) findViewById(R.id.addTaskButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskEditText = (EditText) findViewById(R.id.addTaskEditText);
                description = taskEditText.getText().toString();
                placeEditText = (EditText) findViewById(R.id.place_editText);
                place = placeEditText.getText().toString();
                addTaskToDatabase(description, place, typeOfTask, beginDateString, endDateString);
            }
        });

        Button work_button = (Button) findViewById(R.id.work_button);
        work_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTask = "#ff8080";
                RelativeLayout rel = (RelativeLayout) findViewById(R.id.layout_add_task);
                rel.setBackgroundColor(Color.parseColor("#ff8080"));
            }
        });
        Button free_button = (Button) findViewById(R.id.free_button);
        free_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTask = "#b4eeb4";
                RelativeLayout rel = (RelativeLayout) findViewById(R.id.layout_add_task);
                rel.setBackgroundColor(Color.parseColor("#b4eeb4"));
            }
        });
        Button else_button = (Button) findViewById(R.id.else_button);
        else_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTask = "#315a97";
                RelativeLayout rel = (RelativeLayout) findViewById(R.id.layout_add_task);
                rel.setBackgroundColor(Color.parseColor("#315a97"));
            }
        });

        Button calEntry = (Button) findViewById(R.id.addToCal);
        calEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertEntry();
            }
        });

        startTime = (Button) findViewById(R.id.startTimeButton);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonClicked = true;
                startTimeDialog = onCreateTimeDialog();
                startTimeDialog.show();
            }
        });

        stopTime = (Button) findViewById(R.id.endTimeButton);
        stopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButtonClicked = true;
                endTimeDialog = onCreateTimeDialog();
                endTimeDialog.show();
            }
        });
    }

    public void addTaskToDatabase(String desc, String pl, String ty, String beda, String enda){
        dbHelper = new TaskDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(TaskContract.Columns.DESC, desc);
        values.put(TaskContract.Columns.PLACE, pl);
        values.put(TaskContract.Columns.TYPEOFTASK, ty);
        values.put(TaskContract.Columns.BEGINDATE, beda);
        values.put(TaskContract.Columns.ENDDATE, enda);
        long newId;
        newId = db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(LOG_TAG, "addTaskToDatabase " + values.valueSet() + " " + newId);
        db.close();
        Log.d(LOG_TAG, "Duration: " + getDuration(beginDate.getTime(), endDate.getTime(), TimeUnit.MINUTES));
    }

    public Dialog onCreateTimeDialog(){
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return new TimePickerDialog(AddTask.this, this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (startButtonClicked) {
            Log.d(LOG_TAG, "starttime: " + hourOfDay + " " + minute);
            beginDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            beginDate.set(Calendar.MINUTE, minute);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            beginDateString = format.format(beginDate.getTime());
            Log.d(LOG_TAG, "BeginDate: " + beginDateString);
            startTime.setText(hourOfDay + " " + minute);
            startButtonClicked = false;
        } else if (stopButtonClicked){
            Log.d(LOG_TAG, "endtime: " + hourOfDay + " " + minute);
            endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endDate.set(Calendar.MINUTE, minute);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            endDateString = format.format(endDate.getTime());
            Log.d(LOG_TAG, "EndDate: " + endDateString);
            stopButtonClicked = false;
            stopTime.setText(hourOfDay + " " + minute);
        }
    }

    public Dialog onCreateCalendarDialog(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(AddTask.this, this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        if (beginDateButtonClicked){
            beginDate = Calendar.getInstance();
            beginDate.set(year,monthOfYear,dayOfMonth);
            beginDateString = formatDateToString(beginDate.getTime());
            Log.d(LOG_TAG, "BeginDate: " + beginDateString);
            beginDateButtonClicked = false;
            beginDateButton.setText(dayOfMonth + " " + monthOfYear);
        }
        else if (endDateButtonClicked){
            endDate = Calendar.getInstance();
            endDate.set(year,monthOfYear,dayOfMonth);
            endDateString = formatDateToString(endDate.getTime());
            Log.d(LOG_TAG, "EndDate: " + endDateString);
            endDateButtonClicked = false;
            endDateButton.setText(dayOfMonth + " " + monthOfYear);
        }
    }

    private String formatDateToString(Date c){
        String output;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        output = format.format(c.getTime());
        return output;
    }

    private void insertEntry() {

        ContentValues values = new ContentValues();
        ContentResolver mContentResolver = getApplicationContext().getContentResolver();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, description);
        values.put(CalendarContract.Events.EVENT_LOCATION, place);
        long startTime = beginDate.getTimeInMillis();
        values.put(CalendarContract.Events.DTSTART, startTime);
        long endTime = endDate.getTimeInMillis();
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        Uri uri = mContentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.i(LOG_TAG,"Kalendereintrag erstellt");
    }

    private long getDuration(Date begin, Date end, TimeUnit timeUnit){

        long diff = end.getTime() -begin.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }
}
