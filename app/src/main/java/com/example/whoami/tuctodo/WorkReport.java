package com.example.whoami.tuctodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.whoami.tuctodo.db.TaskContract;
import com.example.whoami.tuctodo.db.TaskDBHelper;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WorkReport extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String LOG_TAG = getClass().getSimpleName();
    TaskDBHelper dbHelper;
    Button push;
    TextView hours;
    NumberPicker monthPicker;
    DecimalFormat dc = new DecimalFormat("00");
    Integer month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);

        hours = (TextView) findViewById(R.id.count);

        push = (Button) findViewById(R.id.button);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWorkDuration();

            }
        });
        monthPicker = (NumberPicker) findViewById(R.id.numberPicker);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                month = newVal;
                String s = String.format("%02d", month);
                month = Integer.valueOf(s);
                Log.d(LOG_TAG, "Month: " + month + " " + s);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Date StringToDate(String d){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "Date: " + convertedDate.toString());
        return  convertedDate;
    }

    private long getDuration(Date begin, Date end, TimeUnit timeUnit){

        long diff = end.getTime() -begin.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    private void getWorkDuration(){

        dbHelper = new TaskDBHelper(getApplicationContext());
        String typeOfTask = "#ff8080";
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        Cursor cursor = sql.query(
                TaskContract.TABLE,
                new String[]{TaskContract.Columns.BEGINDATE, TaskContract.Columns.ENDDATE},
                TaskContract.Columns.TYPEOFTASK + "=?",
                new String[]{typeOfTask},
                null,
                null,
                null,
                null);
        long dur = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            dur += getDuration(StringToDate(cursor.getString(0)), StringToDate(cursor.getString(1)), TimeUnit.MINUTES);
            Log.d(LOG_TAG, "Duration: " + getDuration(StringToDate(cursor.getString(0)), StringToDate(cursor.getString(1)), TimeUnit.MINUTES)
            + cursor.getString(0) + " " +cursor.getString(1));
        }
        cursor.close();
        sql.close();
        double durence = (double)dur;
        hours.setText("" + durence);
    }


    public Dialog onCreateCalendarDialog(){
        DatePickerDialog picker = new DatePickerDialog(WorkReport.this, this,2014,1,24);
        try {
            Field f[] = picker.getClass().getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals("mYearPicker")) {
                    field.setAccessible(true);
                    Object yearPicker = new Object();
                    yearPicker = field.get(picker);
                    ((View) yearPicker).setVisibility(View.GONE);
                }
            }
        }
        catch (SecurityException e) {
            Log.d("ERROR", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        }
        catch (IllegalAccessException e) {
            Log.d("ERROR", e.getMessage());
        }
        return picker;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}
