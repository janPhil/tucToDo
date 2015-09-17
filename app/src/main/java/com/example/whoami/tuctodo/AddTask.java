package com.example.whoami.tuctodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

/**
 * Created by whoami on 06.09.15.
 */
public class AddTask extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    private String LOG_TAG = getClass().getSimpleName();
    private TaskDBHelper dbHelper;

    private String description;
    private String place;
    private String typeOfTask;
    private String date;
    private String startTime;
    private String endTime;
    private Integer duration;
    private boolean startButtonClicked = false;
    private boolean stopButtonClicked = false;
    private Dialog dateDialog;
    private Dialog startTimeDialog;
    private Dialog endTimeDialog;
    private EditText taskEditText;
    private EditText placeEditText;
    Button dateButton;
    Button addButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        dateButton = (Button) findViewById(R.id.task_date_button);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog = onCreateCalendarDialog();
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
                addTaskToDatabase(description, place, typeOfTask, date);
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

        Button startTime = (Button) findViewById(R.id.startTimeButton);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonClicked = true;
                stopButtonClicked = false;
                startTimeDialog = onCreateTimeDialog();
                startTimeDialog.show();
            }
        });

        Button stopTime = (Button) findViewById(R.id.endTimeButton);
        stopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonClicked = false;
                stopButtonClicked = true;
                endTimeDialog = onCreateTimeDialog();
                endTimeDialog.show();
            }
        });
    }

    public void addTaskToDatabase(String desc, String pl, String ty, String da){
        dbHelper = new TaskDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(TaskContract.Columns.DESC, desc);
        values.put(TaskContract.Columns.PLACE, pl);
        values.put(TaskContract.Columns.TYPEOFTASK, ty);
        values.put(TaskContract.Columns.DATE, da);
        long newId;
        newId = db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(LOG_TAG, "addTaskToDatabase " + values.valueSet() + " " + newId);
        //Task task = new Task(description,date.toString(),null,place,typeOfTask);
        db.close();
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
            startButtonClicked = false;
            startTime = String.valueOf(hourOfDay) + String.valueOf(minute);
        } else if (stopButtonClicked){

            endTime = String.valueOf(hourOfDay) + String.valueOf(minute);
            stopButtonClicked = false;
            duration = Integer.parseInt(endTime)-Integer.parseInt(startTime);
            Log.d(LOG_TAG, "stopttime: " + hourOfDay + " " + minute + " " + duration);

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
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-MMM-yyyy");
        String formated = simpleDateFormat.format(cal.getTime());
        Log.d(LOG_TAG, "onDateSet: " + formated);
        date = formated;
    }

    private void insertEntry(/*String pTitle, String pDescription, String pLocation*/) {

        /*ContentValues values = new ContentValues();
        ContentResolver mContentResolver = getApplicationContext().getContentResolver();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, pTitle);
        //values.put(CalendarContract.Events.DESCRIPTION, pDescription);
        values.put(CalendarContract.Events.EVENT_LOCATION, pLocation);
        //values.put(CalendarContract.Events.DTSTART, pStartTimestamp);
        //values.put(CalendarContract.Events.DTEND, pEndTimestamp);
        values.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName()); //get the Timezone
        Uri uri = mContentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.i(LOG_TAG,"calendar entry inserted");
        */
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
        calendarIntent.putExtra(CalendarContract.Events.TITLE, description);
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, place);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 0);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTime().getTime());
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 30);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTime().getTime());
        AddTask.this.startActivity(calendarIntent);

    }
}
