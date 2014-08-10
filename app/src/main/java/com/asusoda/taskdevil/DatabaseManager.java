package com.asusoda.taskdevil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tyler on 8/9/2014.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    private Context mContext;


    DatabaseManager(Context pContext) {
        super(pContext, pContext.getString(R.string.database_name), null, 1);

        mContext = pContext;
    }

    public void populateDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(mContext.getString(R.string.table_create));
    }

    public void insertTask(String pTitle, String pDescription, int pRecType, int pRecValue, int pReminderTime, int pOccursAt) {
        String insertProcedure = mContext.getString(R.string.sql_insert_task);

        insertProcedure.replace("{0}", pTitle);
        insertProcedure.replace("{1}", pDescription);
        insertProcedure.replace("{2}", "" + pRecType);
        insertProcedure.replace("{3}", "" + pRecValue);
        insertProcedure.replace("{4}", "" + pReminderTime);
        insertProcedure.replace("{5}", "" + pOccursAt);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertProcedure);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
