package com.example.whoami.tuctodo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDBHelper extends SQLiteOpenHelper {

		String LOG_TAG = getClass().getSimpleName();

		public TaskDBHelper(Context context) {
			super(context, com.example.whoami.tuctodo.db.TaskContract.DB_NAME, null, com.example.whoami.tuctodo.db.TaskContract.DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase sqlDB) {
			String sqlQuery =
					String.format("CREATE TABLE %s (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT, " + "%s TEXT, " + "%s TEXT, " + "%s TEXT, " + "%s TEXT," + "%s TEXT)",
							com.example.whoami.tuctodo.db.TaskContract.TABLE,
							com.example.whoami.tuctodo.db.TaskContract.Columns.DESC,
							com.example.whoami.tuctodo.db.TaskContract.Columns.PLACE,
							com.example.whoami.tuctodo.db.TaskContract.Columns.TYPEOFTASK,
							com.example.whoami.tuctodo.db.TaskContract.Columns.BEGINDATE,
							com.example.whoami.tuctodo.db.TaskContract.Columns.ENDDATE,
							com.example.whoami.tuctodo.db.TaskContract.Columns.MONTH);
			Log.d(LOG_TAG, "sql: " + sqlQuery);
			sqlDB.execSQL(sqlQuery);
		}

		@Override
		public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
			sqlDB.execSQL("DROP TABLE IF EXISTS " + com.example.whoami.tuctodo.db.TaskContract.TABLE);
			onCreate(sqlDB);
		}
}