package com.example.whoami.tuctodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.whoami.tuctodo.db.TaskContract;
import com.example.whoami.tuctodo.db.TaskDBHelper;

public class ToDoDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_to_do_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ToDoDetailContainer, new ToDoDetailFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do_detail, menu);
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

    public static class ToDoDetailFragment extends Fragment{


        private String titel;
        private String date;
        private String place;
        private TextView titelTextView;
        private TextView placeTextView;
        private TextView dateTextView;

        public ToDoDetailFragment(){
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            Intent intent = getActivity().getIntent();
            String id = intent.getStringExtra("EXTRA_NUM");

            View rootView = inflater.inflate(R.layout.activity_to_do_detail,container, false);
            titelTextView = (TextView) rootView.findViewById(R.id.toDoDetailTitel);
            placeTextView = (TextView) rootView.findViewById(R.id.toDoDetailPlace);
            dateTextView = (TextView) rootView.findViewById(R.id.toDoDetailDate);
            getToDo(id);
            return rootView;
        }

        public void getToDo(String id){
            TaskDBHelper dbHelper = new TaskDBHelper(getActivity());
            SQLiteDatabase sql = dbHelper.getReadableDatabase();
            String[] args = new String[]{id};
            Cursor c = sql.query(TaskContract.TABLE,
                    new String[]{TaskContract.Columns._ID, TaskContract.Columns.DESC, TaskContract.Columns.TYPEOFTASK, TaskContract.Columns.BEGINDATE,
                            TaskContract.Columns.PLACE}, TaskContract.Columns._ID + " =?", args, null, null, null);

            c.moveToFirst();
            Log.d("Cursor:", " " + c.getColumnCount() + " " + c.getCount() + id);
            titelTextView.setText(c.getString(1));
            placeTextView.setText(c.getString(3));
            dateTextView.setText(c.getString(4));
            c.close();
            sql.close();
        }

    }
}
