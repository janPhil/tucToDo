package com.example.whoami.tuctodo;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.whoami.tuctodo.db.TaskContract;
import com.example.whoami.tuctodo.db.TaskDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by whoami on 06.09.15.
 */
public class AddTask extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private String LOG_TAG = getClass().getSimpleName();
    private TaskDBHelper dbHelper;
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
    private String month;
    private static final int PERMISSION_TO_WRITE_CAL = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        taskEditText = (EditText) findViewById(R.id.TaskTitel);
        placeEditText = (EditText) findViewById(R.id.PlaceTitel);

        beginDateButton = (Button) findViewById(R.id.startDateButton);
        beginDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginDateButtonClicked = true;
                Dialog dateDialog = onCreateCalendarDialog();
                dateDialog.show();
            }
        });

        endDateButton = (Button) findViewById(R.id.endDateButton);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDateButtonClicked = true;
                Dialog dateDialog = onCreateCalendarDialog();
                dateDialog.show();
            }
        });

        addButton = (Button) findViewById(R.id.addListButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addTaskToDatabase(typeOfTask, beginDateString, endDateString, month);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button work_button = (Button) findViewById(R.id.work_button);
        work_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTask = "#ff8080";
                LinearLayout rel = (LinearLayout) findViewById(R.id.linearLayoutBackround);
                rel.setBackgroundColor(Color.parseColor("#ff8080"));
            }
        });
        Button free_button = (Button) findViewById(R.id.free_button);
        free_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTask = "#b4eeb4";
                LinearLayout rel = (LinearLayout) findViewById(R.id.linearLayoutBackround);
                rel.setBackgroundColor(Color.parseColor("#b4eeb4"));
            }
        });
        Button else_button = (Button) findViewById(R.id.else_button);
        else_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfTask = "#315a97";
                LinearLayout rel = (LinearLayout) findViewById(R.id.linearLayoutBackround);
                rel.setBackgroundColor(Color.parseColor("#315a97"));
            }
        });

        Button calEntry = (Button) findViewById(R.id.addCalButton);
        calEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertEntry();
            }
        });

        startTime = (Button) findViewById(R.id.button9);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonClicked = true;
                startTimeDialog = onCreateTimeDialog();
                startTimeDialog.show();
            }
        });

        stopTime = (Button) findViewById(R.id.button2);
        stopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButtonClicked = true;
                endTimeDialog = onCreateTimeDialog();
                endTimeDialog.show();
            }
        });
    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = String.format(new SimpleDateFormat("MMM", Locale.US).format(cal.getTime()));
        month = monthName;
        Log.d("Monat: ", month);
        return monthName;
    }

    public void addTaskToDatabase(String ty, String beda, String enda, String mon) throws ParseException {
        if(getMonth(beda)!= getMonth(enda)){

        }
        month = getMonth(beda);
        Log.d(LOG_TAG, month);
        dbHelper = new TaskDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(TaskContract.Columns.DESC, taskEditText.getText().toString());
        values.put(TaskContract.Columns.PLACE, placeEditText.getText().toString());
        values.put(TaskContract.Columns.TYPEOFTASK, ty);
        values.put(TaskContract.Columns.BEGINDATE, beda);
        values.put(TaskContract.Columns.ENDDATE, enda);
        values.put(TaskContract.Columns.MONTH, month);
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
        String hour = String.format("%02d", hourOfDay);
        String min = String.format("%02d", minute);
        if (startButtonClicked) {
            Log.d(LOG_TAG, "starttime: " + hourOfDay + " " + minute);
            beginDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            beginDate.set(Calendar.MINUTE, minute);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            beginDateString = format.format(beginDate.getTime());
            Log.d(LOG_TAG, "BeginDate: " + beginDateString);
            startTime.setText(hour + ":" + min);
            startButtonClicked = false;
        } else if (stopButtonClicked){
            Log.d(LOG_TAG, "endtime: " + hour + " " + min);
            endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endDate.set(Calendar.MINUTE, minute);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            endDateString = format.format(endDate.getTime());
            Log.d(LOG_TAG, "EndDate: " + endDateString);
            stopButtonClicked = false;
            stopTime.setText(hour + ":" + min);
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
            monthOfYear+=1;
            Log.d(LOG_TAG, "BeginDate: " + monthOfYear);
            beginDateButtonClicked = false;
            beginDateButton.setText(beginDate.get(Calendar.DAY_OF_MONTH) + "." + monthOfYear + ".");
        }
        else if (endDateButtonClicked){
            endDate = Calendar.getInstance();
            endDate.set(year,monthOfYear,dayOfMonth);
            endDateString = formatDateToString(endDate.getTime());
            monthOfYear+=1;
            Log.d(LOG_TAG, "EndDate: " + endDateString);
            endDateButtonClicked = false;
            endDateButton.setText(endDate.get(Calendar.DAY_OF_MONTH) + "." + monthOfYear + ".");
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
        values.put(CalendarContract.Events.TITLE, taskEditText.getText().toString());
        values.put(CalendarContract.Events.EVENT_LOCATION, placeEditText.getText().toString());
        long startTime = beginDate.getTimeInMillis();
        values.put(CalendarContract.Events.DTSTART, startTime);
        long endTime = endDate.getTimeInMillis();
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_TO_WRITE_CAL);
        } else {
            Uri uri = mContentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
            Log.i(LOG_TAG,"Kalendereintrag erstellt");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_TO_WRITE_CAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    insertEntry();
                } else {

                }
                return;
            }
        }
    }

    private long getDuration(Date begin, Date end, TimeUnit timeUnit){
        long diff = end.getTime() -begin.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }
}
