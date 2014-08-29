package com.asusoda.taskdevil.data_access_layer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asusoda.taskdevil.R;
import com.asusoda.taskdevil.models.Task;

/**
 * Created by Tyler on 8/9/2014.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    private Context mContext;
    public static final int DATABASE_VERSION = -1999;

    public DatabaseManager(Context pContext) {
        super(pContext, pContext.getString(R.string.database_name), null, 1);

        mContext = pContext;
    }

    private String addBackslashes(String val) {
        int test = 1;
        val = val.replace("'", "\'\'");
        return val;
    }

    public void insertTask(Task task) {
        String insertProcedure = mContext.getString(R.string.sql_insert_task);

        insertProcedure = insertProcedure.replace("{0}",  addBackslashes( task.getTitle() ) );

        insertProcedure = insertProcedure.replace("{1}",  addBackslashes( task.getDescription() ) );

        insertProcedure = insertProcedure.replace("{2}", "" + task.getRecurrenceType().ordinal() );
        insertProcedure = insertProcedure.replace("{3}", "" + task.getRecurrenceValue());
        insertProcedure = insertProcedure.replace("{4}", "" + task.getReminderAdvanceTime());
        insertProcedure = insertProcedure.replace("{5}", "" + task.getOccursAt());

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertProcedure);
    }

    public Cursor retrieveAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        String getAllTasksProcedure = mContext.getString(R.string.sql_get_all_tasks);
        Cursor rows = db.rawQuery(getAllTasksProcedure, null);
        return rows;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // these are the procedures for creating the database from scratch
        String createProcedure = mContext.getString(R.string.sql_create_database);

        // here we execute the sql
        db.execSQL(createProcedure);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
