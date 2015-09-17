package com.example.whoami.tuctodo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.whoami.tuctodo.db.TaskContract;
import com.example.whoami.tuctodo.db.TaskDBHelper;

/**
 * Created by whoami on 06.09.15.
 */
public class CustomAdapter extends SimpleCursorAdapter{

    private Context mContext;
    private int layout;
    private LayoutInflater inflater;
    private TaskDBHelper dbHelper;
    private Button but;
    private RelativeLayout rel;
    private TextView date;

    public String LOG_TAG = getClass().getSimpleName();

    public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags){
        super(context, layout, c, from, to, flags);
        this.layout=layout;
        this.mContext = context;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = super.getView(position, convertView, parent);
        final Cursor c = getCursor();
        Log.d(LOG_TAG, "c: " + c.getString(0) + " " + c.getString(2) + c.getString(3) +" "+ c.getColumnCount());
        int color = Color.parseColor(c.getString(2));
        rel = (RelativeLayout) view.findViewById(R.id.wrapper);
        rel.setBackgroundColor(color);
        rel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(LOG_TAG, "looong click");
                return false;
            }
        });
        but = (Button) view.findViewById(R.id.doneButton);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDoneButtonClick(v);
            }
        });
        date = (TextView) view.findViewById(R.id.date_textview);
        date.setText(c.getString(3));
        Log.d(LOG_TAG, " getView with position: " + position);
        return rel;
    }

    public void onDoneButtonClick( View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();
        Log.d(LOG_TAG, "taskTextView: " + task);
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.DESC,
                task);
        dbHelper = new TaskDBHelper(mContext);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL(sql);
        Log.d(LOG_TAG, "onDoneButtonCLick");
        SQLiteDatabase sqlRead = dbHelper.getReadableDatabase();
        Cursor newCursor = sqlRead.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.DESC, TaskContract.Columns.TYPEOFTASK, TaskContract.Columns.DATE},
                null, null, null, null, null);
        changeCursor(newCursor);
        notifyDataSetChanged();
        sqlDB.close();
        sqlRead.close();
    }
}
