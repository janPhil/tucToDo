package com.example.whoami.tuctodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.whoami.tuctodo.db.TaskContract;
import com.example.whoami.tuctodo.db.TaskDBHelper;

/**
 * Created by whoami on 06.09.15.
 */
public class ToDoFragment extends Fragment {

    public String LOG_TAG = getClass().getSimpleName();
    private CustomAdapter mCustomAdapter;
    private TaskDBHelper dbHelper;
    Cursor c;

    public ToDoFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        getActivity().setTitle("To-Do List");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.to_to_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add_task){
            startActivity(new Intent(getActivity(),AddTask.class));
        }
        if (id == R.id.action_settings){
            startActivity(new Intent(getActivity(),SettingsActivity.class));
        }

        if (id == R.id.action_work_report)
            startActivity(new Intent(getActivity(),WorkReport.class));

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        dbHelper = new TaskDBHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TaskContract.TABLE, new String[]{TaskContract.Columns._ID, TaskContract.Columns.DESC, TaskContract.Columns.TYPEOFTASK,
                        TaskContract.Columns.BEGINDATE},
                null, null, null, null, null);

        mCustomAdapter = new CustomAdapter(getActivity(),
                R.layout.task, c,
                new String[]{TaskContract.Columns.DESC},
                new int[]{R.id.taskTextView},0);

        Log.d(LOG_TAG, " No.:" + c.getCount());

        View rooView = inflater.inflate(R.layout.to_do_fragment, container, false);
        ListView listView = (ListView) rooView.findViewById(R.id.fragment_listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteToDo(id);
                Toast.makeText(getActivity(), "To-Do wurde gelöscht", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "looong ID: " + id + " position: " + position);
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "short click");
                final SimpleCursorAdapter simpleCursorAdapter = (SimpleCursorAdapter) parent.getAdapter();
                final Cursor cursor = simpleCursorAdapter.getCursor();
                final int idColIndex = cursor.getColumnIndex(TaskContract.Columns._ID);
                final String rowId = cursor.getString(idColIndex);
                Intent intent = new Intent(getActivity(), ToDoDetail.class);
                intent.putExtra("EXTRA_NUM", rowId);
                startActivity(intent);
            }
        });


        listView.setAdapter(mCustomAdapter);

        return rooView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        Cursor newCursor = sql.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.DESC,TaskContract.Columns.TYPEOFTASK, TaskContract.Columns.BEGINDATE},
                null, null, null, null, null);
        mCustomAdapter.swapCursor(newCursor);
        mCustomAdapter.notifyDataSetChanged();
    }

    public void deleteToDo(long id) {
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns._ID,
                id);
        dbHelper = new TaskDBHelper(getActivity());
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL(sql);
        Log.d(LOG_TAG, "deleteToDo: "+ id);
        sqlDB.close();
        updateUI();
    }

    public void updateUI(){
        Log.d(LOG_TAG, "updateUI");
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        Cursor newCursor = sql.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.DESC,TaskContract.Columns.TYPEOFTASK, TaskContract.Columns.BEGINDATE},
                null, null, null, null, null);
        mCustomAdapter.swapCursor(newCursor);
        mCustomAdapter.notifyDataSetChanged();
    }
}
