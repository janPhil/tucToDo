package com.example.whoami.tuctodo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.content.SharedPreferences.Editor;

public class SettingsActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText mailEditText;
    private Button save;
    private Button clear;
    SharedPreferences sharedPreference;
    public static final String preferences = "prefs";
    public static final String namePreference = "name";
    public static final String mailPreference = "mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameEditText = (EditText) findViewById(R.id.nameInsertEditText);
        mailEditText = (EditText) findViewById(R.id.mailInsertEditText);
        sharedPreference = getSharedPreferences(preferences, Context.MODE_PRIVATE);
        if (sharedPreference.contains(namePreference)){
            nameEditText.setText(sharedPreference.getString(namePreference, ""));
        }
        if (sharedPreference.contains(mailPreference)){
            mailEditText.setText(sharedPreference.getString(mailPreference, ""));
        }

        save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);
            }
        });
        clear = (Button) findViewById(R.id.editButton);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(v);
            }
        });
    }


    public void save(View v){

        String name = nameEditText.getText().toString();
        String mail = mailEditText.getText().toString();
        Editor editor = sharedPreference.edit();
        editor.putString(namePreference, name);
        editor.putString(mailPreference, mail);
        editor.commit();

    }

    public void clear(View view) {

        nameEditText.setText("");
        nameEditText.setText("");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
}
