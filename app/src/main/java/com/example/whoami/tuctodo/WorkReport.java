package com.example.whoami.tuctodo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.whoami.tuctodo.db.TaskContract;
import com.example.whoami.tuctodo.db.TaskDBHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class WorkReport extends AppCompatActivity {

    private String LOG_TAG = getClass().getSimpleName();
    TaskDBHelper dbHelper;
    Button january;
    Button february;
    Button march;
    Button april;
    Button mai;
    Button june;
    Button july;
    Button august;
    Button september;
    Button october;
    Button november;
    Button december;
    Button sendMailButton;
    TextView result;
    String month;
    private final int PERMISSION_TO_WRITE_FILE = 1;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);
        result = (TextView) findViewById(R.id.result);
        january = (Button) findViewById(R.id.button1);
        january.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("January");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        february = (Button) findViewById(R.id.button2);
        february.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("February");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        march = (Button) findViewById(R.id.button3);
        march.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("March");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        april = (Button) findViewById(R.id.button4);
        april.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("April");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mai = (Button) findViewById(R.id.button5);
        mai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("Mai");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        june = (Button) findViewById(R.id.button6);
        june.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("June");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        july = (Button) findViewById(R.id.button7);
        july.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("July");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        august = (Button) findViewById(R.id.button8);
        august.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("August");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        september = (Button) findViewById(R.id.button9);
        september.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("September");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        october = (Button) findViewById(R.id.button10);
        october.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("October");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        november = (Button) findViewById(R.id.button11);
        november.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("November");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        december = (Button) findViewById(R.id.button12);
        december.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setWorkDuration("December");
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sendMailButton = (Button) findViewById(R.id.sendMailButton);
        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendWorkReport();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Date StringToDate(String d) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "Date: " + convertedDate.toString());
        getMonth(d);
        return  convertedDate;
    }

    private String getMonth(String date) throws ParseException{
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM", Locale.US).format(cal.getTime());
        month = monthName.substring(0,3);
        Log.d("Monat: ", month);
        return monthName;
    }

    private long getDuration(Date begin, Date end, TimeUnit timeUnit){
        long diff = end.getTime() -begin.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    private long getWorkDuration(String m) throws ParseException, IOException {
        m = m.substring(0,3);
        dbHelper = new TaskDBHelper(getApplicationContext());
        String typeOfTask = "#ff8080";
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, " M ist: " + m + " und month ist: " + month);
        Cursor cursor = sql.query(
                TaskContract.TABLE,
                new String[]{TaskContract.Columns.BEGINDATE, TaskContract.Columns.ENDDATE},
                TaskContract.Columns.TYPEOFTASK + "=?" + " AND " + TaskContract.Columns.MONTH + " =? ",
                new String[]{typeOfTask, m},
                null,
                null,
                null,
                null);
        long d = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            d += getDuration(StringToDate(cursor.getString(0)), StringToDate(cursor.getString(1)), TimeUnit.MINUTES);
        }
        Log.d(LOG_TAG, Long.toString(d));
        cursor.close();
        sql.close();

        return d;
    }

    private void setWorkDuration(String m) throws ParseException, IOException {
        long durationInMinutes = getWorkDuration(m);
        if(durationInMinutes != 0) {
            int hours = 0;
            while (durationInMinutes/60 != 0){
                hours++;
                durationInMinutes-=60;
            }
            if(hours !=0) {
                if (hours == 1) {
                    result.setText(hours + " Stunde" + " und " + durationInMinutes + " Minuten");
                    sendMailButton.setVisibility(View.VISIBLE);
                } else {
                    result.setText(hours + " Stunden" + " und " + durationInMinutes + " Minuten");
                    sendMailButton.setVisibility(View.VISIBLE);
                }
            } else {
                result.setText(durationInMinutes + " Minuten");
                sendMailButton.setVisibility(View.VISIBLE);

            }
        } else {
            result.setText("Keine Einträge");
            sendMailButton.setVisibility(View.INVISIBLE);
        }
        month = m;
    }

    private void sendWorkReport() throws ParseException, IOException {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String filename = "WorkReport_for_" + month;
        String m = month.substring(0,3);
        String filePath = baseDir + File.separator + filename;
        File f = new File(filePath);
        PrintWriter csvWriter = null;
        FileWriter mFileWriter;
        Log.d(LOG_TAG, "sendWorkReport");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_TO_WRITE_FILE);
        } else {
            if(f.exists() && !f.isDirectory()){
                f.delete();
                mFileWriter = new FileWriter(filePath, true);
                csvWriter = new PrintWriter(mFileWriter);
            } else {
                csvWriter = new PrintWriter(new FileWriter(filePath));
            }
        }
        dbHelper = new TaskDBHelper(getApplicationContext());
        String typeOfTask = "#ff8080";
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "sendWorkReport -  month ist: " + month + " m ist: " + m);
        Cursor cursor = sql.query(
                TaskContract.TABLE,
                new String[]{TaskContract.Columns.BEGINDATE, TaskContract.Columns.ENDDATE},
                TaskContract.Columns.TYPEOFTASK + "=?" + " AND " + TaskContract.Columns.MONTH + " =? ",
                new String[]{typeOfTask, m},
                null,
                null,
                null,
                null);
        long d = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            d += getDuration(StringToDate(cursor.getString(0)), StringToDate(cursor.getString(1)), TimeUnit.MINUTES);
            String[] data = {cursor.getString(0), cursor.getString(1), Long.toString(getDuration(StringToDate(cursor.getString(0)), StringToDate(cursor.getString(1)), TimeUnit.MINUTES))};
            csvWriter.write(data[0] + "," + data[1] + "," + data[2] + "\r\n");
            Log.d(LOG_TAG, "writing data to .csv");
        }
        csvWriter.close();
        cursor.close();
        sql.close();
        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "noname");
        String mail = sharedPreferences.getString("mail", "nomail");
        Log.d(LOG_TAG, name);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {mail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Workreport for " + month);
        intent.putExtra(Intent.EXTRA_TEXT, "Dear Sir or Madam, \nplease find attached my Workreport as .csv-File.\nBest Regards "+ name);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        startActivity(Intent.createChooser(intent, "Send Mail"));
    }
}
