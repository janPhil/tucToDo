package com.example.whoami.tuctodo.db;

import android.provider.BaseColumns;

public class TaskContract {
	public static final String DB_NAME = "com.example.whoami.tuctodo.db.tasks";
	public static final int DB_VERSION = 5;
	public static final String TABLE = "tasks";


	public class Columns {
		public static final String DESC = "description";
		public static final String PLACE = "place";
		public static final String TYPEOFTASK = "typeOfTask";
		public static final String _ID = BaseColumns._ID;
		public static final String BEGINDATE = "begindate";
		public static final String ENDDATE = "enddate";

	}
}
